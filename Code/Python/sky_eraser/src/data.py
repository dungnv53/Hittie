"""
Sky Eraser (For real this time!)
By Prerisoft

Data Management
File creation date: Jan 09, 2013

THIS FILE IS LICENSED UNDER THE 3-CLAUSE BSD LICENSE. PLEASE SEE LICENSE(.txt)
FOR DETAILS.
"""

from __future__ import print_function
import sys, os, errno, atexit, yaml, appdirs, struct

from constants import (GAME_TITLE,
                       GAME_FILE,
                       GAME_VERSION_HEX,
                       OLDEST_COMPATIBLE_VERSION,
                       GAME_AUTHOR,
                       CONFIG_FILE,
                       PLAYDATA_FILE,
                       
                       SCREEN_SCALE,
                       
                       AUDIO_FREQUENCY,
                       AUDIO_BUFFER,
                       
                       INPUT_KEY_DEFAULTS,
                       INPUT_JOY_DEFAULTS,
                       INPUT_JOY_DEADZONE,
                       
                       DEFAULT_SCORERS)

class ConfigFile(object):
    """
    YAML-based configuration file, containing various variables that can be
    changed for a different play experience.
    """
    update_flag = False
    defaults = dict(starting_lives = 2,
                    starting_bombs = 3,
                    starting_power = 0,

                    screen_scale   = SCREEN_SCALE,
                    full_screen    = 0,
                    frame_skip     = 0,
                    show_fps       = False,

                    sound_volume = 0.5,
                    music_volume = 0.5,
                    audio_frequency = AUDIO_FREQUENCY,
                    audio_buffer    = AUDIO_BUFFER,

                    key_bindings = INPUT_KEY_DEFAULTS.copy(),
                    joy_buttons  = INPUT_JOY_DEFAULTS.copy(),
                    joy_deadzone = INPUT_JOY_DEADZONE,
                    
                    last_name = "")
    optionals = ["wave_seed"]
    def __init__(self):
        """
        Initialises with the default contents.
        """
        object.__setattr__(self, "__dict__", self.defaults.copy())

    def __setattr__(self, attribute, value):
        """
        Private method, used to check if the change to an attribute is
        significant enough to warrant saving the file later.
        """
        try:
            if (type(value) == type(getattr(self, attribute)) and
               (value == getattr(self, attribute))):
                return
        except StandardError:
            pass
        object.__setattr__(self, attribute, value)
        ConfigFile.update_flag = True

    def load(self, *filenames):
        """
        Iteratively loads configuration data from a list of filenames. If a
        parsing error occurs or no valid files are found, the config file is
        marked as being in need of saving.
        """
        valids = [] # List of filenames that parsed correctly.
        for filename in filenames:
            try:
                with open(os.path.join(filename, CONFIG_FILE),"r") as filedata:
                    data = yaml.safe_load(filedata)
                    if isinstance(data, dict):
                        for key in data.keys():
                            if key not in (ConfigFile.defaults.keys()
                                           + ConfigFile.optionals):
                                del data[key]
                                ConfigFile.update_flag = True
                        self.__dict__.update(data)
                valids.append(filename)
            except IOError as e:
                if e.errno != errno.ENOENT:
                    print("Error when reading config data: {}".format(e),
                          file=sys.stderr)
            except Exception as e:
                print("{} when parsing config data: {}".format(
                    type(e).__name__, e), file=sys.stderr)
                ConfigFile.update_flag = True # Time to clean up the mess
        if not valids: # No config were found or functional.
            ConfigFile.update_flag = True # We'll just make ourselves a new one

    def save(self, filename):
        """
        If the configuration file is in need of saving, this will write the
        contents on disk, overwriting any previous configuration file available
        """
        if ConfigFile.update_flag:
            if not os.path.exists(filename):
                os.makedirs(filename)
            with open(os.path.join(filename, CONFIG_FILE), "w") as filedata:
                yaml.safe_dump(self.__dict__, filedata,
                               default_flow_style=False)
        ConfigFile.update_flag = False

    def valid_play(self):
        """
        Checks if any of the given gameplay variables are altered, which
        invalidates high score entry.
        """
        return not (self.starting_lives != 2 or
                    self.starting_bombs != 3 or
                    self.starting_power != 0 or
                    hasattr(self, "wave_seed"))

    def reset(self, attribute):
        """
        Resets an attribute to its default value.
        """
        try:
            setattr(self, attribute, self.defaults[attribute])
        except KeyError:
            delattr(self, attribute)

