"""
Sky Eraser (For real this time!)
By Prerisoft

Video Subroutines
File creation date: Jan 07, 2013

THIS FILE IS LICENSED UNDER THE 3-CLAUSE BSD LICENSE. PLEASE SEE LICENSE(.txt)
FOR DETAILS.
"""

from __future__ import print_function, division
import os, pygame.display, pygame.surface, pygame.rect, pygame.time
from weakref import WeakSet, WeakValueDictionary
from struct import Struct

import calcs, data
from constants import (GAME_TITLE, SCREEN_SIZE, SCREEN_SCALE, VIDEO_BASE_FILE,
                      VIDEO_BASE_ICON, VIDEO_FRAMERATE, LERP_CLAMP, LERP_EXTRA,
                      LERP_REPEAT, LERP_REVERSE, PACKAGE_GRAPHICS_DIR, PACKAGE_GRAPHICS_FILE,
                      PACKAGE_TEXTURE_FILE, CLOUD_SPEED, CLOUD_PARALLAX,
                      CLOUD_FILE, FONT_FILE, FONT_NUM, PARALLAX_DEFAULT)

class VideoSystem(object):
    """
    Internal video handler.
    """
    def __init__(self):
        os.environ["SDL_VIDEO_CENTERED"] = "1"
        pygame.display.init()
        self.screen = None # Backbuffer - display surface when non-scaled
        self.output = None # Secondary backbuffer for scaled displays
        self.framerate = None
        self.scale = data.config.screen_scale # Display scale
        self.textures = dict() # Texture database
        self.renderers = list() # Render callback list
        self.clock = pygame.time.Clock() # Timer
        self.framecounter = 0 # Number of frames passed
        self.cloud_time = 0 # Seperate framecounter for seamless clouds
        self.packer = Struct(">6h") # Unpacker for .txc entries
        self.parallax = 0
        self.parallax_callback = None

class DynamicAttribute(object):
    """
    Descriptor that reads back the contents of callable attributes.
    """
    __slots__ = ("attribute", "callback")
    def __init__(self, attribute):
        self.attribute = attribute
    def __get__(self, instance, owner):
        return getattr(instance, self.attribute)()
    def __set__(self, instance, value):
        if callable(value):
            setattr(instance, self.attribute, value)
        else:
            setattr(instance, self.attribute,
                    lambda: value)
    def __delete__(self, instance):
        delattr(instance, self.attribute)

class ExecutiveAttribute(object):
    """
    Descriptor that executes an associated function when the contents change.
    """
    __slots__ = ("attribute", "callback")
    def __init__(self, attribute, callback):
        self.attribute = attribute # Variable to use with instance
        self.callback = callback # Callback to execute when new value is set
    def __get__(self, instance, owner):
        return getattr(instance, self.attribute)
    def __set__(self, instance, value):
        setattr(instance, self.attribute, value)
        self.callback(instance, value)
    def __delete__(self, instance):
        delattr(instance, self.attribute)

class Texture(object):
    """
    Texture data containing an appropriate surface and subsurface blocks.
    """
    __slots__ = ("surface", "blocks", "references", "lock")
    def __init__(self, surface, blocks):
        self.surface = surface # Surface data for texture
        self.blocks = blocks # Series of subsurface data
        self.references = WeakSet() # Sprites which are using the texture
        self.lock = False # Persistence lock for unloading operations.

class FontTexture(Texture):
    """
    A texture object that contains a font as well as a cache of coloured
    variants.
    """
    __slots__ = ("cache", "glyph_size")
    def __init__(self, surface, blocks):
        Texture.__init__(self, surface, blocks)
        self.cache = WeakValueDictionary()
        self.glyph_size = self.blocks[0][0].size
    def get_color(self, color):
        """
        Retrieves a cached colour entry for a textbox sprite to use, making one
        and storing it if it isn't already in the cache.
        """
        if color not in self.cache:
            colored_surface = self.surface.copy()
            colored_surface.fill(color, special_flags=pygame.BLEND_MULT)
            self.cache.update({color:colored_surface})
            return colored_surface
        else:
            return self.cache[color]

