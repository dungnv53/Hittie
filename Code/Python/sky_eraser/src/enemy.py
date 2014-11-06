"""
Sky Eraser (For real this time!)
By Prerisoft

Enemy Routines
File creation date: Jan 25, 2013

THIS FILE IS LICENSED UNDER THE 3-CLAUSE BSD LICENSE. PLEASE SEE LICENSE(.txt)
FOR DETAILS.
"""

from __future__ import division
import sys, math, weakref, pygame.rect
import calcs, video, audio, particle, bullets, event, items
from constants import (PLAY_AREA_EXIT, PLAY_AREA_EXIT_RECT, LERP_REPEAT,
                       OBJ_PARALLAX, MAXIMUM_RANK, PLAYER_PROXIMITY,
                       
                       CHANNEL_ZAKO, CHANNEL_BIGBOOM,
                       
                       LINKNODE_SPEED, LINKNODE_ROTATE,
                       LINKNODE_CENTERPOINT, LINKNODE_ENTRY_RANGE,
                       LINKNODE_EXIT_RANGE, LINKNODE_FIRE,
                       LINKNODE_WAVE_TIME,
                       
                       INTERNODE_ENTRY_RANGE, INTERNODE_ENTRY_VAR,
                       INTERNODE_ENTRY_TIME, INTERNODE_SPEED_RANGE,
                       INTERNODE_COUNT_RANGE, INTERNODE_WAVE_TIME,
                       INTERNODE_ANGLE, INTERNODE_BASE_ANGLE, INTERNODE_FIRE,
                       
                       WARNODE_WAVE_TIME, WARNODE_SPREAD, WARNODE_SPREAD_WIDTH)

class BaseEnemy(object):
    """
    Base-level enemy code. Initialises all starting values.
    """
    __slots__ = ("parent", "x", "y", "x_velocity", "y_velocity",
                 "x_acceleration", "y_acceleration", "hp", "collisions",
                 "bullets", "event_queue", "hitbox", "sprite")
    rank = 1 # Rank increase when destroyed
    score = 1 # Score granted by enemy / 100
    combo = 10 # Combo time bonus granted by enemy 

    def __init__(self, parent, location=(0, 0)):
        self.parent = weakref.proxy(parent)
        # Game Scene object, for manipulating score and getting player-targeting
        # coordinates.
        
        # Positional variables
        self.hp = 1
        self.x, self.y = location
        self.x_velocity, self.y_velocity = 0.0, 0.0
        self.x_acceleration, self.y_acceleration = 1.0, 1.0
        # Acceleration. 1.0 is maintaining speed
        
        # Gameplay variables
        self.collisions = True
        self.bullets = weakref.WeakSet()
        # Bullet groups, for clearing in case of special destruction
        self.event_queue = event.StaticQueue() 
        self.hitbox = pygame.Rect((self.x, self.y), (1, 1))

    def update(self):
        """
        Basic update function. If overridden, this should still be called as
        it handles a lot of important upkeep work.
        """
        self.event_queue.pump()
        self.x_velocity *= self.x_acceleration
        self.y_velocity *= self.y_acceleration
        self.x += self.x_velocity
        self.y += self.y_velocity
        self.hitbox.center = (self.x, self.y)

    def collide_test(self, obstacles):
        """
        Tests if the enemy's hitbox is colliding with any known obstacles in
        the group provided. This will modify the contents as it discards
        objects.
        """
        if self.collisions:
            for target in obstacles.copy():
                if self.hitbox.colliderect(target.hitbox):
                    if target.focus: self.parent.add_combo(target.damage, 0)
                    self.hp -= target.damage
                    self.parent.add_score(target.damage, False)
                    obstacles.discard(target)
                    if self.hp <= 0:
                        self.destroy()
                        return

    def destroy(self):
        """
        Rewards the player the spoils of war after it's destroyed. For simply
        decommissioning the enemy object, see remove()
        """
        self.parent.add_score(self.score)
        self.parent.add_rank(self.rank)
        self.parent.kills += 1
        self.parent.add_combo(self.combo)
        self.remove()

    def remove(self):
        """
        Handles basic enemy deletion. If overridden, it should be called, since
        it does handle the most valuable part of the deletion process.
        """
        self.parent.enemies.discard(self)

    def set_movement(self, position=None, velocity=None, acceleration=None):
        """
        A method for changing any of the movement values, mostly to provide
        easy implementation in the static event queue.
        """
        if position: self.x, self.y = position
        if velocity: self.x_velocity, self.y_velocity = velocity
        if acceleration: self.x_acceleration, self.y_acceleration = acceleration

    def fire_bullet(self, bullet, **characteristics):
        """
        Creates a bullet onscreen, though it does not shoot if the player's
        right in its face.
        """
        if calcs.distance(self.hitbox.center,
                          self.parent.player_coordinates) > PLAYER_PROXIMITY:
            # Increase bullet speed based on rank
            characteristics["velocity"] *= 1 + self.parent.rank / MAXIMUM_RANK
            bullet_object = bullet(**characteristics)
            self.bullets.add(bullet_object)
            self.parent.enemy_bullets.add(bullet_object)

    def render(self):
        """
        Blank rendering method to be overridden. Put your own rendering code
        here, or just direct this to your sprite's render method!
        """
        pass

