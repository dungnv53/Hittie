import javax.media.j3d.*;
import javax.vecmath.*;

/**
 * This class extends Shape3D, and is represents a sky filled with stars. The
 * star generation createStars() method's algorithm was based on a nice and
 * simple one used by Mike Prosser's Arabian Flights open source first person
 * fantasy game.
 * 
 * @author James Attenborough
 */
public class SkyWithStars extends Shape3D
{
    private final int NUM_STARS; // The number of stars in the sky.
    private final double DISTANCE; // The distance of the stars from the centre.

    /**
     * Constructs a SkyWithStars object from a given number of stars and
     * distance.
     * 
     * @param stars
     *            The number of stars to generate.
     * @param distance
     *            The radius of the sphere that the stars are generated within.
     */
    public SkyWithStars(int stars, double distance)
    {
        super(); // Call the Shape3D default constructor.
        NUM_STARS = stars;
        DISTANCE = distance;
        createStars(); // Generates the star field.
    }

    /**
     * Returns the number of stars in the sky.
     * 
     * @return NUM_STARS The number of stars in the sky.
     */
    public int getNumStars()
    {
        return NUM_STARS;
    }

    /**
     * Returns the distance of the stars from the centre.
     * 
     * @return DISTANCE The distance of the stars from the centre.
     */
    public double getDistance()
    {
        return DISTANCE;
    }

    /**
     * Creates the stars as a PointArray, and then sets this objects geometry to
     * that PointArray. For each star in the sky, a random angle between 0 and
     * 360 and a random angle between 0 and 90 are chosen, and a star is
     * positioned at the point on the surrounding sphere that is getDistance()
     * units away from the centre, and is calculated using the two random
     * angles.
     */
    private void createStars()
    {
        // The PointArray that holds the geometry of the star field:
        PointArray stars = new PointArray(getNumStars(), PointArray.COLOR_3 | PointArray.COORDINATES);

        double vertical;
        double horizontal;
        float b;

        for (int i = 0; i < getNumStars(); i++)
        {
            // Generate a random angle between 0 and 90:
            vertical = (Math.random() * 90);
            // Generate a random angle between 0 and 360:
            horizontal = (Math.random() * 360);

            // Set the coordinate of this star:
            stars.setCoordinate(i, new Point3d(
                    ((Math.sin(Math.toRadians(horizontal)) * Math.cos(Math.toRadians(vertical))) * getDistance()), (Math.sin(Math
                            .toRadians(vertical)) * getDistance()), ((Math.cos(Math.toRadians(horizontal)) * Math.cos(Math
                            .toRadians(vertical))) * getDistance())));

            // Calculate a random 'brightness' for this star:
            b = ((float) Math.random() * 0.5f);

            // Set the colour of this star:
            stars.setColor(i, new Color3f(b + ((float) Math.random() * 0.1f), b + ((float) Math.random() * 0.1f), b
                    + ((float) Math.random() * 0.2f)));
        }
        // Set the geometry of this object to be this PointArray of stars:
        this.setGeometry(stars);
    }

    /**
     * Returns a String representation of the attributes of this object.
     * 
     * @return The attributes of this object.
     */
    public String toString()
    {
        return "Number of Stars: " + getNumStars() + " Distance: " + getDistance();
    }
}