class VideoState(object):
    """
    An object that captures the entirety of the screen's current state, for
    later restoration.
    """
    def __init__(self, target, clear=False):
        self.screen = target.screen 
        self.renderers = target.renderers
        self.frame_counter = target.framecounter
        self.cloud_time = target.cloud_time
        self.parallax = target.parallax
        self.parallax_callback = target.parallax_callback
        if clear:
            # Blank out the video system.
            target.renderers = list()
            target.framecounter = 0
            target.cloud_time = 0 
            target.parallax = 0
            target.parallax_callback = None

    def restore(self, target):
        """
        Restores the contents of its state to the screen.
        """
        target.screen = self.screen
        target.renderers = self.renderers
        target.framecounter = self.frame_counter
        target.cloud_time = self.cloud_time
        target.parallax = self.parallax
        target.parallax_callback = self.parallax_callback

class Sprite(object):
    """
    A self-contained surface instance tied into the texture storage model
    that likewise provides persistent and dynamic attribute storage.
    """
    x = DynamicAttribute("_x")
    y = DynamicAttribute("_y")
    texture = ExecutiveAttribute("_texture",
                                 lambda x, y: reference_texture(x, y))
    block = DynamicAttribute("_block")
    __slots__ = ("_texture", "_block", "_x", "_y", "parallax", "__weakref__")
    def __init__(self, texture, block=0, location=(0, 0)):
        self.texture = texture # Assigned texture (auto-registering with this)
        self.block = block # Subsurface block number
        self.x, self.y = location # Location, location, location!
        self.parallax = 0
    def render(self, target):
        """
        Renders the sprite to the target surface.
        """
        block = video_system.textures[self.texture].blocks[int(self.block)]
        target.blit(video_system.textures[self.texture].surface,
                    (int(self.x) + (video_system.parallax * self.parallax)
                     - (block[1][0]),
                     self.y - block[1][1]), block[0])

class AlphaSprite(Sprite):
    """
    A subclass of sprite that offers alpha levels for blitting.
    """
    a = DynamicAttribute("_a")
    __slots__ = ("_a")
    def __init__(self, texture, block=0, location=(0, 0)):
        Sprite.__init__(self, texture, block, location)
        self.a = 255
    def render(self, target):
        """
        Renders the sprite to the target surface after wrapping in an alpha
        assignment/deassignment. Clumsy, but...
        """
        video_system.textures[self.texture].surface.set_alpha(self.a,
                                                              pygame.RLEACCEL)
        Sprite.render(self, target)
        video_system.textures[self.texture].surface.set_alpha(None)

class TempSprite(Sprite):
    """
    A subclass of sprite that stores a single-use texture locally, which is
    destroyed when the sprite is no longer in use.
    """
    __slots__ = ("surface")
    texture = NotImplemented
    block = NotImplemented
    def __init__(self, surface, location=(0, 0), alpha=False):
        self.x, self.y = location
        self.parallax = 0
        self.surface = surface
        if alpha: self.surface.set_colorkey((0xC0, 0xFF, 0xEE))
    def render(self, target):
        """
        Renders the sprite to the target surface.
        """
        target.blit(self.surface,
                    (self.x + (video_system.parallax * self.parallax), self.y))