class PlayData(object):
    """
    Binary information file containing play time and high scores.
    """
    def __init__(self):
        """
        Initialises to a default value.
        """
        self.playtime = 0
        self.reset_scores()
        # TODO: implement seperate endless and time attack scores
        self.base_packer = struct.Struct(">3L")
        self.score_packer = struct.Struct(">12sQ2Hl")
    
    def read_scores(self, file_buffer, start=0):
        """
        Private method for parsing the contents of the file's buffer for score
        data.
        """
        for block_offset in xrange(start,
                                   start + (self.score_packer.size * 10),
                                   self.score_packer.size):
            
            unpacked_data = self.score_packer.unpack_from(file_buffer,
                                                          block_offset)
            yield ScoreEntry(name=unpacked_data[0],
                             score=unpacked_data[1],
                             kills=unpacked_data[2],
                             playtime=unpacked_data[3],
                             timestamp=unpacked_data[4])

    def reset_scores(self):
        """
        Public method for erasing the scores on request.
        """
        self.scores = list(ScoreEntry(DEFAULT_SCORERS[i], (10 - i) * 10000,
                                      (10 - i) * 20, (11 - i) * 500)
                           for i in xrange(10))

    def load(self, *filenames):
        """
        Iterates over a list of filenames and loads the first one it can locate
        before discarding the rest.
        """
        for filename in filenames:
            try:
                with open(os.path.join(filename, PLAYDATA_FILE), "rb") as filedata:
                    file_buffer = buffer(filedata.read())
                verification, version, self.playtime = \
                    self.base_packer.unpack_from(file_buffer)
                if ((verification == hash(GAME_TITLE+GAME_AUTHOR) & 0xFFFFFFFF)
                    and (version >= OLDEST_COMPATIBLE_VERSION)):
                    self.scores = list(self.read_scores(file_buffer,
                                                        self.base_packer.size))
                return
            except IOError as e:
                if e.errno != errno.ENOENT:
                    print("Error when reading playdata: {}".format(e),
                          file=sys.stderr)
            except Exception as e:
                print("{} when parsing playdata: {}".format(type(e).__name__, e),
                      file=sys.stderr)

    def save(self, filename):
        """
        Saves the playdata to a specific file, overwriting any existing data.
        """
        if not os.path.exists(filename):
            os.makedirs(filename)
        with open(os.path.join(filename, PLAYDATA_FILE), "wb") as filedata:
            filedata.write(self.base_packer.pack(
                hash(GAME_TITLE + GAME_AUTHOR) & 0xFFFFFFFF, GAME_VERSION_HEX,
                self.playtime))
            for score in self.scores:
                filedata.write(self.score_packer.pack(score.name,
                                                      score.score,
                                                      score.kills,
                                                      score.playtime,
                                                      score.timestamp))

    def get_position(self, score): # TODO: Add score list separation
        """
        Checks the rank that a given score would occupy.
        """
        for high_scorer in self.scores:
            if score > high_scorer.score:
                return self.scores.index(high_scorer)
        return None
    def add_score(self, score, rank):
        """
        Adds a score to the high score list.
        """
        assert type(score) is ScoreEntry
        entry = score
        self.scores.insert(rank, entry)
        self.scores = self.scores[:10]

class ScoreEntry(object):
    """
    A score object for the high score table.
    """
    def __init__(self, name, score, kills, playtime, timestamp=0):
        self.set_name(name)
        self.score = score
        self.kills = kills
        self.playtime = playtime
        self.timestamp = timestamp
        if timestamp:
            self.replay_file = "{}_{}".format(name, timestamp)
        else:
            self.replay_file = None
    def set_name(self, name):
        """
        Sets the name for the score entry.
        """
        if name.strip(): # Testing if name is not just empty or whitespace
            self.name = name[:12].strip(chr(0)) # Doublecheck sizes, too
        else:
            self.name = "PRERISOFT" # Set to default provider

def get_path(filename, location, extension):
    """
    Find the path!
    
    I don't know why I'm not using this in more parts of the code. Something to
    deal with before the full release.
    """
    return os.path.join(os.path.realpath(os.path.dirname(sys.argv[0])),
                        location,
                        "{0}.{1}".format(filename, extension))

def sanity_test(attribute, replacement=None, assertion=None):
    """
    Performs sanity testing on a given attribute for the config file, using
    either an assertion to test for validity (and reverting to the default if
    it fails), or restricting the given value to a filtered version (or
    reverting to the default if the given data is invalid enough to jam it).
    """
    if assertion is not None:
        try:
            assertion(getattr(config, attribute))
            return
        except StandardError:
            print("ERROR >> Config attribute {} invalid".format(attribute),
                  file=sys.stderr)
    if replacement is not None:
        try:
            compare = getattr(config, attribute)
            setattr(config, attribute, replacement(getattr(config, attribute)))
            if compare != getattr(config, attribute):
                print("ERROR >> Config attribute {} invalid".format(attribute),
                      file=sys.stderr)
            return
        except StandardError:
            pass
    config.reset(attribute)

def get_directory(*directories):
    """
    Returns the first directory that contains the configuration file,
    and returns the last directory if none of them match.
    """
    for directory in directories:
        if os.path.exists(os.path.join(directory, CONFIG_FILE)):
            return directory
    return directories[-1] # Fallback to final if all else fails
    

def on_exit():
    """
    Automatically saves playdata and configuration info on exit.
    """
    if override_location:
        location = override_location
    else:
        location = get_directory(program_dir, site_dir, local_dir)
    config.save(location)
    playdata.save(location)

override_location = None # Manual definition by commandline argument - unimplemented
program_dir = os.path.realpath(os.path.dirname(sys.argv[0]))
site_dir = appdirs.site_data_dir(GAME_FILE, GAME_AUTHOR)
local_dir = appdirs.user_data_dir(GAME_FILE, GAME_AUTHOR)
config = ConfigFile()
config.load(program_dir, site_dir, local_dir)
playdata = PlayData()
playdata.load(program_dir, site_dir, local_dir)
atexit.register(on_exit)

#### BETA RELEASE CODE - FOR SKY ERASER DEVELOPERS AND TESTERS ONLY ####
