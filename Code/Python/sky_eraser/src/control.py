"""
Sky Eraser (For real this time!)
By Prerisoft

Input Control
File creation date: Jan 18, 2013

This was pretty much entirely recycled from Valiancer/Sky Eraser b's code. I
may in fact update it if I come up with something better (which I'll be testing
in Valiancer).

THIS FILE IS LICENSED UNDER THE 3-CLAUSE BSD LICENSE. PLEASE SEE LICENSE(.txt)
FOR DETAILS.
"""

from __future__ import print_function
import struct, pygame.joystick, pygame.mouse, pygame.event
from weakref import WeakSet
import calcs, data, audio
from constants import (INPUT_UP, INPUT_DOWN, INPUT_LEFT, INPUT_RIGHT,
                       INPUT_BUTTONS, INPUT_GROUP_GAME, INPUT_GROUP_MENU,
                       INPUT_KEY_DEFAULTS, INPUT_JOY_DEFAULTS, AUDIO_LOOP)

class ControlSystem(object):
    """
    The internal system for handling player input variables.
    """
    def __init__(self):
        """
        Initializes what doesn't require other systems to be ready.
        """
        self.acquired_controllers = set()
        self.collectors = set()
        self.override_controller = None
        self.playback_unpacker = struct.Struct("B")
        self.input_methods = [key_pump]
        self.key_assign = data.config.key_bindings
        self.joy_assign = data.config.joy_buttons
        #self.press_callbacks = dict((i, WeakSet()) for i in INPUT_BUTTONS)
        #self.release_callbacks = dict((i, WeakSet()) for i in INPUT_BUTTONS)
        self.joystick = None
        self.joy_deadzone = data.config.joy_deadzone
        self.joy_axis_state = [0, 0]
        self.joy_hat_state = (0, 0)
        self.focus_state = False
        self.exit_callback = lambda: None
        self.exit_parameters = {}
        self.state = dict((i, False) for i in INPUT_BUTTONS)
    def start(self):
        """
        Initializes everything that requires outside preparation beforehand.
        """
        pygame.mouse.set_visible(False)
        pygame.event.set_allowed(None)
        pygame.event.set_allowed([pygame.KEYDOWN,
                              pygame.KEYUP,
                              pygame.QUIT,
                              pygame.JOYAXISMOTION,
                              pygame.JOYHATMOTION,
                              pygame.JOYBUTTONDOWN,
                              pygame.JOYBUTTONUP
                              ])

class Controller(object):
    """
    A standard handler for acquiring input.
    """
    def __init__(self):
        self.clear_callbacks()
    def acquire(self):
        control_system.acquired_controllers.add(self)
    def override(self):
        control_system.override_controller = self
    def release(self):
        if control_system.override_controller == self:
            control_system.override_controller = None
#        for i in self.press_callbacks:
#            self.press_callbacks[i] = set()
#        for i in self.release_callbacks:
#            self.press_callbacks[i] = set()
        control_system.acquired_controllers.discard(self)
    def add_press(self, button, callback):
        self.press_callbacks[button].add(callback)
    def add_release(self, button, callback):
        self.release_callbacks[button].add(callback)
    def remove_press(self, button, callback):
        self.press_callbacks[button].remove(callback)
    def remove_release(self, button, callback):
        self.release_callbacks[button].remove(callback)
    def clear_callbacks(self, button=None):
        if button:
            self.press_callbacks[button] = set()
            self.release_callbacks[button] = set()
        else:
            self.press_callbacks = dict((i, set()) for i in INPUT_BUTTONS)
            self.release_callbacks = dict((i, set()) for i in INPUT_BUTTONS)
    def get_functional(self):
        return ((self in control_system.acquired_controllers
               and not control_system.override_controller) or
               (control_system.override_controller == self))
    def get_pressed(self, button):
        return (self.get_functional() and
               control_system.state[button])