class Textbox(Sprite):
    """
    A specialised sprite that displays text instead of a pre-existing graphic.
    """
    __slots__ = ("color_key", "cache", "local_surface", "color_key")
    def __init__(self, font, location=(0, 0), size=(8, 1)):
        Sprite.__init__(self, font, 0, location)
        glyph = video_system.textures[self.texture].glyph_size
        self.color_key = video_system.textures[self.texture].surface.get_colorkey()
        self.cache = {}
        self.local_surface = pygame.Surface((glyph[0] * size[0],
                                             glyph[1] * size[1]))
        self.local_surface.set_colorkey(self.color_key)
    def write(self, text, position=(0, 0), color=None, clear=False):
        """
        Writes to the textbox's surface, starting from the position given and
        using the color requested, if any.
        """
        if clear: self.local_surface.fill(self.color_key)
        if color:
            surface = video_system.textures[self.texture].get_color(color)  
            self.cache.update({color:surface})
        else:
            surface = video_system.textures[self.texture].surface
        texture = video_system.textures[self.texture]
        cursor = pygame.Rect((position[0] * texture.glyph_size[0],
                              position[1] * texture.glyph_size[1]),
                             texture.glyph_size)
        for letter in text:
            if letter == "\n": # Newline
                cursor.x = 0 # Carriage return
                cursor.y += texture.glyph_size[1] # Line feed
            else: # Glyph
                # if letter != "\x00":
                self.local_surface.fill(self.color_key, cursor) # Blank area
                self.local_surface.blit(surface, (cursor.x, cursor.y),
                                        area=texture.blocks[(ord(letter) & 0x7F)
                                                            - 32][0])
                # Write from font
                cursor.x += texture.glyph_size[0] # Move forward one character 
        
    def render(self, target):
        """
        Renders the textbox to the target surface.
        """
        target.blit(self.local_surface,
                    (self.x + (video_system.parallax * self.parallax), self.y))

class RectSprite(object):
    """
    A sprite-like rectangle object that can be used with Sky Eraser's rendering
    routines, including dynamic attribute assignment and parallax support.
    """
    x = DynamicAttribute("_x")
    y = DynamicAttribute("_y")
    width = DynamicAttribute("_w")
    height = DynamicAttribute("_h")
    color = DynamicAttribute("_c")
    __slots__ = ("_x", "_y", "_w", "_h", "_c", "parallax", "rect",
                 "__weakref__")
    def __init__(self, location=(0, 0), size=(8, 8), color=(0, 0, 0)):
        self.x, self.y = location
        self.width, self.height = size
        self.color = color
        self.parallax = 0
        self.rect = pygame.Rect((self.x, self.y), (self.width, self.height))
        
    def render(self, target):
        """
        Fills the rectangle's area with its color on the target surface.
        """
        self.rect.size = (self.width, self.height)
        self.rect.centerx = self.x + (video_system.parallax * self.parallax)
        self.rect.centery = self.y
        target.fill(self.color, rect=self.rect)

class AlphaRect(object):
    """
    A sprite-like rectangle object that can support alpha assignments.
    As a limitation, it cannot dynamically resize height/width. 
    """
    x = DynamicAttribute("_x")
    y = DynamicAttribute("_y")
    width = ExecutiveAttribute("_w", lambda x, y: AlphaRect.set_size(x))
    height = ExecutiveAttribute("_h", lambda x, y: AlphaRect.set_size(x))
    color = DynamicAttribute("_c")
    a = DynamicAttribute("_a")
    __slots__ = ("_x", "_y", "_x_call", "_y_call", "_w", "_h", "_c", "_c_call",
                 "_a", "_a_call", "parallax", "surface", "last_color",
                 "__weakref__")
    def __init__(self, location=(0, 0), size=(8, 8), color=(0, 0, 0),
                 alpha=255):
        self.x, self.y = location
        self.color = color
        self.last_color = color
        self._w, self._h = size
        self.set_size()
        self.a = alpha
        self.parallax = 0
    def set_size(self):
        """
        Internal function to set the size after adjustments are made to it.
        """
        self.surface = pygame.Surface((self.width, self.height))
        self.surface.fill(self.color)
    def render(self, target):
        """
        Renders the alpha rect to the target surface.
        """
        if self.color != self.last_color:
            self.surface.fill(self.color)
            self.last_color = self.color
        self.surface.set_alpha(self.a, pygame.RLEACCEL)
        target.blit(self.surface,
                    (self.x + (video_system.parallax * self.parallax), self.y))

