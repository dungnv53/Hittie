"""
Sky Eraser (For real this time!)
By Prerisoft

Settings Menus
File creation date: Dec 23, 2013

The settings code was getting a bit big, so I moved it all here for the title
screen and game to access separately.

THIS FILE IS LICENSED UNDER THE 3-CLAUSE BSD LICENSE. PLEASE SEE LICENSE(.txt)
FOR DETAILS.
"""

from __future__ import division, print_function

import sys, pygame.key, pygame.locals
import calcs, data, video, audio, control, menu

from constants import (MENU_CURSOR_SOUND, MENU_SELECT_SOUND, MENU_ERROR_SOUND,
                       INPUT_UP, INPUT_DOWN, INPUT_LEFT, INPUT_RIGHT,
                       INPUT_FIRE, INPUT_BOMB, INPUT_AUTOFIRE,
                       INPUT_MENU_OK, INPUT_MENU_CANCEL, INPUT_PAUSE,
                       INPUT_GROUP_ALL, INPUT_GROUP_GAME, INPUT_GROUP_MENU,
                       CHANNEL_MENU, FONT_FILE, LERP_REVERSE)

############
# Settings #
############

class VolumeControl(menu.MenuEntry):
    """
    Menu Entry object for displaying volume controls.
    """
    def __init__(self, position, text, start_value=5, callback=None):
        menu.MenuEntry.__init__(self, position, text.ljust(22))
        if callback is not None:
            self.callback = callback
        self.value = start_value
        self.edit_value()
    def edit_value(self):
        """
        Draws the bar's current state.
        """
        self.text.write("|" * self.value, (12, 0), (0x80, 0xFF, 0x00))
        self.text.write("|" * (10 - self.value), (12 + self.value, 0),
                        (0xA0, 0x00, 0x00))
    def on_left(self):
        """
        Decreases the menu entry's bar.
        """
        audio.play_sound(MENU_CURSOR_SOUND, CHANNEL_MENU)
        self.value = max(0, self.value - 1)
        self.edit_value()
        self.callback(self.value)
    def on_right(self):
        """
        Increases the menu entry's bar.
        """
        audio.play_sound(MENU_CURSOR_SOUND, CHANNEL_MENU)
        self.value = min(10, self.value + 1)
        self.edit_value()
        self.callback(self.value)
    def callback(self, x):
        """
        Blank callback - like on_activate() but with an argument to discard.
        """
        pass
    def on_activate(self):
        """
        Blank activation method. Does absolutely nothing, not even playing an
        error sound as would be normal.
        """
        pass

class MenuSwitch(menu.MenuEntry):
    """
    Menu Entry object for providing a changable list of elements.
    """
    def __init__(self, position, text, values, start_value=0, callback=None):
        menu.MenuEntry.__init__(self, position, text.ljust(22))
        if callback is not None:
            self.callback = callback
        self.value_list = values
        self.value = start_value
        self.blank_area = 22 - len(text)
        self.edit_value(False)
    def edit_value(self, active=True):
        """
        Writes a new value to the button's text object.
        """
        if active:
            text = "({0})".format(self.value_list[self.value][0])
        else:
            text = " {0} ".format(self.value_list[self.value][0])
        self.text.write(text.rjust(self.blank_area),
                        (22 - self.blank_area, 0))
    def get_value(self):
        """
        Returns the entry's current value.
        """
        return self.value_list[self.value][1]
    
    def select(self):
        """
        Selects the cursor as normal, and creates arrows as well.
        """
        menu.MenuEntry.select(self)
        self.edit_value(active=True)
    def deselect(self):
        """
        Deselects the cursor as normal, and erases the arrows as well.
        """
        menu.MenuEntry.deselect(self)
        self.edit_value(active=False)
    def on_left(self):
        """
        Moves the value to the previous in the list, looping around if it
        passes the threshold.
        """
        audio.play_sound(MENU_CURSOR_SOUND, CHANNEL_MENU)
        self.value -= 1
        self.value %= len(self.value_list)
        self.edit_value()
        self.callback(self.value_list[self.value][1])
    def on_right(self):
        """
        Moves the value to the next in the list, looping around if it passes
        the threshold.
        """
        audio.play_sound(MENU_CURSOR_SOUND, CHANNEL_MENU)
        self.value += 1
        self.value %= len(self.value_list)
        self.edit_value()
        self.callback(self.value_list[self.value][1])
    def callback(self, x):
        """
        Blank callback - like on_activate() but with an argument to discard.
        """
        pass
    def on_activate(self):
        """
        Blank activation method. Does absolutely nothing, not even playing an
        error sound as would be normal.
        """
        pass

