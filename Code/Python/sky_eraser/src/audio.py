"""
Sky Eraser (For real this time!)
By Prerisoft

Audio Subroutines
File creation date: Feb 08, 2013

Like control, this was much entirely recycled from Valiancer/Sky Eraser b's
code. Also like control, I'll be updating it if I come up with something better.

THIS FILE IS LICENSED UNDER THE 3-CLAUSE BSD LICENSE. PLEASE SEE LICENSE(.txt)
FOR DETAILS.
"""

from __future__ import division
import atexit, pygame.mixer, pygame.time, threading
import calcs, data
from constants import (AUDIO_FRAMERATE, AUDIO_FREQUENCY, AUDIO_BUFFER,
                       AUDIO_CHANNELS, AUDIO_RESERVED_CHANNELS, AUDIO_LOOP,
                       PACKAGE_AUDIO_DIR, PACKAGE_AUDIO_FILE, CHANNEL_MUSIC)


class AudioSystem(object):
    """
    The internal system for managing audio caches and function calls.
    """
    def __init__(self):
        pygame.mixer.init(frequency=data.config.audio_frequency,
                          buffer=data.config.audio_buffer) # Initiates Pygame's mixer.
        pygame.mixer.set_num_channels(AUDIO_CHANNELS)
        self.reserved_channels = {} 
        self.extra_channels = []
        pygame.mixer.set_reserved(len(AUDIO_RESERVED_CHANNELS))
        for channel in AUDIO_RESERVED_CHANNELS:
            self.reserved_channels.update({channel:pygame.mixer.Channel(channel)})
        self.sound_cache = {} # Creates a blank sound cache.
        self.music_cache = {} # Creates a seperate music cache.
        self.sound_volume = data.config.sound_volume
        # Default sound effect volume. Music volume is handled seperately.

class MusicThread(threading.Thread):
    """
    A program thread for handling the music apart from the game logic.
    """
    def __init__(self):
        """
        Initializes the threading code and generates the music channel.
        """
        threading.Thread.__init__(self, name="MusicPlayer")
        self.daemon = True # With this flag, thread closes when game does
        # Boolean flag for subloop execution - switched off on game exit.
        self.running = True # Maybe we won't need it with the threading model?
        # Copy of the reserved music channel for local access.
        self.channel = audio_system.reserved_channels[CHANNEL_MUSIC]
        self.music_blocks = [] # A sequence of Sound objects are loaded here.
        # Index number for the next sample in the block sequence to queue
        self.next_block = 0
        self.music_loop = 0 # Loops to this number at the end of the list.
        self.fading = False # To prevent volume changes while fading.
        self.event_command = None # Commands to play, stop, loop, fade music.
        self.event_parameters = {} # Keyword arguments for event commands.
        self.event_notifier = threading.Event() # To signal new events.
        self.interaction_lock = threading.RLock() # To avoid funtimes.
        
        self.music_volume = data.config.music_volume # Channel base volume.
        self.clock = pygame.time.Clock() # Handles timing during fades.

    def play(self, samples, loop=0):
        """
        Plays the sequence of samples in order, looping back to the index number
        given in the loop argument.
        """
        self.stop()
        self.music_blocks = list(audio_system.music_cache[sample]
                                 for sample in samples)
        self.music_loop = loop
        self.channel.set_volume(self.music_volume)
        self.channel.play(self.music_blocks[0])
        self.channel.set_endevent(AUDIO_LOOP) # For threading system
        if len(self.music_blocks) > 1:
            self.channel.queue(self.music_blocks[1])
            self.next_block = 2

    def stop(self):
        """
        Stops the music played, flushes the queue and cache, and enters a halted
        phase.
        """
        # disabling loop notifications so it doesn't trigger when we stop...
        self.channel.set_endevent()
        self.channel.stop()
        self.music_blocks = []
        self.next_block = 0
        self.music_loop = 0

    def fade(self, time=60):
        """
        Sets the music thread to a fading state, where it'll gradually fade out
        the music being played and stop within the number of frames.
        (currently, one music frame is two gameplay frames)
        """
        self.fading = True
        fade_volume = self.music_volume
        fade_time = self.music_volume / time
        while fade_volume > 0:
            fade_volume -= fade_time
            self.channel.set_volume(max(0, fade_volume))
            self.clock.tick(AUDIO_FRAMERATE)
        self.stop()
        self.fading = False

    def loop(self):
        """
        Upon triggering this condition, the music player feeds another sample
        into the buffer.
        """
        if self.channel.get_busy() and not (self.channel.get_queue() or self.music_loop == -1):
            if self.next_block >= len(self.music_blocks):
                self.next_block = self.music_loop
            self.channel.queue(self.music_blocks[self.next_block])
            self.next_block += 1
        elif self.music_loop == -1:
            self.stop()

    def set_volume(self, volume=1.0):
        """
        Changes the music channel's volume.
        """
        self.music_volume = volume
        if not self.fading:
            self.channel.set_volume(volume)

    def run(self):
        """
        Main runtime for the music thread. To safely exit, set running to false
        and join().
        """
        while self.running:
            self.event_notifier.wait() # Hold until next event called
            self.interaction_lock.acquire() # Lock event queue
            command = self.event_command
            params = self.event_parameters # Set command
            self.event_command = None
            self.event_parameters = {} # Flush event queue
            self.event_notifier.clear()
            self.interaction_lock.release()
            # While we're running, other events can queue.
            command(**params)

