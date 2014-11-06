"""
Sky Eraser (For real this time!)
By Prerisoft

Scoreboard
File creation date: Oct 12, 2013

Hey, we're finally getting this shit in order!

THIS FILE IS LICENSED UNDER THE 3-CLAUSE BSD LICENSE. PLEASE SEE LICENSE(.txt)
FOR DETAILS.
"""

from __future__ import print_function

import string

import scene, event, data, video, audio, control, menu
from constants import (LAYER_BACKGROUND_1, LAYER_UI, LAYER_OVERLAY_1,
                       FONT_FILE, LERP_REVERSE,
                       
                       INPUT_UP, INPUT_DOWN, INPUT_LEFT, INPUT_RIGHT,
                       INPUT_MENU_OK, INPUT_MENU_CANCEL, INPUT_PAUSE,
                       
                       CHANNEL_MENU, MENU_CURSOR_SOUND, MENU_SELECT_SOUND,
                       MENU_CANCEL_SOUND, MENU_ERROR_SOUND, MENU_PAUSE_SOUND)

values = string.digits + string.ascii_uppercase + "!?#$',.:;-_~@ "
table = "\n\n".join((" ".join(i) for i in
                     (values[n:n+10] for n in range(0, len(values), 10))))

class ScoreScene(scene.BaseScene):
    """
    The one-stop scene for entering and viewing high scores.
    """
    gfx_resources = []
    sfx_resources = menu.sfx_resources + [MENU_PAUSE_SOUND, "pl_die"]
    def __init__(self, score=None, rank=None):
        self.event_queue = event.DynamicQueue()
        self.menu = None
        self.alpha_background = video.AlphaRect(size=(240, 320))
        self.alpha_background.a = video.Lerp(255, 0, 30)
        video.add_renderer(video.draw_clouds, LAYER_BACKGROUND_1)
        video.add_renderer(self.alpha_background.render, LAYER_OVERLAY_1)
        self.event_queue.add_event(30,
                                   lambda: video.remove_renderer(
                                                 self.alpha_background.render))
        if score and rank < 10:
            if data.config.valid_play():
                self.event_queue.add_event(30,
                                           lambda: self.new_score(score, rank))
            else:
                self.event_queue.add_event(30, self.cheater_menu)
        else:
            self.event_queue.add_event(30, self.load_list)
    
    def load_list(self, show_buttons=True):
        """
        Loads the high score list. If told to, it'll display 'return' and
        'clear' buttons, for use when accessed from the title screen.
        """
        if show_buttons:
            self.menu = menu.MenuArray((menu.MenuEntry((40, 284), "RETURN",
                                                       self.fade_menu),
                                        menu.MenuEntry((204, 284), "CLEAR",
                                                       self.erase_scores)
                                        ))
            self.menu.controller.remove_press(INPUT_UP, self.menu.move_up)
            self.menu.controller.remove_press(INPUT_DOWN, self.menu.move_down)
            self.menu.controller.add_press(INPUT_LEFT, self.menu.move_up)
            self.menu.controller.add_press(INPUT_RIGHT, self.menu.move_down)
            video.add_renderer(self.menu.render_all, LAYER_UI)
            self.event_queue.add_event(video.get_time() + 15, self.menu.ready)
        else:
            self.menu = None
            self.controller = control.Controller()
            self.controller.add_press(INPUT_MENU_OK, self.fade_out)
            self.controller.add_press(INPUT_MENU_CANCEL, self.fade_out)
            self.controller.add_press(INPUT_PAUSE, self.fade_out)
            self.event_queue.add_event(video.get_time() + 30,
                                       self.controller.acquire)
        self.name_box = video.Textbox(FONT_FILE, (72, 16), (11, 1))
        self.name_box.write("HIGH SCORES")
        self.scores = list(HighScore(j, i) for i, j in
                           zip(data.playdata.scores, range(1, 11)))
        for score in self.scores:
            self.event_queue.add_event(video.get_time() + 15 + score.rank,
                                       score.ready)
        video.add_renderer(self.name_box.render, LAYER_UI)
        video.add_renderer(self.render_scores, LAYER_UI)
        
    
    def fade_menu(self):
        """
        Menu-executed frontend to the fadeout callback so that the sound effect
        can be played.
        """
        audio.play_sound(MENU_CANCEL_SOUND, CHANNEL_MENU)
        self.fade_out()
    
    def fade_out(self):
        """
        Exits the score scene with a fade effect.
        """
        self.alpha_background.a = video.Lerp(0, 255, 30)
        video.add_renderer(self.alpha_background.render, LAYER_OVERLAY_1)
        for score in self.scores:
            score.destroy()
        if self.menu:
            self.event_queue.add_event(video.get_time(), self.menu.destroy)
        else:
            self.event_queue.add_event(video.get_time(),
                                       self.controller.release)
        self.event_queue.add_event(video.get_time() + 30, self.change_scene)
    
    def change_scene(self):
        """
        Destroys all nonessential renderers and calls the title scene in.
        """
        video.remove_renderer(video.draw_clouds)
        video.remove_renderer(self.render_scores)
        if self.menu:
            video.remove_renderer(self.menu.render_all)
        import title
        scene.load_scene(title.TitleScene, show_splash=False)
    
    def close(self):
        """
        Removes the last of the renderers now that control's passing over.
        """
        video.remove_renderer(self.name_box.render)
        video.remove_renderer(self.alpha_background.render)
    
    def new_score(self, score, rank):
        """
        Initiates the name entry process for a new score.
        """
        self.current_score = score
        self.current_rank = rank
        self.name_box = video.Textbox(FONT_FILE, (16, 80), (26, 6))
        self.name_box.write("\n\n".join(("NEW HIGH SCORE!".center(26),
                                        "PLEASE ENTER YOUR NAME:".center(26),
                                       "".join(
                                             ("\n{}.".format(rank+1).ljust(15),
                                              str(score.score).rjust(12))))))
        self.name_box.write("_", (3, 5))
        self.name_box.write("_" * 11, (4, 5), color=(0x40, 0x40, 0x40))
        input_table = InputTable((116, 148), self.name_box)
        if data.config.last_name:
            self.name_box.write((data.config.last_name + "_")[:12],
                                (3, 5))
            input_table.name_size = min(12, len(data.config.last_name))
            input_table.name_field[0:input_table.name_size] = \
                data.config.last_name[:input_table.name_size]
        self.menu = ScoreEntry((input_table,
                                menu.MenuEntry((144, 236), "BACK",
                                                input_table.cancel),
                                menu.MenuEntry((180, 236), "END",
                                               self.push_score)
                                ), self.exit_score)
        self.score_background = video.RectSprite((119, 123), (0, 15))
        self.score_background.width = video.Lerp(0, 215, 15)
        video.add_renderer(self.score_background.render, LAYER_UI - 1)
        video.add_renderer(self.menu.render_all, LAYER_UI)
        self.event_queue.add_event(video.get_time() + 15, self.menu.ready)
        self.event_queue.add_event(video.get_time() + 15,
                               lambda: video.add_renderer(self.name_box.render,
                                                          LAYER_UI))
    
    def cheater_menu(self):
        """
        A menu displayed when a gameplay session that did not use default
        gameplay settings submits a high score.
        """
        self.current_score = None
        self.current_rank = None
        audio.play_sound(MENU_ERROR_SOUND, CHANNEL_MENU)
        self.name_box = video.Textbox(FONT_FILE, (16, 120), (26, 4))
        self.name_box.write("\n".join(("SCORE NOT RECORDED".center(26),
                                         "", "PLAY WITH DEFAULT GAMEPLAY",
                                         "SETTINGS TO SAVE SCORE".center(26))),
                            color=(0xFF, 0x40, 0x40))
        self.score_background = video.RectSprite((119, 135), (0, 39))
        self.score_background.width = video.Lerp(0, 215, 15)
        self.menu = menu.MenuArray((menu.MenuEntry((120, 176), "OK",
                                  lambda: (audio.play_sound(MENU_SELECT_SOUND,
                                                            CHANNEL_MENU),
                                           self.exit_score())),))
        video.add_renderer(self.score_background.render, LAYER_UI - 1)
        video.add_renderer(self.menu.render_all, LAYER_UI)
        self.event_queue.add_event(video.get_time() + 15, self.menu.ready)
        self.event_queue.add_event(video.get_time() + 15,
                               lambda: video.add_renderer(self.name_box.render,
                                                          LAYER_UI))

    def push_score(self):
        """
        Takes the name inputted to the menu, sets a default name if a blank
        one was inserted, and passes it to the playdata handler before calling
        the high score table.
        """
        audio.play_sound(MENU_SELECT_SOUND, CHANNEL_MENU)
        if self.menu.entries[0].name_field in ("000000000000",
                                               "            "):
            self.menu.entries[0].name_field = "PRERISOFT   "
        else:
            data.config.last_name = str(self.menu.entries[0].name_field).rstrip()
        self.current_score.set_name(str(self.menu.entries[0].name_field))
        data.playdata.add_score(self.current_score, self.current_rank)
        self.exit_score()
        
    def exit_score(self):
        """
        Leaves the name entry process and goes to the high score table.
        """
        score_bg = self.score_background
        score_bg.width = video.Lerp(215, 0, 15)
        video.remove_renderer(self.name_box.render)
        self.event_queue.add_event(video.get_time(), self.menu.destroy)
        self.event_queue.add_event(video.get_time() + 15,
                           lambda: video.remove_renderer(self.menu.render_all))
        self.event_queue.add_event(video.get_time() + 15,
                                lambda: video.remove_renderer(score_bg.render))
        self.event_queue.add_event(video.get_time() + 16,
                                   lambda: self.load_list(show_buttons=False))
        del self.current_score, self.current_rank, self.score_background

    def erase_scores(self):
        """
        Brings the menu button into a confirmation mode, where the select
        button activates but anything else cancels.
        """
        audio.play_sound(MENU_PAUSE_SOUND, CHANNEL_MENU)
        self.menu.entries[1].cursor.color = lambda x=video.Lerp(0, 255, 15,
                                              extend=LERP_REVERSE): (x(), 0, 0)
        self.controller = control.Controller()
        self.controller.add_press(INPUT_MENU_OK, self.confirm_erase)
        for i in (INPUT_MENU_CANCEL, INPUT_UP, INPUT_DOWN, INPUT_LEFT,
                  INPUT_RIGHT, INPUT_PAUSE):
            self.controller.add_press(i, self.cancel_erase)
        self.event_queue.add_event(0, self.menu.controller.release)
        self.event_queue.add_event(0, self.controller.acquire)
    
    def confirm_erase(self):
        """
        Now that request for deletion has been confirmed, destroy the scores
        and rewrite the table with the blank entries.
        """
        audio.play_sound("pl_die")
        data.playdata.reset_scores()
        for score, playdata in zip(self.scores, data.playdata.scores):
            score.score = playdata
            score.ready()
        self.menu.entries[1].cursor.color = (lambda x=video.Lerp(255, 0, 30):
                                             (x(), 0, 0))
        self.event_queue.add_event(0, self.controller.release)
        del self.controller
        self.event_queue.add_event(30, self.menu.controller.acquire)
        self.event_queue.add_event(30, lambda: self.menu.set_position(0))
    
    def cancel_erase(self):
        """
        The erase operation is aborted, and the menu restored to normal state.
        """
        audio.play_sound(MENU_CANCEL_SOUND, CHANNEL_MENU)
        self.menu.entries[1].select()
        self.event_queue.add_event(0, self.controller.release)
        self.event_queue.add_event(0, self.menu.controller.acquire)
        del self.controller
        

    def update(self):
        """
        Standard update call, checks the event queue for anything new.
        """
        self.event_queue.pump()
    
    def render_scores(self, target):
        """
        Renders the list of score objects for the high score list.
        """
        for score in self.scores:
            score.render(target)

