import javax.media.j3d.Shape3D;
import javax.swing.JPanel;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;

import com.sun.j3d.utils.image.TextureLoader;

/**
 * The Plane is an extension of the GameObject class, and reperesents a player
 * in the game.
 * 
 * @author James Attenborough
 */
public class Plane extends GameObject
{
    private double roll; // The roll value of this Plane.
    private double pitch; // The pitch value of this Plane.

    private int health; // The health of this Plane.

    private int throttle; // The throttle of this Plane.
    private int bullets; // The number of bullets.

    private int level; // This Plane's strength, and if it is an AI Plane, then
    // it's skill level too.

    private String textureFilename; // This is the filename of the Texture to be
    // used.

    private Matrix3d noseRotation; // The matrix that holds the current nose
    // rotation of this Plane.

    private boolean ai; // Whether this Plane is Artificial Intelligence or not.

    private final double MIN_PITCH = -0.9; // The minimum pitch allowed.
    private final double MAX_PITCH = 0.9; // The maximum pitch allowed.
    private final double MIN_ROLL = -1.8; // The minimum roll allowed.
    private final double MAX_ROLL = 1.8; // The maximum roll allowed.

    // The amount by which to increment the pitch:
    private final double PITCH_INCREMENT = 0.1;

    // The amount by which to increment the roll:
    private final double ROLL_INCREMENT = 0.2;

    /**
     * The pitch constant for this Plane. The greater the pitch constant, the
     * faster this Plane will pitch up or down.
     */
    public final double PITCH_CONSTANT;

    /**
     * The roll constant for this Plane. The greater the roll constant, the
     * faster this Plane will roll left or right.
     */
    public final double ROLL_CONSTANT;

    /**
     * The speed constant for this Plane. The smaller the speed constant, the
     * faster this Plane will fly.
     */
    public final double SPEED_CONSTANT;

    /**
     * The minimum allowable level for player strength and AI skill.
     */
    public static final int MIN_LEVEL = 1;

    /**
     * The maximum allowable level for player strength and AI skill.
     */
    public static final int MAX_LEVEL = 9;

    // The default player strength / AI skill level:
    private static final int DEFAULT_LEVEL = 6;

    // The number of bullets that a Plane starts with:
    private final static int START_BULLETS = 2500;
    // The starting throttle value for every Plane:
    private final static int START_THROTTLE = 100;
    // The starting health value for every Plane:
    private final static int START_HEALTH = 100;

    /**
     * The human controlled Plane and AI Plane constructor.
     * 
     * @param aiIn
     *            Whether or not this plane is AI controlled.
     * @param nameIn
     *            The name of this Plane.
     * @param modelIn
     *            The filename of the model of this Plane.
     * @param textureFilenameIn
     *            The filename of the Texture for this Plane.
     * @param startIn
     *            The starting location for this Plane.
     * @param pitchIn
     *            The pitch constant for this Plane.
     * @param rollIn
     *            The roll constant for this Plane.
     * @param speedIn
     *            The speed constant for this Plane.
     * @param rotationIn
     *            The initial nose rotation matrix for this Plane.
     * @param levelIn
     *            The AI skill level and/or player strength.
     */
    public Plane(boolean aiIn, String nameIn, String modelIn, String textureFilenameIn, Point3d startIn, double pitchIn, double rollIn,
            double speedIn, Matrix3d rotationIn, int levelIn)
    {
        super(nameIn, modelIn, new TextureLoader(textureFilenameIn, new JPanel()).getTexture(), startIn, true);

        setTextureFilename(textureFilenameIn); // sets the Texture filename.

        // After calling the GameObject constructor using the passed values, the
        // flight constants are set to their passed values:
        PITCH_CONSTANT = pitchIn;
        ROLL_CONSTANT = rollIn;
        SPEED_CONSTANT = speedIn;

        setAI(aiIn); // Whether this plane is AI or human controlled.
        setLevel(levelIn); // This refers to the player's strength.
        setNoseRotation(rotationIn);

        // Set the attributes to their starting values:
        setNumBullets(START_BULLETS);
        setThrottle(START_THROTTLE);
        setHealth(START_HEALTH);
        setRoll(0); // Level the roll.
        setPitch(0); // Level the pitch.

        // Set the necessary capabilities for this object:
        setCapability(Shape3D.ALLOW_COLLIDABLE_READ);
        setCapability(Shape3D.ALLOW_COLLIDABLE_WRITE);
        setCapability(Shape3D.ENABLE_COLLISION_REPORTING);
        setCapability(Shape3D.ALLOW_GEOMETRY_READ);
        setCollidable(true);
    }

