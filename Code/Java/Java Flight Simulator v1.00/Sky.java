import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.vecmath.Point3d;

import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;

/**
 * This class represents a Sky object, which extends the Background class, and
 * can be added to a three-dimensional scene.
 * 
 * @author James Attenborough
 */
public class Sky extends Background
{
    /**
     * The constructor for a Sky object, which is created and then set to use
     * the appearance passed to the constructor. The Sky object is just a large
     * sphere that the player is positioned within.
     * 
     * @param appearance
     *            The Appearance object that defines how this object appears to
     *            the user.
     */
    public Sky(Appearance appearance)
    {
        super();
        setApplicationBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE));
        // This BranchGroup holds the geometry of this object:
        BranchGroup geometry = new BranchGroup();

        // The Sphere primitive is created and the appearance applied to it. The
        // Primitive.GENERATE_NORMALS_INWARD flag is the key here, as it allows
        // us to view the Sphere from the inside.
        geometry.addChild(new Sphere(1.0f, Primitive.GENERATE_TEXTURE_COORDS | Primitive.GENERATE_NORMALS_INWARD, appearance));
        setGeometry(geometry);
    }
}