class SettingsMenu(menu.MenuArray):
    """
    Settings Menu. Handles audio, video, and input configurations.
    """
    def __init__(self, exit_callback, input_callback,
                 from_pause=False, start_y=180):
        self.pause_menu = from_pause
        menu.MenuArray.__init__(self, (VolumeControl((120, start_y),
                                                     "SFX VOLUME",
                                start_value=int(data.config.sound_volume * 10),
                            callback=lambda x: audio.set_volume(sound=x / 10)),
                                       VolumeControl((120, start_y + 16),
                                                     "BGM VOLUME",
                                start_value=int(data.config.music_volume * 10),
                            callback=self.adjust_volume),
                                       MenuSwitch((120, start_y + 32),
                                                  "SCREEN SCALE",
                                                   (("1X", 1), ("2X", 2),
                                                    ("3X", 3), ("4X", 4)),
                                      start_value=data.config.screen_scale - 1,
                                                 callback=self.adjust_display),
                                       MenuSwitch((120, start_y + 48),
                                                  "FULLSCREEN",
                                                  (("OFF", 0),
                                                   ("ON", pygame.FULLSCREEN)),
                                     start_value=bool(data.config.full_screen),
                                                 callback=self.adjust_display),
                                       MenuSwitch((120, start_y + 64),
                                                  "FRAME SKIP",
                                                  (("OFF", 0), ("ON", 1)),
                                            start_value=data.config.frame_skip,
                                            callback=self.toggle_frameskip),
                                       menu.MenuEntry((100, start_y + 80),
                                                      "INPUT ASSIGNMENT&",
                                                      input_callback),
                                       menu.MenuEntry((56, start_y + 96),
                                                      "RETURN",
                                                      self.exit_cmd),
                                       ))
        self.exit_callback = exit_callback
        self.cancel = self.exit_cmd
        self.escape = self.exit_cmd

    def adjust_volume(self, x):
        """
        Change the sound and music volume (keeping in mind if this menu was
        called from the pause screen).
        """
        if self.pause_menu:
            audio.set_volume(music=x / 20)
        else:
            audio.set_volume(music=x / 10)
    
    def adjust_display(self, x):
        """
        Resets the window configuration to apply the settings changes.
        """
        data.config.screen_scale = self.entries[2].get_value()
        data.config.full_screen = self.entries[3].get_value()
        video.create_window(data.config.screen_scale)
    
    def toggle_frameskip(self, value):
        """
        Changes the frameskip to the equivalent value.
        """
        data.config.frame_skip = value
    
    def exit_cmd(self):
        """
        Pushes all changes to the config file, then returns to the old menu.
        """
        data.config.sound_volume = self.entries[0].value / 10
        data.config.music_volume = self.entries[1].value / 10
        data.config.frame_skip = self.entries[4].get_value()
        self.exit_callback()

#########
# Input #
#########

class InputDefinition(menu.MenuEntry):
    """
    Menu Entry object that provides the keyboard and joystick assignments for
    the button it's assigned to, as well as storing relevant reassignment data.
    """
    def __init__(self, position, name, button, group, callback):
        menu.MenuEntry.__init__(self, position, name.ljust(24))
        self.button = button # Record button
        self.group = group # Record the button's active group
        self.callback = callback # Get the menu's assignment callback
    def active_select(self):
        """
        Change the colour of the menu entry's cursor to indicate its activity.
        """
        color = video.Lerp(0, 200, 30, extend=LERP_REVERSE)
        self.cursor.color = lambda x=color: (x(), x() / 1.5, 0)
    def write_entries(self):
        """
        Rewrites its keyboard and joystick assignments.
        """
        # Draw keyboard button assignment, trim it if too long
        key_string = pygame.key.name(control.get_key(self.button)).upper()
        if len(key_string) > 10:
            key_string = key_string[:9] + "&"
        self.text.write(key_string.ljust(10), (9, 0))
        # Draw joystick button assignment
        if self.button < INPUT_FIRE:
            # Directional buttons are hardlocked to axis and hat switch.
            self.text.write(".<>.{...}"[self.button], (20, 0),
                            (0x80, 0x80, 0x80))
        else:
            # If there's a joystick, this is rendered properly. If not,
            # the buttons are greyed out.
            color = ((0xFF, 0xFF, 0xFF) if control.control_system.joystick
                     else (0x80, 0x80, 0x80))
            self.text.write(str(control.get_button(self.button)),
                            (20, 0), color)
    def ready(self):
        """
        Draws its assignment information after readying as normal.
        """
        menu.MenuEntry.ready(self)
        self.write_entries()
    def on_activate(self):
        """
        Passes itself to the assigned callback.
        """
        self.callback(self)