class Lerp(object):
    """
    TODO: INCOMPLETE, FINISH AT SOME POINT
    
    Creates an instance that, when called, returns a linear interpolation of
    its start and end values based on the framecounter's position.
    """
    __slots__ = ("start", "end", "start_time", "end_time", "extend_mode",
                 "cache", "cache_time", "dt_unit")
    def __init__(self, start, end, time=None, start_time=None, end_time=None,
                 extend=LERP_CLAMP, cache=False, interpolation=None):
        self.start = start # Start value
        self.end = end # End value
        self.extend_mode = {LERP_CLAMP:self.get_clamped,
                            LERP_EXTRA:self.get_extrapolated,
                            LERP_REPEAT:self.get_repeat,
                            LERP_REVERSE:self.get_reversing}[extend] # Extension type
        if start_time is None:
            self.start_time = get_time()
        else:
            self.start_time = start_time
        if end_time is None:
            self.end_time = get_time() + time
        else:
            self.end_time = end_time
        if cache:
            self.cache = True
            self.cache_time = -1
        else:
            self.cache = False
        self.dt_unit = (end - start) / (self.end_time - self.start_time)
    def get_clamped(self):
        """
        Provides the linear interpolation's current state, clamped by its
        start and end values.
        """
        return calcs.clamp(self.get_extrapolated(),
                           min(self.start, self.end),
                           max(self.start, self.end))
    def get_extrapolated(self):
        """
        Provides the linear interpolation's current state without regard for
        where it should've started or ended.
        """
        return self.start + (self.dt_unit * (get_time() - self.start_time))
    def get_repeat(self):
        """
        Gets the linear interpolation value, repeating its sequence whenever it
        exceeds its lifetime.
        """
        return self.start + (self.dt_unit * ((get_time() - self.start_time) %
                                             (self.end_time - self.start_time)))
    def get_reversing(self):
        """
        Gets the linear interpolation value, playing it in reverse and forward
        again after its normal lifetime.
        """
        position = get_time() - self.start_time
        duration = self.end_time - self.start_time
        position %= duration * 2
        if position > duration:
            return self.end - (self.dt_unit * (position - duration))
        else:
            return self.start + (self.dt_unit * position)
    def __call__(self):
        """
        Provides the linear interpolation's current state.
        """
        return self.extend_mode()

#------------------------------------------------------------------------------ 

def create_window(scale=None):
    """
    Creates a new window at the provided scale. If the texture cache is in a
    fresh, empty state, it'll also load the important base textures.
    """
    pygame.display.set_caption(GAME_TITLE)
    if scale is not None:
        video_system.scale = scale
    else:
        scale = video_system.scale
    try:
        if scale > 1:
            video_system.output = pygame.display.set_mode((SCREEN_SIZE[0] * scale,
                                                           SCREEN_SIZE[1] * scale),
                                       data.config.full_screen & pygame.FULLSCREEN)
            video_system.screen = pygame.Surface(SCREEN_SIZE).convert()
        else:
            video_system.screen = pygame.display.set_mode(SCREEN_SIZE,
                                       data.config.full_screen & pygame.FULLSCREEN)
            video_system.output = None

    except pygame.error as e:
        data.config.full_screen = 0
        data.config.scale = 1
        import sys
        print(e, file=sys.stderr)
        video_system.screen = pygame.display.set_mode(SCREEN_SIZE)
        video_system.output = None
    if not video_system.textures: # Initialising required texture data
        load_texture(VIDEO_BASE_FILE)
        load_texture(CLOUD_FILE)
        load_texture(FONT_FILE, color_key=(0, 0, 0), object_type=FontTexture)
        load_texture(FONT_NUM, color_key=(0, 0, 0), object_type=FontTexture)
        lock_texture(VIDEO_BASE_FILE)
        lock_texture(CLOUD_FILE)
        lock_texture(FONT_FILE)
        lock_texture(FONT_NUM)
        pygame.display.set_icon(
            video_system.textures[VIDEO_BASE_FILE].surface.subsurface(
            video_system.textures[VIDEO_BASE_FILE].blocks[VIDEO_BASE_ICON][0]))
        set_parallax(PARALLAX_DEFAULT)
        if data.config.show_fps:
            video_system.framerate = Textbox(FONT_FILE, (224, 312), (2, 1))

