import java.awt.Color;
import java.awt.Image;
import java.awt.image.PixelGrabber;
import java.util.Enumeration;

import javax.media.j3d.Appearance;
import javax.media.j3d.Geometry;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

import com.sun.j3d.utils.picking.PickTool;

/**
 * This class represents a Terrain object which is generated using a height map
 * and a colour map. Both the height and colour maps are generated from an
 * Image.
 * 
 * @author James Attenborough
 */
public class Terrain extends WorldObject
{
    /**
     * This is the maximum height of this Terrain object.
     */
    private final double MAX_HEIGHT;
    /**
     * This is the default maximum height of a Terrain object.
     */
    public static final double DEFAULT_MAX_HEIGHT = 30;

    /**
     * This is the only constructor of a Terrain object. It creates a Terrain
     * object, from the values passed to it, which can be loaded to a 3D scene.
     * 
     * @param image
     *            The image to be used to generate the height map for this
     *            Terrain object.
     * @param breadth
     *            The breadth of this Terrain object. This should be the width
     *            of the Image passed to this constructor.
     * @param length
     *            The length of this Terrain object.This should be the height of
     *            the Image passed to this constructor.
     * @param minimumHeight
     *            The minimum height of this Terrain object.
     * @param maximumHeight
     *            The maximum height of this Terrain object.
     */
    public Terrain(Image image, int breadth, int length, double minimumHeight, double maximumHeight)
    {
        super(breadth, length, minimumHeight, new Color[breadth][length], new double[breadth][length]);
        // Call the superconstructor.

        if (maximumHeight >= 0)
        {
            MAX_HEIGHT = maximumHeight;
        }
        else
        {
            MAX_HEIGHT = DEFAULT_MAX_HEIGHT;
        }

        // Genereate the height and oclour maps for the terrain, then generate
        // the Terrain geometry and Appearance:
        convertImageToHeightsAndColours(image);
        generateGeometry();
        generateAppearance();

        // Set the necessarry capabilities:
        setCapability(Shape3D.ALLOW_COLLIDABLE_READ);
        setCapability(Shape3D.ALLOW_COLLIDABLE_WRITE);
        setCapability(Shape3D.ENABLE_COLLISION_REPORTING);
        setCapability(Shape3D.ALLOW_PICKABLE_WRITE);
        setCapability(Shape3D.ALLOW_PICKABLE_READ);
        setCapability(Shape3D.ENABLE_PICK_REPORTING);
        setCapability(Shape3D.ALLOW_GEOMETRY_READ);

        setPickable(true);
        setCollidable(true);

        PickTool.setCapabilities(this, PickTool.INTERSECT_FULL);

        for (Enumeration e = this.getAllGeometries(); e.hasMoreElements();)
        {
            Geometry g = (Geometry) e.nextElement();
            g.setCapability(Geometry.ALLOW_INTERSECT);
        }
    }

    /**
     * This method is used to generate the height map and the colour map for the
     * Terrain object. The colour of each pixel in the Image is extracted. Then
     * a height value (between the minimum and maximum heights) is calculated
     * for the point (x,z) on the Terrain, based on the colour of the pixel at
     * the point (x,z) in the Image.
     * 
     * @param image
     *            The Image used to generate the height and colour maps for this
     *            Terrain object.
     */
    private void convertImageToHeightsAndColours(Image image)
    {
        // This PixelGrabber is used to retrieve the pixels in the specified
        // Image:
        PixelGrabber pg = new PixelGrabber(image, 0, 0, getBreadth(), getLength(), true);
        try
        {
            pg.grabPixels();
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
        }

        int[] pixels = ((int[]) pg.getPixels());

        int red, green, blue;

        // For each point in the height and colour maps:
        for (int x = 0; x < getBreadth(); x++)
        {
            for (int z = 0; z < getLength(); z++)
            {
                // Retrieve the red component of the specified pixel:
                red = ((pixels[(z * getLength()) + x] >> 16) & 0xff);
                // Retrieve the blue component of the specified pixel:
                green = ((pixels[(z * getLength()) + x] >> 8) & 0xff);
                // Retrieve the green component of the specified pixel:
                blue = ((pixels[(z * getLength()) + x]) & 0xff);

                // Set this point in the colour and height maps:
                setColour(x, z, new Color(red, green, blue));
                // Calculate the height at the point by comparing where the
                // colour is situated between black (0x000000) and white
                // (0xffffff) [and add the minimum height]:
                setHeight(x, z, (((pixels[(z * getLength()) + x] & 0xffffff) * MAX_HEIGHT) / 0xffffff) + getHeight());
            }
        }
    }

    /**
     * Returns the maximum height of this Terrain object.
     * 
     * @return MAX_HEIGHT The maximum height of this Terrain object.
     */
    public double getMaximumHeight()
    {
        return MAX_HEIGHT;
    }

    /**
     * This method generates the geometry of this Terrain object so that it can
     * be viewed three-dimensionally. It uses a QuadArray to hold the coordinate
     * and colour of each point in the terrain. This method creates four
     * vertices at a time: the four vertices of each of the (getLength() *
     * getBreadth()) quadrilaterals which make up this Terrain object.
     */
    protected void generateGeometry()
    {
        QuadArray vertices = new QuadArray((4 * getLength() * getBreadth()), QuadArray.COORDINATES | QuadArray.COLOR_3);

        int counter = 0;

        // For each point,
        for (int j = 0; j < (getBreadth() - 1); j++)
        {
            for (int i = 0; i < (getLength() - 1); i++)
            {
                // Set the coordinates and colours of the quadrilater that
                // starts at this point:

                vertices.setCoordinate(counter, new Point3d(i, getHeight(i, j), j));
                vertices.setColor(counter, new Color3f(getColour(i, j)));
                counter++;

                vertices.setCoordinate(counter, new Point3d(i, getHeight(i, (j + 1)), (j + 1)));
                vertices.setColor(counter, new Color3f(getColour(i, (j + 1))));
                counter++;

                vertices.setCoordinate(counter, new Point3d((i + 1), getHeight((i + 1), (j + 1)), (j + 1)));
                vertices.setColor(counter, new Color3f(getColour((i + 1), (j + 1))));
                counter++;

                vertices.setCoordinate(counter, new Point3d((i + 1), getHeight((i + 1), j), j));
                vertices.setColor(counter, new Color3f(getColour((i + 1), j)));
                counter++;
            }
        }
        setGeometry(vertices, 0);
    }

    /**
     * This method generates the Appearance for this Terrain object.
     */
    protected void generateAppearance()
    {
        Appearance appearance = new Appearance();
        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        setAppearance(appearance);
    }

    /**
     * Returns a String representation of the attributes of this Terrain object.
     * 
     * @return The string representation of this Terrain object.
     */
    public String toString()
    {
        return super.toString() + " Maximum Height: " + getMaximumHeight();
    }
}