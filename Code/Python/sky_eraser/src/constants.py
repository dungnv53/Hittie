"""
Sky Eraser (For real this time!)
By Prerisoft

Constants
File creation date: Jan 14, 2013

THIS FILE IS LICENSED UNDER THE 3-CLAUSE BSD LICENSE. PLEASE SEE LICENSE(.txt)
FOR DETAILS.
"""

# Now I did not find anything satisfactory to load selectively, so I've
# resorted to just importing each relevant constant individually. How ugly.

from __future__ import division 

import pygame.constants
from math import pi

GAME_TITLE   = "Sky Eraser"
GAME_VERSION = "0.4.2"
GAME_VERSION_HEX = 0x00040200
OLDEST_COMPATIBLE_VERSION = 0x00040000
GAME_FILE    = "sky_eraser"
GAME_AUTHOR  = "Prerisoft"
COPYRIGHT_NOTICE = ("CREATED IN 2013\nBY PRERISOFT",
                    "SOME RIGHTS RESERVED\nSEE LICENSE FOR DETAILS",
                    "MUSIC BY CYBER-RAINFORCE\nHTTP://CYBER-RAINFORCE.NET")
CONFIG_FILE  = "sky_eraser.cfg"
PLAYDATA_FILE = "play_data.dat"

SCREEN_SIZE = (240, 320)
SCREEN_SCALE = 2

VIDEO_BASE_FILE = "base"
VIDEO_BASE_ICON = 1
FONT_FILE       = "font"
FONT_NUM        = "font_num"
VIDEO_FRAMERATE = 60
VIDEO_LOADING_POSITION = (132, 306)

# Helpful labels for the video rendering system's layer arrangement
LAYER_BACKGROUND_1 = 0x01
LAYER_BACKGROUND_2 = 0x02
LAYER_PARTICLE_LOWER = 0x03
LAYER_ITEMS = 0x04
LAYER_PLAYER = 0x06
LAYER_ENEMY = 0x05
LAYER_PARTICLE_UPPER = 0x07
LAYER_PLAYER_AURA = 0x08
LAYER_PLAYER_BULLETS = 0x09
LAYER_ENEMY_BULLETS = 0x0A
LAYER_UI = 0x0B
LAYER_OVERLAY_1 = 0x0C
LAYER_OVERLAY_2 = 0x0D

RECT_CENTER = 0x0

AUDIO_FREQUENCY         = 44100
AUDIO_BUFFER            = 1024
AUDIO_CHANNELS          = 8
CHANNEL_MUSIC           = 0
CHANNEL_BULLET          = 1
CHANNEL_ZAKO            = 2
CHANNEL_BIGBOOM         = 3
CHANNEL_MENU            = 7
AUDIO_RESERVED_CHANNELS = [CHANNEL_MUSIC, CHANNEL_BULLET, CHANNEL_ZAKO,
                           CHANNEL_BIGBOOM]
AUDIO_FRAMERATE         = 30 # Framerate used to handle music fading operations
AUDIO_LOOP              = 30 # Event type to signal a buffer end event.

PACKAGE_GRAPHICS_DIR  = "gfx"
PACKAGE_GRAPHICS_FILE = "tga"
PACKAGE_TEXTURE_FILE  = "txc"
PACKAGE_AUDIO_DIR     = "sfx"
PACKAGE_AUDIO_FILE    = "ogg"


LERP_CLAMP  = 0x1 # Linear interpolation that starts and ends at the two points
LERP_EXTRA  = 0x2 # Linear interpolation that goes beyond the two points
LERP_REPEAT = 0x4 # Linear interpolation that loops over the two points
LERP_REVERSE = 0x8 # Linear interpolation that bounces between two ends

INPUT_STATE_NULL    = 0x0 # Indication of button not active
INPUT_STATE_PRESSED = 0x1 # Indication of button being pressed this frame
INPUT_UP            = 0x01 # Indicator for moving up
INPUT_DOWN          = 0x02 # Indicator for moving down
INPUT_LEFT          = 0x04 # Indicator for moving left
INPUT_RIGHT         = 0x08 # Indicator for moving right
INPUT_FIRE          = 0x10 # Indicator for firing
INPUT_BOMB          = 0x20 # Indicator for bombing 
INPUT_AUTOFIRE      = 0x40 # Indicator for spray-firing
INPUT_PAUSE         = 0x80 # Indicator for pausing
INPUT_MENU_OK       = 0x100 # Indicator for selecting
INPUT_MENU_CANCEL   = 0x200 # Indicator for cancelling
INPUT_BUTTONS       = [INPUT_UP, INPUT_DOWN, INPUT_LEFT, INPUT_RIGHT,
                       INPUT_FIRE, INPUT_BOMB, INPUT_AUTOFIRE, INPUT_PAUSE,
                       INPUT_MENU_OK, INPUT_MENU_CANCEL]