def load_texture(filename, color_key=(0xC0, 0xFF, 0xEE), object_type=Texture):
    """
    Loads the associated graphics files and stores their data in the texture
    database for use.
    """
    if filename not in video_system.textures:
        new_surface = pygame.image.load(data.get_path(filename,
                                                      PACKAGE_GRAPHICS_DIR,
                                                      PACKAGE_GRAPHICS_FILE)
                                        ).convert()
        new_surface.set_colorkey(color_key)
        new_blocks = tuple(read_blocks(filename))
        video_system.textures.update({filename:object_type(new_surface,
                                                           new_blocks)})

def lock_texture(filename):
    """
    Locks a texture so it cannot be removed on an unloading operation.
    """
    video_system.textures[filename].lock = True

def unlock_texture(filename):
    """
    Unlocks a texture so it can be removed on an unloading operation.
    """
    video_system.textures[filename].lock = False

def unload_texture(filename, force=False):
    """
    Removes the texture from memory unless it's locked or has references.
    """
    if (video_system.textures[filename].lock == False and
        not video_system.textures[filename].references):
        del video_system.textures[filename]
    elif force and filename:
        for reference in video_system.textures[filename].references:
            reference.texture = None
        del video_system.textures[filename]

def unload_cache(keep=[]):
    """
    Removes all textures from memory unless they're locked, have references or
    are excluded.
    """
    for key in video_system.textures.keys():
        if key not in keep:
            unload_texture(key)

def read_blocks(filename):
    """
    A generator function that loads the texture coordinates (.txc) file
    corresponding to the filename provided
    """
    with open(data.get_path(filename, PACKAGE_GRAPHICS_DIR,
                            PACKAGE_TEXTURE_FILE), "rb") as texture_data:
        texture_buffer = buffer(texture_data.read())
    for block_offset in xrange(0, len(texture_buffer),
                               video_system.packer.size):
        unpacked_data = video_system.packer.unpack_from(texture_buffer,
                                                        block_offset)
        rect = pygame.Rect(*unpacked_data[0:4])
        offset = unpacked_data[4:6]
        yield (rect, offset)

def reference_texture(sprite, texture):
    """
    Loads the sprite into the texture's references list.
    """
    video_system.textures[texture].references.add(sprite)

def get_size(texture, block=None):
    """
    Gets the total dimensions of a given texture, or simply the size of a
    particular texture block if requested.
    """
    if block is not None:
        return video_system.textures[texture].blocks[block][0].size
    else: return video_system.textures[texture].surface.get_size()

def draw_texture(target, texture, block, location):
    """
    Draws the associated texture and its block ID to the target, in situations
    where you don't want an object-oriented control system.
    
    In the event of sending this callback to the renderer stack, it is
    recommended to package it in a single-argument lambda with x for target.
    """
    target_block = video_system.textures[texture].blocks[block]
    target.blit(video_system.textures[texture].surface,
                (location[0] - target_block[1][0],
                 location[1] - target_block[1][1]),
                target_block[0])

def get_time():
    """
    Retrieves the current time of the framebuffer.
    """
    return video_system.framecounter

def set_time(value=0):
    """
    Changse the time of the framebuffer, which can alter lerp and event queue
    operations. It really shouldn't do the latter but the code was kind of
    hastily thrown together and never really fixed for lack of need.
    """
    video_system.framecounter = value

