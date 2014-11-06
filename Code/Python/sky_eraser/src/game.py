"""
Sky Eraser (For real this time!)
By Prerisoft

Game Code
File creation date: Jan 14, 2013

THIS FILE IS LICENSED UNDER THE 3-CLAUSE BSD LICENSE. PLEASE SEE LICENSE(.txt)
FOR DETAILS.
"""
from __future__ import print_function, division

import sys, math, pygame.rect, random, time

import calcs, data, video, audio, control, particle, scene, menu, event
import player, enemy, items, settings

from constants import (LAYER_BACKGROUND_1, LAYER_PARTICLE_LOWER, LAYER_ITEMS,
                       LAYER_PLAYER, LAYER_PARTICLE_UPPER, LAYER_ENEMY,
                       LAYER_ENEMY_BULLETS, LAYER_UI,
                       LAYER_OVERLAY_1, LAYER_OVERLAY_2,
                       
                       START_ENEMIES, START_WAVE, WAVE_INCREMENT, WAVE_OFFSET,
                       PLAY_AREA_EXIT, WAVE_SEED, RANK_WAVE_DIVIDER,
                       MAXIMUM_RANK, PLAYER_START, FONT_FILE, FONT_NUM,
                       PAUSE_TEXT, QUIT_TEXT, PAUSE_LOC, PAUSE_SIZE, 
                       INPUT_MENU_OK, INPUT_MENU_CANCEL, INPUT_PAUSE,
                       MENU_SELECT_SOUND, MENU_CANCEL_SOUND, MENU_PAUSE_SOUND,
                       CHANNEL_MENU, SCREEN_SIZE)