class RecordingController(Controller):
    """
    A controller that records its inputs for later playback.
    """
    def increment(self):
        pass    # TODO: Make this (you had a good idea going with tempfiles and
                # binary flags. Do that)

class RecordPlayer(Controller):
    """
    A pseudocontroller that reads its inputs from a file.
    """
    pass    # TODO: Make this too. Second verse, same as the first.

class OpenCollector(Controller):
    """
    Special controller object that will take any keyboard/joystick input.
    """
    def __init__(self):
        self.clear_callbacks()
    def acquire(self):
        control_system.collectors.add(self)
    def release(self):
        control_system.collectors.remove(self)
    def override(self):
        raise NotImplementedError
    def add_press(self, callback):
        self.press_callbacks.add(callback)
    def remove_press(self, callback):
        self.press_callbacks.remove(callback)
    def add_release(self, callback):
        self.release_callbacks.add(callback)
    def remove_release(self, callback):
        self.release_callbacks.remove(callback)
    def clear_callbacks(self):
        self.press_callbacks = set()
        self.release_callbacks = set()
    def get_pressed(self):
        raise NotImplementedError

#------------------------------------------------------------------------------ 
    
def pump():
    """
    Retrieves current keyboard and/or gamepad states.
    """
    # Test for window focus...
    control_system.focus_state = pygame.key.get_focused()
    for i in pygame.event.get(AUDIO_LOOP): audio.signal_loop()
    for i in control_system.input_methods: i()
    for i in pygame.event.get(pygame.QUIT): # Hacky but it works, I guess
        # Call the exit callback...
        control_system.exit_callback(**control_system.exit_parameters)
        break # And then dump the rest.

def key_pump():
    """
    Reads and handles keyboard input.
    """
    for i in pygame.event.get(pygame.KEYDOWN): # Test for new key presses...
        for j in control_system.collectors:
            for k in j.press_callbacks:
                k(pygame.KEYDOWN, i.key)
        if i.key in control_system.key_assign:
            press(control_system.key_assign[i.key])
    for i in pygame.event.get(pygame.KEYUP): # Test for key releases...
        for j in control_system.collectors:
            for k in j.release_callbacks:
                k(pygame.KEYUP, i.key)
        if i.key in control_system.key_assign:
            release(control_system.key_assign[i.key])

def joy_pump():
    """
    Reads and handles joystick input. Directional control is hardwired.
    """
    for i in pygame.event.get(pygame.JOYAXISMOTION):
        if i.axis == 0:
            joy_axis_handler(i, INPUT_LEFT, INPUT_RIGHT)
        elif i.axis == 1:
            joy_axis_handler(i, INPUT_UP, INPUT_DOWN)
        else:
            pass
            
    for i in pygame.event.get(pygame.JOYHATMOTION):
        if i.value != control_system.joy_hat_state:
            hat_axis_handler(control_system.joy_hat_state[0], i.value[0],
                             INPUT_LEFT, INPUT_RIGHT)
            hat_axis_handler(control_system.joy_hat_state[1], i.value[1],
                             INPUT_DOWN, INPUT_UP)
            control_system.joy_hat_state = i.value
            
    for i in pygame.event.get(pygame.JOYBUTTONDOWN):
        for j in control_system.collectors:
            for k in j.press_callbacks:
                k(pygame.JOYBUTTONDOWN, i.button)
        if i.button in control_system.joy_assign:
            press(control_system.joy_assign[i.button])
    for i in pygame.event.get(pygame.JOYBUTTONUP):
        for j in control_system.collectors:
            for k in j.release_callbacks:
                k(pygame.JOYBUTTONUP, i.button)
        if i.button in control_system.joy_assign:
            release(control_system.joy_assign[i.button])