    /**
     * Sets this planes strength / AI level.
     * 
     * @param levelIn
     *            This Plane's strength / AI level.
     * @return true if the level was set successfully, otherwise false is
     *         returned and the level is set to the default value.
     */
    private boolean setLevel(int levelIn)
    {
        if ((levelIn >= MIN_LEVEL) && (levelIn <= MAX_LEVEL))
        {
            level = levelIn;
            return true;
        }

        level = DEFAULT_LEVEL; // As a precaution.
        return false;
    }

    /**
     * Returns this Plane's strength / AI level.
     * 
     * @return This Plane's strength / AI level.
     */
    public int getLevel()
    {
        return level;
    }

    /**
     * Sets whether this Plane is AI or human controlled.
     * 
     * @param aiIn
     *            Whether his Plane is controlled by AI or not.
     */
    private void setAI(boolean aiIn)
    {
        ai = aiIn;
    }

    /**
     * Checks whether this Plane is AI or not.
     * 
     * @return true if this Plane is AI, otherwise false.
     */
    public boolean isAI()
    {
        return ai;
    }

    /**
     * Sets this Plane's nose rotation matrix.
     * 
     * @param rotationIn
     *            The nose rotation matrix to set this Plane's nose rotation to.
     */
    public void setNoseRotation(Matrix3d rotationIn)
    {
        noseRotation = rotationIn;
    }

    /**
     * Returns this Plane's nose roation matrix.
     * 
     * @return This Plane's nose rotation matrix.
     */
    public Matrix3d getNoseRotation()
    {
        return noseRotation;
    }

    /**
     * Checks whether this Plane is alive.
     * 
     * @return true if this Plane is alive, otherwise false.
     */
    public boolean isAlive()
    {
        if (health > 0)
            return true;
        return false;
    }

    /**
     * Sets this Plane's roll amount.
     * 
     * @param rollIn
     *            The roll to be set.
     * @return true if the roll was successfully set, otherwise false.
     */
    public boolean setRoll(double rollIn)
    {
        if ((rollIn >= MIN_ROLL) && (rollIn <= MAX_ROLL))
        {
            roll = rollIn;
            return true;
        }
        return false;
    }

    /**
     * Returns this Plane's roll amount.
     * 
     * @return The amount of roll.
     */
    public double getRoll()
    {
        return roll;
    }

    /**
     * Sets this Plane's pitch amount.
     * 
     * @param pitchIn
     *            The pitch to be set.
     * @return true if the pitch was successfully set, otherwise false.
     */
    public boolean setPitch(double pitchIn)
    {
        if ((pitchIn >= MIN_PITCH) && (pitchIn <= MAX_PITCH))
        {
            pitch = pitchIn;
            return true;
        }
        return false;
    }

    /**
     * Returns this Plane's pitch amount.
     * 
     * @return The amount of pitch.
     */
    public double getPitch()
    {
        return pitch;
    }

    /**
     * Pitches the Plane down.
     */
    public void pitchDown()
    {
        if (pitch >= MIN_PITCH)
            pitch -= PITCH_INCREMENT;
    }

    /**
     * Pitches the Plane up.
     */
    public void pitchUp()
    {
        if (pitch <= MAX_PITCH)
            pitch += PITCH_INCREMENT;
    }

    /**
     * Rolls the Plane to the right.
     */
    public void rollRight()
    {
        if (roll >= MIN_ROLL)
            roll -= ROLL_INCREMENT;
    }

    /**
     * Rolls the Plane to the left.
     */
    public void rollLeft()
    {
        if (roll <= MAX_ROLL)
            roll += ROLL_INCREMENT;
    }

    /**
     * Sets this Plane's throttle amount.
     * 
     * @param throttleIn
     *            The throttle to be set.
     * @return true if the throttle was set successfully, otherwise false.
     */
    public boolean setThrottle(int throttleIn)
    {
        if ((throttle >= 0) && (throttleIn <= 100))
        {
            throttle = throttleIn;
            return true;
        }
        return false;
    }

    /**
     * Increases this Plane's throttle.
     */
    public void throttleUp()
    {
        if (throttle < 100)
            throttle++;
    }

    /**
     * Decreases this Plane's throttle.
     */
    public void throttleDown()
    {
        if (throttle > 0)
            throttle--;
    }

    /**
     * Returns the amount of throttle of this Plane.
     * 
     * @return This Plane's throttle amount.
     */
    public int getThrottle()
    {
        return throttle;
    }

    /**
     * Sets the number of bullets of this Plane.
     * 
     * @param numIn
     *            the number of bullets of this Plane.
     * @return true if the number of bullets was set successfully, otherwise
     *         returns false and sets the number of bullets to default start
     *         amount.
     */
    public boolean setNumBullets(int numIn)
    {
        if (numIn >= 0)
        {
            bullets = numIn;
            return true;
        }
        bullets = START_BULLETS; // As a precaution.
        return false;
    }

    /**
     * Returns the number of bullets of this Plane.
     * 
     * @return The number of bullets.
     */
    public int getNumBullets()
    {
        return bullets;
    }

