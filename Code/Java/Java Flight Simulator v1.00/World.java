import java.awt.Color;
import java.awt.Image;
import java.util.Vector;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ExponentialFog;
import javax.media.j3d.Texture;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * This class extends the BranchGroup class, and represents a three-dimensional
 * world to which objects can be added. It should be used like this: 1. Create a
 * new World object using the necessary flags and dimensions. 2. Set any
 * optional variables that you might need for the flags you used. 3. Call
 * generateWorld(), and check the boolean result of this to see if the world was
 * created successfully.
 * 
 * @author James Attenborough
 */
public class World extends BranchGroup
{
    /**
     * The flag which signals that this World has terrain.
     */
    public static final int TERRAIN = (1 << 0);

    /**
     * The flag which signals that this World has still water.
     */
    public static final int WATER_STILL = (1 << 1);

    /**
     * The flag which signals that this World has animated water.
     */
    public static final int WATER_ANIMATED = (1 << 2);

    /**
     * The flag which signals that this World has a starless sky.
     */
    public static final int SKY = (1 << 3);

    /**
     * The flag which signals that this World has a sky with stars.
     */
    public static final int SKY_STARS = (1 << 4);

    /**
     * The flag which signals that this World has fog.
     */
    public static final int FOG = (1 << 5);

    private final int FLAGS;
    // The FLAGS int stores which of the above flags are set for this World. The
    // flags are all set to the value of a different bit, by shifting to the
    // left:
    // e.g:
    // (1 << 0) = 2^0 = 00000001
    // (1 << 1) = 2^1 = 00000010 (moved 1 bit to the left)
    // (1 << 2) = 2^2 = 00000100 (moved 2 bits to the left), etc.
    // This makes it easy to check whether a certain flag is set, as you can
    // just check to see if the bit for that flag is set.

    private boolean worldGenerated = false; // Used to make sure that the
    // generateWorld method is only run
    // successfully once.

    private int length; // The length, before scaling, of this World.
    private int breadth;// The breadth, before scaling, of this World.
    private double minHeight; // The minimum height, before scaling, of this
    // World.
    private double maxHeight;// The maximum height, before scaling, of this
    // World.

    private final int SCALE; // The amount by which to scale this World.

    private Terrain terrain; // The Terrain object for this World (stored so
    // that the height at a point may be retrieved later, without having to
    // regenerate the Terrain).
    private boolean terrainGenerated = false; // Used to keep track of whether
    // the Terrain has been generated
    // or not.

    //------------------------------------------
    // These variables are optional ones for this class, and need to be set
    // if a certain flag is set that requires their use:
    private Image terrainImage; // The image that is used to generate the
    // terrain (must be set if TERRAIN flag is set).
    private double waterLevel; // The water level, before scaling, of this World
    // (must be set if WATER_STILL or WATER_ANIMATED flags are set).
    private Texture skyTexture; // The Texture of the sky for this World (must
    // be set if SKY flag is set).
    // -----------------------------------------
    // These booleans keep track of which of the above have been set:
    private boolean terrainImageSet = false;
    private boolean skyTextureSet = false;
    private boolean waterLevelSet = false;

    // A Vector which holds all the Fire objects added to the World:
    private Vector fires = new Vector();

    /**
     * The World constructor. Initializes a World object using the dimensions
     * passed to it, and also the flags passed to it, which indicate which
     * scenery elements will be added when generateWorld() is called.
     * 
     * @param flagsIn
     *            The flags which specify the scenery elements of this World.
     * @param scaleIn
     *            The amount by which to scale this World.
     * @param lengthIn
     *            The length, before scaling, of this World.
     * @param breadthIn
     *            The breadth, before scaling, of this World.
     * @param minHeightIn
     *            The minimum height, before scaling, of this World.
     * @param maxHeightIn
     *            The maximum height, before scaling, of this World.
     */
    public World(int flagsIn, int scaleIn, int lengthIn, int breadthIn, double minHeightIn, double maxHeightIn)
    {
        super();
        FLAGS = flagsIn; // Store the set flags.
        SCALE = scaleIn; // Set the scale.

        //Set the dimension attributes of this World:
        setLength(lengthIn);
        setBreadth(breadthIn);
        setMinimumHeight(minHeightIn);
        setMaximumHeight(maxHeightIn);

        //Set the necessary capabilities of this World:
        setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        setCapability(BranchGroup.ALLOW_DETACH);
    }

