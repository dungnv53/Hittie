#!/usr/bin/env python2

"""
Sky Eraser (For real this time!)
By Prerisoft

Execution script
File creation date: Jan 07, 2013

THIS FILE IS LICENSED UNDER THE 3-CLAUSE BSD LICENSE. PLEASE SEE LICENSE(.txt)
FOR DETAILS.
"""

import sys
import constants, data, video, audio, control, scene

memorial = "In memory of Elizabeth McClung, who fought to live life against \
the overwhelming inevitability of terminal illness."

def bootstrap():
    """
    Loads the title menu in a manner that keeps the scene data from being
    persistent in memory.
    """
    import title
    scene.load_scene(title.TitleScene, block=True)

def main():
    """
    Main routine and loop. Let's go!
    """
    video.create_window() # Make our game window
    control.init() # Set blocked events, hide the cursor
    control.set_exit_callback(sys.exit) # Set a basic exit callback
    bootstrap() # Load our scene
    while 1:
        control.pump() # Update control states
        scene.update() # Update active scene state
        video.render_all() # Put it all onscreen!

if __name__ == "__main__":
    main()