def load_sample(name, music=False):
    """
    Loads a sample into the sound cache, or the music cache if specified.
    """
    cache = audio_system.music_cache if music else audio_system.sound_cache
    if name not in cache:
        sound = pygame.mixer.Sound(data.get_path(name,
                                                      PACKAGE_AUDIO_DIR,
                                                      PACKAGE_AUDIO_FILE))
        if not music:
            sound.set_volume(audio_system.sound_volume)
        cache.update({name:sound})

def unload_cache(keep=[]):
    """
    Empties the sound and music caches, excepting any samples kept on request.
    """
    for cache in (audio_system.sound_cache, audio_system.music_cache):
        for key in cache.keys():
            if key not in keep:
                del cache[key]

def play_sound(name, channel=None):
    """
    Plays a sound, on a reserved channel if available.
    """
    try:
        if channel in audio_system.reserved_channels:
            audio_system.reserved_channels[channel].play(audio_system.sound_cache[name])
        else:
            audio_system.sound_cache[name].play()
    except KeyError: pass

def signal_player(command, parameters={}):
    """
    Overarching system for sending events to the music player.
    """
    audio_system.music_thread.interaction_lock.acquire()
    audio_system.music_thread.event_command = command
    audio_system.music_thread.event_parameters = parameters
    audio_system.music_thread.event_notifier.set()
    audio_system.music_thread.interaction_lock.release()

def play_music(names, loop=0):
    """
    Starts playback on the music thread with the list of samples. Loop argument
    directs which sample to return to when playback has finished.
    """
    signal_player(audio_system.music_thread.play,
                  dict(samples=names, loop=loop))

def play_jingle(name):
    """
    DEPRECATED: Halts playback on the music thread and plays a non-looped
    sample.
    """
    play_music([name], loop=-1)

def stop_music():
    """
    Immediately halts playback on the music thread.
    """
    signal_player(audio_system.music_thread.stop)

def fade_music(time=60):
    """
    Sets the music thread to gradually fade out.
    """
    signal_player(audio_system.music_thread.fade,
                  dict(time=time))

def signal_loop():
    """
    Internal function called by event processor in the control module.
    """
    signal_player(audio_system.music_thread.loop)

def pause_sounds():
    for i in xrange(1, AUDIO_CHANNELS):
        pygame.mixer.Channel(i).pause()

def resume_sounds():
    for i in xrange(1, AUDIO_CHANNELS):
        pygame.mixer.Channel(i).unpause()

def set_volume(sound=None, music=None):
    """
    Changes the volume of sound effects.
    """
    if music is not None:
        audio_system.music_thread.set_volume(music)
    if sound is not None:
        audio_system.sound_volume = sound
        for soundfx in audio_system.sound_cache.values():
            soundfx.set_volume(sound)

def on_exit():
    audio_system.music_thread.running = False

# Config file sanity tests 

data.sanity_test("sound_volume",
                 replacement=lambda x: float(calcs.clamp(x, 0, 1)))
data.sanity_test("music_volume",
                 replacement=lambda x: float(calcs.clamp(x, 0, 1)))
data.sanity_test("audio_frequency",
                 replacement=lambda x: int(x))
data.sanity_test("audio_buffer",
                 replacement=lambda x: int(calcs.next_power(x)))

audio_system = AudioSystem()
audio_system.music_thread = MusicThread()
audio_system.music_thread.start()
atexit.register(on_exit)
