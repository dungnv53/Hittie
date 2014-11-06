"""
Sky Eraser (For real this time!)
By Prerisoft

Title Screen and Assorted
File creation date: Jun 18, 2013

Made following the realisation of a potential cross-dependency issue that'd
come up during development of the menu code. Hurr.

THIS FILE IS LICENSED UNDER THE 3-CLAUSE BSD LICENSE. PLEASE SEE LICENSE(.txt)
FOR DETAILS.
"""

from __future__ import division, print_function

import sys, pygame.locals
import data, video, audio, control, scene, event, menu, settings
from constants import (LAYER_BACKGROUND_1, LAYER_BACKGROUND_2, LAYER_UI,
                       LAYER_OVERLAY_1, LAYER_OVERLAY_2,
                       
                       MENU_SELECT_SOUND, MENU_CANCEL_SOUND, CHANNEL_MENU,
                       FONT_FILE, COPYRIGHT_NOTICE, INPUT_MENU_CANCEL,
                       INPUT_PAUSE, LERP_REPEAT, SCREEN_SIZE)

class TitleScene(scene.BaseScene):
    """
    Menu functionality for Sky Eraser. Provides the basis for all menu
    functions.
    """
    sfx_resources = menu.sfx_resources
    def __init__(self, show_splash=True, position=0):
        video.load_texture("title")
        video.load_texture("title_glow")
        self.title_screen = video.Sprite("title")
        self.glow_1 = video.Sprite("title_glow", 74)
        self.glow_1.y = -90
        self.glow_1.block = video.Lerp(0, 249, start_time=-74, end_time=175,
                                       extend=LERP_REPEAT)
        self.glow_2 = video.Sprite("title_glow", 176)
        self.glow_2.y = 115
        self.glow_2.block = video.Lerp(249, 0, start_time=-74, end_time=175,
                                       extend=LERP_REPEAT)
        video.add_renderer(self.glow_1.render, LAYER_BACKGROUND_1)
        video.add_renderer(self.glow_2.render, LAYER_BACKGROUND_1)
        video.add_renderer(self.title_screen.render, LAYER_BACKGROUND_2)
        self.scoreboard = None
        self.copyright = None
        self.copyright_no = 0
        self.fader = None
        self.event_queue = event.DynamicQueue()
        self.event_sequence = event.StaticQueue()
        if show_splash:
            self.load_splash_screen()
        else:
            self.fade_in()

    def load_splash_screen(self):
        """
        Creates the splash screen.
        """
        video.load_texture("prerisoft")
        self.splash_screen = video.AlphaSprite("prerisoft")
        self.splash_screen.a = video.Lerp(0, 255, time=15)
        self.event_sequence.set_queue((52, self.fade_splash_screen),
                                   (90, self.unload_splash_screen))
        self.screen_clearer = lambda x: video.clear_screen(x, (255, 255, 255))
        video.add_renderer(self.screen_clearer, LAYER_OVERLAY_1)
        video.add_renderer(self.splash_screen.render, LAYER_OVERLAY_2)

    def fade_splash_screen(self):
        """
        Fades out the splash screen.
        """
        video.remove_renderer(self.screen_clearer)
        del self.screen_clearer 
        self.splash_screen.a = video.Lerp(255, 0, time=35)

    def unload_splash_screen(self):
        """
        Destroys the splash screen.
        """
        video.remove_renderer(self.splash_screen.render)
        del self.splash_screen
        video.unload_texture("prerisoft")
        self.make_background_text()
        self.load_main_menu()

    def fade_in(self):
        """
        Initialises the scene with a black fading overlay.
        """
        fader = video.AlphaRect((0, 0), SCREEN_SIZE)
        fader.a = video.Lerp(255, 0, 30)
        video.add_renderer(fader.render, LAYER_OVERLAY_1)
        self.event_sequence.set_queue((30,
                                  lambda: video.remove_renderer(fader.render)),
                                      (30, self.make_background_text),
                                      (30, self.load_main_menu))

    def load_main_menu(self, position=0):
        """
        Creates the main menu.
        """
        self.menu = menu.MenuArray((menu.MenuEntry((120, 204),
                                                   "LAUNCH".center(8),
                                                   self.play_callback),
                                    menu.MenuEntry((120, 220),
                                                   "SCORES".center(8),
                                                   self.score_callback),
                                    menu.MenuEntry((120, 236),
                                                   "SETTINGS".center(8),
                                                   self.settings_callback),
                                    menu.MenuEntry((120, 252),
                                                   "EXIT".center(8),
                                                   self.exit_callback),
                                    ), position)
        self.menu.controller.remove_press(INPUT_MENU_CANCEL, self.menu.cancel)
        self.menu.controller.remove_press(INPUT_PAUSE, self.menu.escape)
        self.menu.controller.add_press(INPUT_MENU_CANCEL, self.pause_callback)
        self.menu.controller.add_press(INPUT_PAUSE, self.pause_callback)
        # Hack because I'm too lazy to make a subclass 
        self.event_queue.add_event(video.get_time() + 15, self.menu.ready)
        video.add_renderer(self.menu.render_all, LAYER_UI)

    def make_background_text(self):
        """
        Initialises the top score listing and the footer message.
        """
        if not (self.scoreboard or self.copyright):
            self.scoreboard = video.Textbox(FONT_FILE, (48, 8), (18, 1))
            self.scoreboard.write("TOP", (0, 0), (0xFF, 0x80, 0x80))
            self.scoreboard.write(str(data.playdata.scores[0].score).rjust(12), (6, 0))
            self.copyright = video.Textbox(FONT_FILE, (8, 296), (28, 2))
            self.set_copyright()
            video.add_renderer(self.scoreboard.render, LAYER_UI)
            video.add_renderer(self.copyright.render, LAYER_UI)

    def set_copyright(self):
        """
        Draws a new footer message, then sets an event for itself to draw a new
        one.
        """
        self.copyright.write(center(COPYRIGHT_NOTICE[self.copyright_no],
                                    28))
        self.copyright_no += 1
        self.copyright_no %= len(COPYRIGHT_NOTICE)
        self.event_queue.add_event(video.get_time() + 300, self.set_copyright)

    def play_callback(self):
        """
        Closes the menu and begins the process to go to the game scene.
        """
        audio.play_sound(MENU_SELECT_SOUND, CHANNEL_MENU)
        self.event_queue.add_event(video.get_time(), self.menu.destroy)
        self.event_queue.add_event(video.get_time() + 15, self.play_event)
        self.fader = video.AlphaRect((0, 0), SCREEN_SIZE)
        self.fader.a = video.Lerp(0, 255, 15)
        video.add_renderer(self.fader.render, LAYER_OVERLAY_1)
    def play_event(self):
        """
        Loads and runs the game scene.
        """
        video.remove_renderer(self.copyright.render)
        import game
        scene.load_scene(game.GameScene)
    
    def score_callback(self):
        """
        Closes the menu and begins the process to go to the high score table.
        """
        audio.play_sound(MENU_SELECT_SOUND, CHANNEL_MENU)
        self.event_queue.add_event(video.get_time(), self.menu.destroy)
        self.event_queue.add_event(video.get_time() + 15, self.score_event)
        self.fader = video.AlphaRect((0, 0), SCREEN_SIZE)
        self.fader.a = video.Lerp(0, 255, 15)
        video.add_renderer(self.fader.render, LAYER_OVERLAY_1)
    def score_event(self):
        """
        Loads and runs the high score table.
        """
        video.remove_renderer(self.copyright.render)
        import score
        scene.load_scene(score.ScoreScene)

    def settings_callback(self):
        """
        Closes the main or input menu and goes to the settings menu.
        """
        audio.play_sound(MENU_SELECT_SOUND, CHANNEL_MENU)
        self.event_queue.add_event(video.get_time(), self.menu.destroy)
        self.event_queue.add_event(video.get_time() + 15, self.settings_event)
    def settings_event(self):
        """
        Opens the settings menu.
        """
        video.remove_renderer(self.menu.render_all)
        self.menu = settings.SettingsMenu(self.settings_return, self.input_callback)
        self.event_queue.add_event(video.get_time() + 15, self.menu.ready)
        video.add_renderer(self.menu.render_all, LAYER_UI)
    def settings_return(self):
        """
        Returns to the main menu from the settings menu.
        """
        audio.play_sound(MENU_CANCEL_SOUND, CHANNEL_MENU)
        this_renderer = self.menu.render_all
        self.event_queue.add_event(video.get_time(), self.menu.destroy)
        self.event_queue.add_event(video.get_time() + 15, lambda: video.remove_renderer(this_renderer))
        self.event_queue.add_event(video.get_time() + 15, lambda: self.load_main_menu(position=2))
    
    def input_callback(self):
        """
        Closes the settings menu and goes to the input menu.
        """
        audio.play_sound(MENU_SELECT_SOUND, CHANNEL_MENU)
        self.event_queue.add_event(video.get_time(), self.menu.destroy)
        self.event_queue.add_event(video.get_time() + 15, self.input_event)
    def input_event(self):
        """
        Opens the input menu.
        """
        video.remove_renderer(self.menu.render_all)
        self.menu = settings.InputMenu(self.input_return, self.event_queue)
        self.event_queue.add_event(video.get_time() + 15, self.menu.ready)
        video.add_renderer(self.menu.render_all, LAYER_UI)
    def input_return(self):
        """
        Returns to the settings menu from the input menu.
        """
        audio.play_sound(MENU_CANCEL_SOUND, CHANNEL_MENU)
        self.event_queue.add_event(video.get_time(), self.menu.destroy)
        self.event_queue.add_event(video.get_time() + 15, self.settings_event)
    
    def pause_callback(self):
        """
        Changes the active cursor to the exit entry if the cancel or pause
        button's pressed on the main menu, or activates it if it's already
        there. 
        """
        if self.menu.position == 3:
            self.exit_callback()
        else:
            audio.play_sound(MENU_CANCEL_SOUND, CHANNEL_MENU)
            self.menu.set_position(3)

    def exit_callback(self):
        """
        Quits the game.
        """
        audio.play_sound(MENU_CANCEL_SOUND, CHANNEL_MENU)
        self.event_queue.add_event(video.get_time(), self.menu.destroy)
        self.event_queue.add_event(video.get_time() + 15, sys.exit)

    def update(self):
        """
        Pumps the contents of both event queues for every frame.
        """
        self.event_sequence.pump()
        self.event_queue.pump()

    def close(self):
        """
        Thread closing method - destroys all the title graphics.
        """
        video.remove_renderer(self.glow_1.render)
        video.remove_renderer(self.glow_2.render)
        video.remove_renderer(self.title_screen.render)
        if self.scoreboard:
            video.remove_renderer(self.scoreboard.render)
        if self.menu:
            video.remove_renderer(self.menu.render_all)
        if self.fader:
            video.remove_renderer(self.fader.render)

#########
# Misc. #
#########

def center(string, size):
    """
    Replacement string method to handle newlines.
    """
    return "\n".join(i.center(size) for i in string.split("\n"))
