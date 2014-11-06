"""
Sky Eraser (For real this time!)
By Prerisoft

Event Handling
File creation date: Jan 23, 2013

THIS FILE IS LICENSED UNDER THE 3-CLAUSE BSD LICENSE. PLEASE SEE LICENSE(.txt)
FOR DETAILS.
"""

import sys, heapq
import video

class StaticQueue(object):
    """
    An event queue that runs from a static event list.
    """
    def __init__(self, queue=None, start_time=None):
        self.event_queue = queue if queue else []
        # Creates the event queue. If none is provided, it initialises with a
        # basic default
        self.start_time = start_time if start_time else video.get_time() 
        self.event_position = 0
    def pump(self, *arg, **kwarg):
        """
        Pumps all events that are applicable to be called."
        """
        # TODO: Reconfigure to use a number provided in pump method arguments
        while (self.event_position < len(self.event_queue) and
               self.event_queue[self.event_position][0] <= (video.get_time()
                                                            - self.start_time)):
            self.event_queue[self.event_position][1](*arg, **kwarg)
            self.event_position += 1
    def set_queue(self, *queue):
        """
        Loads a sequence of events into the queue for processing.
        """
        self.event_queue = queue
        self.event_position = 0

class DynamicQueue(object):
    """
    An event queue that relies on dynamically-provided events.
    """
    def __init__(self):
        self.event_queue = []
    def pump(self, *arg, **kwarg):
        """
        Pumps all events that are applicable to be called.
        """
        # TODO: Reconfigure to use a number provided in pump method arguments
        while self.event_queue and self.event_queue[0][0] <= video.get_time():
            heapq.heappop(self.event_queue)[1](*arg, **kwarg)
    def add_event(self, timepoint, command):
        """
        Adds an event to the queue.
        """
        heapq.heappush(self.event_queue, (timepoint, command))
    def clear(self, flush=False):
        """
        Blanks the queue.
        """
        # TODO: Allow flushing after pump method is modified
        self.event_queue = []

#### BETA RELEASE CODE - FOR SKY ERASER DEVELOPERS AND TESTERS ONLY ####
