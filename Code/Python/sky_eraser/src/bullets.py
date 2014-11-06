"""
Sky Eraser (For real this time!)
By Prerisoft

Player Code
File creation date: Apr 07, 2013

The result of a heavy code rearrangement.

THIS FILE IS LICENSED UNDER THE 3-CLAUSE BSD LICENSE. PLEASE SEE LICENSE(.txt)
FOR DETAILS.
"""

from __future__ import division

import math, pygame.rect
import calcs, video
from constants import PLAY_AREA_EXIT_RECT, OBJ_PARALLAX

class Bullet(object):
    """
    A generic bullet object, for friend or foe.
    """
    damage = 1
    focus = False
    __slots__ = ("x", "y", "x_velocity", "y_velocity", "sprite", "hitbox",
                 "__weakref__")
    def __init__(self, location, sprite, vector, velocity, hitbox=(4, 4),
                 script=None):
        self.x, self.y = location
        self.x_velocity = vector[0] * velocity
        self.y_velocity = vector[1] * velocity
        self.sprite = sprite
        self.sprite.x = lambda: self.x
        self.sprite.y = lambda: self.y
        self.sprite.parallax = OBJ_PARALLAX
        self.hitbox = pygame.Rect((self.x, self.y), hitbox)
    def update(self):
        """
        Shifts the bullet's location according to its velocity, and returns
        a boolean value to its exit handler if it's out of bounds.
        """
        self.x += self.x_velocity
        self.y += self.y_velocity
        self.hitbox.center = (self.x, self.y)
        return not PLAY_AREA_EXIT_RECT.collidepoint(self.x, self.y)
        # TODO: write script system and make deletion status the end-part of it
        # ...maybe not for this project, though. focus on optimisation for now.

class AimedBullet(Bullet):
    """
    A bullet whose sprite block is calculated dependent on its angle.
    """
    __slots__ = ()
    def __init__(self, location, sprite, angle, velocity, hitbox=(4, 4),
                 script=None):
        Bullet.__init__(self, location, sprite,
                        calcs.aim(angle), velocity,
                        hitbox, script)
        original_block = self.sprite.block
        self.sprite.block = (int((angle + calcs.angle_64) / calcs.angle_32)
                             % 32) + original_block