# ---

class LinkNode(BaseEnemy):
    """
    The most simplistic enemy of Sky Eraser, the Link Node is part of a chain
    that enters, rotates, and exits the battlefield, firing once in the process.
    It is also the only enemy that has suicide bullets.
    """
    rank = 3
    score = 4
    combo = 30
    __slots__ = ("angle", "comparator", "increment", "motive_state", "render",
                 "hitsprite", "double")
    def __init__(self, parent, location, entry_angle, exit_angle,
                 comparator, increment, double=False):
        BaseEnemy.__init__(self, parent, location)
        self.hp = 2
        self.hitbox = pygame.Rect((self.x, self.y), (20, 20))
        self.event_queue.set_queue((30, self.change_state))
        
        self.angle = entry_angle
        self.angle_movement(entry_angle)
        self.comparator = comparator
        self.increment = increment
        self.double = double
        self.motive_state = self.forward_state
        
        self.sprite = video.Sprite("enemy_link")
        self.sprite.x = lambda: self.x
        self.sprite.y = lambda: self.y
        self.sprite.parallax = OBJ_PARALLAX
        self.sprite.block = video.Lerp(start=0,
                                       end=24,
                                       time=64,
                                       extend=LERP_REPEAT)
        self.render = self.sprite.render

    def update(self):
        """
        Runs the standard update and runs a state execution.
        """
        BaseEnemy.update(self)
        self.motive_state()

    def destroy(self):
        """
        Destroys the link node with a small explosion.
        """
        audio.play_sound("en_die0", channel=CHANNEL_ZAKO)
        if self.motive_state == self.forward_state:
            self.create_bullet()
        self.parent.particles.add_particle(particle.Explosion((self.x, self.y),
                                                              (self.x_velocity,
                                                              self.y_velocity)))
        BaseEnemy.destroy(self)

    def angle_movement(self, angle):
        """
        Aims the Link Node towards a given vector.
        """
        direction = calcs.aim(angle)
        self.x_velocity = direction[0] * LINKNODE_SPEED
        self.y_velocity = direction[1] * LINKNODE_SPEED

    def forward_state(self):
        """
        Empty state.
        """
        pass

    def rotate_state(self):
        """
        Changes the Link Node's vector and moves to the exit state if it's at
        a given threshold.
        """
        self.angle += self.increment
        self.angle_movement(self.angle)
        if self.comparator(self.angle):
            self.motive_state = self.exit_state

    def exit_state(self):
        """
        Once out of the playfield, the Link Node removes itself.
        """
        if not PLAY_AREA_EXIT_RECT.collidepoint(self.x, self.y):
            self.remove()

    def create_bullet(self):
        """
        Aims and fires a bullet at the player.
        """
        sprite = video.Sprite("dot_o")
        sprite.block = video.Lerp(0, 4, time=12, extend=LERP_REPEAT)
        self.fire_bullet(bullets.Bullet,
                         location=(self.x, self.y), sprite=sprite,
                         vector=calcs.vector_unit((self.x, self.y),
                                                self.parent.player_coordinates),
                         velocity=LINKNODE_FIRE)
        self.parent.particles.add_particle(
            particle.ShotFlare((self.x, self.y),
                               (self.x_velocity, self.y_velocity), 3))

    def double_bullet(self):
        """
        Aims and fires two bullets to either side of the player.
        """
        for i in (0.3, -0.3):
            sprite = video.Sprite("dot_o")
            sprite.block = video.Lerp(0, 4, time=12, extend=LERP_REPEAT)
            self.fire_bullet(bullets.Bullet,
                             location=(self.x, self.y), sprite=sprite,
                             vector=calcs.aim(i + calcs.angle((self.x, self.y),
                                              self.parent.player_coordinates)),
                             velocity=LINKNODE_FIRE)
        self.parent.particles.add_particle(
            particle.ShotFlare((self.x, self.y),
                               (self.x_velocity, self.y_velocity), 3))
    
    def change_state(self):
        """
        Drops its payload and proceeds to twist around to its exit vector.
        """
        if self.double: self.double_bullet()
        else: self.create_bullet()
        self.motive_state = self.rotate_state

