"""
Sky Eraser (For real this time!)
By Prerisoft

Calculation Handling
File creation date: Feb 08, 2013

Like control, this was much entirely recycled from Valiancer/Sky Eraser b's
code. Also like control, I'll be updating it if I come up with something
better.

THIS FILE IS LICENSED UNDER THE 3-CLAUSE BSD LICENSE. PLEASE SEE LICENSE(.txt)
FOR DETAILS.
"""

from __future__ import division
import math

########################
# Numeric calculations #
########################

def next_power(value):
    """
    Returns the next-highest power-of-two for a value.
    """
    return int(2 ** math.ceil(math.log(value, 2)))

def bits(n):
    """
    Returns a generator of the bits in an int.
    
    From Duncan on StackOverflow.
    """
    while n:
        b = n & (~n+1)
        yield b
        n ^= b

def popcount(n):
    """
    Returns the number of high bits in an int, based on the kernighan
    algorithm. 80% faster than the idea I first came up with.
    """
    count = 0
    while n:
        n &= n - 1
        count += 1
    return count

def angle(start, end):
    """
    Calculates an angle in radians. Arguments in are (x, y) tuples.
    """
    return math.atan2(*vector(start, end))

def aim(angle):
    """
    Calculates the x and y motion for an angle. Argument is in radians.
    """ 
    return math.sin(angle), math.cos(angle)

def distance(start, end):
    """
    Returns the distance between coordinates. Arguments are in (x, y) tuples.
    """
    return math.hypot(*vector(start, end))

def vector(start, end):
    """
    Returns the vector between coordinates. Arguments are in (x, y) tuples.
    """
    return end[0] - start[0], end[1] - start[1]

def vector_unit(start, end):
    """
    Generates the vector unit of a given vector.
    """
    hypot = distance(start, end)
    vect = vector(start, end)
    try:
        return vect[0] / hypot, vect[1] / hypot
    except ZeroDivisionError:
        return (0, 0)

def clamp(target, minimum, maximum):
    """
    Locks the given value between a minimum and maximum.
    """
    return min(max(target, minimum), maximum)

def sign(target):
    """
    Returns the positive or negative sign of a value, or 0 if it isn't a value.
    """
    return target // abs(target) if target else target

def closest(a, middle, b):
    """
    Determines which of two values is closest in the given range. 
    """
    minimum = min(a, b)
    maximum = max(a, b)
    return maximum if abs(maximum - middle) < abs(middle - minimum) else minimum

tau = math.pi * 2 # stored float for angle calculation
angle_64 = tau / 64 # stored float for bullet centering calculation
angle_32 = tau / 32 # stored float for bullet angling calculation