    /**
     * @return The amount by which this World is scaled.
     */
    public int getScale()
    {
        return SCALE;
    }

    /**
     * @return The flags that define the scenery elements of this World.
     */
    public int getFlags()
    {
        return FLAGS;
    }

    /**
     * Used to generate the World based on the flags that have been set. This
     * method will only successfully initialize a World once.
     * 
     * @return true if the World was generated successfully, otherwise false.
     */
    public boolean generateWorld()
    {
        boolean worked = true; // Keeps track of whether this method completes
        // successfully.

        if (!worldGenerated) // If the World has not already been generated:
        {
            // Check to see if the TERRAIN bit is set in FLAGS:
            if ((getFlags() & TERRAIN) != 0)
            {
                // If the terrain image has been set:
                if (terrainImageSet)
                {
                    // Generate and add the Terrain:
                    addChild(generateTerrain());
                }
                else
                {
                    // Or this method will return false:
                    worked = false;
                }
            }

            // Check to see if the WATER_ANIMATED bit is set in FLAGS:
            if ((getFlags() & WATER_ANIMATED) != 0)
            {
                // If the water level has been set:
                if (waterLevelSet)
                {
                    // Generate and add the AnimatedWater:
                    addChild(generateAnimatedWater());
                }
                else
                {
                    // Or this method will return false:
                    worked = false;
                }
            }

            // Check to see if the WATER_STILL bit is set in FLAGS:
            if ((getFlags() & WATER_STILL) != 0)
            {
                // If the water level has been set:
                if (waterLevelSet)
                {
                    // Generate and add the StillWater:
                    addChild(generateStillWater());
                }
                else
                {
                    // Or this method will return false:
                    worked = false;
                }
            }

            // Check to see if the SKY bit is set in FLAGS:
            if ((getFlags() & SKY) != 0)
            {
                if (skyTextureSet)
                {
                    // Generate and add the Sky:
                    addChild(generateSky());
                }
                else
                {
                    // Or this method will return false:
                    worked = false;
                }
            }

            // Check to see if the SKY_STARS bit is set in FLAGS:
            if ((getFlags() & SKY_STARS) != 0)
            {
                // Add the SkyWithStars object:
                addChild(new SkyWithStars(2000, 1000 + getScale() * 200));
            }

            // Check to see if the FOG bit is set in FLAGS:
            if ((getFlags() & FOG) != 0)
            {
                // Construct the ExponentialFog object and add it to the World:

                ExponentialFog myFog = new ExponentialFog();
                myFog.setColor(new Color3f(0.35f, 0.35f, 0.4f));
                myFog.setDensity(0.015f);
                myFog.setInfluencingBounds(new BoundingSphere(new Point3d(), 2000.0));

                addChild(myFog);
            }
        }
        else
        {
            // If the World has already been generated, then return false:
            worked = false;
        }

        if (worked)
        {
            // If the World was generated successfully,
            // then stop it from being regenerated:
            worldGenerated = true;
        }

        // Return the success boolean of the method:
        return worked;
    }

    /**
     * Sets the length, before scaling, of this World.
     * 
     * @param lengthIn
     *            The length to set this World's length to.
     * @return true if the length was successfully set, otherwise false is
     *         returned and the length is set to the default WorldObject length
     *         just in case.
     */
    private boolean setLength(int lengthIn)
    {
        if (lengthIn > 0)
        {
            length = lengthIn;
            return true;
        }
        length = WorldObject.DEFAULT_LENGTH; // Just in case.
        return false;
    }

    /**
     * Returns the length, before scaling, of this World.
     * 
     * @return The length of this World.
     */
    public int getLength()
    {
        return length;
    }

    /**
     * Sets the breadth, before scaling, of this World.
     * 
     * @param breadthIn
     *            The breadth to set this World's breadth to.
     * @return true if the breadth was successfully set, otherwise false is
     *         returned and the breadth is set to the default WorldObject
     *         breadth just in case.
     */
    private boolean setBreadth(int breadthIn)
    {
        if (breadthIn > 0)
        {
            breadth = breadthIn;
            return true;
        }
        breadth = WorldObject.DEFAULT_BREADTH; // Just in case.
        return false;
    }

