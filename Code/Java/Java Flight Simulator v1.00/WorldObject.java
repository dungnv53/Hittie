import java.awt.Color;

import javax.media.j3d.Shape3D;

/**
 * This abstract class extends Shape3D, and represents a three-dimensional
 * scenery object.
 * 
 * @author James Attenborough
 */
public abstract class WorldObject extends Shape3D
{
    /**
     * The breadth of this WorldObject.
     */
    private final int BREADTH;
    /**
     * The length of this WorldObject.
     */
    private final int LENGTH;
    /**
     * The height of this WorldObject.
     */
    private final double HEIGHT;

    /**
     * The default breadth of a WorldObject.
     */
    public static final int DEFAULT_BREADTH = 256;
    /**
     * The default length of a WorldObject.
     */
    public static final int DEFAULT_LENGTH = 256;
    /**
     * The default height of a WorldObject.
     */
    public static final int DEFAULT_HEIGHT = 0;

    /**
     * This is a two-dimensional array which holds the height of every point
     * (x,z) that lies upon this object.
     */
    private double[][] heights;
    /**
     * This is a two-dimensional array of Color objects, which holds the colour
     * of each point (x,z) that lies upon this object.
     */
    private Color[][] colours;

    /**
     * The WorldObject default constructor. It sets the length, breadth and
     * height to the default values.
     */
    public WorldObject()
    {
        super(); // WorldObject extends Shape3D, therefore we must call the
        // superclass's constructor.

        // In this default constructor, the breadth, length and height are set
        // to the default values:
        BREADTH = DEFAULT_BREADTH;
        LENGTH = DEFAULT_LENGTH;
        HEIGHT = DEFAULT_HEIGHT;

        // By default, the two-dimensional arrays which hold the colour map and
        // the height map are initialized to hold the number of points necessary
        // to construct this WorldObject:
        colours = new Color[getBreadth()][getLength()];
        heights = new double[getBreadth()][getLength()];
    }

    /**
     * The WorldObject parameterized constructor. It sets the length, breadth
     * and height to the values passed to it.
     * 
     * @param breadth
     *            the breadth of the WorldObject.
     * @param length
     *            the length of the WorldObject.
     * @param height
     *            the height of the WorldObject.
     * @param coloursIn
     *            the colour map for this WorldObject
     * @param heightsIn
     *            the height map for this WorldObject
     */
    public WorldObject(int breadth, int length, double height, Color[][] coloursIn, double heightsIn[][])
    {
        super();
        HEIGHT = height;

        if ((breadth > 0) && (length > 0))
        {
            BREADTH = breadth;
            LENGTH = length;
        }
        else
        {
            BREADTH = DEFAULT_BREADTH;
            LENGTH = DEFAULT_LENGTH;
        }

        colours = coloursIn;
        heights = heightsIn;
    }

    /**
     * Set the height at a particular point that lies on this object.
     * 
     * @param x
     *            The x-value of the point.
     * @param z
     *            The z-value of the point.
     * @param heightIn
     *            The height to set this point to.
     * @return true if this operation was completed successfully, false
     *         otherwise.
     */
    public boolean setHeight(int x, int z, double heightIn)
    {
        if (((x >= 0) && (x <= getBreadth())) && ((z >= 0) && (z <= getLength())))
        {
            heights[x][z] = heightIn;
            return true;
        }
        return false;
    }

    /**
     * Returns the height at a particular point that lies on this object.
     * 
     * @param x
     *            The x-value of the point.
     * @param z
     *            The z-value of the point.
     * @return The height at this point, or 0 if the point does not lie on this
     *         object.
     */
    public double getHeight(int x, int z)
    {
        if (((x >= 0) && (x <= getBreadth())) && ((z >= 0) && (z <= getLength())))
            return heights[x][z];
        return 0;
    }

    /**
     * Set the colour at a particular point that lies on this object.
     * 
     * @param x
     *            The x-value of the point.
     * @param z
     *            The z-value of the point.
     * @param colorIn
     *            The Color to set this point to.
     * @return true if this operation was completed successfully, false
     *         otherwise.
     */
    public boolean setColour(int x, int z, Color colorIn)
    {
        if (((x >= 0) && (x <= getBreadth())) && ((z >= 0) && (z <= getLength())))
        {
            colours[x][z] = colorIn;
            return true;
        }
        return false;
    }

    /**
     * Returns the Color at a particular point that lies on this object.
     * 
     * @param x
     *            The x-value of the point.
     * @param z
     *            The z-value of the point.
     * @return The Color at this point, or Color.BLACK if the point does not lie
     *         on this object.
     */
    public Color getColour(int x, int z)
    {
        if (((x >= 0) && (x <= getBreadth())) && ((z >= 0) && (z <= getLength())))
            return colours[x][z];
        return Color.BLACK;
    }

    /**
     * This abstract method must be defined in subclasses, and is used to
     * generate the geometry for that three-dimensional scenery object.
     */
    protected abstract void generateGeometry();

    /**
     * This abstract method must be defined in subclasses, and is used to
     * generate the appearance for that three-dimensional scenery object.
     */
    protected abstract void generateAppearance();

    /**
     * Returns the length of this WorldObject.
     * 
     * @return LENGTH the length of this WorldObject.
     */
    public int getLength()
    {
        return LENGTH;
    }

    /**
     * Returns the breadth of this WorldObject.
     * 
     * @return BREADTH the breadth of this WorldObject.
     */
    public int getBreadth()
    {
        return BREADTH;
    }

    /**
     * Returns the height of this WorldObject.
     * 
     * @return HEIGHT the height of this WorldObject.
     */
    public double getHeight()
    {
        return HEIGHT;
    }

    /**
     * Returns the String representation of this WorldObject.
     * 
     * @return the attributes of this WorldObject.
     */
    public String toString()
    {
        return "Breadth: " + getBreadth() + " Length: " + getLength() + " Height: " + getHeight();
    }

}