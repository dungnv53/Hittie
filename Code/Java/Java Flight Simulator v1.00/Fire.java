import java.util.Enumeration;

import javax.media.j3d.Appearance;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.GeometryUpdater;
import javax.media.j3d.PointArray;
import javax.media.j3d.PointAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

/**
 * This objects is represents a Fire in the game, and is a particle system witin
 * itself. The particles radiate upwards and outwards from the centre of the
 * fire, following a parabolic curve. As they move along the curve, their colour
 * values are also changed to create the effect of fire. The particles are
 * reused: When they have gone below their initiali starting height, then they
 * are returned to their starting point and reused.
 * 
 * @author James Attenborough
 */
public class Fire extends Shape3D
{
    private final int PARTICLES = 5000; // The number of particles to use.

    private Point3d location; // The location of this object.

    private Animator behaviour; // This Fire's Animator.
    private PointArray points; // The PointArray that holds the particles.

    // The arrays which describe each of the particles:
    private float[] positions;
    private float[] colours;
    private float[] velocities;

    // The effect of gravity on the y-value of each particle:
    private final float GRAVITY = -10f;

    // The starting colour of all particles:
    private final static Color3f ORIGINAL = new Color3f(1.0f, 1.0f, 0.6f);

    // Keeps track of whether to pause/continue the animation of the Fire:
    private boolean running;

    // These are used when calculating each particle's velocitry components:
    private double velocityX;
    private double velocityZ;

    private final static int RADIUS = 2; // The radius of the fire.

    /**
     * Constructs a Fire, which is to be located at the specified point.
     * 
     * @param locationIn
     *            The location of this Fire.
     */
    public Fire(Point3d locationIn)
    {
        running = true;
        setLocation(locationIn);

        // Initilaize the PointArray that contains the particles, and set the
        // necessary capabilities:
        points = new PointArray(PARTICLES, PointArray.COORDINATES | PointArray.BY_REFERENCE | PointArray.COLOR_3);
        points.setCapability(PointArray.ALLOW_COORDINATE_WRITE);
        points.setCapability(PointArray.ALLOW_COLOR_WRITE);
        points.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
        points.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);