    /**
     * Returns the breadth, before scaling, of this World.
     * 
     * @return The breadth of this World.
     */
    public int getBreadth()
    {
        return breadth;
    }

    /**
     * Sets the minimum height, before scaling, of this World.
     * 
     * @param minHeightIn
     *            The minimum height to set this World's minimum height to.
     */
    private void setMinimumHeight(double minHeightIn)
    {
        minHeight = minHeightIn;
    }

    /**
     * Returns the minimum height, before scaling, of this World.
     * 
     * @return The minimum height of this World.
     */
    public double getMinimumHeight()
    {
        return minHeight;
    }

    /**
     * Sets the maximum height, before scaling, of this World.
     * 
     * @param maxHeightIn
     *            The maximum height to set this World's minimum height to.
     */
    private void setMaximumHeight(double maxHeightIn)
    {
        maxHeight = maxHeightIn;
    }

    /**
     * Returns the maximum height, before scaling, of this World.
     * 
     * @return The maximum height of this World.
     */
    public double getMaximumHeight()
    {
        return maxHeight;
    }

    /**
     * Sets the water level, before scaling, of this World.
     * 
     * @param waterLevelIn
     *            The water level to set this World's minimum height to.
     */
    public void setWaterLevel(double waterLevelIn)
    {
        if (!waterLevelSet)
        {
            waterLevel = waterLevelIn;
            // Signal that the water level has been set:
            waterLevelSet = true;
        }
    }

    /**
     * Returns the water level, before scaling, of this World.
     * 
     * @return The water level of this World.
     */
    public double getWaterLevel()
    {
        if (waterLevelSet)
            return waterLevel;
        return 0;
    }

    /**
     * Sets the Sky Texture of this World.
     * 
     * @param textureIn
     *            The Texture to apply to the Sky object.
     */
    public void setSkyTexture(Texture textureIn)
    {
        if (!skyTextureSet)
        {
            skyTexture = textureIn;
            // Signal that the Sky Texture has been set:
            skyTextureSet = true;
        }
    }

    /**
     * Returns the Sky Texture of this World.
     * 
     * @return The Sky Texture if it has been set, otherwise null;
     */
    public Texture getSkyTexture()
    {
        if (skyTextureSet)
            return skyTexture;
        return null;
    }

    /**
     * Sets the Terrain Image of this World.
     * 
     * @param terrainIn
     *            The Image used to generate the Terrain of this World.
     */
    public void setTerrainImage(Image terrainIn)
    {
        if (!terrainImageSet)
        {
            terrainImage = terrainIn;
            // Signal that the Terrain Image has been set:
            terrainImageSet = true;
        }
    }

    /**
     * Returns the Terrain Image of this World.
     * 
     * @return The Terrain Image if it has been set, otherwise null;
     */
    public Image getTerrainImage()
    {
        if (terrainImageSet)
            return terrainImage;
        return null;
    }

    /**
     * Returns the height of the Terrain at the point (xIn, zIn).
     * 
     * @param xIn
     *            The x-value of the point.
     * @param zIn
     *            The z-value of the point.
     * @return The height at this point if the Terrain has been generated,
     *         otherwise 0.
     */
    public double getTerrainHeightAtPoint(int xIn, int zIn)
    {
        // If the was generated:
        if (terrainGenerated)
            return terrain.getHeight(xIn, zIn);
        return 0;
    }

    /**
     * Generated the Terrain object, and then returns a TransformGroup which
     * contains the scaled Terrain.
     * 
     * @return The TransformGroup which contains the scaled Terrain which was
     *         generated.
     */
    private TransformGroup generateTerrain()
    {
        TransformGroup tg = new TransformGroup(); // Used to hold the scaled
        // Terrain.
        Transform3D scaler = new Transform3D(); // Used to scale the Terrain.
        scaler.setScale(getScale()); // Set the scale of the Transform3D to this
        // World's scale.
        tg.setTransform(scaler); // Set the scaling Transform as that of the
        // TransformGroup.

        // Initialize the Terrain:
        terrain = new Terrain(getTerrainImage(), getLength(), getBreadth(), getMinimumHeight(), getMaximumHeight());

        tg.addChild(terrain); // Add the Terrain to the TransformGroup.
        terrainGenerated = true; // Signal that the Terrain has been generated.

        return tg;
    }

