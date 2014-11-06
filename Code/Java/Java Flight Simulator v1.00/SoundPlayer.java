import java.applet.Applet;
import java.applet.AudioClip;
import java.util.Vector;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

/**
 * The SoundPlayer is a class used to keep track of and control AudioClip
 * objects. It implements the LineListener interface, which receives events when
 * a line's status changes.
 * 
 * @author James Attenborough
 */
public class SoundPlayer implements LineListener
{
    private Vector clips; // This vector holds all the clips that can be played.
    private int counter; // To keep track of how many clips there are.

    /**
     * The SoundPlayer constructor. Initializes a default SoundPlayer.
     */
    public SoundPlayer()
    {
        clips = new Vector();
        counter = 0;
    }

    /**
     * Loads a sound into the SoundPlayer.
     * 
     * @param filename
     *            The filename of the sound to load.
     * @return An int which should be stored. This int can be used to play, loop
     *         or pause the loaded sound.
     */
    public int loadSound(String filename)
    {
        // A new clip is added to the Vector of clips:
        clips.add(counter, Applet.newAudioClip(getClass().getResource(filename)));
        counter++;
        // The position of this clip in the Vector is returned:
        return (counter - 1);
    }

    /**
     * Plays the specified sound.
     * 
     * @param numIn
     *            The index of the necessary sound.
     */
    public void playSound(int numIn)
    {
        // Plays the sound at the specified index:
        ((AudioClip) clips.get(numIn)).play();
    }

    /**
     * Loops the specified sound.
     * 
     * @param numIn
     *            The index of the necessary sound.
     */
    public void loopSound(int numIn)
    {
        // Loops the sound at the specified index:
        ((AudioClip) clips.get(numIn)).loop();
    }

    /**
     * Pauses the specified sound.
     * 
     * @param numIn
     *            The index of the necessary sound.
     */
    public void pauseSound(int numIn)
    {
        // Pauses the sound at the specified index:
        ((AudioClip) clips.get(numIn)).stop();
    }

    /**
     * Informs the listener that a line's state has changed. This method is
     * unnecessary, but must be included so that the LineListener interface may
     * be used.
     * 
     * @param l
     *            The LineEvent.
     */
    public void update(LineEvent l)
    {
    }
}