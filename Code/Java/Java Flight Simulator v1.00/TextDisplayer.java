import java.util.LinkedList;

/**
 * This class is used to manage a LinkedList which serves as a DisplayMessage
 * queue.
 * 
 * @author James Attenborough
 */
public class TextDisplayer
{
    private LinkedList queue; // The queue of DisplayMessage objects.

    /**
     * The TextDisplayer constructor.
     */
    public TextDisplayer()
    {
        // Initialize the LinkedList queue:
        queue = new LinkedList();
    }

    /**
     * Adds a message to the TextDisplayer DisplayMessage queue.
     * 
     * @param messageIn
     *            The DisplayMessage to add to the queue.
     */
    public void output(DisplayMessage messageIn)
    {
        synchronized (queue)
        {
            queue.add(messageIn);
        }
    }

    /**
     * Returns the DisplayMessage at the front of the queue.
     * 
     * @return The next DisplayMessage in the queue.
     */
    public DisplayMessage getNextMessage()
    {
        if (getNumMessages() > 0)
        {
            // This action must be synchronized, as calls are made to this
            // method every time a frame is rendered. Because it is being called
            // so often and working so quickly, it is essential that we do not
            // do anything like remove the same message at the same time.
            synchronized (queue)
            {
                return ((DisplayMessage) queue.removeFirst());
            }
        }
        return null;
    }

    /**
     * Returns the number of messages in the queue.
     * 
     * @return The number of DisplayMessage objects in the queue.
     */
    public int getNumMessages()
    {
        return queue.size();
    }

    /**
     * Returns a String representation of this class.
     * 
     * @return A String that represents this class.
     */
    public String toString()
    {
        return "Num Mesage: " + getNumMessages();
    }
}