"""
Sky Eraser (For real this time!)
By Prerisoft

Player Code
File creation date: Apr 07, 2013

The player code was getting a bit long in the tooth in the game subroutines, so
I moved it here.

THIS FILE IS LICENSED UNDER THE 3-CLAUSE BSD LICENSE. PLEASE SEE LICENSE(.txt)
FOR DETAILS.
"""
from __future__ import division

import math, weakref, pygame.rect, random, gc
from collections import deque
import calcs, data, video, control, audio, particle, bullets, items
from constants import (LAYER_PLAYER_AURA,
                       INPUT_UP, INPUT_DOWN, INPUT_LEFT, INPUT_RIGHT,
                       INPUT_FIRE, INPUT_BOMB, INPUT_AUTOFIRE,
                       PLAYER_SPEED, PLAYER_FOCUS_SPEED, PLAYER_TILT,
                       PLAYER_TILT_MAX,
                       PLAYER_FIRE_VELOCITY, PLAYER_START,
                       PLAY_AREA, PLAY_AREA_EXIT, PLAYER_HITBOX,
                       PLAYER_BULLET_HITBOX, OBJ_PARALLAX, ORBIT_OFFSET,
                       PLAYER_ORBIT, PLAYER_ORBIT_LENGTH,
                       PLAYER_FIRE_TIME, PLAYER_FIRE_DELAY, PLAYER_FOCUS_TIME,
                       LERP_REPEAT, CHANNEL_BULLET, CHANNEL_BIGBOOM,
                       MAXIMUM_RANK)