class InputTable(menu.MenuEntry):
    """
    The special menu entry for setting a new high score. Lots of overrides are
    present in the code, so please keep in mind the comments here if editing
    the menu code elsewhere.
    """
    def __init__(self, position, name_box):
        menu.MenuEntry.__init__(self, position, " " * 19)
        self.text = video.Textbox(FONT_FILE, (position[0] - 76,
                                              position[1] - 4), (19, 9))
        self.text.write(table)
        self.legend_background.height = 79
        self.legend_background.y = position[1] + 31
        self.position = 0
        self.name_box = name_box
        self.name_field = bytearray(" " * 12)
        self.name_size = 0
        self.cursor.x = lambda x=position[0] - 73: (x + (self.position % 10)
                                                    * 16)
        self.cursor.y = lambda y=position[1] - 1: (y + (self.position // 10)
                                                   * 16)

    def select(self):
        """
        Shows the cursor at its special size.
        """
        menu.MenuEntry.select(self)
        self.cursor.width = video.Lerp(0, 15, 10)
    def deselect(self):
        """
        Shrinks the cursor from its special size.
        """
        menu.MenuEntry.deselect(self)
        self.cursor.width = video.Lerp(15, 0, 10)

    def on_up(self):
        """
        Moves the cursor back by ten spaces - no checks are involved here
        because they're present in the menu array, owing to the need to change
        active buttons.
        """
        audio.play_sound(MENU_CURSOR_SOUND, CHANNEL_MENU)
        self.position -= 10
    def on_down(self):
        """
        Moves the cursor forward by ten spaces - see on_up() for reasons why
        no checks are involved.
        """
        audio.play_sound(MENU_CURSOR_SOUND, CHANNEL_MENU)
        self.position += 10
    def on_left(self):
        """
        Moves the cursor back a space, unless it's on the far left column, to
        which it loops to the other side. 
        """
        audio.play_sound(MENU_CURSOR_SOUND, CHANNEL_MENU)
        if self.position % 10 == 0:
            self.position += 9
        else:
            self.position -= 1
    def on_right(self):
        """
        Moves the cursor forward a space, blah blah blah, see on_left()
        """
        audio.play_sound(MENU_CURSOR_SOUND, CHANNEL_MENU)
        if (self.position % 10) == 9:
            self.position -= 9
        else:
            self.position += 1
    def on_activate(self):
        """
        Adds a letter to the name if space is available.
        """
        if self.name_size == 12:
            audio.play_sound(MENU_ERROR_SOUND, CHANNEL_MENU)
        else:
            audio.play_sound(MENU_SELECT_SOUND, CHANNEL_MENU)
            self.name_field[self.name_size] = values[self.position]
            self.name_size += 1
            name_writeout = str(self.name_field)[:self.name_size] + "_"
            self.name_box.write(name_writeout[:min(12, self.name_size + 1)],
                                (3, 5))
    def cancel(self):
        """
        Erases the last letter added to the name.
        """
        if self.name_size == 0:
            audio.play_sound(MENU_ERROR_SOUND, CHANNEL_MENU)
        else:
            audio.play_sound(MENU_CANCEL_SOUND, CHANNEL_MENU)
            self.name_size -= 1
            self.name_field[self.name_size] = " "
            name_writeout = str(self.name_field)[:self.name_size] + "_"
            self.name_box.write(name_writeout[:12].ljust(12), (3, 5))
            self.name_box.write("_" * (11 - self.name_size),
                                (4 + self.name_size, 5),
                                color=(0x40, 0x40, 0x40))

class ScoreEntry(menu.MenuArray):
    """
    The menu controlling the high score entry functions. A considerable number
    of overrides were needed to get the desired behaviour, so keep comments in
    mind.
    """
    def __init__(self, entries, exit_callback):
        menu.MenuArray.__init__(self, entries)
        self.escape_callback = exit_callback
    def move_up(self):
        """
        Moves the cursor to an appropriate place depending on where it is on
        the entry box, if it's on there at all.
        """
        if self.position == 0:
            if self.entries[0].position >= 10:
                self.entries[0].on_up()
            else:
                if self.entries[0].position % 10 > 7:
                    self.set_position(2)
                else:
                    self.set_position(1)
        else:
            if self.position == 2:
                self.entries[0].position = max(8,
                                               self.entries[0].position % 10)
            else:
                self.entries[0].position %= 10
            self.entries[0].position += 40
            self.set_position(0)
    def move_down(self):
        """
        Like move_up() only down.
        """
        if self.position == 0:
            if self.entries[0].position < 40:
                self.entries[0].on_down()
            else:
                if self.entries[0].position % 10 > 7:
                    self.set_position(2)
                else:
                    self.set_position(1)
        else:
            if self.position == 2:
                self.entries[0].position = max(8,
                                               self.entries[0].position % 10)
            else:
                self.entries[0].position %= 10
            self.set_position(0)
    def move_right(self):
        """
        Overrides the normal behaviour if in a position to change buttons by
        moving right.
        """
        if self.position == 1:
            self.set_position(2)
        else:
            menu.MenuArray.move_right(self)
    def move_left(self):
        """
        Do I really have to explain this with the description of the opposite
        function just prior? Jeez, I've made this docstring longer than its
        sibling just by griping...
        """
        if self.position == 2:
            self.set_position(1)
        else:
            menu.MenuArray.move_left(self)
    def activate(self):
        """
        Wraps the default activation function in a check on the name size,
        whether it has changed and if it is now at the maximum value.
        """
        temp_size = self.entries[0].name_size
        menu.MenuArray.activate(self)
        if self.position==0 and (temp_size < self.entries[0].name_size == 12):
            menu.MenuArray.set_position(self, 2)
    def cancel(self):
        """
        Forwards all cancellation requests to the name erase function.
        """
        self.entries[0].cancel()
    def escape(self):
        """
        Aborts the name entry process.
        """
        audio.play_sound(MENU_CANCEL_SOUND, CHANNEL_MENU)
        self.escape_callback()
        
    def set_position(self, position):
        """
        A basic override since the number of situations where the cursor sound
        would play were far greater than when it wouldn't.
        """
        audio.play_sound(MENU_CURSOR_SOUND, CHANNEL_MENU)
        menu.MenuArray.set_position(self, position)
        

class HighScore(object):
    """
    An entry displaying an entry on the high score table. It's a non-
    interactive object now, but when replay functionality is added, it'll
    be turned into a menu entry that can be activated to view the replay.
    """
    def __init__(self, rank, score):
        self.legend_background = video.RectSprite((119, 23 + (24 * rank)), (0, 23))
        self.legend_background.width = video.Lerp(0, 215,
                                           start_time=video.get_time() + rank,
                                           end_time=video.get_time()+rank+15)
        self.text = video.Textbox(FONT_FILE, (16, 16 + (24 * rank)), (26, 2))
        self.score = score
        self.rank = rank
    def ready(self):
        """
        Writes the gigantic formatting nightmare that is the text values.
        
        TODO: It could probably be simplified... Or at least consolidated into
        a single formatting operation.
        """
        self.text.write("".join(("{}.".format(self.rank).ljust(3),
                                self.score.name.ljust(13),
                                str(self.score.score).rjust(10),
                                "\nTIME {0:>2}'{1:0>2}\"{2:0>2}".format(
                                    self.score.playtime // 6000,
                                    self.score.playtime // 100 % 60,
                                    self.score.playtime % 100),
                                "KILLS {}".format(self.score.kills).rjust(13))))
    def destroy(self):
        """
        Destroys the high score entry. This'll be unneeded once we're using
        menu entries to display it.
        """
        self.text.write("", clear=True)
        self.legend_background.width = video.Lerp(215, 0,
                                       start_time=video.get_time() + self.rank,
                                       end_time=video.get_time()+self.rank+15)
    def render(self, target):
        """
        Renders the legend_background and then the text.
        """
        self.legend_background.render(target)
        self.text.render(target)

data.sanity_test("last_name", replacement=lambda x: str(x).rstrip()[:12])