class GameScene(scene.BaseScene):
    """
    The scene where all the magic happens - a gameplay session occurs here.
    """
    gfx_resources = (["hud", "messages", "explosion", "explosion_big"]
                     + player.gfx_resources
                     + enemy.gfx_resources)
    sfx_resources = (["en_die0", "en_die1", MENU_PAUSE_SOUND]
                     + player.sfx_resources
                     + menu.sfx_resources
                     + items.sfx_resources)
    bgm_resources = ["m030", "m031", "m020"]
    def __init__(self, mode=None, replay=None):

        # Initialise basic parts
        self.event_queue = event.DynamicQueue()
        self.random_generator = random.Random(data.config.wave_seed
                                              if hasattr(data.config,
                                                         "wave_seed")
                                              else WAVE_SEED)
        self.particles = particle.ParticleSystem()
        self.hud = GameHUD(self)
        self.controller = control.Controller()
        self.controller.acquire()
        self.controller.add_press(INPUT_PAUSE, self.pause)
        control.set_exit_callback(self.pause)
        audio.play_music(["m030", "m031"], 1)
        fader = video.AlphaRect((0, 0), SCREEN_SIZE)
        fader.a = video.Lerp(255, 0, 30)
        self.event_queue.add_event(30,
                                   lambda: video.remove_renderer(fader.render))

        # Initialise enemy systems
        self.enemies = set()
        self.enemy_bullets = set()
        self.available_enemies = START_ENEMIES
        self.wave_number = 0
        self.next_wave = START_WAVE
        self.rank = 0
        self.min_rank = 0
        self.score = 0
        self.kills = 0
        self.combo_count = 0
        self.combo_time = 0
        self.extends = 0
        self.extend_point = 1000000
        self.aborting = False
        self.pause_screen = None

        # Initialise player
        self.items = set()
        self.player_bullets = set()
        self.player = player.PlayerShip(self)
        self.player_coordinates = PLAYER_START
        self.lives = data.config.starting_lives

        # Initialise renderers
        video.add_renderer(video.draw_clouds, LAYER_BACKGROUND_1)
        self.lower_particle_renderer = lambda x: self.particles.render(x, 0)
        video.add_renderer(self.lower_particle_renderer, LAYER_PARTICLE_LOWER)
        video.add_renderer(self.render_items, LAYER_ITEMS)
        video.add_renderer(self.player.render, LAYER_PLAYER)
        video.add_renderer(self.render_enemies, LAYER_ENEMY)
        video.add_renderer(self.particles.render, LAYER_PARTICLE_UPPER)
        #video.add_renderer(self.player.render_aura, LAYER_PLAYER_AURA)
        video.add_renderer(self.render_bullets, LAYER_ENEMY_BULLETS)
        video.add_renderer(self.hud.render, LAYER_UI)
        video.add_renderer(fader.render, LAYER_OVERLAY_1)
        video.set_parallax(lambda: self.player_coordinates[0])
        
        #self.sound_fade = 0
        #self.channels = list(pygame.mixer.Channel(i) for i in xrange(1, 8))
        
    def generate_enemies(self):
        """
        Generates a list of enemies using the object's random state generator.
        The specifics have yet to have been decided on, naturally.
        """
        self.wave_number += 1
        if self.score >= self.extend_point: # Costs less energy to put it here
            self.extend_point += 2000000
            self.extends += 1
        if True: #self.wave_number % RESUPPLIER_WAVE: # ...does not equal zero
            enemy_range = min(enemy.max_enemies,
                              (self.wave_number + WAVE_OFFSET)
                              // WAVE_INCREMENT + 1)
            result = self.random_generator.choice(
                           enemy.sequence[0:enemy_range])(self.random_generator)
            self.next_wave += (result[0] // (1 + (self.wave_number % 2 / 2))
                               - (self.rank // RANK_WAVE_DIVIDER))
            for entry in result[1]:
                self.event_queue.add_event(entry[0] + video.get_time(),
                                        lambda x=entry[1:3]: self.add_enemy(*x))
                # For some reason it's resolving the namespace instead of
                # caching, so we have to rely on old tricks.
        else:
            pass # Special miniboss code here

    def generate_player(self):
        """
        Creates a new player if there are lives left and the game isn't already
        ending. Otherwise, move on to the game over sequence.
        """
        if not self.aborting:
            if self.lives > 0:
                self.lives -= 1
                self.rank //= 3
                if self.combo_count > 1:
                    self.combo_time = 0
                    self.combo_count = 0
                    self.hud.combo_time = 0
                video.set_parallax(video.Lerp(self.player_coordinates[0],
                                              PLAYER_START[0],
                                              time=30))
                self.enemy_bullets = set()
                self.player = player.PlayerShip(self)
                video.add_renderer(self.player.render, LAYER_PLAYER)
                #video.add_renderer(self.player.render_aura, LAYER_PLAYER_AURA)
            else:
                self.game_over()

    def game_over(self, playerinit=False):
        """
        The game has ended. Disable player input, further enemy spawning, and
        so on.
        """
        self.aborting = True
        control.set_exit_callback(sys.exit)
        if self.player and not self.player.dying:
            self.player.collisions = False
            self.player.item_collisions = False
            self.player.controller.release()
            self.player.y_velocity = 0.1
            self.player.update = self.player.aborting_state
        audio.play_jingle("m020")
        self.next_wave = sys.maxint
        video.load_texture("game_over")
        self.game_over = video.AlphaSprite("game_over")
        self.game_over.a = video.Lerp(0, 255, time=90)
        self.game_over_bg = video.AlphaRect((0, 0), SCREEN_SIZE)
        self.game_over_bg.a = 0
        video.add_renderer(self.game_over_bg.render, LAYER_OVERLAY_1)
        video.add_renderer(self.game_over.render, LAYER_OVERLAY_2)
        position = data.playdata.get_position(self.score)
        if position is not None and not playerinit:
            self.scene_args = {"score":data.ScoreEntry("", self.score,
                                                       self.kills,
                                                       int(video.get_time()
                                                           * (100 / 60)),
                                                       int(time.time())),
                               "rank":position}
        else:
            self.scene_args = {}
        self.controller.remove_press(INPUT_PAUSE, self.pause)
        self.event_queue.add_event(video.get_time() + 90,
            lambda: self.controller.add_press(INPUT_MENU_OK,
                                              self.exit_callback))
    
    def exit_callback(self):
        """
        Callback to execute post-game over cleanup.
        """
        self.event_queue.add_event(video.get_time(), self.controller.release)
        if self.player:
            video.remove_renderer(self.player.render)
            if video.check_renderer(self.player.render_aura):
                video.remove_renderer(self.player.render_aura)
        video.remove_renderer(self.hud.render)
        video.remove_renderer(self.render_items)
        video.remove_renderer(self.render_bullets)
        video.remove_renderer(self.render_enemies)
        video.remove_renderer(self.particles.render)
        video.remove_renderer(video.draw_clouds)
        video.set_parallax()
        self.game_over_bg.a = 255
        self.game_over.a = video.Lerp(255, 0, 90)
        self.event_queue.add_event(video.get_time() + 90, self.exit_event)

    def exit_event(self):
        """
        Remaining post-game over cleanup before the game changes scenes.
        """
        video.remove_renderer(self.game_over_bg.render)
        video.remove_renderer(self.game_over.render)
        video.remove_renderer(self.lower_particle_renderer)
        if self.scene_args:
            import score
            scene.load_scene(score.ScoreScene, **self.scene_args)
        else:
            import title
            scene.load_scene(title.TitleScene, show_splash=False)

    def set_player_parallax(self):
        """
        Sets the game's parallax tracker to follow the player ship. Mostly
        a separate function so it can be added to an event queue without
        namespace resolution issues.
        """
        video.set_parallax(lambda: self.player_coordinates[0])

    def pause(self):
        """
        Goes through the process of pausing the game and passing control over
        too the pause screen.
        """
        if not self.pause_screen:
            audio.pause_sounds()
            audio.set_volume(music=data.config.music_volume / 2)
            audio.play_sound(MENU_PAUSE_SOUND, CHANNEL_MENU)
            control.set_exit_callback(sys.exit)
            self.pause_screen = PauseScreen(self)
            self.pause_screen.event_queue.add_event(0, self.controller.release)
            if self.player:
                self.pause_screen.event_queue.add_event(0,
                                                self.player.controller.release)
            scene.update = self.pause_screen.update

    def unpause(self):
        """
        Goes through the process of resuming the game and retaking control from
        the pause screen.
        """
        audio.resume_sounds()
        audio.set_volume(music=data.config.music_volume)
        if not self.aborting: control.set_exit_callback(self.pause)
        self.event_queue.add_event(0, self.controller.acquire)
        if (not self.aborting) and self.player and self.player.item_collisions:
            self.event_queue.add_event(0, self.player.controller.acquire)
        scene.update = self.update
        self.pause_screen = None

    def update(self):
        """
        Processes all game events.
        """

        # Process game queue and general events
        self.event_queue.pump()
        self.rank -= calcs.sign(self.rank - self.min_rank)

        # Process all enemies and their bullets
        for bullet in self.enemy_bullets.copy():
            if bullet.update():
                self.enemy_bullets.remove(bullet)
        for enemy in self.enemies.copy():
            enemy.update()
            enemy.collide_test(self.player_bullets)

        # Process item movements
        for item in self.items.copy():
            item.update()
            if (self.player and self.player.item_collisions and
                item.hitbox.colliderect(self.player.hitbox)):
                item.activate(self)
                self.items.discard(item)
            elif item.y > PLAY_AREA_EXIT[3]:
                self.items.discard(item)

        # Process player movements
        if self.player:
            self.player.update()
            self.player_coordinates = (self.player.x, self.player.y)
            self.player.collide_test(self.enemy_bullets, self.enemies)
        for bullet in self.player_bullets.copy():
            if bullet.update():
                self.player_bullets.remove(bullet)

        # Decrement combo meter
        self.combo_time = max(0, self.combo_time - 1)
        if self.combo_count:
            if self.combo_count > 1:
                self.hud.combo_time = self.combo_time
            if not self.combo_time:
                self.combo_count = 0

        # Update the scorecard in the HUD
        self.hud.write_score(self.score)

        # Test for new wave launch
        if self.next_wave < video.get_time():
            self.generate_enemies()

    def add_score(self, score, combo=True):
        """
        Increases the player's score by a given value, including whether the
        combo meter multiplies it (in which case it should be 1/100 of what it
        intends to give)
        """
        if combo:
            self.score += score * (100 + min(self.combo_count, 900))
        else:
            self.score += score

    def add_enemy(self, enemy, parameters):
        """
        Creates a new enemy in the playfield.
        """
        self.enemies.add(enemy(parent=self, **parameters))

    def add_rank(self, rank):
        """
        Increases the game's difficulty rank.
        """
        self.rank = min(self.rank + rank, MAXIMUM_RANK)

    def add_combo(self, time, combo=1):
        """
        Increases the combo meter.
        """
        self.combo_count = min(self.combo_count + combo, 999)
        self.combo_time = min(self.combo_time + time, 90)
        if self.combo_count > 1:
            self.hud.combo_bar.x = 0
            self.hud.combo_text.x = 32
            self.hud.combo_num.x = 8
            self.hud.combo_num.write(str(self.combo_count).rjust(3))


    def render_items(self, target):
        """
        Rendering subroutine for power-up items.
        """
        for item in self.items:
            item.render(target)
        
    def render_enemies(self, target):
        """
        Rendering subroutine for enemy objects.
        """
        for enemy in self.enemies:
            enemy.render(target)

    def render_bullets(self, target):
        """
        Rendering subroutine for bullet objects.
        """
        for bullet in self.player_bullets:
            bullet.sprite.render(target)
            # target.fill((0, 128, 0), bullet.hitbox) # TEST CODE
        for bullet in self.enemy_bullets:
            # target.fill((255, 128, 0), bullet.hitbox) # TEST CODE
            bullet.sprite.render(target)

    def abort_callback(self):
        """
        Callback for changing to the ending titlecard.
        """
        fade_out = video.AlphaRect((0, 0), (240, 320), (0, 0, 0))
        fade_out.a = video.Lerp(0, 255, 90)
        video.add_renderer(fade_out.render, LAYER_OVERLAY_2)
        audio.fade_music(150)
        self.sound_fade = 90
        self.event_queue.add_event(video.get_time() + 90, self.abort_event)
    def abort_event(self):
        """
        Queued event for changing to the ending titlecard.
        """
        video.reset_renderers()
        import alpha_titlecard
        scene.load_scene(alpha_titlecard.TitleCard, True, exiting=True)

class GameHUD(object):
    """
    Object that handles the HUD functions of the game, including score, lives,
    bombs, and the combo meter.
    """
    def __init__(self, parent):
        self.parent = parent
        self.scoreboard = video.Textbox(FONT_FILE, (48, 8), (18, 2))
        self.scoreboard.write("TOP", (0, 0), (0xFF, 0x80, 0x80))
        self.scoreboard.write("SCORE", (0, 1), (0x80, 0x80, 0xFF))
        self.score = 0L
        self.high_score = data.playdata.scores[0].score
        self.bombs = 3
        self.scoreboard.write(str(self.high_score).rjust(12), (6, 0))
        self.scoreboard.write(str(self.score).rjust(12), (6, 1))
        
        self.combo_count = 0
        self.combo_time = -1
        self.combo_num = video.Textbox(FONT_NUM, (0, 32), (3, 1))
        self.combo_num.x = -64
        self.combo_text = video.Textbox(FONT_FILE, (0, 32), (5, 1))
        self.combo_text.write("COMBO", (0, 0), (0xFF, 0xE0, 0x00))
        self.combo_text.x =-40
        self.combo_bar = video.RectSprite((0, 40), (0, 6), (0xE0, 0x98, 0x0))
        self.combo_bar.x = -72
        self.combo_bar.width = lambda: self.combo_time * 2

    def write_score(self, score):
        """
        Writes new values to the score table - including the high score, if it
        has been breached.
        """
        if score != self.score:
            self.score = score
            self.scoreboard.write(str(score).rjust(12), (6, 1))
        if score > self.high_score:
            self.high_score = score
            self.scoreboard.write(str(score).rjust(12), (6, 0))
    def render(self, target):
        if not self.combo_time:
            self.combo_time = -1
            self.combo_bar.x = video.Lerp(0, -72, 30)
            self.combo_text.x = video.Lerp(32, -40, 30)
            self.combo_num.x = video.Lerp(8, -64, 30)
        if self.combo_bar.x > -72 or self.parent.combo_count > 1:
            self.combo_bar.render(target)
            self.combo_num.render(target)
            self.combo_text.render(target)
        if self.parent.player:
            self.bombs = self.parent.player.bombs
        for i in xrange(0, self.bombs):
            video.draw_texture(target, "hud", 1, (224 - i * 8, 304))
        for i in xrange(0, self.parent.lives):
            video.draw_texture(target, "hud", 0, (16 + i * 8, 304))
        self.scoreboard.render(target)

class PauseScreen(object):
    """
    The pause screen's routines, separated from the game code for the sake of
    cleaner integration. While it bears some resemblances to a scene object,
    it is most certainly not configured for such, so do not use it as one!
    """
    def __init__(self, parent):
        self.parent = parent # I really need to stop relying on this.
        self.event_queue = event.DynamicQueue()
        self.buffer = video.capture_buffer()
        self.buffer.surface.fill((128, 128, 128),
                                 special_flags=pygame.BLEND_MULT)
        self.video_state = video.save_state(clear=True)
        self.menu = None
        self.text = video.Textbox(FONT_FILE, PAUSE_LOC,
                                  PAUSE_SIZE)
        self.pause_menu()
        video.add_renderer(self.render)
    
    def pause_menu(self):
        """
        Generates the pause menu.
        """
        self.menu = menu.MenuArray((menu.MenuEntry((120, 172),
                                                   "RESUME".center(8),
                                                   self.resume_callback),
                                    menu.MenuEntry((120, 188),
                                                   "SETTINGS",
                                                   self.settings_callback),
                                    menu.MenuEntry((120, 204),
                                                   "QUIT".center(8),
                                                   self.quit_menu_callback)))
        self.menu.controller.remove_press(INPUT_MENU_CANCEL, self.menu.cancel)
        self.menu.controller.remove_press(INPUT_PAUSE, self.menu.escape)
        self.menu.controller.add_press(INPUT_MENU_CANCEL, self.cancel_callback)
        self.menu.controller.add_press(INPUT_PAUSE, self.resume_callback)
        self.text.write(PAUSE_TEXT, clear=True)
        self.event_queue.add_event(video.get_time() + 15, self.menu.ready)

    def cancel_callback(self):
        """
        Moves the cursor back to the resume button, or if it's already on it,
        goes straight to resuming.
        """
        if self.menu.position == 0:
            self.resume_callback()
        else:
            audio.play_sound(MENU_CANCEL_SOUND, CHANNEL_MENU)
            self.menu.set_position(0)

    def resume_callback(self):
        """
        Begins the process of relinquishing control and resuming the game.
        """
        audio.play_sound(MENU_PAUSE_SOUND, CHANNEL_MENU)
        self.event_queue.add_event(video.get_time(), self.menu.destroy)
        self.event_queue.add_event(video.get_time() + 15, self.resume)
    def resume(self):
        """
        Relinquishes the local extent of its control.
        """
        video.remove_renderer(self.buffer.render)
        video.load_state(self.video_state)
        self.parent.unpause()
    
    def settings_callback(self):
        """
        Destroys the current menu and loads up the settings menu from the title
        screen.
        """
        audio.play_sound(MENU_SELECT_SOUND, CHANNEL_MENU)
        self.event_queue.add_event(video.get_time(), self.menu.destroy)
        self.event_queue.add_event(video.get_time() + 15, self.settings_menu)
    def settings_menu(self):
        """
        Generates the settings menu.
        """
        self.menu = settings.SettingsMenu(self.pause_menu_callback,
                                          self.input_callback,
                                       True, 172)
        self.event_queue.add_event(video.get_time() + 15, self.menu.ready)
        
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
        self.menu = settings.InputMenu(self.input_return, self.event_queue)
        self.event_queue.add_event(video.get_time() + 15, self.menu.ready)
    def input_return(self):
        """
        Returns to the settings menu from the input menu.
        """
        audio.play_sound(MENU_CANCEL_SOUND, CHANNEL_MENU)
        self.event_queue.add_event(video.get_time(), self.menu.destroy)
        self.event_queue.add_event(video.get_time() + 15, self.settings_menu)
    
    def quit_menu_callback(self):
        """
        Destroys the current menu and loads up the confirmation menu for
        quitting.
        """
        audio.play_sound(MENU_SELECT_SOUND, CHANNEL_MENU)
        self.event_queue.add_event(video.get_time(), self.menu.destroy)
        self.event_queue.add_event(video.get_time() + 15, self.quit_menu)
    def quit_menu(self):
        """
        Generates the quit confirmation menu.
        """
        self.menu = menu.MenuArray((menu.MenuEntry((120, 172),
                                                   "YES",
                                                   self.quit_confirm_callback),
                                    menu.MenuEntry((120, 188),
                                                   "NO ",
                                                   self.pause_menu_callback)))
        self.text.write(QUIT_TEXT)
        self.event_queue.add_event(video.get_time() + 15, self.menu.ready)
    
    def quit_confirm_callback(self):
        """
        Destroys the quit confirmation menu and prepares to end the game.
        """
        self.event_queue.add_event(video.get_time(), self.menu.destroy)
        self.event_queue.add_event(video.get_time() + 15, self.quit_game)
    
    def quit_game(self):
        """
        Relinquishes the local extent of its control, then calls the game over
        method before resuming.
        """
        video.remove_renderer(self.buffer.render)
        video.load_state(self.video_state)
        self.parent.game_over(playerinit=True)
        self.parent.unpause()
    
    def pause_menu_callback(self):
        """
        Destroys the current menu and goes back to the main pause one.
        """
        audio.play_sound(MENU_CANCEL_SOUND, CHANNEL_MENU)
        self.event_queue.add_event(video.get_time(), self.menu.destroy)
        self.event_queue.add_event(video.get_time() + 15, self.pause_menu)
    
    def update(self):
        self.event_queue.pump()

    def render(self, target):
        self.buffer.render(target)
        self.text.render(target)
        self.menu.render_all(target)


data.sanity_test("starting_lives",
                 replacement=lambda x: int(calcs.clamp(x, 0, 5)))
data.sanity_test("starting_bombs",
                 replacement=lambda x: int(calcs.clamp(x, 0, 5)))
data.sanity_test("starting_power",
                 replacement=lambda x: int(calcs.clamp(x, 0, 2)))