    /**
     * Generates a Sky object and then returns a BranchGroup that the Sky has
     * been added to.
     * 
     * @return The BranchGroup which contains the Sky object.
     */
    private BranchGroup generateSky()
    {
        BranchGroup skyGroup = new BranchGroup(); // The BranchGroup that will
        // hold
        // the Sky.

        Appearance appearance = new Appearance();
        appearance.setTexture(getSkyTexture()); // Apply the Sky Texture to the
        // Appearance.

        // Generate the sky and add it to the BranchGroup:
        skyGroup.addChild(new Sky(appearance));

        return skyGroup; // Return the BranchGroup that holds the Sky.
    }

    /**
     * Generates an AnimatedWater object to the scaled dimensions of this World.
     * 
     * @return The AnimatedWater object that has been generated.
     */
    private AnimatedWater generateAnimatedWater()
    {
        return new AnimatedWater(new Color3f(new Color(100, 150, 232)), getLength() * SCALE, getBreadth() * SCALE, getWaterLevel() * SCALE,
                30);
    }

    /**
     * Generates a StillWater object to the scaled dimensions of this World.
     * 
     * @return The StillWater object that has been generated.
     */
    private StillWater generateStillWater()
    {
        // Generate the StillWater object using the RGB Color (100, 150, 232):
        return new StillWater(new Color(100, 150, 232), getLength() * SCALE, getBreadth() * SCALE, getWaterLevel() * SCALE);
    }

    /**
     * Adds a GroundObject to the World.
     * 
     * @param object
     *            The GroundObject to add to the World.
     */
    public void addGroundObject(GroundObject object)
    {
        TransformGroup tg = new TransformGroup(); // The TransformGroup that
        // holds the GroundObject to
        // be transformed.

        // These Transform3D objects are used to positon, scale and rotate the
        // GRoundObject:
        Transform3D positioner = new Transform3D();
        Transform3D scaler = new Transform3D();
        Transform3D rotator = new Transform3D();

        // Scale this Transform3D by the GroundObject's scale:
        scaler.setScale(object.getScale());
        // Apply a rotation about the y-axis for the GroundObject's rotation:
        rotator.rotY(Math.toRadians(object.getRotation()));
        // Translate this Transform3D by the GroundObject's location:
        positioner.set(new Vector3d(object.getLocation()));

        // Combine the 3 Transform3D's into one, so that this new matrix can be
        // used to transform the TransformGroup:
        rotator.mul(scaler);
        positioner.mul(rotator);
        tg.setTransform(positioner);

        tg.addChild(object); // Add the GroundObject to the transformed
        // TransformGroup.

        addChild(tg); // Add the TransformGroup to the World.
    }

    /**
     * Adds a Fire object at a certain location.
     * 
     * @param fireLocation
     *            The point to place this Fire at.
     */
    public void addFire(Point3d fireLocation)
    {
        boolean continueToAddFire = true;
        for (int f = 0; f < fires.size(); f++)
        {
            if (((Fire) fires.get(f)).getLocation() == fireLocation)
            {
                continueToAddFire = false;
            }
        }

        if (continueToAddFire)
        {
            // Add a new Fire to the Vector of Fire objects:
            fires.add(new Fire(fireLocation));

            // The BranchGroup to hold this Fire:
            BranchGroup bg = new BranchGroup();
            // The TransformGroup for this Fire.
            TransformGroup group = new TransformGroup();
            // The translation Transform3D for this Fire:
            Transform3D trans = new Transform3D();

            // Apply the this translation to the Fire's TransformGroup.
            trans.setTranslation(new Vector3d(((Fire) fires.get((fires.size() - 1))).getLocation()));
            group.setTransform(trans);
            group.addChild((Fire) (fires.get((fires.size() - 1))));
            bg.addChild(group);

            // Add the Fire's behaviour to the BranchGroup:
            bg.addChild(((Fire) (fires.get((fires.size() - 1)))).getBehaviour());

            addChild(bg); // Add the BranchGroup to this World object.
        }
    }

    /**
     * Sets whether the Fire objects should be animated or not.
     * 
     * @param animate
     *            Whether to animate the Fires in this World or not.
     */
    public void animateFires(boolean animate)
    {
        for (int i = 0; i < fires.size(); i++)
            ((Fire) fires.get(i)).setRunning(animate);
    }
}