class PlayerShip(object):
    """
    This is it! The player ship, one of the most important parts of the game.
    Are you ready?
    """
    def __init__(self, parent):
        
        # Initialise basic information.
        self.parent = weakref.proxy(parent)
        self.bullets = parent.player_bullets
        self.spawn_time = video.get_time()
        
        # Initialise location.
        self.x = PLAYER_START[0]
        self.y = PLAY_AREA_EXIT[3]
        
        # Initialise positioning and graphical variables.
        self.tilt = 0
        self.orbit_time = 0
        self.orbit_x = 0
        self.orbit_y = 0
        
        # Initialise gameplay elements.
        self.hitbox = pygame.Rect((self.x, self.y), PLAYER_HITBOX)
        self.controller = control.Controller()
        self.controller.add_press(INPUT_FIRE, self.fire_callback)
        self.controller.add_press(INPUT_BOMB, self.generate_bomb)
        #self.firing = False
        #self.fire_timeout = 0 # Allows for tapping fire
        #self.focus_timer = 0 # Rises to a threshold to determine focused fire
        self.burst_timer = 0
        self.focus_timer = 0
        self.flare = False
        self.power = data.config.starting_power
        self.firing_modes = [self.power_0_fire, self.power_1_fire,
                             self.power_2_fire]
        self.make_options()
        self.bombs = data.config.starting_bombs
        self.collisions = False
        self.item_collisions = False
        self.dying = False
        self.bombing = False
        self.bomb_destroy = False
        
        # Initialise sprites.
        self.sprite = video.Sprite("player_ship2", block=3)
        self.sprite.x = lambda: self.x
        self.sprite.y = lambda: self.y
        neutral_tilt = PLAYER_TILT * 3.5
        self.sprite.block = lambda: (neutral_tilt + self.tilt) // PLAYER_TILT
        self.sprite.parallax = OBJ_PARALLAX
        self.engine_sprite = video.Sprite("player_engine")
        self.engine_sprite.x = lambda: self.x
        self.engine_sprite.y = lambda: self.y + 11
        self.engine_sprite.block = lambda: video.get_time() % 6 // 3
        self.engine_sprite.parallax = OBJ_PARALLAX
        self.bit_a_sprite = video.Sprite("player_option", block=0)
        self.bit_a_sprite.x = lambda: self.x + (self.orbit_x * PLAYER_ORBIT[0])
        self.bit_a_sprite.y = lambda: self.y + ORBIT_OFFSET + (self.orbit_y *
                                                               PLAYER_ORBIT[1])
        self.bit_a_sprite.parallax = OBJ_PARALLAX
        self.bit_b_sprite = video.Sprite("player_option", block=0)
        self.bit_b_sprite.x = lambda: self.x - (self.orbit_x * PLAYER_ORBIT[0])
        self.bit_b_sprite.y = lambda: self.y + ORBIT_OFFSET - (self.orbit_y *
                                                               PLAYER_ORBIT[1])
        block_func = lambda: 3 - (self.orbit_x * 2.5) 
        self.bit_a_sprite.block = block_func
        self.bit_b_sprite.block = block_func
        self.bit_b_sprite.parallax = OBJ_PARALLAX
        
        self.hitbox_sprite = video.RectSprite(size=self.hitbox.size,
                                              color=(255, 0, 0))
        self.hitbox_sprite.x = lambda: self.x
        self.hitbox_sprite.y = lambda: self.y
        self.hitbox_sprite.parallax = OBJ_PARALLAX
        
        self.aura_sprite = video.Sprite("aura")
        self.aura_sprite.x = lambda: self.x
        self.aura_sprite.y = lambda: self.y
        self.aura_sprite.block = lambda: 2 + self.bombing
        self.aura_sprite.parallax = OBJ_PARALLAX
        
        # Set state system.
        self.update = self.opening_state
        
    def opening_state(self):
        """
        Internal state. This brings the ship up to the starting location, either
        at the start of the game session or after having been destroyed.
        """
        self.y -= 3
        if self.orbit_x < 1: self.orbit_x += 1 / 30
        if self.y <= PLAYER_START[1]:
            self.item_collisions = True
            self.parent.set_player_parallax()
            self.update = self.active_state
            self.controller.acquire()
            video.add_renderer(self.render_aura, LAYER_PLAYER_AURA)
            self.parent.event_queue.add_event(video.get_time() + 120,
                                              self.make_vulnerable)

    def aborting_state(self):
        """
        Externally-set state. Scrolls the ship back off the playfield while the
        game over sequence plays when the game is aborted.
        """
        if self.tilt: self.tilt -= calcs.sign(self.tilt)
        self.y_velocity *= 1.05
        if self.y_velocity > 2: self.y_velocity = 2
        self.y += self.y_velocity
        self.orbit_time += 1
        orbit_shape = (self.orbit_time / PLAYER_ORBIT_LENGTH) % math.pi
        self.orbit_x = math.cos(orbit_shape)
        self.orbit_y = math.sin(orbit_shape)
        if self.y > PLAY_AREA_EXIT[3]: self.y_velocity = 0
        
    def make_vulnerable(self):
        """
        Removes the ship's invulnerability, but in case it's called in the
        midst of other tasks that provide it, it does not remove the renderer
        until it is certain it should.
        """
        if not (self.bombing or self.update == self.aborting_state):
            self.collisions = True
            video.remove_renderer(self.render_aura)
    
    def make_options(self):
        """
        Creates and fills the option positional queue.
        """
        self.option_positions = deque(maxlen=self.power + 2)
        for i in range(len(self.option_positions)):
            self.add_option()
        
    def add_option(self):
        """
        Adds the options' current positions to the option queue.
        """
        self.option_positions.appendleft(((self.x + (self.orbit_x * PLAYER_ORBIT[0]),
                                          self.y + (self.orbit_y * PLAYER_ORBIT[1])),
                                         (self.x - (self.orbit_x * PLAYER_ORBIT[0]),
                                          self.y - (self.orbit_y * PLAYER_ORBIT[1])),
                                          self.orbit_x / 4))

    def active_state(self):
        """
        Processes all relevant attributes of the player ship.
        """
        self.velocity = self.get_movement()
        tilt = (self.controller.get_pressed(INPUT_RIGHT) -
                self.controller.get_pressed(INPUT_LEFT))
        if tilt == 0:
            self.tilt -= calcs.sign(self.tilt)
        else:
            self.tilt = calcs.clamp(-PLAYER_TILT_MAX,
                                    self.tilt + tilt,
                                    PLAYER_TILT_MAX)
        self.x = calcs.clamp(self.x + self.velocity[0],
                             PLAY_AREA[0], PLAY_AREA[2])
        self.y = calcs.clamp(self.y + self.velocity[1],
                             PLAY_AREA[1], PLAY_AREA[3])
        self.hitbox.center = (self.x, self.y)
        self.orbit_time += 1
        orbit_shape = (self.orbit_time / PLAYER_ORBIT_LENGTH) % math.pi
        self.orbit_x = math.cos(orbit_shape)
        self.orbit_y = math.sin(orbit_shape)
        self.add_option()
        self.fire_input()
        if self.burst_timer: self.generate_bullets()
        
        if self.bomb_destroy:
            self.parent.enemy_bullets = set()
            for enemy in self.parent.enemies.copy():
                enemy.hp -= 1
                if enemy.hp <= 0 and enemy.collisions:
                    enemy.destroy()

    def get_movement(self):
        """
        Handles movement calculations for the player ship.
        """
        speed = (PLAYER_FOCUS_SPEED if self.focus_timer >= PLAYER_FOCUS_TIME
                                    else PLAYER_SPEED)
        return (((self.controller.get_pressed(INPUT_RIGHT) -
                  self.controller.get_pressed(INPUT_LEFT)) * speed),
                ((self.controller.get_pressed(INPUT_DOWN) -
                  self.controller.get_pressed(INPUT_UP)) * speed))