class DetectorButton(menu.MenuEntry):
    """
    Button that changes text when selected, showing the joystick's status when
    not active and offering to redetect it when active.
    """
    def __init__(self, position, assignments):
        menu.MenuEntry.__init__(self, position, "CHECKING JOYSTICK")
        self.assignments = assignments
    def ready(self):
        if control.control_system.joystick:
            self.text.write("JOYSTICK DETECTED")
        else:
            self.text.write("NO JOYSTICK FOUND", color=(0xFF, 0x40, 0x40))
        menu.MenuEntry.ready(self)
    def select(self):
        self.text.write("REDETECT JOYSTICK", color=(0xFF, 0xDB, 0x35))
        menu.MenuEntry.select(self)
    def deselect(self):
        self.ready()
        menu.MenuEntry.deselect(self)
    def on_activate(self):
        control.detect_joystick()
        audio.play_sound(MENU_SELECT_SOUND
                         if control.control_system.joystick else
                         MENU_ERROR_SOUND, CHANNEL_MENU)
        for button, assignment in self.assignments.items():
            if button >= INPUT_FIRE:
                assignment.write_entries()
        self.ready()

class InputMenu(menu.MenuArray):
    """
    Menu array object with methods for managing input assignments.
    """
    def __init__(self, settings_callback, event_queue):
        # Create assignments with equivalent button values for easy lookup
        self.assignments = {INPUT_UP:
                            InputDefinition((120, 68), "UP", INPUT_UP,
                                            INPUT_GROUP_ALL,
                                            self.assign_button),
                            INPUT_DOWN:
                            InputDefinition((120, 84), "DOWN", INPUT_DOWN,
                                            INPUT_GROUP_ALL,
                                            self.assign_button),
                            INPUT_LEFT:
                            InputDefinition((120, 100), "LEFT", INPUT_LEFT,
                                            INPUT_GROUP_ALL,
                                            self.assign_button),
                            INPUT_RIGHT:
                            InputDefinition((120, 116), "RIGHT", INPUT_RIGHT,
                                            INPUT_GROUP_ALL,
                                            self.assign_button),
                            INPUT_FIRE:
                            InputDefinition((120, 132), "FIRE", INPUT_FIRE,
                                            INPUT_GROUP_GAME,
                                            self.assign_button),
                            INPUT_BOMB:
                            InputDefinition((120, 148), "BOMB", INPUT_BOMB,
                                            INPUT_GROUP_GAME,
                                            self.assign_button),
                            INPUT_AUTOFIRE:
                            InputDefinition((120, 164), "AUTOFIRE",
                                            INPUT_AUTOFIRE, INPUT_GROUP_GAME,
                                            self.assign_button),
                            INPUT_MENU_OK:
                            InputDefinition((120, 180), "SELECT",
                                            INPUT_MENU_OK, INPUT_GROUP_MENU,
                                            self.assign_button),
                            INPUT_MENU_CANCEL:
                            InputDefinition((120, 196), "CANCEL",
                                            INPUT_MENU_CANCEL,
                                            INPUT_GROUP_MENU,
                                            self.assign_button),
                            INPUT_PAUSE:
                            InputDefinition((120, 212), "PAUSE", INPUT_PAUSE,
                                            INPUT_GROUP_ALL,
                                            self.assign_button)
                            }
        # Initialise menu array, with contents of assignments list
        menu.MenuArray.__init__(self, (self.assignments[INPUT_UP],
                                       self.assignments[INPUT_DOWN],
                                       self.assignments[INPUT_LEFT],
                                       self.assignments[INPUT_RIGHT],
                                       self.assignments[INPUT_FIRE],
                                       self.assignments[INPUT_BOMB],
                                       self.assignments[INPUT_AUTOFIRE],
                                       self.assignments[INPUT_MENU_OK],
                                       self.assignments[INPUT_MENU_CANCEL],
                                       self.assignments[INPUT_PAUSE],
                                       # Enforce layout because dict hashes
                                       DetectorButton((92, 236),
                                                      self.assignments),
                                       menu.MenuEntry((84, 252),
                                                      "SET TO DEFAULTS",
                                                      self.reset_controls),
                                       menu.MenuEntry((48, 268),
                                                      "RETURN",
                                                      settings_callback),
                                       ))
        
        # Objects for displaying the legend for the key/joy assignments 
        self.legend_background = video.RectSprite((155, 51),
                                           (0, 15))
        self.legend_background.width = video.Lerp(0, 127, 10)
        self.legend_text = video.Textbox(FONT_FILE, (96, 48), size=(15, 1))
        
        # Duplicate event queue for controller switching
        self.event_queue = event_queue
        
    def assign_button(self, assignment):
        """
        Callback for the input assignment menu entry, seizing control from the
        menu's controller and creating an open collector object for retrieving
        the next key or button press.
        """
        audio.play_sound(MENU_SELECT_SOUND, CHANNEL_MENU)
        self.collector = control.OpenCollector() # Collector for getting values
        self.collector.add_press(lambda x, y:
                                 self.complete_assignment(assignment, x, y))
        # Seize control from the normal menu controller
        self.event_queue.add_event(video.get_time(), self.controller.release)
        self.event_queue.add_event(video.get_time()+1, self.collector.acquire)
        assignment.active_select() # Color entry to indicate active use

    def complete_assignment(self, assignment, event, value):
        """
        Callback for the input assignment's open collector object, assigning
        the value if valid and then returning control to the menu's normal
        controller.
        """
        if (event == pygame.KEYDOWN and
            calcs.popcount(control.check_key_assignment(
                value, assignment.group)) < 2):
            # Above: popcount is to make sure the key used is assigned to
            # multiple other keys (such as overlapping menu, game keys)
            audio.play_sound(MENU_SELECT_SOUND, CHANNEL_MENU)
            # Save the old key
            old_key = control.get_key(assignment.button)
            # If the new key's in use, retrieve it while assigning a new value
            opposite_button = control.assign_key(assignment.button, value,
                                                 assignment.group)
            self.assignments[assignment.button].write_entries()
            if opposite_button:
                # Set the equivalent button to the active key's prior value,
                # effectively switching them
                control.assign_key(opposite_button, old_key,
                                   assignment.group)
                # Write opposite key's value to equivalent menu entry
                self.assignments[opposite_button].write_entries()
        elif (event == pygame.JOYBUTTONDOWN and
              assignment.button >= INPUT_FIRE and
              calcs.popcount(control.check_joy_assignment(
                value, assignment.group)) < 2):
            # as the keyboard event popcount info, plus making sure the user
            # isn't trying to assign a joystick button to a direction
            # (those are locked to axis and hat switch values for simplicity)
            audio.play_sound(MENU_SELECT_SOUND, CHANNEL_MENU)
            # Save the old button
            old_joy = control.get_button(assignment.button)
            # If the button's in use, retrieve it while assigning a new value
            opposite_button = control.assign_joy(assignment.button, value,
                                              assignment.group)
            if opposite_button:
                # Set the equivalent virtual button to the active button's
                # prior value, effectively switching them
                control.assign_joy(opposite_button, old_joy,
                                    assignment.group)
                # Write opposite button's value to equivalent menu entry
                self.assignments[opposite_button].write_entries()
        else:
            # Broken call, more than one other assignment, or joy directions 
            audio.play_sound(MENU_ERROR_SOUND, CHANNEL_MENU)
        assignment.write_entries() # Write new value to menu entry
        assignment.select() # Switch back to regular selection animation
        # Change back to the menu's controller
        self.collector.press_callbacks = set()
        self.event_queue.add_event(video.get_time(), self.collector.release)
        self.event_queue.add_event(video.get_time(), self.controller.acquire)
        del self.collector
        
    def reset_controls(self):
        """
        Callback for calling the controller API's reset method, and then
        updates all of the menu entries to reflect the changes.
        """
        audio.play_sound(MENU_SELECT_SOUND, CHANNEL_MENU)
        control.reset_controls()
        for entry in self.assignments.values():
            entry.write_entries()

    def ready(self):
        """"
        Creates a legend for the button assignments, then continues as normal.
        """
        self.legend_text.write("KEY         JOY", color=(0xFF, 0xAB, 0x9))
        menu.MenuArray.ready(self)
    def destroy(self):
        """
        Destroys the legend for the button assignments, then continues as
        normal.
        """
        self.legend_text.write("", clear=True)
        self.legend_background.width = video.Lerp(127, 0, 10)
        menu.MenuArray.destroy(self)
    def render_all(self, target):
        """
        Renders the legend for the button assignments, then continues as
        normal.
        """
        self.legend_background.render(target)
        self.legend_text.render(target)
        menu.MenuArray.render_all(self, target)
