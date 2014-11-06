"""
Sky Eraser (For real this time!)
By Prerisoft

Menu Code
File creation date: Jan 19, 2013

And it apparently took me until October to realise this didn't have the right
boilerplate. Whoops!

THIS FILE IS LICENSED UNDER THE 3-CLAUSE BSD LICENSE. PLEASE SEE LICENSE(.txt)
FOR DETAILS.
"""

import video, audio, control
from constants import (INPUT_UP, INPUT_DOWN, INPUT_LEFT, INPUT_RIGHT,
                       INPUT_MENU_OK, INPUT_MENU_CANCEL, INPUT_PAUSE,
                       LERP_REVERSE, CHANNEL_MENU, MENU_CURSOR_SOUND,
                       MENU_SELECT_SOUND, MENU_CANCEL_SOUND, MENU_ERROR_SOUND,
                       FONT_FILE)

class MenuArray(object):
    """
    A basic menu system that seems to work, but don't trust it not to break on
    you with the slightest provocation. Enjoy!
    """
    def __init__(self, entries, position=0):
        self.controller = control.Controller()
        self.entries = entries
        self.position = position
        self.controller.add_press(INPUT_UP, self.move_up)
        self.controller.add_press(INPUT_DOWN, self.move_down)
        self.controller.add_press(INPUT_LEFT, self.move_left)
        self.controller.add_press(INPUT_RIGHT, self.move_right)
        self.controller.add_press(INPUT_MENU_OK, self.activate)
        self.controller.add_press(INPUT_MENU_CANCEL, self.cancel)
        self.controller.add_press(INPUT_PAUSE, self.escape)
    def ready(self):
        """
        Activates any entries on the menu for use, and unlocks the input.
        """
        self.controller.acquire()
        for entry in self.entries:
            entry.ready()
        self.entries[self.position].select()
    def destroy(self):
        """
        Releases the controller and destroys all menu entries.
        """
        self.controller.release()
        for entry in self.entries:
            entry.on_destroy()

    def move_up(self):
        """
        Changes which menu option is in focus - the one behind it, or the
        one at the bottom if it loops.
        """
        self.entries[self.position].deselect()
        self.position -= 1
        self.position %= len(self.entries)
        self.entries[self.position].select()
        audio.play_sound(MENU_CURSOR_SOUND, CHANNEL_MENU)
    def move_down(self):
        """
        Changes which menu option is in focus - the one ahead of it, or the
        one at the top if it loops.
        """
        self.entries[self.position].deselect()
        self.position += 1
        self.position %= len(self.entries)
        self.entries[self.position].select()
        audio.play_sound(MENU_CURSOR_SOUND, CHANNEL_MENU)
    def move_left(self):
        """
        Forwards the lefthand movement to the entry in question for parsing.
        """
        self.entries[self.position].on_left()
    def move_right(self):
        """
        Forwards the righthand movement to the entry in question for parsing.
        """
        self.entries[self.position].on_right()
    def activate(self):
        """
        Forwards the activation button to the entry in question for parsing.
        """
        self.entries[self.position].on_activate()
    def cancel(self):
        pass # To be defined by subclass
    def escape(self):
        pass # To be defined by subclass
    def set_position(self, position):
        """
        Moves the cursor to a specific entry.
        """
        self.entries[self.position].deselect()
        self.position = position % len(self.entries)
        self.entries[self.position].select()

    def render_all(self, target):
        """
        Calls the render method for all of its menu entries.
        """
        for entry in self.entries:
            entry.render(target)


class MenuEntry(object):
    """
    An excessively complex subobject that will probably break with the slightest
    provocation. Enjoy!
    """
    def __init__(self, position, text, on_activate=None):
        self.legend_background = video.RectSprite((position[0] -1, position[1] - 1),
                                           (0, 15))
        self.width = (len(text) * 8) + 7
        self.legend_background.width = video.Lerp(0, self.width, 10)
        self.cursor = video.RectSprite((position[0] - 1, position[1] - 1), (0, 15))
        self.text = video.Textbox(FONT_FILE,
                                  (position[0] - (len(text) * 4),
                                   position[1] - 4),
                                  size=(len(text), 1))
        self.text.write(text)
        if on_activate is not None:
            self.on_activate = on_activate
    def ready(self):
        """
        Overrides the blank text rendering method with the textbox's actual
        method.
        """
        self.text_render = self.text.render

    def select(self):
        """
        Animation handling for when the entry is selected.
        """
        self.cursor.width = video.Lerp(0, self.width, 10)
        color = video.Lerp(0, 160, 30, extend=LERP_REVERSE)
        self.cursor.color = lambda x=color: (x(), x(), x())
    def deselect(self):
        """
        Animation handling for when the entry is no longer selected.
        """
        self.cursor.width = video.Lerp(self.width, 0, 10)
        color = video.Lerp(self.cursor.color[0], 0, 10)
        self.cursor.color = lambda x=color: (x(), x(), x())
    def on_left(self):
        """
        Functionality for overriding in child classes.
        """
        pass
    def on_right(self):
        """
        Functionality for overriding in child classes.
        """
        pass
    def on_activate(self):
        """
        Functionality for overriding in object creation or child classes.
        """
        audio.play_sound(MENU_ERROR_SOUND, CHANNEL_MENU)
    def on_destroy(self):
        """
        Functionality for the menu to disappear before it's removed.
        """
        self.legend_background.width = video.Lerp(self.legend_background.width, 0, 10)
        self.cursor.width = 0
        del self.text_render
    
    def text_render(self, target):
        pass # To be overridden during runtime
    
    def render(self, target):
        self.legend_background.render(target)
        self.cursor.render(target)
        self.text_render(target)

sfx_resources = [MENU_CURSOR_SOUND, MENU_SELECT_SOUND, MENU_CANCEL_SOUND,
                 MENU_ERROR_SOUND]