#################
# Bullet firing #
#################

    def fire_input(self):
        """
        Tracks the status of the firing buttons and sets the various timers
        controlling them likewise.
        """
        if self.controller.get_pressed(INPUT_FIRE):
            if self.focus_timer < PLAYER_FOCUS_TIME: self.focus_timer += 1
        elif self.focus_timer > 0:
            self.focus_timer -= 1
        if ((self.controller.get_pressed(INPUT_FIRE) or
            self.controller.get_pressed(INPUT_AUTOFIRE)) and
            self.burst_timer == 0):
                self.burst_timer = PLAYER_FIRE_TIME

    def fire_callback(self):
        """
        Callback to trigger a burst of fire upon hitting the fire button.
        """
        if self.burst_timer < PLAYER_FIRE_TIME:
            self.burst_timer += PLAYER_FIRE_TIME

    def power_0_fire(self, focus, block):
        """
        At no power, only a single two-bullet block is fired.
        """
        self.bullets.add(CentralBullet((self.x, self.y - 6),
                                        focus, block))
    
    def power_1_fire(self, focus, block):
        """
        With some power, the bullets are split into two angled pairs.
        """
        self.bullets.add(CentralBullet((self.x - 6, self.y - 6),
                                        focus, block + 1))
        self.bullets.add(CentralBullet((self.x + 6, self.y - 6),
                                        focus, block + 2))
    
    def power_2_fire(self, focus, block):
        """
        At full power, three pairs are fired as a combination of both prior
        power states.
        """
        self.bullets.add(CentralBullet((self.x, self.y - 6),
                                        focus, block))
        self.bullets.add(CentralBullet((self.x - 12, self.y),
                                        focus, block + 1))
        self.bullets.add(CentralBullet((self.x + 12, self.y),
                                        focus, block + 2))

    def generate_bullets(self):
        """
        Creates the bullets that the player fires.
        
        TODO: Properly comment this
        """
        self.parent.add_rank(2)
        if not self.burst_timer % PLAYER_FIRE_DELAY:
            focus_range = (PLAYER_FOCUS_TIME -
                           self.focus_timer) / PLAYER_FOCUS_TIME
            focus_value = not focus_range
            self.flare = not self.flare
            block = 0
            if focus_value: block += 3
            if self.flare: block += 6
            audio.play_sound("pl_shot01" if focus_value
                             else "pl_shot00", channel=CHANNEL_BULLET) 
            self.parent.particles.add_particle(
                particle.ShotFlare((self.x - 4, self.y - 6), self.velocity,
                                   focus_value * 3), 0)
            self.parent.particles.add_particle(
                particle.ShotFlare((self.x + 4, self.y - 6), self.velocity,
                                    focus_value * 3), 0)
            self.parent.particles.add_particle(
                particle.ShotFlare(self.option_positions[0][0], self.velocity,
                                   focus_value * 3), 0)
            self.parent.particles.add_particle(
                particle.ShotFlare(self.option_positions[0][1], self.velocity,
                                   focus_value * 3), 0)
            self.firing_modes[self.power](focus_value, block)
            for i in self.option_positions:
                self.bullets.update((OptionBullet(i[0],
                                                  focus_value,
                                                  -i[2] * focus_range, block),
                                     OptionBullet(i[1],
                                                  focus_value,
                                                  i[2] * focus_range, block)))
        self.burst_timer -= 1

