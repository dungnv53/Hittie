"""
Sky Eraser (For real this time!)
By Prerisoft

Scene Control
File creation date: Jan 14, 2013

THIS FILE IS LICENSED UNDER THE 3-CLAUSE BSD LICENSE. PLEASE SEE LICENSE(.txt)
FOR DETAILS.
"""

from __future__ import print_function

import sys, pygame, threading, gc
import data, video, audio

from constants import (VIDEO_BASE_FILE, VIDEO_LOADING_POSITION,
                       
                       LAYER_OVERLAY_2, LERP_REPEAT, PARALLAX_DEFAULT)

class SceneSystem(object):
    def __init__(self):
        self.scene_loader = None
        self.active_scene = BaseScene()
        self.target_scene = None
        self.target_arguments = ([], {})
        self.loader_sprite = None

class SceneLoader(threading.Thread):
    """
    Scene loader thread. Provides I/O operations so the main thread can do
    something else, even if it's just looking pretty and servicing interrupts.
    """
    def __init__(self, gfx, sfx, bgm, callback=None):
        threading.Thread.__init__(self, name="LoaderThread")
        self.gfx = gfx
        self.sfx = sfx
        self.bgm = bgm
        self.callback = callback
        self.daemon = True
    def run(self):
        """
        Thread execution method. Loads the associated resources for the scene.
        """
        try:
            for graphic in self.gfx:
                video.load_texture(graphic)
            for sound in self.sfx:
                audio.load_sample(sound)
            for music in self.bgm:
                audio.load_sample(music, True)
        finally:
            if self.callback: self.callback()
            if scene_system.loader_sprite:
                video.remove_renderer(scene_system.loader_sprite.render)
                scene_system.loader_sprite = None
            scene_system.scene_loader = None
        

class BaseScene(object):
    """
    Underlying scene code. Mostly there so as not to cause conflict with code
    that makes any assumptions.
    """
    gfx_resources = []
    sfx_resources = []
    bgm_resources = []
    def update(self):
        pass
    def close(self):
        pass

def load_scene(scene, block=False, *args, **kwargs):
    """
    Changes scenes, including any arguments that are to be passed to the new
    scene object - if block is True, it will perform the full loading sequence
    in the main thread, thus preventing the mainloop from executing until it is
    complete.
    """
    global update
    load_resources(scene.gfx_resources, scene.sfx_resources,
                   scene.bgm_resources, block=block)
    if not block:
        update = waiter_update
    scene_system.target_scene = scene
    scene_system.target_arguments = (args, kwargs)
    if block:
        run_scene()

def run_scene():
    """
    An internally-applied function that runs the finalisation for sceneloading,
    whether from the waiter or the load function itself depending on whether it
    was blocking or not.
    """
    global update
    target_scene = scene_system.target_scene
    target_arguments = scene_system.target_arguments
    video.unload_cache(keep=target_scene.gfx_resources)
    audio.unload_cache(keep=target_scene.sfx_resources
                            + target_scene.bgm_resources)
    del scene_system.target_scene
    del scene_system.target_arguments
    scene_system.active_scene.close()
#     print("DEBUG >> Orphaned renderers:", file=sys.stderr)
#     for i in video.video_system.renderers: print(i, file=sys.stderr)
    video.set_time(0)
    video.set_parallax(PARALLAX_DEFAULT)
    scene_system.active_scene = target_scene(*target_arguments[0],
                                             **target_arguments[1])
    update = scene_system.active_scene.update
    gc.collect()

def load_resources(gfx, sfx, bgm, callback=None, block=False):
    """
    Starts a loader thread that loads and prepares resources from the lists of
    graphical and audio filenames. On completion, an optional callback is
    executed. If block is true, the loader will run in the same thread instead
    of its own.
    """
    if scene_system.scene_loader: # Does a loader already exist?
        raise RuntimeError("loader thread already running")
    scene_system.scene_loader = SceneLoader(gfx, sfx, bgm, callback)
    if block:
        scene_system.scene_loader.run()
    else:
        scene_system.loader_sprite = video.Sprite(VIDEO_BASE_FILE,
            block=video.Lerp(2, 5, time=60, extend=LERP_REPEAT),
            location=VIDEO_LOADING_POSITION)
        video.add_renderer(scene_system.loader_sprite.render, LAYER_OVERLAY_2)
        scene_system.scene_loader.start()

def waiter_update():
    """
    An internally-applied function that stalls the main thread during scene
    loads while the loader thread operates.
    """
    if not scene_system.scene_loader:
        run_scene()

scene_system = SceneSystem()
update = lambda: None