def add_renderer(callback, position=0):
    """
    Adds a callback to the rendering stack - if a position is given, it places
    it in that location (negative numbers will apply it from the right end).
    Otherwise, it is added to the end.
    
    Callbacks should have at least one argument indicating the surface to draw
    to. Callbacks with more than one argument should be wrapped in a lambda x
    statement.
    """
    video_system.renderers.append((position, callback))
    video_system.renderers.sort()

def check_renderer(callback):
    """
    Returns true or false dependent on whether the callback is in the current
    rendering stack.
    """
    for position, renderer in video_system.renderers:
        if renderer == callback:
            return True
    return False

def reset_renderers():
    """
    Dumps the entirety of the rendering stack.
    """
    video_system.renderers = []

def remove_renderer(callback, recurse=False):
    """
    Removes the callback from the rendering stack.
    """
    for renderer in video_system.renderers[:]:
        if renderer[1] == callback:
            video_system.renderers.remove(renderer)
            if not recurse: return


def get_parallax():
    """
    Frontend for the existing parallax value.
    """
    return video_system.parallax

def set_parallax(value=PARALLAX_DEFAULT):
    """
    Sets (or, with no argument, resets) the parallax value. 
    """
    if callable(value):
        video_system.parallax_callback = value
    else:
        video_system.parallax = value
        video_system.parallax_callback = None  

def save_state(clear=False):
    """
    Captures the screen's current state, and optionally blanks it for reuse.
    """
    return VideoState(video_system, clear)

def load_state(state):
    """
    Restores the screen's state.
    """
    state.restore(video_system)

def capture_buffer():
    """
    Returns a TempSprite object containing the last frame's contents.
    """
    return TempSprite(video_system.screen.copy())

def clear_screen(target, color=(0, 0, 0)):
    """
    Blanks the target surface with a solid colour.
    """
    target.fill(color)

def draw_clouds(target):
    """
    Renders a set of clouds based on the module's parallax callback. Don't
    forget to load the cloud texture first!
    """
    cloud_size = get_size(CLOUD_FILE, 0)[1]
    video_system.cloud_time += 1 + data.config.frame_skip
    cloud_time = (video_system.cloud_time * CLOUD_SPEED) % cloud_size
    if cloud_time < SCREEN_SIZE[1]:
        draw_texture(target, CLOUD_FILE, 0, ((SCREEN_SIZE[0] / 2) +
                                             (get_parallax() *
                                              CLOUD_PARALLAX), cloud_time))
    draw_texture(target, CLOUD_FILE, 0, ((SCREEN_SIZE[0] / 2) +
                                         (get_parallax() *
                                          CLOUD_PARALLAX),
                                         cloud_time - cloud_size))

def render_all(force=False):
    """
    Renders and redraws the window contents, as well as waiting to the next
    execution frame.
    """
    if force or not (data.config.frame_skip and get_time() % 2):
        if not get_time() % 60:
            data.playdata.playtime += 1
            if video_system.framerate:
                video_system.framerate.write(str(int(round(
                                  video_system.clock.get_fps()))).rjust(2)[:2],
                                             color=(0xFF, 0xFF, 0x0),
                                             clear=True)
        if video_system.parallax_callback:
            video_system.parallax = video_system.parallax_callback()
        for position, render in video_system.renderers:
            render(video_system.screen)
        if video_system.framerate:
            video_system.framerate.render(video_system.screen)
        if video_system.output:
            pygame.transform.scale(video_system.screen,
                                   (SCREEN_SIZE[0] * video_system.scale,
                                    SCREEN_SIZE[1] * video_system.scale),
                                   video_system.output)
    video_system.clock.tick(VIDEO_FRAMERATE)
    pygame.display.flip()
    video_system.framecounter += 1

data.sanity_test("screen_scale",
                 replacement=lambda x: clamp(int(x), 1, 4))
data.sanity_test("full_screen",
                 replacement=lambda x: x & pygame.FULLSCREEN)
video_system = VideoSystem()
