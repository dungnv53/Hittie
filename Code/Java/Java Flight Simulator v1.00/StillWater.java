import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

/**
 * This class represents a StillWater three-dimensional scenery object.
 * 
 * @author James Attenborough
 */
public class StillWater extends WorldObject
{
    /**
     * The Color of this StillWater object.
     */
    private final Color COLOUR;

    /**
     * This parameterized constructor is used to initialize a StillWater object
     * with a specific Color, breadth, length and height.
     * 
     * @param colour
     *            The Color of this StillWater object.
     * @param breadth
     *            The breadth of this StillWater object.
     * @param length
     *            The length of this StillWater object.
     * @param height
     *            The height of this StillWater object.
     */
    public StillWater(Color colour, int breadth, int length, double height)
    {
        super(breadth, length, height, new Color[2][2], new double[2][2]);

        COLOUR = colour;

        setHeightsAndColours();

        generateGeometry();
        generateAppearance();

        setCapability(Shape3D.ALLOW_COLLIDABLE_READ | Shape3D.ALLOW_COLLIDABLE_WRITE);
        setCollidable(false);
    }

    /**
     * Returns the Color of this object.
     * 
     * @return COLOUR The Color associated with this object.
     */
    public Color getColour()
    {
        return COLOUR;
    }

    /**
     * This method is used to set the heights and colours of each point that
     * makes up this StillWater object.
     */
    private void setHeightsAndColours()
    {
        for (int x = 0; x < 2; x++)
        {
            for (int z = 0; z < 2; z++)
            {
                setHeight(x, z, getHeight());
                setColour(x, z, getColour());
            }
        }
    }

    /**
     * This method generates the geometry of this StillWater object so that it
     * can be viewed three-dimensionally. It uses a QuadArray to hold the
     * coordinate and colour of each point of the water's geometry. This method
     * creates four vertices: the four corners of this rectangular object.
     */
    protected void generateGeometry()
    {
        QuadArray vertices = new QuadArray(4, QuadArray.COORDINATES | QuadArray.COLOR_3);

        for (int x = 0; x < 2; x++)
        {
            for (int z = 0; z < 2; z++)
            {
                vertices.setCoordinate(0, new Point3d(0, getHeight(x, z), 0));
                vertices.setColor(0, new Color3f(getColour(x, z)));
                vertices.setCoordinate(1, new Point3d(0, getHeight(x, z), getBreadth()));
                vertices.setColor(1, new Color3f(getColour(x, z)));
                vertices.setCoordinate(2, new Point3d(getLength(), getHeight(x, z), getBreadth()));
                vertices.setColor(2, new Color3f(getColour(x, z)));
                vertices.setCoordinate(3, new Point3d(getLength(), getHeight(x, z), 0));
                vertices.setColor(3, new Color3f(getColour(x, z)));
            }
        }
        setGeometry(vertices, 0);
    }

    /**
     * This method generates the Appearance for this StillWater object.
     */
    protected void generateAppearance()
    {
        Appearance appearance = new Appearance();
        PolygonAttributes polyAttrib = new PolygonAttributes();
        polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
        polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        polyAttrib.setBackFaceNormalFlip(true);
        appearance.setPolygonAttributes(polyAttrib);
        TransparencyAttributes ta = new TransparencyAttributes();
        ta.setTransparency(0.15f);
        ta.setTransparencyMode(TransparencyAttributes.FASTEST);
        appearance.setTransparencyAttributes(ta);
        setAppearance(appearance);
    }

    /**
     * Returns a String representation of this object.
     * 
     * @return a String representation of this object's attributes.
     */
    public String toString()
    {
        return super.toString() + " Colour: " + getColour().toString();
    }

}