def generate_links(params):
    """
    Generator function for create_link_nodes()
    """
    double = False
    for i in xrange(0, 41, 10):
        parameters = dict(params, double=double)
        double = not double
        yield (i, LinkNode, parameters)

def create_link_nodes(generator):
    """
    Enemy creation function for the Link Node enemy type. Creates five link
    nodes from the sides or top, as well as configuring their total rotation
    period.
    """
    if generator.choice((True, False)): # Enters from top
        entry_x = generator.randint(PLAY_AREA_EXIT[0], PLAY_AREA_EXIT[2])
        entry_y = PLAY_AREA_EXIT[1]
    else: # Enters from side
        entry_x = generator.choice((PLAY_AREA_EXIT[0], PLAY_AREA_EXIT[2]))
        entry_y = generator.randint(PLAY_AREA_EXIT[1] + 48,
                                    LINKNODE_CENTERPOINT[1] - 48)
    main_angle = calcs.angle((entry_x, entry_y), LINKNODE_CENTERPOINT)
    entry_angle = generator.uniform(main_angle - LINKNODE_ENTRY_RANGE,
                                 main_angle + LINKNODE_ENTRY_RANGE) % calcs.tau
    exit_angle = generator.uniform(math.pi - LINKNODE_EXIT_RANGE,
                                     math.pi + LINKNODE_EXIT_RANGE) % calcs.tau
    if entry_angle > math.pi:
        increment = -LINKNODE_ROTATE
        comparator = lambda x: x < exit_angle
        if exit_angle > entry_angle: exit_angle -= calcs.tau
    else:
        increment = LINKNODE_ROTATE
        comparator = lambda x: x > exit_angle
        if exit_angle < entry_angle: exit_angle += calcs.tau
    parameters = {"location":(entry_x, entry_y),
                  "entry_angle":entry_angle,
                  "exit_angle":exit_angle,
                  "comparator":comparator,
                  "increment":increment}
    return (LINKNODE_WAVE_TIME, tuple(generate_links(parameters)))

# ---