###############
# Bomber Code #
###############

    def generate_bomb(self):
        """
        Drops da bomb! This'll turn the player invincible and later cause lots
        of pretty explosions as the screen's cleared of bullets and enemies.
        """
        if self.bombs > 0 and self.bombing == False: # Make sure we can bomb
            self.bombs -= 1
            self.bombing = True # Prevent multiple bombs from going at once
            self.collisions = False # Invincibility frames!
            video.add_renderer(self.render_aura, LAYER_PLAYER_AURA)
            audio.play_sound("pl_bomb")
            self.parent.event_queue.add_event(video.get_time() + 60,
                                              self.bomb_destroy_on)
            # Trigger the damaging flag
            self.parent.event_queue.add_event(video.get_time() + 60,
                                              self.create_blast)
            self.parent.event_queue.add_event(video.get_time() + 68,
                                              self.create_blast)
            self.parent.event_queue.add_event(video.get_time() + 76,
                                              self.create_blast)
            self.parent.event_queue.add_event(video.get_time() + 84,
                                              self.create_blast)
            self.parent.event_queue.add_event(video.get_time() + 92,
                                              self.create_blast)
            self.parent.event_queue.add_event(video.get_time() + 100,
                                              self.create_blast)
            self.parent.event_queue.add_event(video.get_time() + 108,
                                              self.create_blast)
            # All of these are explosion effects.
            self.parent.event_queue.add_event(video.get_time() + 150,
                                              self.cancel_bomb)
            # Reset variables.

    def bomb_destroy_on(self):
        """
        Bomber event callback to start dealing constant damage to enemies.
        """
        self.bomb_destroy = True

    def create_blast(self):
        """
        Explosion effects for the bomb. Much of the calculations here are to
        prevent heavily overlapping explosions.
        """
        audio.play_sound("en_die1", channel=CHANNEL_BIGBOOM)
        x = random.randint(0, 200)
        y = random.randint(0, 280)
        a = random.randint(-10, 10)
        b = random.randint(-10, 10)
        particle.macro_bomb(self.parent.particles, (20 + x, 20 + y))
        particle.macro_bomb(self.parent.particles, (20 + (x + 120 + a) % 200,
                                                    20 + (x + 160 + b) % 280))

    def cancel_bomb(self):
        """
        Called a set time after dropping the bomb, resetting various variables
        that made it invincible.
        """
        self.parent.rank = max(0, self.parent.rank - (MAXIMUM_RANK // 2))
        self.bomb_destroy = False
        self.bombing = False
        if not self.parent.aborting: self.collisions = True
        video.remove_renderer(self.render_aura, recurse=True)

########################
# Collisions and death #
########################

    def collide_test(self, bullet_obstacles, enemy_obstacles=()):
        """
        Checks the list of bullets (and/or enemies) for whether their hitboxes
        are in contact with the player's.
        """
        if self.collisions:
            for target in bullet_obstacles:
                if self.hitbox.colliderect(target.hitbox):
                    bullet_obstacles.discard(target)
                    self.destroy()
                    return
            for target in enemy_obstacles:
                if self.hitbox.colliderect(target.hitbox):
                    self.destroy()
                    return

    def destroy(self):
        """
        The player ship's death wail -- called when on contact with a bullet or
        enemy.
        """
        self.dying = True
        if self.power == 2: # Drop a powerup if it's powerful enough to do so
            self.parent.items.add(items.PowerUp((self.x, self.y), True))
        self.collisions = False # Already blowing up, don't need to die twice!
        self.item_collisions = False # No sense picking up powerups now.
        self.controller.release() # The rudder's dead, we're going down!
        parent = self.parent # Taking a temporary reference for future events.
        audio.play_sound("pl_die") # Boom!
        particle.macro_med_explosion(parent.particles, (self.x, self.y))
        parent.event_queue.add_event(video.get_time() + 9,
                                     self.remove) # Delete yourself!
        parent.event_queue.add_event(video.get_time() + 90,
                                     parent.generate_player) # Come back (or game over)!

    def remove(self):
        """
        The second step in the destruction process - deleting the player object
        from the game.
        """
        if self.parent.lives == 0: # If the game's over, end the music.
            audio.fade_music(35)
        video.remove_renderer(self.render) # Delete our rendering callback
        self.parent.player = None 
        self.parent = None
        # Remove references. Now the garbage collector can pick us up.
        gc.collect() # Speaking of which, good of a time as any...

#############
# Rendering #
#############

    def render(self, target):
        """
        Rendering callback for the player ship.
        """
        self.bit_a_sprite.render(target) # Draw the underside option
        self.sprite.render(target) # Draw the ship itself
        self.engine_sprite.render(target) # Draw the engine flare
        self.bit_b_sprite.render(target) # Draw the overhanging option!
    
    def render_aura(self, target):
        """
        Rendering callback for the player ship's aura, displayed during
        invincible periods.
        """
        if (video.get_time() // (1 + data.config.frame_skip)) % 2:
            self.aura_sprite.render(target)

##################
# Bullet objects #
##################

class OptionBullet(bullets.AimedBullet):
    """
    Child object to handle the behaviours of the player's bullets. These angle
    themselves based on the direction the player's facing.
    """
    __slots__ = ["focus"]
    def __init__(self, coordinates, focus, angle, block):
        bullets.AimedBullet.__init__(self, coordinates,
                                     video.Sprite("player_shot", block=block),
                                     math.pi + angle, PLAYER_FIRE_VELOCITY,
                                     PLAYER_BULLET_HITBOX)
        self.focus = focus

class CentralBullet(bullets.Bullet):
    """
    Child object to handle the behaviours of the player's bullets. These are
    in fact clumps of bullets to reduce excess collision handling. 
    """
    __slots__ = ["focus"]
    damage = 2
    def __init__(self, coordinates, focus, block):
        bullets.Bullet.__init__(self, coordinates,
                                video.Sprite("player_shot", block=block),
                                (0, -1), PLAYER_FIRE_VELOCITY,
                                PLAYER_BULLET_HITBOX)
        self.focus = focus

gfx_resources = ["player_ship2", "player_option", "player_engine",
                 "aura", "effects", "explosion", "explosion_big",
                 "player_shot"]
sfx_resources = ["pl_shot00", "pl_shot01", "pl_bomb", "pl_die"]
