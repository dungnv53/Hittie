import java.awt.Color;

/**
 * This class represents a message that must be displayed on screen for a
 * certain period of time. I went further, to create different message types,
 * which carry with them diffrerent message colours.
 * 
 * @author James Attenborough
 */
public class DisplayMessage
{
    private int type; // This holds the message type.
    private String text; // This holds the message text.
    private long displayTime; // This holds the time to display the message for.

    /**
     * This is the standard length of time to display a message for.
     */
    public static final long STANDARD_DISPLAY_TIME = 8000;

    /**
     * This is the the length of time to display a message for that is equal to
     * one second.
     */
    public static final long SECOND_DISPLAY_TIME = 1000;

    /**
     * This is the identifier for a game status message.
     */
    public static final int GAME_STATUS_MESSAGE = (1 << 0);

    /**
     * This is the colour to be used for a game status message.
     */
    private static final Color GAME_STATUS_COLOUR = new Color(22, 83, 126);

    /**
     * This is the identifier for a game error message.
     */
    public static final int ERROR_MESSAGE = (1 << 1);

    /**
     * This is the colour to be used for a game error message.
     */
    private static final Color ERROR_COLOUR = new Color(255, 0, 0);

    /**
     * This is the identifier for a game warning message.
     */
    public static final int WARNING_MESSAGE = (1 << 2);

    /**
     * This is the colour to be used for a game warning message.
     */
    private static final Color WARNING_COLOUR = new Color(222, 33, 99);

    /**
     * This is the identifier for a global chat message.
     */
    public static final int GLOBAL_CHAT_MESSAGE = (1 << 3);

    /**
     * This is the colour to be used for a global chat message.
     */
    private static final Color GLOBAL_CHAT_COLOUR = new Color(63, 102, 23);

    /**
     * This is the identifier for a global chat message sent from this player.
     */
    public static final int GLOBAL_CHAT_FROM_PLAYER_MESSAGE = (1 << 4);

    /**
     * This is the colour to be used for a global chat message sent from this
     * player.
     */
    private static final Color GLOBAL_CHAT_FROM_PLAYER_COLOUR = new Color(123, 0, 69);

    /**
     * This is the identifier for a private chat message sent to this player.
     */
    public static final int PRIVATE_CHAT_TO_PLAYER_MESSAGE = (1 << 5);

    /**
     * This is the colour to be used for a private chat message sent to this
     * player.
     */
    private static final Color PRIVATE_CHAT_TO_PLAYER_COLOUR = new Color(134, 65, 46);

    /**
     * This is the colour to be used for a private chat message sent from this
     * player.
     */
    public static final int PRIVATE_CHAT_FROM_PLAYER_MESSAGE = (1 << 6);

    /**
     * This is the colour to be used for a private chat message sent from this
     * player.
     */
    private static final Color PRIVATE_CHAT_FROM_PLAYER_COLOUR = new Color(120, 113, 5);

    /**
     * The DisplayMessage constructor. This initializes a DisplayMessage with
     * the values passed to it.
     * 
     * @param typeIn
     *            The DisplayMessage type.
     * @param textIn
     *            The message text to display.
     * @param displayTimeIn
     *            the time to display the message for.
     */
    public DisplayMessage(int typeIn, String textIn, long displayTimeIn)
    {
        // Set the values passed to this constructor:
        setType(typeIn);
        setText(textIn);
        setDisplayTime(displayTimeIn);
    }

    /**
     * Sets the text of this DisplayMessage
     * 
     * @param textIn
     *            The text to set this DisplayMessage text to.
     */
    public void setText(String textIn)
    {
        text = textIn;
    }

    /**
     * Returns the text of this DisplayMessage.
     * 
     * @return The text of this DisplayMessage.
     */
    public String getText()
    {
        return text;
    }

    /**
     * Sets the type of this DisplayMessage
     * 
     * @param typeIn
     *            The type to set this DisplayMessage's type to.
     */
    public void setType(int typeIn)
    {
        type = typeIn;
    }

    /**
     * Returns the DisplayMessage type of this DisplayMessage.
     * 
     * @return The type of this DisplayMessage.
     */
    public int getType()
    {
        return type;
    }

    /**
     * Sets the display time of this DisplayMessage
     * 
     * @param timeIn
     *            The length of time display this DispalyMessage for.
     */
    public void setDisplayTime(long timeIn)
    {
        displayTime = timeIn;
    }

    /**
     * Returns the length of time to display this message for.
     * 
     * @return The display time of this DisplayMessage.
     */
    public long getDisplayTime()
    {
        return displayTime;
    }

    /**
     * Returns the colour to display this message in, based on this
     * DisplayMessage's type.
     * 
     * @return The display coloud for this DisplayMessage.
     */
    public Color getColour()
    {
        // Returns the corresponding colour for each type:
        switch (getType())
        {
            case GAME_STATUS_MESSAGE:
                return GAME_STATUS_COLOUR;
            case ERROR_MESSAGE:
                return ERROR_COLOUR;
            case WARNING_MESSAGE:
                return WARNING_COLOUR;
            case GLOBAL_CHAT_MESSAGE:
                return GLOBAL_CHAT_COLOUR;
            case GLOBAL_CHAT_FROM_PLAYER_MESSAGE:
                return GLOBAL_CHAT_FROM_PLAYER_COLOUR;
            case PRIVATE_CHAT_TO_PLAYER_MESSAGE:
                return PRIVATE_CHAT_TO_PLAYER_COLOUR;
            case PRIVATE_CHAT_FROM_PLAYER_MESSAGE:
                return PRIVATE_CHAT_FROM_PLAYER_COLOUR;
        }
        return Color.WHITE; // Last resort.
    }

    /**
     * Returns a String representation of this DisplayMessage.
     * 
     * @return A String representation of this DisplayMessage.
     */
    public String toString()
    {
        return "Type: " + getType() + " Text: " + getText() + " Display Time: " + getDisplayTime();
    }
}