INPUT_GROUP_ALL     = 0x3FF # Buttons involved for all operations
INPUT_GROUP_GAME    = 0x0FF # Buttons involved in gameplay
INPUT_GROUP_MENU    = 0x38F # Buttons involved in menu access
# Default keymaps
INPUT_KEY_DEFAULTS  = {
                       pygame.K_UP:INPUT_UP,
                       pygame.K_DOWN:INPUT_DOWN,
                       pygame.K_LEFT:INPUT_LEFT,
                       pygame.K_RIGHT:INPUT_RIGHT,
                       pygame.K_z:INPUT_FIRE | INPUT_MENU_OK,
                       pygame.K_x:INPUT_BOMB | INPUT_MENU_CANCEL,
                       pygame.K_c:INPUT_AUTOFIRE,
                       pygame.K_ESCAPE:INPUT_PAUSE
                       }
INPUT_JOY_DEFAULTS  = {
                       0:INPUT_FIRE | INPUT_MENU_OK,
                       1:INPUT_BOMB | INPUT_MENU_CANCEL,
                       2:INPUT_AUTOFIRE,
                       3:INPUT_PAUSE
                       }
INPUT_JOY_DEADZONE  = 0.5

MENU_SPLASH  = 0x1
MENU_TITLE   = 0x2
MENU_SCORES  = 0x4
MENU_ATTRACT = 0x8
MENU_CURSOR_SOUND = "me_cursor"
MENU_SELECT_SOUND = "me_select"
MENU_ERROR_SOUND  = "me_error"
MENU_CANCEL_SOUND = "me_cancel"
MENU_PAUSE_SOUND  = "me_pause"

DEFAULT_SCORERS = ("TEMI#MOO", "SITER SKAIN", "EDELWEISS", "CT. FROGULA", 
                   "LORDSPEC13", "ZEROGRAV-01", "ROXAHRIS", "DOCTOR WEST",
                   "THOMAS", "SAYONARA")

PLAY_AREA           = [0, 0, 282, 320]
PLAY_AREA_EXIT      = [-16, -16, 298, 336]
PLAY_AREA_EXIT_RECT = pygame.Rect((-16, -16), (314, 352))

PLAYER_SPEED           = 3
PLAYER_FOCUS_SPEED     = 2
PLAYER_START           = (141, 240)
PLAYER_HITBOX          = (4, 4)
PLAYER_TILT            = 5
PLAYER_TILT_MAX        = 17
PLAYER_ORBIT_LENGTH    = 12
PLAYER_ORBIT           = (32, 8)
ORBIT_OFFSET           = 8
PLAYER_START_TIME      = 30
PLAYER_FIRE_TIME       = 20
PLAYER_FIRE_DELAY      = 4
PLAYER_FOCUS_TIME      = 20
PLAYER_FIRE_VELOCITY   = 16
PLAYER_BULLET_HITBOX   = (12, 16)
PLAYER_FOCUS_THRESHOLD = 20
PLAYER_PROXIMITY       = 40 # Minimum distance needed for an enemy to shoot

CLOUD_SPEED      = 6
CLOUD_PARALLAX   = -0.1
CLOUD_FILE       = "layer0"
OBJ_PARALLAX     = -0.15
PARALLAX_DEFAULT = 141

PAUSE_TEXT = "     PAUSED     "
QUIT_TEXT = "ARE YOU SURE YOU\n WANT TO QUIT?"
PAUSE_LOC = (56, 128)
PAUSE_SIZE = (22, 2)

START_ENEMIES = 1 # Number of enemies available on the list
START_WAVE = 90 # Number of frames before the first wave flies
WAVE_INCREMENT = 30 # How many waves will occur before a new enemy type appears
WAVE_OFFSET = 22 # Starting wave number
WAVE_SEED = 0xDECAFBAD

RANK_WAVE_DIVIDER = 64
MAXIMUM_RANK = 4096

# Various variables for the Link Node enemy
LINKNODE_SPEED       = 4.5 #5
LINKNODE_ROTATE      = pi / 32
LINKNODE_ENTRY_RANGE = pi / 12
LINKNODE_EXIT_RANGE  = pi / 1.5
LINKNODE_CENTERPOINT = (141, 120)
LINKNODE_FIRE        = 2.75
LINKNODE_WAVE_TIME   = 90

# Various variables for the Interceptor Node enemy
INTERNODE_SPEED_RANGE = (4, 7)
INTERNODE_ENTRY_RANGE = (60, 222)
INTERNODE_ENTRY_TIME  = 8
INTERNODE_ENTRY_VAR   = (-36, 36)
INTERNODE_COUNT_RANGE = (3, 8)
INTERNODE_WAVE_TIME   = 75
INTERNODE_BASE_ANGLE  = (pi / 2)
INTERNODE_ANGLE       = pi / 7
INTERNODE_FIRE        = 3.5

# Various variables for the Soldier Node enemy (the old prefix is just legacy)
WARNODE_ENTRY_TIME    = 25
WARNODE_WAVE_TIME     = 100
WARNODE_SPEED_RANGE   = (5, 9)
WARNODE_ENTRY_RANGE   = (18, 64)
WARNODE_MAX_Y         = 116
WARNODE_SPREAD_OFFSET = pi / 16
WARNODE_SPREAD_WIDTH  = pi / 8
WARNODE_FIRE          = 3
WARNODE_SPREAD        = 2.25
