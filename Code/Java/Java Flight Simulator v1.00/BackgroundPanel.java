import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * This JPanel extension is used to draw the image or colour background for all
 * the non-opaque JPanel's which are placed on top of it.
 * 
 * @author James Attenborough
 */
public class BackgroundPanel extends JPanel
{
    private Color colour; // The Color to draw as the background.
    private final int HEIGHT; // The height of the background.

    private Image image; // The Image to draw as the background.
    // Whether this background is an image or a colour:
    private boolean imageBackground;
    private final int WIDTH; // The width of the background.

    /**
     * The BackgroundPanel constructor for a coloured background.
     * 
     * @param colourIn
     *            The Color to draw as the background.
     * @param widthIn
     *            The width of the background.
     * @param heightIn
     *            The height of the background.
     */
    public BackgroundPanel(Color colourIn, int widthIn, int heightIn)
    {
        // Set the background, and the width and height:
        setBackground(colourIn);
        WIDTH = widthIn;
        HEIGHT = heightIn;
    }

    /**
     * The BackgroundPanel constructor for an Imaged background.
     * 
     * @param imageIn
     *            The Image to draw as the background.
     * @param widthIn
     *            The width of the background.
     * @param heightIn
     *            The height of the background.
     */
    public BackgroundPanel(Image imageIn, int widthIn, int heightIn)
    {
        // Set the background, and the width and height:
        setBackground(imageIn);
        WIDTH = widthIn;
        HEIGHT = heightIn;
    }

    /**
     * Sets the background to the specified Color.
     * 
     * @param colourIn
     *            The Color to apply as the background.
     */
    public void setBackground(Color colourIn)
    {
        imageBackground = false; // Using a Color.
        colour = colourIn;
        repaint();
    }

    /**
     * Sets the background to the specified Image.
     * 
     * @param imageIn
     *            The Image to apply as the background.
     */
    public void setBackground(Image imageIn)
    {
        imageBackground = true; // Using and Image.
        image = imageIn;
        repaint();
    }

    /**
     * Overridden paintComponent of the JPanel class. This method draws the
     * background.
     * 
     * @param g
     *            The Graphics object associated with this panel.
     */
    public void paintComponent(Graphics g)
    {
        if (imageBackground)
        {
            // Draw the Image:
            g.drawImage(image, 0, 0, WIDTH, HEIGHT, this);
        }
        else
        {
            // Or fill a rectangle with the background's Color.
            g.setColor(colour);
            g.fillRect(0, 0, WIDTH, HEIGHT);
        }
    }

}