        // It is essential that we can read this Fire's geometry:
        setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);

        // Initialize the animation behaviour with a GeometryUpdater
        // implementing:
        behaviour = new Animator(new Updater());
        behaviour.setSchedulingBounds(new BoundingSphere(getLocation(), 1000));

        // For each particle, we need to store it's 3 position components, it's
        // 3 colour components and its 3 velocity components, so we intiialize
        // these arrays to hold (PARTICLES * 3) elements, as the components of
        // each of the particles will be stored in threes. This
        // means that the location of any given particle would be:
        // (positions[number], positions[number + 1], positions[number+2), and
        // the same applies for the colours and velocities:
        positions = new float[(PARTICLES * 3)];
        colours = new float[(PARTICLES * 3)];
        velocities = new float[(PARTICLES * 3)];

        for (int j = 0; j < (PARTICLES * 3); j += 3)
        {
            // We want to initialize a particle at every third index, so that we
            // can also use the indexes of (j+1) and (j+2) to store data for
            // this particle:
            initializeParticle(j);
        }

        // Set the positions and colours of each particle in the PointArray:
        points.setCoordRefFloat(positions);
        points.setColorRefFloat(colours);

        // Set the PointArray to be this Fire's geometry:
        this.setGeometry(points);

        // Intiialize and set this Fire's appearance:
        Appearance appearance = new Appearance();
        PointAttributes pointAttributes = new PointAttributes();
        pointAttributes.setPointSize(10);
        appearance.setPointAttributes(pointAttributes);
        setAppearance(appearance);
    }

    /**
     * Sets this Fire's location to the one passed to it.
     * 
     * @param locationIn
     *            The point used to set this Fire's location.
     */
    private void setLocation(Point3d locationIn)
    {
        location = locationIn;
    }

    /**
     * Returns this Fire's location.
     * 
     * @return The location of this Fire.
     */
    public Point3d getLocation()
    {
        return location;
    }

    /**
     * Return this Fire's animation behaviour. This must be added to any scene
     * along with the Fire.
     * 
     * @return This Fire's behaviour.
     */
    public Behavior getBehaviour()
    {
        return behaviour;
    }

    /**
     * Initializes the particle at the specified index. Its position is set to
     * the starting position, it's intial velocity are calculated, and its
     * colour is set to the starting colour.
     * 
     * @param num
     *            The particle number to initliaize.
     */
    private void initializeParticle(int num)
    {
        // Reset this particles position to the starting values:
        positions[num] = 0f;
        positions[(num + 1)] = 0f;
        positions[(num + 2)] = 0f;

        // Calculate the components of it's velocity:
        velocityX = (Math.random() * RADIUS);

        // Using the basic circle equation: x^2 + z^2 = r^2, we can find the z
        // component for the velocity.
        velocityZ = Math.sqrt((RADIUS * RADIUS) - (velocityX * velocityX));

        // This if statement decides if the x-component of the velocity should
        // be negated or not (If it didn't, then the particles would only travel
        // along the positive x-axis):
        if (Math.random() < 0.5)
            velocities[num] = ((float) -velocityX);
        else
            velocities[num] = ((float) velocityX);

        // Set the y-component of this particle's velocity:
        velocities[(num + 1)] = (float) (Math.random() * 6.0f);

        // This if statement decides if the z-component of the velocity should
        // be negated or not (If it didn't, then the particles would only travel
        // along the positive z-axis):
        if (Math.random() < 0.5)
            velocities[(num + 2)] = ((float) -velocityZ);
        else
            velocities[(num + 2)] = ((float) velocityZ);

        // Set the colours to their starting values:
        colours[num] = ORIGINAL.x;
        colours[(num + 1)] = ORIGINAL.y;
        colours[(num + 2)] = ORIGINAL.z;
    }

    /**
     * Controls whether this Fire's animation is running or not.
     * 
     * @param runningIn
     *            Whether or not to animate this Fire.
     */
    public void setRunning(boolean runningIn)
    {
        running = runningIn;
    }

    /**
     * Checks whether this Fire's animation is running or not.
     * 
     * @return true if this Fire's animation is running, otherwise false.
     */
    public boolean isRunning()
    {
        return running;
    }

    /**
     * This class implements GeometryUpdater, and is used to update the
     * position, colour and velocity of each particle.
     * 
     * @author James Attenborough
     */
    public class Updater implements GeometryUpdater
    {
        /**
         * This method goes through particle in this system, and it will
         * re-initilize it if necessary, or else it will update the particle's
         * position, velocity and colour.
         * 
         * @param g
         *            The geometry to be updated.
         *  
         */
        public void updateData(Geometry g)
        {
            // Every third number gives us the first component for that
            // particle:
            for (int p = 0; p < (PARTICLES * 3); p += 3)
            {
                // If the particle's y-value has gone below it's starting
                // y-value:
                if (positions[(p + 1)] < 0.0f)
                    initializeParticle(p); // then re-initialize the particle.
                else
                {
                    // Or if it is still at an acceptable height:

                    // Add the velocity components to their respective position
                    // components:
                    positions[p] += (velocities[p] * 0.05f);
                    positions[(p + 1)] += ((velocities[p + 1] * 0.05f) + (GRAVITY * 0.00125f));
                    positions[(p + 2)] += (velocities[p + 2] * 0.05f);

                    // Add gravity:
                    velocities[(p + 1)] += (GRAVITY * 0.05f);

                    // Lastly, decrease the green and blue elements of this
                    // particle's colour, i.e., make it redder as it moves
                    // outwards from the centre:

                    // Decrease the green component:
                    colours[(p + 1)] -= 0.05f;
                    if (colours[(p + 1)] < 0.0f)
                        colours[(p + 1)] = 0.0f;

                    // Decrease the blue component:
                    colours[(p + 2)] -= 0.05f;
                    if (colours[(p + 2)] < 0.0f)
                        colours[(p + 2)] = 0.0f;
                }
            }
        }
    }

    /**
     * This class scehdules an update of the particle geometry to occur at a
     * certain regular interval.
     * 
     * @author James Attenborough
     */
    public class Animator extends Behavior
    {
        private Updater updater; // The updater of the geometry.
        private WakeupCondition delay; // The wakeup scheduler.

        /**
         * The Animator constructor. Initializes an Animator behaviour to update
         * the geometry associated with the Updater passed to it every 20
         * milliseconds.
         * 
         * @param updaterIn
         *            The Updater to use for geometry updates.
         */
        public Animator(Updater updaterIn)
        {
            updater = updaterIn;

            // Schedule a wakeup every 20 milliseconds:
            delay = new WakeupOnElapsedTime(20);
        }

        /**
         * This method had to be implemented, and is used to schedule the first
         * wakeup event.
         */
        public void initialize()
        {
            wakeupOn(delay);
        }

        /**
         * This method had to be implemented, and is used to handle each wakeup
         * event.
         * 
         * @param criteria
         *            The criteria that has caused this method to be invoked.
         */
        public void processStimulus(Enumeration criteria)
        {
            if (running)
            {
                // Update the geometry of the Fire:
                points.updateData(updater);
            }

            //Schedule the next wakeup:
            wakeupOn(delay);
        }
    }

}