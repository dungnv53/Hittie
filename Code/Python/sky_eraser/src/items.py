"""
Sky Eraser (For real this time!)
By Prerisoft

Powerups
File creation date: Oct 21, 2013

THIS FILE IS LICENSED UNDER THE 3-CLAUSE BSD LICENSE. PLEASE SEE LICENSE(.txt)
FOR DETAILS.
"""

import pygame.rect
import video, audio, particle
from constants import PLAY_AREA, OBJ_PARALLAX

class Item(object):
    """
    Base item code.
    """
    particle_block = 0
    points_block = 12
    def __init__(self, position):
        self.x, self.y = position
        self.y_velocity = 0.1
        self.y_acceleration = 1.025
        
        self.aura = video.Sprite("aura", 0)
        self.aura.x = lambda: self.x
        self.aura.y = lambda: self.y
        self.aura.block = lambda: video.get_time() // 4 % 2
        self.aura.parallax = OBJ_PARALLAX
        self.sprite = video.Sprite("hud", 0)
        self.sprite.x = lambda: self.x
        self.sprite.y = lambda: self.y
        self.sprite.parallax = OBJ_PARALLAX

        self.hitbox = pygame.Rect((0, 0), (32, 32))
    def get_particle(self, generator):
        """
        Code to override for the creation of the item caption.
        """
        pass
    def update(self):
        """
        Basic update sequence.
        """
        self.y += self.y_velocity
        self.y_velocity *= self.y_acceleration
        if self.y_velocity > 2:
            self.y_velocity = 2
            self.y_acceleration = 1
        self.hitbox.center = self.x, self.y
    def render(self, target):
        self.aura.render(target)
        self.sprite.render(target)

class Message(particle.Particle):
    """
    Temporary message indicating what the item you just picked up was.
    """
    ttl = 90
    __slots__ = ("start_block", "turnover_time")
    def __init__(self, location, block):
        self.start_block = block
        self.turnover_time = video.get_time() + 12
        video.Sprite.__init__(self, "messages", self.set_block, location)
        particle.Particle.__init__(self)
        self.y = video.Lerp(location[1], location[1] - 45, 90)
    def set_block(self):
        """
        Callable method to generate the right sort of blinking behaviour while
        still unfolding at the start of the particle's lifetime.
        """
        return (self.start_block + (video.get_time() // 6) % 2
                + (2 if video.get_time() >= self.turnover_time else 0))

class PowerUp(Item):
    """
    Item that increases the player ship's shot power.
    """
    particle_block = 0
    def __init__(self, position, reverse=False):
        Item.__init__(self, position)
        self.sprite.block = 2
        if reverse == True:
            self.y_velocity = -2
            self.y_acceleration = 0.98
            self.update = self.update_reverse
    def update_reverse(self):
        """
        Special method that tracks when the item has stopped flying upward.
        """
        Item.update(self)
        if self.y_velocity > -0.1:
            self.y_velocity = 0.1
            self.y_acceleration = 1.025
            del self.update
    def activate(self, parent):
        """
        Activation method for providing the player extra shot power or - if
        at full power - extra score.
        """
        if parent.player.power == 2:
            audio.play_sound("pl_point")
            parent.add_score(100)
            block = self.points_block
        else:
            audio.play_sound("pl_power")
            parent.player.power += 1
            parent.player.make_options()
            block = self.particle_block
        parent.particles.add_particle(Message((self.x, self.y), block), 0)
    def get_particle(self, generator):
        generator.add_particle()
    
class Bomber(Item):
    """
    Pickup that provides the player another bomb. Fully functional, but the
    enemy that drops it isn't implemented yet!
    """
    particle_block = 4
    def __init__(self, position):
        Item.__init__(self, position)
        self.sprite.block = 1
    def activate(self, parent):
        """
        Activation method for providing the player another bomb or - if fully
        stocked - extra score.
        """
        if parent.player.bombs == 5:
            audio.play_sound("pl_point")
            parent.add_score(100)
            block = self.points_block
        else:
            audio.play_sound("pl_power")
            parent.player.bombs += 1
            block = self.particle_block
        parent.particles.add_particle(Message((self.x, self.y), block), 0)
    
class Extend(Item):
    """
    Item that is dropped in lieu of a bomb or powerup when the player's score
    has passed 1,000,000 points or any odd million past that.
    """
    particle_block = 8
    def __init__(self, position):
        Item.__init__(self, position)
        self.sprite.block = 3
    def activate(self, parent):
        """
        Grants the player an extend or - if they have somehow managed to have
        maximum lives - a large amount of points!
        """
        if parent.lives == 5:
            audio.play_sound("pl_point")
            parent.add_score(1000)
            block = self.points_block
        else:
            audio.play_sound("pl_extend")
            parent.lives += 1
            block = self.particle_block
            parent.particles.add_particle(Message((self.x, self.y), block), 0)

sfx_resources = ["pl_power", "pl_point", "pl_extend"]