def joy_axis_handler(event, neg, pos):
    """
    Ugly, bruteforced code, but it works.
    """
    if ((control_system.joy_axis_state[event.axis] != -1)
        and (event.value < -control_system.joy_deadzone)):
        press(neg)
        release(pos)
        control_system.joy_axis_state[event.axis] = -1
    if ((control_system.joy_axis_state[event.axis] != 1)
          and (event.value > control_system.joy_deadzone)):
        release(neg)
        press(pos)
        control_system.joy_axis_state[event.axis] = 1
    if ((event.value < control_system.joy_deadzone)
        and (event.value > -control_system.joy_deadzone)):
        if control_system.joy_axis_state[event.axis] < 0:
            release(neg)
        if control_system.joy_axis_state[event.axis] > 0:
            release(pos)
        control_system.joy_axis_state[event.axis] = 0

def hat_axis_handler(old_value, new_value, neg, pos):
    """
    Still ugly, still bruteforced, but it works.
    """
    if old_value != -1 and new_value < 0:
        press(neg)
        release(pos)
    if old_value != 1 and new_value > 0:
        release(neg)
        press(pos)
    if new_value == 0:
        if old_value < 0: release(neg)
        if old_value > 0: release(pos) 

def press(buttons):
    """
    Takes a mapped input for a physical button and presses the relevant virtual
    button(s), adjusting their state and executing any on press callbacks for
    active controllers.
    """
    for button in calcs.bits(buttons): # Unfold the virtual button bits
        if control_system.state[button] == False: # Is the button not pressed?
            control_system.state[button] = True # Change state
            if control_system.override_controller: # If a controller is exclusive
                for callback in control_system.override_controller.press_callbacks[button]:
                        callback()
            else: # No controller has exclusive access
                for controller in control_system.acquired_controllers:
                    for callback in controller.press_callbacks[button]:
                        callback()

def release(buttons):
    """
    Takes a mapped input for a physical button and releases the relevant virtual
    button(s), adjusting their state and executing any on release callbacks for
    active controllers.
    """
    for button in calcs.bits(buttons):
        if control_system.state[button] == True:
            control_system.state[button] = False
            if control_system.override_controller:
                for callback in control_system.override_controller.release_callbacks[button]:
                        callback()
            else:
                for controller in control_system.acquired_controllers:
                    for callback in controller.release_callbacks[button]:
                        callback()

def get_focus():
    """
    Returns the current window focus state.
    """
    return control_system.focus_state

def set_exit_callback(callback, **parameters):
    """
    Sets the callback executed when a window close event is detected.
    """
    control_system.exit_callback = callback
    control_system.exit_parameters = parameters

def detect_joystick():
    """
    Checks whether a joystick is plugged in, and configures the input-handling
    routines appropriately.
    """
    pygame.joystick.quit()
    pygame.joystick.init()
    if pygame.joystick.get_count():
        control_system.joystick = pygame.joystick.Joystick(0)
        control_system.joystick.init()
        control_system.input_methods.append(joy_pump)
        return True
    else:
        if control_system.joystick:
            control_system.joystick = None
            control_system.joy_axis_state = (0, 0)
            control_system.joy_hat_state = (0, 0)
            control_system.input_methods.remove(joy_pump)
        return False

def get_key(button):
    """
    Gets the bound keyboard assignment for a given button.
    """
    for i, j in control_system.key_assign.items():
        if j & button:
            return i
    return 0 # No key found

def get_button(button):
    """
    Gets the bound joystick button for a given button.
    
    http://www.giantitp.com/comics/oots0012.html
    """
    for i, j in control_system.joy_assign.items():
        if j & button:
            return i
    return 0 # No key found

def check_key_assignment(key, group=0, ignore=0):
    """
    Checks if a given key is assigned to any buttons and returns the relevant
    button mask, restricting to a specific keygroup or ignoring a particular
    button if needs be.
    """
    if key in control_system.key_assign:
        return control_system.key_assign[key] & group & ~ignore 
    else: return 0 # No key assigned