class InterceptorNode(BaseEnemy):
    """
    One of the standard rank-and-file like the link nodes, the interceptor
    nodes sweep in from above in groups, firing before retreating. 
    """
    rank = 3
    score = 4
    combo = 30
    __slots__ = ("check", "render", "origin_coordinates", "target_vector",
                 "hitsprite")
    def __init__(self, parent, location, velocity):
        BaseEnemy.__init__(self, parent, (location, PLAY_AREA_EXIT[1]))
        self.hp = 4
        self.y_velocity = velocity
        self.y_acceleration = 0.95
        self.hitbox = pygame.Rect((self.x, self.y), (16, 22))
        self.event_queue.set_queue((10, self.lock),
                                    (10, self.create_bullet),
                                    (14, self.create_bullet),
                                    (18, self.create_bullet))
        self.check = self.enter_check
        self.sprite = video.Sprite("enemy_intercept2")
        self.sprite.x = lambda: self.x
        self.sprite.y = lambda: self.y - 4
        self.sprite.parallax = OBJ_PARALLAX
        self.render = self.sprite.render

    def destroy(self):
        """
        Plays a basic explosion effect.
        """
        audio.play_sound("en_die0", channel=CHANNEL_ZAKO)
        self.parent.particles.add_particle(particle.Explosion((self.x, self.y),
                                           (self.x_velocity, self.y_velocity)))
        BaseEnemy.destroy(self)

    def get_angle(self, coordinates):
        """
        Gets the interceptor node's pointed direction and converts it to a
        format that the sprite can use to visually represent it.
        """
        return calcs.clamp(((calcs.angle((self.x, self.y),
                                          coordinates) + INTERNODE_BASE_ANGLE)
                            // INTERNODE_ANGLE),
                           0, 6)

    def lock(self):
        """
        Gets the direction to fire in and stores it for the period of its
        firing arc.
        """
        self.origin_coordinates = (self.x, self.y + 16)
        self.target_vector = calcs.vector_unit((self.x, self.y),
                                        self.parent.player_coordinates)

    def create_bullet(self):
        """
        Fires a set of bullets in a line at the player, one with each call.
        """
        sprite = video.Sprite("dot_o")
        sprite.block = video.Lerp(0, 3, time=8, extend=LERP_REPEAT)
        self.fire_bullet(bullets.Bullet,
                         location=self.origin_coordinates, sprite=sprite,
                         vector=self.target_vector,
                         velocity=INTERNODE_FIRE)
        self.parent.particles.add_particle(
            particle.ShotFlare(self.origin_coordinates,
                               (self.x_velocity, self.y_velocity), 3))

    def enter_check(self):
        """
        Checks if it's slowed down enough to begin rising back up.
        """
        self.sprite.block = self.get_angle(self.parent.player_coordinates)
        if self.y_velocity < 0.2:
            self.y_velocity = -self.y_velocity
            self.y_acceleration = 1.06
            self.check = self.exit_check

    def exit_check(self):
        """
        Checks if it's out of the playfield yet.
        """
        if not PLAY_AREA_EXIT_RECT.collidepoint(self.x, self.y):
            self.remove()

    def update(self):
        """
        Performs standard upkeep and checks its vectors.
        """
        BaseEnemy.update(self)
        self.check()
    
def internode_generator(generator, count, center):
    """
    Generator function for create_interceptor_nodes()
    """
    for i in xrange(0, count):
        parameters = {"location":center + generator.randint(*INTERNODE_ENTRY_VAR),
                      "velocity":generator.uniform(*INTERNODE_SPEED_RANGE)
                      }
        yield (i * INTERNODE_ENTRY_TIME, InterceptorNode, parameters)
    

def create_interceptor_nodes(generator):
    """
    Enemy creation function for the Interceptor Node enemy type. Chooses a
    random number of link nodes and a center point for them to spawn around,
    a behaviour which will be relegated to 'clump generation' or removed when
    more formation patterns are created.
    """
    count = generator.randint(*INTERNODE_COUNT_RANGE)
    center = generator.randint(*INTERNODE_ENTRY_RANGE)
    return (INTERNODE_WAVE_TIME + (INTERNODE_ENTRY_TIME * count),
            tuple(internode_generator(generator, count, center)))

# ---

class SoldierNode(BaseEnemy):
    """
    An intermediate enemy that flies in from the top of the screen and fires
    several bursts of spreading bullets roughly centered on the player before
    its retreat. Elite variants can spawn which fire chained bullets and drop
    power-ups (yet to be implemented).
    """
    rank = 10
    score = 20
    combo = 90
    def __init__(self, parent, location, speed, elite=False):
        BaseEnemy.__init__(self, parent, location)
        self.hp = 50
        self.x_limits = (location[0] - 20, location[0] + 20)
        # Precalculates x values to reverse direction at
        self.y_velocity = speed

        self.hitbox = pygame.Rect((self.x, self.y), (28, 28))
        self.event_queue.set_queue((10, self.brake))
        
        self.sprite = video.Sprite("enemy_warrior", 8)
        self.sprite.x = lambda: self.x
        self.sprite.y = lambda: self.y
        self.sprite.block = 8
        self.sprite.parallax = OBJ_PARALLAX
        self.render = self.sprite.render
        
        self.elite = elite # Stores the elite flag
        if elite:
            self.score = 20 # Double score, these fuckers are rich
            self.hp = 75 # 1.5x HP, these fuckers are tough
    
    def destroy(self):
        """
        Called when the soldier node's HP reaches 0, marking it as immune to
        further collisions while it ceases script execution and explodes.
        """
        if self.elite:
            if self.parent.extends:
                self.parent.extends -= 1
                self.parent.items.add(items.Extend((self.x, self.y))) # make extend
            else:
                self.parent.items.add(items.PowerUp((self.x, self.y))) # make powerup
        audio.play_sound("en_die0", CHANNEL_ZAKO)
        self.collisions = False
        del self.update # Should drop back to BaseEnemy...
        particle.macro_large_explosion(self.parent.particles, (self.x, self.y))
        self.parent.event_queue.add_event(video.get_time() + 4,
                                          lambda: audio.play_sound("en_die1",
                                                               CHANNEL_BIGBOOM))
        self.parent.event_queue.add_event(video.get_time() + 9,
                                          lambda: BaseEnemy.destroy(self))

    def brake(self):
        """
        Changes the soldier node's Y acceleration and moves to an update state
        where it monitors its speed.
        """
        self.y_acceleration = 0.95
        self.update = self.state_brake
    
    def lock_on(self):
        """
        Combat script where it sets the target coordinates for its bullet
        patterns to the player's position.
        """
        self.fire_angle = 0
        self.fire_target = calcs.angle((self.x, self.y),
                                       self.parent.player_coordinates)

    def retreat(self):
        """
        An enemy like would be annoying if it stayed around for a long time,
        so after a few volleys, it breaks off and flies away.
        """
        self.update = self.exit_state
        self.sprite.block = video.Lerp(8, (0, 16)[self.x >= 141],
                                       32) # Animate strafe
        self.x_velocity = 0.35 * (-1, 1)[self.x >= 141] # Which direction to go
        self.x_acceleration = 1.045
        self.y_velocity = 0.3

    def fire_initial(self):
        """
        Fires a single bullet (or a pair of them if the node's an elite
        variant) towards the player.
        """
        angle = calcs.aim(self.fire_target)
        location = (self.x + (angle[0] * 30),
                    self.y - 10 + (angle[1] * 30))
        self.parent.particles.add_particle(
            particle.ShotFlare(location,
                               (self.x_velocity, self.y_velocity), 6))
        self.fire_bullet(bullets.AimedBullet,
                         location=location, sprite=video.Sprite("beam_b"),
                         angle=self.fire_target,
                         velocity=WARNODE_SPREAD)
        if self.elite: # When you're an elite, your bullet count doubles
            self.fire_bullet(bullets.AimedBullet,
                 location=location, sprite=video.Sprite("beam_b"),
                 angle=self.fire_target,
                 velocity=WARNODE_SPREAD * 0.75)
    
    def fire_spread(self):
        """
        Fires a pair of bullets in an increasingly outward angle from its
        target direction. Fires two more behind them if the node's an elite
        variant.
        """
        self.fire_angle += WARNODE_SPREAD_WIDTH 
        for i in (self.fire_target + self.fire_angle,
                  self.fire_target - self.fire_angle):
            angle = calcs.aim(i)
            location = (self.x + (angle[0] * 30),
                        self.y - 10 + (angle[1] * 30))
            self.parent.particles.add_particle(
                particle.ShotFlare(location,
                                   (self.x_velocity, self.y_velocity), 6))
            self.fire_bullet(bullets.AimedBullet,
                             location=location, sprite=video.Sprite("beam_b"),
                             angle=i,
                             velocity=WARNODE_SPREAD)
            if self.elite:
                self.fire_bullet(bullets.AimedBullet,
                                 location=location, sprite=video.Sprite("beam_b"),
                                 angle=i,
                                 velocity=WARNODE_SPREAD * 0.75)
    
    def state_brake(self):
        """
        Monitors the Y velocity during the decceleration, and once the
        threshold is reached it locks and moves to the combat script.
        """
        if self.y_velocity < 0.3: # We're slow enough now.
            # Start script from beginning
            self.event_queue.start_time = video.get_time()
            self.event_queue.set_queue((0, self.lock_on), # Lock on
                                       (0, self.fire_initial), # Volley 1
                                       (6, self.fire_spread),
                                       (12, self.fire_spread),
                                       (18, self.fire_spread), # Volley ends
                                       (60, self.lock_on), # Lock on again
                                       (60, self.fire_initial), # Volley 2
                                       (66, self.fire_spread),
                                       (72, self.fire_spread),
                                       (78, self.fire_spread), # Volley ends
                                       (120, self.lock_on), # Lock on again!
                                       (120, self.fire_initial), # Volley 3
                                       (126, self.fire_spread),
                                       (132, self.fire_spread),
                                       (138, self.fire_spread), # Volley ends
                                       (180, self.lock_on), # Final lock on
                                       (180, self.fire_initial), # Volley 4
                                       (186, self.fire_spread),
                                       (192, self.fire_spread),
                                       (198, self.fire_spread), # Volley ends
                                       (270, self.retreat) # Still alive? Leave
                                       )
            self.y_acceleration = 1.0
            self.y_velocity = 0.2
            self.x_velocity = 0.35
            if self.x > 141: # If the enemy's on the righthand side,
                self.x_velocity *= -1 # Make them weave left to start.
            self.update = self.state_idle
        BaseEnemy.update(self)
    
    def exit_state(self):
        """
        During its retreat, the node checks if it's offscreen and removes
        itself from play once it is.
        """
        BaseEnemy.update(self)
        if not PLAY_AREA_EXIT_RECT.collidepoint(self.x, self.y):
            self.remove()

    def state_idle(self):
        """
        Executes the standard script while slowly weaving from side to side.
        """
        if (self.x < self.x_limits[0]) or (self.x > self.x_limits[1]):
            self.x_velocity *= -1
        BaseEnemy.update(self)  

def create_soldiers(generator):
    """
    Enemy creation function for the soldier node. Vastly simpler than the
    other creation functions, this creates a single warrior node somewhere at
    the top of the screen, its entry speed, and whether or not it is an elite
    variant.
    """
    return (WARNODE_WAVE_TIME,
            ((0, SoldierNode, {"elite":generator.random() < 0.2,
                                "speed":generator.uniform(2, 4),
                                "location":(generator.randint(80, 200),
                                            -16)}),))

# ---

class SentryNode(BaseEnemy):
    pass

def create_sentry_node(generator):
    pass

gfx_resources = ["enemy_link", "enemy_intercept2", "enemy_warrior",
                 "dot_o", "beam_b"]
sequence = [create_link_nodes, create_interceptor_nodes, create_soldiers]
max_enemies = len(sequence)
