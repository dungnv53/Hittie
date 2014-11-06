import java.util.Enumeration;

import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.Morph;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

/**
 * AnimatedWater is an extension of the BranchGroup class, and represents water
 * that ripples with a wave effect, by making use of a Morph object.
 * 
 * @author James Attenborough
 */
public class AnimatedWater extends BranchGroup
{
    private final int BREADTH; // The breadth of this object.
    private final int LENGTH; // The length of this object.
    private final double HEIGHT; // The height of this object.

    // The colour of this object's Appearance:
    private Color3f waterColour;

    /**
     * The AnimatedWater constructor. This initializes an AnimatedWater object
     * using the specified values.
     * 
     * @param waterIn
     *            The colour of the water.
     * @param breadthIn
     *            The breadth of this AnimatedWater.
     * @param lengthIn
     *            The length of this AnimatedWater.
     * @param heightIn
     *            The height of this AnimatedWater.
     * @param angle
     *            The angle used when generating the waves.
     */
    public AnimatedWater(Color3f waterIn, int breadthIn, int lengthIn, double heightIn, double angle)
    {
        super(); // Call the BranchGroup constructor.

        // Set the attributes of this object:
        setWaterColour(waterIn);
        BREADTH = breadthIn;
        LENGTH = lengthIn;
        HEIGHT = heightIn;

        // Generate the appearance for this object:
        Appearance appearance = new Appearance();
        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        appearance.setPolygonAttributes(pa);
        appearance.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.6f));
        Material m = new Material();
        m.setShininess(120f);
        appearance.setMaterial(m);

        // Each of these QuadArrays holds a different water mesh, one
        // is generated using the angle passed to this constructor, and the
        // other using the negation of the angle passed to this constructor:
        QuadArray waterOne = generateVertices(generateHeights(angle));
        QuadArray waterTwo = generateVertices(generateHeights(-angle));

        // This GeometryArray array holds the two water meshes, so that they can
        // be used to create the Morph object:
        GeometryArray[] waterMeshes = { waterOne, waterTwo };
        Morph waterMorph = new Morph(waterMeshes, appearance);
        waterMorph.setCapability(Morph.ALLOW_WEIGHTS_READ);
        waterMorph.setCapability(Morph.ALLOW_WEIGHTS_WRITE);
        addChild(waterMorph); // Add the Morph to this object.
        // Add a WaterAnimator, which is the behaviour which animates the water:
        addChild(new WaterAnimator(waterMorph, new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, 0, 0, 2000, 1000, 200,
                2000, 1000, 200)));
    }

    /**
     * Sets this AnimatedWater object's colour.
     * 
     * @param colourIn
     *            The colour to set this object's colour to.
     */
    private void setWaterColour(Color3f colourIn)
    {
        waterColour = colourIn;
    }

    /**
     * Returns the water colour.
     * 
     * @return The colour of the water.
     */
    public Color3f getWaterColour()
    {
        return waterColour;
    }

    /**
     * Returns The breadth of this AnimatedWater object.
     * 
     * @return The breadth of this object.
     */
    public int getBreadth()
    {
        return BREADTH;
    }

    /**
     * Returns The length of this AnimatedWater object.
     * 
     * @return The length of this object.
     */
    public int getLength()
    {
        return LENGTH;
    }

    /**
     * Returns The height of this AnimatedWater object.
     * 
     * @return The height of this object.
     */
    public double getHeight()
    {
        return HEIGHT;
    }

    /**
     * Generates a height map using a specified angle incrementation to generate
     * waves.
     * 
     * @param counter
     *            The amount by which to increment the wave angle each time.
     * @return A two-dimensional array of doubles - the height map of the water
     *         mesh generated.
     */
    private double[][] generateHeights(double counter)
    {
        double[][] heights = new double[getLength()][getBreadth()];

        for (int i = 0; i < getLength(); i++)
        {
            for (int j = 0; j < getBreadth(); j++)
            {
                // Set each value to the initial height:
                heights[i][j] = getHeight();
            }
        }

        int degrees;

        for (int i = 0; i < getLength(); i++)
        {
            degrees = 0;
            for (int j = 0; j < getBreadth(); j++)
            {
                // Use a simple sin graph to generate a wave:
                heights[i][j] += Math.sin(Math.toRadians(degrees));
                degrees += counter;
            }
        }

        return heights;
    }

    /**
     * Generates the geometry for the water mesh based on the height map passed
     * to it.
     * 
     * @param heightsIn
     *            A height map to be used to generate the water mesh.
     * @return The water mesh.
     */
    private QuadArray generateVertices(double[][] heightsIn)
    {
        QuadArray vertices = new QuadArray((4 * getLength() * getBreadth()), QuadArray.COORDINATES | QuadArray.COLOR_3);

        int counter = 0;

        for (int j = 0; j < (getBreadth() - 1); j++)
        {
            for (int i = 0; i < (getLength() - 1); i++)
            {
                // Sets each point to the necessary height, and sets it's colour
                // to the water colour:

                vertices.setCoordinate(counter, new Point3d(i, heightsIn[i][j], j));
                vertices.setColor(counter, getWaterColour());
                counter++;

                vertices.setCoordinate(counter, new Point3d(i, heightsIn[i][(j + 1)], (j + 1)));
                vertices.setColor(counter, getWaterColour());
                counter++;

                vertices.setCoordinate(counter, new Point3d((i + 1), heightsIn[(i + 1)][(j + 1)], (j + 1)));
                vertices.setColor(counter, getWaterColour());
                counter++;

                vertices.setCoordinate(counter, new Point3d((i + 1), heightsIn[(i + 1)][j], j));
                vertices.setColor(counter, getWaterColour());
                counter++;
            }
        }

        return vertices;
    }

    /**
     * The WaterAnimator class is an extension of the Behavior class, and is
     * used to animate a Morph object using a specified Alpha.
     * 
     * @author James Attenborough
     */
    public class WaterAnimator extends Behavior
    {
        // The behaviour waker will be called every frame:
        private WakeupOnElapsedFrames waker = new WakeupOnElapsedFrames(0);

        private Alpha waterAlpha;
        private Morph waterMorph;
        private double weights[];

        /**
         * The WaterAnimator constructor. This initializes a WaterAnimator
         * Behavior object using the specified Morph and Alpha.
         * 
         * @param morphIn
         *            The Morph to be used.
         * @param alphaIn
         *            The Alpha to be used.
         */
        public WaterAnimator(Morph morphIn, Alpha alphaIn)
        {
            waterAlpha = alphaIn;
            waterMorph = morphIn;

            // Retrieve this Morph's weight vector component:
            weights = waterMorph.getWeights();
        }

        /**
         * This method is required by subclasses of the Behavior class, and is
         * used to initialize this WaterAnimator object:
         */
        public void initialize()
        {
            waterAlpha.setStartTime(System.currentTimeMillis());
            setSchedulingBounds(new BoundingSphere(new Point3d(), 10000));
            wakeupOn(waker); // Schedule another wake up.
        }

        /**
         * This method is required by subclasses of the Behavior class, and is
         * used to process a stimulus (in this case, the WakeupOnElapsedFrames
         * object, so it will be called every frame):
         * 
         * @param criteria
         *            The criteria for this stimulus.
         */
        public void processStimulus(Enumeration criteria)
        {
            // Retrieve the current waterAlpha value [waterAlpha.value() returns
            // a value between 0.0 and 1.0 based on
            // the current time and this Alpha's parameters], then update the
            // values of the Morph's weights and set those weights as the
            // current Morph weight values.

            if (waterAlpha.value() < 0.5)
            {
                weights[0] = (1.0 - (waterAlpha.value() * 2.0));
                weights[1] = waterAlpha.value() * 2.0;
            }
            else
            {
                weights[0] = ((waterAlpha.value() - 0.5) * 2.0);
                weights[1] = 1.0f - ((waterAlpha.value() - 0.5) * 2.0);
            }

            waterMorph.setWeights(weights);

            wakeupOn(waker); // Schedule another wakeup.
        }
    }
}