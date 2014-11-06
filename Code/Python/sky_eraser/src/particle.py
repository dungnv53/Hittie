"""
Sky Eraser (For real this time!)
By Prerisoft

Particle Effects
File creation date: Jan 14, 2013

THIS FILE IS LICENSED UNDER THE 3-CLAUSE BSD LICENSE. PLEASE SEE LICENSE(.txt)
FOR DETAILS.
"""

from __future__ import division, print_function

import random

import calcs, event, video
from constants import OBJ_PARALLAX

class ParticleSystem(object):
    """
    Particle effects system. Unlike other base systems, this has to be called by
    an in-game object.
    """
    def __init__(self):
        self.particles = (set(), set())
        self.event_queue = event.DynamicQueue()
    def add_particle(self, particle, layer=1):
        """
        Adds a new particle at the given layer, either 1 or 0. Admittedly a bit
        kludged.
        """
        self.particles[layer].add(particle)
        self.event_queue.add_event(video.get_time() + particle.ttl, lambda: self.particles[layer].discard(particle))
    def queue_particle(self, time, particle, layer=1, **parameters):
        """
        Adds a particle to be created at a later time, likewise on either layer
        1 or 0.
        """
        self.event_queue.add_event(time, lambda: self.add_particle(particle(**parameters),
                                                                   layer))
    def render(self, target, layer=1):
        """
        Renders the particles on the associated layer.
        """
        self.event_queue.pump()
        for particle in self.particles[layer].copy():
            particle.render(target)
            
class Particle(video.Sprite):
    """
    Base class for the particle effects. You have to manually call the sprite's
    __init__() so as to avoid any restrictions on visual functionality.
    """
    ttl = 0
    def __init__(self):
        self.parallax = OBJ_PARALLAX

# ---

class ShotFlare(Particle):
    """
    Brief object for displaying firing graphics. Kind of ugly and unflashy
    at the current time.
    """
    ttl=6
    def __init__(self, coordinates, velocity, offset=0):
        video.Sprite.__init__(self, "effects")
        Particle.__init__(self)
        self.block = video.Lerp(offset, offset + 2, time=self.ttl - 2)
        self.x = video.Lerp(coordinates[0], coordinates[0] + (velocity[0] * self.ttl),
                            time=self.ttl)
        self.y = video.Lerp(coordinates[1], coordinates[1] + (velocity[1] * self.ttl),
                            time=self.ttl)

class Explosion(Particle):
    """
    Standard explosion. Parameters given are coordinates and velocity, both
    x/y tuples.
    """
    ttl = 23
    def __init__(self, coordinates, velocity):
        video.Sprite.__init__(self, "explosion")
        Particle.__init__(self)
        self.block = video.Lerp(0, 7, time=self.ttl - 2)
        self.x = video.Lerp(coordinates[0], coordinates[0] + (velocity[0] * 15)
                            + random.randint(-8, 8), time=self.ttl)
        self.y = video.Lerp(coordinates[1], coordinates[1] + (velocity[1] * 15)
                            + random.randint(-8, 8), time=self.ttl)

class BigExplosion(Particle):
    """
    Grand old explosion's core. Parameters given are coordinates, an x/y tuple.
    """
    ttl = 21
    def __init__(self, coordinates):
        video.Sprite.__init__(self, "explosion_big")
        Particle.__init__(self)
        self.block = video.Lerp(0, 6, time=self.ttl)
        self.x, self.y = coordinates

sixth = calcs.tau / 6
# AFAIK, Python does not optimise in cases like these so we're defining early.
# Keeps extra calculations down.
def macro_bomb(controller, coordinates):
    """
    Creates a large-scale explosion effect, largely for bombing.
    """
    offset = random.uniform(0, sixth) # A little randomness...
    for i in xrange(6): # Creates six periphery explosions
        angle = calcs.aim(offset + (sixth * i) + random.uniform(-0.2, 0.2))
        controller.add_particle(Explosion((coordinates[0] + (angle[0] * 20),
                                           coordinates[1] + (angle[1] * 20)),
                                          (angle[0] * 0.75, angle[1] * 0.75)))
    controller.add_particle(BigExplosion(coordinates)) # And the center blast.

def macro_med_explosion(controller, coordinates):
    """
    A moderate burst of explosion effects, for player and medium-enemy death.
    """
    controller.add_particle(Explosion(coordinates, (-1, 1)))
    controller.queue_particle(video.get_time() + 3, Explosion,
                              coordinates=coordinates, velocity=(-1, -1))
    controller.queue_particle(video.get_time() + 6, Explosion,
                              coordinates=coordinates, velocity=(1, -1))
    controller.queue_particle(video.get_time() + 9, Explosion,
                              coordinates=coordinates, velocity=(1, 1))

random_positioner = lambda: (random.uniform(-0.4, 0.4),
                             random.uniform(-0.4, 0.4))
eighth = calcs.tau / 8
def macro_large_explosion(controller, coordinates):
    """
    A not-insignificant explosion, for large enemies to explode by.
    """
    
    controller.add_particle(Explosion(coordinates, random_positioner()))
    offset = random.uniform(0, eighth)
    for i in range(8):
        distance = 12 * (1 + i % 2)
        angle = calcs.aim(offset + (eighth * i) + random.uniform(-0.2, 0.2))
        controller.queue_particle(video.get_time() + i + 1,
                                  Explosion,
                                  coordinates=(coordinates[0]+(angle[0] * distance),
                                               coordinates[1]+(angle[1] * distance)),
                                              velocity=random_positioner())