def check_joy_assignment(button, group=0, ignore=0):
    """
    Checks if a given joystick button is assigned to any buttons and returns
    the relevant button mask, restricting to a specific keygroup or ignoring a
    particular button if needs be.
    """
    if button in control_system.joy_assign:
        return control_system.joy_assign[button] & group & ~ignore 
    else: return 0 # No key assigned

def assign_key(button, key, group, stop_duplicates=False):
    """
    Assigns a button to its relevant key, and returns a value if another key
    has been assigned in that keygroup, in case reversal is needed. If
    requested, it will prevent assigning the key if it's already assigned to
    something else within the same group.
    """
    value = check_key_assignment(key, group, button)
    if not (value and stop_duplicates):
        # Unassign the old key, removing it from the assignment list if it's
        # no longer being used.
        old_key = get_key(button)
        control_system.key_assign[old_key] &= ~button 
        if not control_system.key_assign[old_key]:
            del control_system.key_assign[old_key]
        # Assign the new key, creating a new entry if one is not yet available.
        if key in control_system.key_assign:
            control_system.key_assign[key] |= button
        else:
            control_system.key_assign[key] = button
        # Since something has clearly changed, force the config file's update
        # flag since it can't track dictionary objects' states.
        data.ConfigFile.update_flag = True
    return value

def assign_joy(button, joybutton, group, stop_duplicates=False):
    """
    Assigns a button to its relevant joystick button, and returns a value if
    another button has been assigned in that keygroup, in case reversal is
    needed. If requested, it will prevent assigning the key if it's already
    assigned to something else within the same group.
    """
    value = check_joy_assignment(joybutton, group, button)
    if not (value and stop_duplicates):
        # Unassign the old button, removing it from the assignment list if it's
        # no longer being used.
        old_joy = get_button(button)
        control_system.joy_assign[old_joy] &= ~button
        if not control_system.joy_assign[old_joy]:
            del control_system.joy_assign[old_joy]
        # Assign the new key, creating a new entry if one is not yet available.
        if joybutton in control_system.joy_assign:
            control_system.joy_assign[joybutton] |= button
        else:
            control_system.joy_assign[joybutton] = button
        # Since something has clearly changed, force the config file's update
        # flag since it can't track dictionary objects' states.
        data.ConfigFile.update_flag = True
    return value

def reset_controls():
    """
    Resets keyboard and joystick assignments to their default values.
    """
    new_keys = INPUT_KEY_DEFAULTS.copy()
    new_joys = INPUT_JOY_DEFAULTS.copy()
    control_system.key_assign = new_keys
    data.config.key_bindings  = new_keys
    control_system.joy_assign = new_joys
    data.config.joy_buttons   = new_joys

def init():
    """
    Initializes the control system.
    """
    control_system.start()
    detect_joystick()

def bindings_assertion(attribute, initial=0b0):
    """
    Assertion code for making sure all buttons are assigned and they aren't
    overlapping.
    """
    buttons = initial
    for mask in attribute.values():
        buttons |= mask # Confirm that the appropriate buttons have bindings
    # Check every bit that is associated with a control method...
    if buttons != 0b1111111111:
        # Assertion failed! We've got empty buttons!
        raise AssertionError("not all buttons are assigned")
    for value in attribute.values():
        if (calcs.popcount(value & INPUT_GROUP_GAME) > 1 or
            calcs.popcount(value & INPUT_GROUP_MENU) > 1):
            raise AssertionError("overlapping keys")
        pass
    
# Config file sanity tests
data.sanity_test("key_bindings", assertion=bindings_assertion)
data.sanity_test("joy_buttons",
                 assertion=lambda x: bindings_assertion(x, initial=0b1111))
                # The initial here is to ignore the directional bindings, as
                # the joystick already has the axis and hat switch to cover it.
data.sanity_test("joy_deadzone",
                 replacement=lambda x: float(calcs.clamp(x, 0, 1)))
control_system = ControlSystem()
