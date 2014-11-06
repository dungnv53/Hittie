import java.util.Enumeration;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Texture;
import javax.vecmath.Point3d;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;

/**
 * This abstract class extends Shape3D, and represents a three-dimensional
 * object which is loaded from a 3D model which is saved in the OBJ format.
 * 
 * @author James Attenborough
 */
public abstract class GameObject extends Shape3D
{
    private String name; // The name of this object.
    private String model; // The filename, relative to the root directory of the
    // game, of the three-dimensional model for this object.
    private Texture texture; // The Texture which is applied to this object.
    private Point3d location; // The location of this object.

    /**
     * The GameObject constructor. This initializes the GameObject, loads the 3D
     * model, and applies the Texture to it.
     * 
     * @param nameIn
     *            The name of this object.
     * @param modelIn
     *            The filename, relative to the root directory of the game, of
     *            the 3D model used to represent this object.
     * @param textureIn
     *            The Texture to be applied to this object.
     * @param locationIn
     *            The location of this GameObject.
     * @param pickable
     *            Whether this GameObject can be picked in the 3D world.
     */
    public GameObject(String nameIn, String modelIn, Texture textureIn, Point3d locationIn, boolean pickable)
    {
        super(); // Call the superclass's constructor.

        // Initialize this object by setting it's attributes:
        setName(nameIn);
        setModel(modelIn);
        setTexture(textureIn);
        setLocation(locationIn);

        // Load the 3D model from the file and set it as this object's geometry:
        generateModel();

        // Sets the appearance to one generated using this object's Texture:
        generateAppearance();

        if (pickable) // If this object can be picked in the 3D world,
        {
            // then set this object's capabilities to allow picking reading and
            // writing
            setCapability(Shape3D.ALLOW_PICKABLE_WRITE);
            setCapability(Shape3D.ALLOW_PICKABLE_READ);
            // and set this object to be pickable:
            setPickable(true);
        }
    }

    /**
     * This method loads the 3D model, and then add's each of it's parts to this
     * GameObject's geometry.
     */
    private void generateModel()
    {
        try
        {
            // The ObjectFile loader is created, using the ObjectFile.RESIZE
            // flag, which centres the object at (0, 0, 0), and scales it until
            // all coordinates are within the range (-1, -1, -1) to (1, 1, 1):
            ObjectFile loader = new ObjectFile(ObjectFile.RESIZE);

            Scene scene = loader.load(getModel());// Load the model.
            // Get the returns BranchGroup containing the scene loaded:
            BranchGroup group = scene.getSceneGroup();

            // Retrieve all the children nodes in the BranchGroup:
            Enumeration enumeration = group.getAllChildren();

            // For each child node of the BranchGroup, i.e., each part of the
            // model:
            while (enumeration.hasMoreElements())
            {
                // Add the Geometry of that node to this one:
                this.addGeometry(((Shape3D) enumeration.nextElement()).getGeometry());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
        }
    }

    /**
     * Sets the appearance of this object to one generated using this object's
     * Texture.
     */
    private void generateAppearance()
    {
        Appearance appearance = new Appearance();

        // A PolygonAttributes object is used to further define the Appearance
        // object:
        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        appearance.setPolygonAttributes(pa);

        // Set the Texture of the Appearance to this object's Texture:
        appearance.setTexture(getTexture());

        // A TexCoordGeneration object is used to generate the texture
        // coordinates easily:
        TexCoordGeneration tcg = new TexCoordGeneration();
        tcg.setFormat(TexCoordGeneration.TEXTURE_COORDINATE_3);
        appearance.setTexCoordGeneration(tcg);

        setAppearance(appearance); // Finally, apply the Appearance.
    }

    /**
     * Sets the name of this GameObject to the value passed to it.
     * 
     * @param nameIn
     *            The name to set this GameObject's name to.
     */
    private void setName(String nameIn)
    {
        name = nameIn;
    }

    /**
     * Returns the name of this GameObject.
     * 
     * @return The name of this GameObject.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the model filename of this GameObject to the value passed to it.
     * 
     * @param modelIn
     *            The model filename to set this GameObject's model filename to.
     */
    private void setModel(String modelIn)
    {
        model = modelIn;
    }

    /**
     * Returns the model filename of this GameObject.
     * 
     * @return The model filename of this GameObject.
     */
    public String getModel()
    {
        return model;
    }

    /**
     * Sets the Texture of this GameObject to the Texture passed to it.
     * 
     * @param textureIn
     *            The Texture to set this GameObject's Texture to.
     */
    private void setTexture(Texture textureIn)
    {
        texture = textureIn;
    }

    /**
     * Returns the Texture object of this GameObject.
     * 
     * @return The Texture object of this GameObject.
     */
    public Texture getTexture()
    {
        return texture;
    }

    /**
     * Sets the location of this GameObject to the Point3d passed to it.
     * 
     * @param locationIn
     *            The Point3d to set this GameObject's location to.
     */
    public void setLocation(Point3d locationIn)
    {
        location = locationIn;
    }

    /**
     * Returns the location of this GameObject.
     * 
     * @return The location of this GameObject.
     */
    public Point3d getLocation()
    {
        return location;
    }

    /**
     * Sets the x-value of the location of this GameObject to the value passed.
     * This method can be used to conserve memory when only x-value of the
     * GameObject's location is required.
     * 
     * @param xIn
     *            The value to set the x-value of this GameObject's location to.
     */
    public void setLocationX(double xIn)
    {
        location.x = xIn;
    }

    /**
     * Returns the x-value of the location of this GameObject.
     * 
     * @return The x-value of the location of this GameObject.
     */
    public double getLocationX()
    {
        return location.x;
    }

    /**
     * Sets the y-value of the location of this GameObject to the value passed.
     * This method can be used to conserve memory when only y-value of the
     * GameObject's location is required.
     * 
     * @param yIn
     *            The value to set the y-value of this GameObject's location to.
     */
    public void setLocationY(double yIn)
    {
        location.y = yIn;
    }

    /**
     * Returns the y-value of the location of this GameObject.
     * 
     * @return The y-value of the location of this GameObject.
     */
    public double getLocationY()
    {
        return location.y;
    }

    /**
     * Sets the z-value of the location of this GameObject to the value passed.
     * This method can be used to conserve memory when only z-value of the
     * GameObject's location is required.
     * 
     * @param zIn
     *            The value to set the z-value of this GameObject's location to.
     */
    public void setLocationZ(double zIn)
    {
        location.z = zIn;
    }

    /**
     * Returns the z-value of the location of this GameObject.
     * 
     * @return The z-value of the location of this GameObject.
     */
    public double getLocationZ()
    {
        return location.z;
    }

    /**
     * Returns a String representation of the attributes of this object.
     * 
     * @return A String representation of this object.
     */
    public String toString()
    {
        return "Name: " + getName() + " Model: " + getModel() + " Texture: " + getTexture().toString() + " Location: "
                + location.toString() + " Pickable: " + getPickable();
    }

}