    /**
     * "Fires" a bullets.
     * 
     * @return true is a bullet could be fired, otherwise false.
     */
    public boolean fireBullet()
    {
        // The actual shooting is handled in the FlightGamePanel class. This
        // just
        // makes it reflect upon the Plane's number of bullets, and controls
        // whether the Plane can shoot (has a bullet left).

        if (getNumBullets() > 0)
        {
            bullets--;
            return true;
        }
        return false;
    }

    /**
     * Sets this Plane's health value.
     * 
     * @param healthIn
     *            The health to set.
     * @return true if the health value was set successfully, otherwise false.
     */
    public boolean setHealth(int healthIn)
    {
        if ((healthIn >= 0) && (healthIn <= 100))
        {
            health = healthIn;
            return true;
        }
        health = 0;
        return false;
    }

    /**
     * Returns this Plane's health value.
     * 
     * @return The health of the Plane.
     */
    public int getHealth()
    {
        return health;
    }

    /**
     * Returns the update String for this Plane, which is used to send and
     * receive updates from clients in a network game.
     * 
     * @return A String that contains all the vital statistics of this Plane.
     */
    public String getUpdateString()
    {
        return getName() + GameProtocol.PARTS_SEPARATOR + getHealth() + GameProtocol.PARTS_SEPARATOR + getLocationX()
                + GameProtocol.FINER_PARTS_SEPARATOR + getLocationY() + GameProtocol.FINER_PARTS_SEPARATOR + getLocationZ()
                + GameProtocol.PARTS_SEPARATOR + noseRotation.m00 + GameProtocol.FINER_PARTS_SEPARATOR + noseRotation.m01
                + GameProtocol.FINER_PARTS_SEPARATOR + noseRotation.m02 + GameProtocol.FINER_PARTS_SEPARATOR + noseRotation.m10
                + GameProtocol.FINER_PARTS_SEPARATOR + noseRotation.m11 + GameProtocol.FINER_PARTS_SEPARATOR + noseRotation.m12
                + GameProtocol.FINER_PARTS_SEPARATOR + noseRotation.m20 + GameProtocol.FINER_PARTS_SEPARATOR + noseRotation.m21
                + GameProtocol.FINER_PARTS_SEPARATOR + noseRotation.m22;

    }

    /**
     * Returns the start String for this Plane, which is used to send and
     * receive messages that specify that a client has joined a network game.
     * 
     * @return A String that contains all the vital starting statistics of this
     *         Plane.
     */
    public String getStartString()
    {
        return getName() + GameProtocol.PARTS_SEPARATOR + getModel() + GameProtocol.PARTS_SEPARATOR + getTextureFilename()
                + GameProtocol.PARTS_SEPARATOR + PITCH_CONSTANT + GameProtocol.PARTS_SEPARATOR + ROLL_CONSTANT
                + GameProtocol.PARTS_SEPARATOR + SPEED_CONSTANT + GameProtocol.PARTS_SEPARATOR + getLocationX()
                + GameProtocol.FINER_PARTS_SEPARATOR + getLocationY() + GameProtocol.FINER_PARTS_SEPARATOR + getLocationZ()
                + GameProtocol.PARTS_SEPARATOR + noseRotation.m00 + GameProtocol.FINER_PARTS_SEPARATOR + noseRotation.m01
                + GameProtocol.FINER_PARTS_SEPARATOR + noseRotation.m02 + GameProtocol.FINER_PARTS_SEPARATOR + noseRotation.m10
                + GameProtocol.FINER_PARTS_SEPARATOR + noseRotation.m11 + GameProtocol.FINER_PARTS_SEPARATOR + noseRotation.m12
                + GameProtocol.FINER_PARTS_SEPARATOR + noseRotation.m20 + GameProtocol.FINER_PARTS_SEPARATOR + noseRotation.m21
                + GameProtocol.FINER_PARTS_SEPARATOR + noseRotation.m22 + GameProtocol.PARTS_SEPARATOR + getLevel();
    }

    /**
     * Sets the Texture's filename to the value passed.
     * 
     * @param filenameIn
     *            the Texture filename to set.
     */
    private void setTextureFilename(String filenameIn)
    {
        textureFilename = filenameIn;
    }

    /**
     * Returns the filename of the Texture applied to this Plane's 3D model.
     * 
     * @return The Texture's filename for this Plane.
     */
    public String getTextureFilename()
    {
        return textureFilename;
    }

    /**
     * Returns a String representation of the attributes of this Plane.
     * 
     * @return A String representation of this Plane.
     */
    public String toString()
    {
        return super.toString() + " Health: " + getHealth() + " Texture: " + getTextureFilename() + " Roll: " + getRoll() + " Pitch: "
                + getPitch() + " Throttle: " + getThrottle() + " Bullets: " + getNumBullets();
    }
}