import javax.media.j3d.Texture;
import javax.vecmath.Point3d;

/**
 * A GroundObject is a GameObject that is positioned at the ground level (not
 * calculated in this class, but in implementing ones). It can be scaled and
 * also rotated before it is added to the 3D scene.
 * 
 * @author James Attenborough
 */
public class GroundObject extends GameObject
{
    private double scale; // The amount by which this object must be scaled.
    private double rotation; //The number of degrees to rotate this object by.

    /**
     * This constructor is used to initialize a GameObject with the specified
     * parameters.
     * 
     * @param nameIn
     *            The name of this GroundObject.
     * @param modelIn
     *            The filename of the model of this GroundObject.
     * @param textureIn
     *            The Texture applied to this GroundObject.
     * @param locationIn
     *            The location of this GroundObject.
     * @param pickable
     *            Whether this GroundObject can be picked or not.
     * @param objectScaleIn
     *            The amount by which to scale this GroundObject.
     * @param rotationIn
     *            The number of degrees to rotate this GroundObject by.
     */
    public GroundObject(String nameIn, String modelIn, Texture textureIn, Point3d locationIn, boolean pickable, double objectScaleIn,
            double rotationIn)
    {
        super(nameIn, modelIn, textureIn, locationIn, pickable);
        // After calling the GameObject constructor,
        // the scale and rotation of this GroundObject are set:
        setScale(objectScaleIn);
        setRotation(rotationIn);
    }

    /**
     * Sets the scale of this GroundObject. If an invalid (one that is less than
     * or equal to 0) value is entered, the scale is set to 1.
     * 
     * @param scaleIn
     *            The amount by which to scale this GroundObject.
     * @return true if the scale was set successfully, otherwise false.
     */
    private boolean setScale(double scaleIn)
    {
        if (scaleIn > 0)
        {
            scale = scaleIn;
            return true;
        }
        scale = 1;
        return false;
    }

    /**
     * Returns the scale of this GroundObject.
     * 
     * @return scale The scale of this GroundObject.
     */
    public double getScale()
    {
        return scale;
    }

    /**
     * Sets the rotation of this GroundObject. If an invalid (one that is not
     * between 0 and 359) value is entered, the rotation is set to 0 degrees.
     * 
     * @param rotationIn
     *            The number of degrees to rotate this GroundObject by.
     * @return true if the rotation was set successfully, otherwise false.
     */
    private boolean setRotation(double rotationIn)
    {
        if ((rotationIn >= 0) && (rotationIn <= 359))
        {
            rotation = rotationIn;
            return true;
        }
        rotation = 0;
        return false;
    }

    /**
     * Returns the rotation of this GroundObject.
     * 
     * @return rotation The rotation of this GroundObject.
     */
    public double getRotation()
    {
        return rotation;
    }

    /**
     * Returns a String representation of the attributes of this GroundObject.
     * 
     * @return A String representation of this GroundObject.
     */
    public String toString()
    {
        return super.toString() + " Scale: " + getScale() + " Rotation: " + getRotation();
    }
}