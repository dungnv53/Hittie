/**
 * This interface is used to keep track of network attributes common to all
 * network users.
 * 
 * @author James Attenborough
 */
public interface GameProtocol
{
    /**
     * The port on which the game operates.
     */
    public static final int PORT = 1337;

    /**
     * The identifier that signals that the game is over.
     */
    public static final int GAME_OVER = 0;

    /**
     * The identifier that signals that a player has one.
     */
    public static final int GAME_WINNER = 1;

    /**
     * The identifier that signals that a client must be added to the game.
     */
    public static final int ADD_CLIENT = 2;

    /**
     * The identifier that signals that a client must be removed from the game.
     */
    public static final int REMOVE_CLIENT = 3;

    /**
     * The identifier that signals that a client must be updated.
     */
    public static final int UPDATE_CLIENT = 4;

    /**
     * The identifier that signals that a client's player must be killed.
     */
    public static final int KILL_CLIENT = 5;

    /**
     * The identifier that signals that a global chat message has been sent or
     * received.
     */
    public static final int CHAT_GLOBAL = 6;

    /**
     * The identifier that signals that a private chat message has been sent or
     * received.
     */
    public static final int CHAT_PLAYER = 7;

    /**
     * Used to keep track of the expected number of parts to a GAME_WINNER
     * message.
     */
    public static final int GAME_WINNER_PARTS = 2;

    /**
     * Used to keep track of the expected number of parts to an ADD_CLIENT
     * message.
     */
    public static final int ADD_CLIENT_PARTS = 9;

    /**
     * Used to keep track of the expected number of parts to an UPDATE_CLIENT
     * message.
     */
    public static final int UPDATE_CLIENT_PARTS = 4;

    /**
     * Used to keep track of the expected number of parts to a CHAT_GLOBAT
     * message.
     */
    public static final int CHAT_GLOBAL_PARTS = 2;

    /**
     * Used to keep track of the expected number of parts to a CHAT_PLAYER
     * message.
     */
    public static final int CHAT_PLAYER_PARTS = 3;

    /**
     * The String that separates the protocol identifier from the rest of the
     * message.
     */
    public static final String PROTOCOL_SEPARATOR = "|";

    /**
     * The String that separates the parts of the message.
     */
    public static final String PARTS_SEPARATOR = ";";

    /**
     * The String that separates the parts of certain parts of the message.
     */
    public static final String FINER_PARTS_SEPARATOR = ",";

}