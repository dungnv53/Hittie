import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.MemoryImageSource;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.J3DGraphics2D;
import javax.media.j3d.PickPoint;
import javax.media.j3d.PickSegment;
import javax.media.j3d.SceneGraphPath;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.media.j3d.WakeupOr;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 * The FlightGamePanel is an extension of the JPanel class, and is responsible
 * for managing the input, graphics, sound and networking of the game.
 * 
 * @author James Attenborough
 */
public class FlightGamePanel extends JPanel implements GameProtocol
{
    private Runtime runtime; // The Runtime object for this application. This is
    // used to obtain dynamic information about the Java Virtual Machine.

    private FlightDisplay canvas; // The FlightDisplay for this game. This is
    // used to display all the flight information during the game.
    private SimpleUniverse universe; // The SimpleUniverse is a class used to
    // easily create a Universe, without having to deal with Locales and other
    // things like that.
    private ViewingPlatform viewingPlatform; // The ViewingPlatform for this
    // class.

    // This vector represents the direction North in this game:
    private final Vector3d NORTH = new Vector3d(0, 0, -1);

    private int viewState; // This is used to hold the current view flag.

    // These flags are used to signal which view is being used:
    private static final int VIEW_FIRST_PERSON = 0; // View from inside the
    // cockpit.
    private static final int VIEW_THIRD_PERSON = 1; // View of the plane from
    // just outside it.
    private static final int VIEW_AERIAL = 2; // View of the plane from above.
    private static final int VIEW_FRONT = 3; // View of the plane from the
    // front.
    private static final int VIEW_STILL = 4; // The camera stays in it's
    // previous position and only the plane moves.
    private static final int VIEW_ENEMY = 5; // View of the plane from the
    // nearest enemy plane in the game, or if there are no enemies, then
    // the camera is position at a point that is the player's location with 20
    // added to it's x, y and z componenents.

    // These menu commands are added to an array so that they may be cycled
    // through by simply incrementing the selected integer value:
    private static final String MENU_CONTINUE = "Continue";
    private static final String MENU_EXIT = "Exit";
    private final String[] menuItems = { MENU_CONTINUE, MENU_EXIT };
    private int selected = 0;

    // Keeps track of whether the console is being used:
    private boolean console = false;
    // Keeps track of whether the menu is being used:
    private boolean menu = false;

    private World world; // The World object used in this game.

    // These keep track of the bearing, pitch and roll of this user's Plane:
    private double playerBearing = 0;
    private double playerPitch = 0;

    private int fps = 30; // The number of frames that are rendered each second.
    // To avoid the non-congruent transform that will result from this not being
    // set to an acceptable value during the first second of operation (FPS is
    // calculated at the end of the game rendering loop), an initial value of 30
    // FPS is set.

    // These variables keep track of the memory statistics of the game (the
    // total memory of the system, the free memory of the system, the amount
    // being used by this application, and the usage percentage which is
    // calculated using the other three variables):
    private long total, free, used, usage;

    // This holds the text being typed into the console:
    private String command = new String();

    // The number of bullets fired by this player that have hit:
    private int hits = 0;
    // The number of enemies shot down by this player:
    private int kills = 0;

    // The SoundPlayer which manages and controls all sounds in the game:
    private SoundPlayer gameSoundPlayer;

    // These are used to store the key for each sound, which can then be called
    // using the SoundPlayer of this class:
    private final int ENGINE; // The key for the engine sound.
    private final int GUN_FIRE; // The key for the gun firing sound.
    private final int EXPLOSION; // The key for the explosion.

    private final String DIGITS = "[0-9]{1,}"; // A regular expression that only
    // allows numbers to be passed to certain console commands.

    private Vector planes = new Vector(); // All the Planes in the game.
    private Vector planeBranchGroups = new Vector(); // The BranchGroups which
    // hold the Planes and their TransformGroups.
    private Vector planeTransformGroups = new Vector(); // The TransformGroup
    // of each Plane.
    private Vector planeTransforms = new Vector(); // The Transform3D for each
    // Plane.
    private Vector transform3DInitial = new Vector(); // Hold a TransformGroup
    // of each Plane.
    private Vector transformGroupInitial = new Vector(); // Holds a rotation
    // Transform3D for each plane.

    // This keeps track of whether you are playing a local or a network game:
    private final boolean LOCAL;
    // This keeps track of whether you run the game as the server or as a
    // client:
    private final boolean HOST;

    private final int PLAYER = 0; // This is just use to make the code more
    // readable. This player's Plane is stored at index 0 in the planes Vectors,
    // but it is much easier to make out the meaning of the code when
    // ((Plane)planes.get(PLAYER) is used to access this player's Plane instead
    // of ((Plane)planes.get(0)).

    // These two variables record how many bytes have been sent and received per
    // second during a network game:
    private int sentBytes = 0, receivedBytes = 0;
    private double sentKBPS = 0, receivedKBPS = 0;

    // The length of the message to send:
    private int sendLength;
    // The ByteBuffer that stores the length of the message to be sent:
    private ByteBuffer sendLengthBuffer;
    // The ByteBuffer that stores the message to be sent:
    private ByteBuffer sendTextBuffer;
    // This ByteBuffer array holds the sendLengthBuffer and the sentTextBuffer:
    private ByteBuffer[] output = new ByteBuffer[2];

    private Selector selector; // Used to select certain channels for reading
    // and writing.
    private ServerSocketChannel serverSocketChannel; // The ServerSocketChannel
    // used when running this game as a server.
    private ServerSocket serverSocket; // The ServerSocket associated with
    // running this game as a server.

    private Vector clientSocketChannels; // This Vector is used to hold the
    // SocketChannel object for each client that joins the game, so that
    // messages may be sent to clients.

    private int incomingLength; // The length of the message that is being
    // received.
    private ByteBuffer incomingLengthInBytes; // The ByteBuffer for receiving
    // the length of the incoming message.
    private ByteBuffer incomingData; // The ByteBuffer for receiving the
    // incoming message.
    private String incomingMessage; // The latest message to have been received.
    private String[] parts; // The parts of the incoming message when it has
    // been split.

    private int numChannelRequests; // The number of channels that have sent a
    // request to the server.
    private Iterator channelIterator; // The Iterator for the all the channels
    // that have made a request.
    private SelectionKey selectionKey; // The SelectionKey used by the server to
    // find the operations requested by a channel.

    private SocketChannel socketChannel; // The SocketChannel that a client
    // uses to communicate with the server.

    private boolean sent; // Keeps track of whether a message was successfully
    // sent or not.

    private ServerSocketChannel clientChannel; // This is used when connecting a
    // client to the game.
    private SocketChannel clientSocket; // This is the SocketChannel of the
    // client that is presently communicating with the server.

    // These are used when breaking a message down into the required parts:
    private String clientName; // The name of the client that sent the message.
    private String[] addClientLocationParts; // Used to split the location
    // String of the client to be added.
    private String[] addClientRotationParts; // Used to split the rotation
    // String of the client to be added.
    private String addClientModel = new String(); // The model name of the
    // client to be added.
    private String addClientTexture = new String(); // The texture filename of
    // the client to be added.
    private double addClientPitchConstant = 0; // The Pitch Constant of the
    // client to be added.
    private double addClientRollConstant = 0; // The Roll Constant of the client
    // to be added.
    private double addClientSpeedConstant = 0; // The Speed Constant of the
    // client to be added.
    private Point3d addClientStart = new Point3d(); // The location of the
    // client to be added.
    private Matrix3d addClientRotation = new Matrix3d(); // The rotation of the
    // client to be added.
    private int addClientLevel = 0; // The level of the client to be added.
    private String[] updateClientLocationParts; // Used to split the new
    // location String of the client to be updated.
    private String[] updateClientRotationParts; // Used to split the new
    // rotation String of the client to be updated.
    private int updateClientHealth; // The new health value of the client to be
    // updated.
    private Point3d updateClientLocation = new Point3d(); // The new location of
    // the client to be updated.
    private Matrix3d updateClientRotation = new Matrix3d(); // The new rotation
    // of
    // the client to be updated.

    private final int MAX_DISPLAY_RANGE = 200; // Planes within this range are
    // displayed on the radar map.
    private final int MAX_AI_RANGE = 250; // AI Planes that have any other Plane
    // within this range of them will start to make movement decisions.
    private final int MAX_SHOOT_DISTANCE = 400; // This is the maximum distance
    // that a bullet can travel in
    // the game.

    // These are the console commands that are recognized. A future development
    // is to make many more of these, to increase the use of the console.
    private final String ENEMY_COMMAND = "addenemy "; // This command add's an
    // enemy to the game. The trailing space is there so that we can look for an
    // index of " " when trying to find the level that is passed to this
    // command.
    private final String PLAYERS_COMMAND = "players"; // This command outputs
    // all the players and their health values to the TextDisplayer.
    private final String EXIT_COMMAND = "exit"; // This command exits the game
    // (prompts for confirmation first).
    private final String GLOBAL_COMMAND = "global "; // This command sends the
    // text message that follows it to all clients in the game.
    private final String SEND_COMMAND = "send "; // This command sends, to the
    // client specified directly after it, the text message that follows the
    // client's name.

    private TextDisplayer textDisplayer; // This is the TextDisplayer for this
    // game. It queues any output messages, which are then read in and
    // displayed.

    private boolean sentToUser; // This boolean keeps track of whether a chat
    // message was successfully sent to the correct user or not.
    private boolean sentToName;// This boolean keeps track of whether a network
    // message was sent to the user with the specified name or not. This is only
    // private to avoid unnecessary memory usage during a method which is called
    // very often.

    private boolean displayVerbose = true; // Whether or not to display all the
    // game information on the screen while flying.
    private boolean displayHealth = true; // Whether or not to display the
    // health bar on the screen while flying.
    private boolean displayMap = true; // Whether or not to display the
    // radar map on the screen while flying.

    private int aiCounter = 0; // Used to count the number of AI enemies that
    // have been in the game at any stage.

    // This is the filename of the Texture which is used on all AI enemy Planes:
    private String aiTextureFilename;

    /**
     * The Solo Flight constructor for a game.
     * 
     * @param playerIn
     *            The player's Plane.
     * @param aiTextureIn
     *            The Texture filename for AI enemies.
     * @param aiEnemiesStartIn
     *            The number of AI enemy Planes to start the game with.
     * @param worldIn
     *            The World in which the game takes place.
     * @param crosshairIn
     *            The crosshair Image to use.
     * @param canvasOutputColourIn
     *            The colour to use for text output.
     * @param gcIn
     *            The GraphicsConfiguration to run the game with.
     * @param boundsIn
     *            The screen bounds.
     */
    public FlightGamePanel(Plane playerIn, String aiTextureIn, int aiEnemiesStartIn, World worldIn, Image crosshairIn,
            Color canvasOutputColourIn, GraphicsConfiguration gcIn, Rectangle boundsIn)
    {
        super(); // Call the JPanel constructor.

        runtime = Runtime.getRuntime(); // Retrieve the Runtime for this
        // application.

        setAITextureFilename(aiTextureIn); // Set the AI texture filename.

        // Iniitialize the TextDisplayer for this game, and output a welcome
        // message:
        textDisplayer = new TextDisplayer();
        textDisplayer.output((new DisplayMessage(DisplayMessage.GAME_STATUS_MESSAGE, "Welcome to Flight Simulation by James Attenborough",
                DisplayMessage.STANDARD_DISPLAY_TIME)));

        LOCAL = true; // This is a local game.
        HOST = true; // Inconsiquential, but neccessary, to avoid any
        // NullPointerExceptions that could happen.

        setWorld(worldIn); // Set the World to the one passed to the
        // constructor.
        // Initialize the player to the Plane passed to this constructor:
        initializePlayer(playerIn);

        // Initialize the SoundPlayer, and load the sounds, each time storing
        // the key as a final integer:
        gameSoundPlayer = new SoundPlayer();
        GUN_FIRE = gameSoundPlayer.loadSound("sound//fire.wav");
        ENGINE = gameSoundPlayer.loadSound("sound//engine.wav");
        EXPLOSION = gameSoundPlayer.loadSound("sound//explode.wav");

        // Initialize the graphics elements of the game:
        initializeGraphics(gcIn, boundsIn, crosshairIn, canvasOutputColourIn);

        // Add the initial AI Enemies:
        if (aiEnemiesStartIn > 0)
        {
            for (int i = 0; i < aiEnemiesStartIn; i++)
            {
                // Add an AI Enemy using a random skill level between
                // Plane.MIN_LEVEL and Plane.MAX_LEVEL:
                addAIEnemy(Plane.MIN_LEVEL + (int) Math.round(Math.random() * (Plane.MAX_LEVEL - Plane.MIN_LEVEL)));
            }
        }
    }

    /**
     * The Network Dogfight constructor for a game.
     * 
     * @param hostNameIn
     *            The name of the host. NB: If you are hosting the game, then
     *            make this your own name.
     * @param playerIn
     *            The player's Plane.
     * @param aiTextureIn
     *            The Texture filename for AI enemies.
     * @param worldIn
     *            The World in which the game takes place.
     * @param crosshairIn
     *            The crosshair Image to use.
     * @param canvasOutputColourIn
     *            The colour to use for text output.
     * @param gcIn
     *            The GraphicsConfiguration to run the game with.
     * @param boundsIn
     *            The screen bounds.
     */
    public FlightGamePanel(String hostNameIn, Plane playerIn, String aiTextureIn, World worldIn, Image crosshairIn,
            Color canvasOutputColourIn, GraphicsConfiguration gcIn, Rectangle boundsIn)
    {
        super(); // Call the JPanel constructor.

        runtime = Runtime.getRuntime(); // Retrieve the Runtime for this
        // application.

        setAITextureFilename(aiTextureIn); // Set the AI texture filename.

        // Iniitialize the TextDisplayer for this game, and output a welcome
        // message:
        textDisplayer = new TextDisplayer();
        textDisplayer.output((new DisplayMessage(DisplayMessage.GAME_STATUS_MESSAGE, "Welcome to Flight Simulation by James Attenborough",
                DisplayMessage.STANDARD_DISPLAY_TIME)));

        LOCAL = false;

        setWorld(worldIn); // Set the World to the one passed to the
        // constructor.
        // Initialize the player to the Plane passed to this constructor:
        initializePlayer(playerIn);

        // If the player's name is the same as the host's name, then the player
        // is playing as the host:
        if (playerIn.getName().equalsIgnoreCase(hostNameIn))
        {
            HOST = true;
            // We only initialize the networking features needed by the host:
            initializeServerNetworking();
        }
        else
        {
            HOST = false;
            // We only initialize the networking features needed by the client:
            initializeClientNetworking(hostNameIn);
        }

        // Initialize the SoundPlayer, and load the sounds, each time storing
        // the key as a final integer:
        gameSoundPlayer = new SoundPlayer();
        GUN_FIRE = gameSoundPlayer.loadSound("sound//fire.wav");
        ENGINE = gameSoundPlayer.loadSound("sound//engine.wav");
        EXPLOSION = gameSoundPlayer.loadSound("sound//explode.wav");

        // Initialize the graphics elements of the game:
        initializeGraphics(gcIn, boundsIn, crosshairIn, canvasOutputColourIn);
    }

    /**
     * Sets the World that this game is set in to the one passed to this method.
     * 
     * @param worldIn
     *            The World to play in.
     */
    private void setWorld(World worldIn)
    {
        world = worldIn;
    }

    /**
     * Returns the World that you are currently flying in.
     * 
     * @return The World in which this game takes place.
     */
    public World getWorld()
    {
        return world;
    }

    /**
     * Initializes this user's player.
     * 
     * @param planeIn
     *            The Plane to initialize this user's player to.
     */
    private void initializePlayer(Plane planeIn)
    {
        // The addPlane(Plane planeIn) method could be used instead of all this,
        // but it is essential that the player's plane is stored at a certain
        // index, and to avoid having to write another addPlane method, this was
        // the best solution.

        // Add this player to the Planes Vector, at the PLAYER index (i.e.,
        // index 0). Once again, I use the final int PLAYER for any operations
        // on the user's Plane, because it makes the code easier to understand,
        // when people read PLAYER instead of 0:
        planes.add(PLAYER, planeIn);

        //Use this static PickTool method to set the intersection capabilities
        // of this plane:
        PickTool.setCapabilities(((Plane) planes.get(PLAYER)), PickTool.INTERSECT_FULL);

        // Set the appropriate bounds and capabilities:
        ((Plane) planes.get(PLAYER)).setCapability(Shape3D.ALLOW_BOUNDS_READ);
        ((Plane) planes.get(PLAYER)).setCapability(Shape3D.ALLOW_BOUNDS_WRITE);
        ((Plane) planes.get(PLAYER)).setBounds(new BoundingSphere(((Plane) planes.get(PLAYER)).getLocation(), 1));

        // Initialize the player's TransformGroup, Transform3D and BranchGroup,
        // and initial Transform3D and TransformGroup:
        planeTransformGroups.add(PLAYER, new TransformGroup());
        planeTransforms.add(PLAYER, new Transform3D());
        planeBranchGroups.add(PLAYER, new BranchGroup());
        transform3DInitial.add(PLAYER, new Transform3D());
        transformGroupInitial.add(PLAYER, new TransformGroup());

        // Set the capabilities of the TransformGroup and create the appropriate
        // parent structure for this object:
        ((TransformGroup) planeTransformGroups.get(PLAYER)).setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        ((TransformGroup) planeTransformGroups.get(PLAYER)).setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        ((TransformGroup) planeTransformGroups.get(PLAYER)).setTransform(((Transform3D) planeTransforms.get(PLAYER)));
        ((TransformGroup) planeTransformGroups.get(PLAYER)).addChild(((Plane) planes.get(PLAYER)));
        ((BranchGroup) planeBranchGroups.get(PLAYER)).setCapability(BranchGroup.ALLOW_DETACH);
        ((BranchGroup) planeBranchGroups.get(PLAYER)).addChild(((TransformGroup) planeTransformGroups.get(PLAYER)));

        // Finally, add the Plane to the World:
        world.addChild(((BranchGroup) planeBranchGroups.get(PLAYER)));
    }

    /**
     * Sets the AI Texture filename for this game.
     * 
     * @param textureIn
     *            The filename of the Texture to be used for all AI enemies.
     */
    private void setAITextureFilename(String textureIn)
    {
        aiTextureFilename = textureIn;
    }

    /**
     * Returns the filename of the Texture used for all AI Enemy Planes.
     * 
     * @return The filename of the Texture.
     */
    public String getAITextureFilename()
    {
        return aiTextureFilename;
    }

    /**
     * Initializes the networking components needed by the host user.
     */
    private void initializeServerNetworking()
    {
        try
        {
            // Initialize the Selector used for channel selection:
            selector = Selector.open();
            clientSocketChannels = new Vector(); // Initialize the Vector used
            // to hold the client SocketChannel objects.
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false); // This is a
            // non-blocking server, i.e., it does not have to wait around for
            // connections all the time.
            serverSocket = serverSocketChannel.socket();
            serverSocket.bind(new InetSocketAddress(PORT)); // Bind the
            // ServerSocket to the port specified in the GameProtocol.
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); // Register
            // for OP_ACCEPTs.
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);

            // If there was an error creating the server components, then tell
            // the user in-game:
            textDisplayer.output((new DisplayMessage(DisplayMessage.ERROR_MESSAGE, "The server could not be started.",
                    DisplayMessage.STANDARD_DISPLAY_TIME)));
        }
    }

    /**
     * Initializes the networking components needed by a client.
     * 
     * @param nameIn
     *            The computer name of the host to connect to.
     */
    private void initializeClientNetworking(String nameIn)
    {
        try
        {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(nameIn, PORT)); // Connect
            // to the specified host on the port that is listed in the
            // GameProtocol.

            // After connecting, send an ADD_CLIENT message to the host so that
            // he can add you to his game, and then send your details to the
            // other players on the network.
            send(socketChannel, (ADD_CLIENT + PROTOCOL_SEPARATOR + ((Plane) planes.get(PLAYER)).getStartString()));
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);

            // If there was an error creating the connecting to the host, then
            // tell the user in-game:
            textDisplayer.output((new DisplayMessage(DisplayMessage.ERROR_MESSAGE, "The host \"" + nameIn + "\" could not be found.",
                    DisplayMessage.STANDARD_DISPLAY_TIME)));
        }
    }

    /**
     * Initializes the graphics components needed for the game.
     * 
     * @param gcIn
     *            The GraphicsConfiguration to use.
     * @param boundsIn
     *            The screen bounds.
     * @param crosshairIn
     *            The crosshair Image to use.
     * @param colourIn
     *            The colour to use to display text during the game.
     */
    private void initializeGraphics(GraphicsConfiguration gcIn, Rectangle boundsIn, Image crosshairIn, Color colourIn)
    {
        // This creates a transparent Cursor:
        int[] pixels = new int[16 * 16]; // We need a 16 by 16 pixel Cursor.
        Image image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, pixels, 0, 16)); // The
        // scan value for the MemoryImageSource value is 16, which means that
        // the space between the rows is same as the height of the Cursor, so
        // basically, all of the Cursor's pixels are set to be transparent.
        Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0), "invisiblecursor"); // Create
        // the Cursor using our transparent 16 by 16 square Image.
        setCursor(cursor); // Set the transparent cursor.

        // Initialize the FlightDisplay object for this game.
        canvas = new FlightDisplay(gcIn, boundsIn, crosshairIn, colourIn);
        canvas.setFocusable(true); // Request focus for the canvas.
        canvas.requestFocus();

        this.setLayout(new BorderLayout());
        this.add("Center", canvas); // Add the canvas.

        // Initialize the SimpleUniverse and ViewingPlatform for the game:
        universe = new SimpleUniverse(canvas);
        viewingPlatform = universe.getViewingPlatform();
        universe.getViewer().getView().setBackClipDistance(600);
        viewingPlatform.setNominalViewingTransform();

        // Add a new Navigator behaviour, passing the FlightDisplay to it, to
        // the World.
        world.addChild(new Navigator(canvas));

        // Add the World to the universe:
        universe.addBranchGraph(world);
    }

    /**
     * This method executes the console command passed to it.
     * 
     * @param commandIn
     *            The console command to execute.
     */
    private void handleConsoleCommand(String commandIn)
    {
        boolean closing = false; // Whether or not this game is closing.

        if (commandIn.startsWith(EXIT_COMMAND))
        {
            // Promt the user to confirm an exit request:

            int choice = JOptionPane.showConfirmDialog(new JPanel(), "Do you really want to quit", "Confirm quit",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION)
            {
                closing = true;
            }
        }
        else
            if (commandIn.startsWith(ENEMY_COMMAND))
            {
                // This command add's an AI Enemy with the specified AI level:

                // If the AI level is invalid, then the AI Enemy is not added,
                // and the user is given an in-gam DisplayMessage error message.

                if (HOST)
                {
                    if (commandIn.substring(commandIn.lastIndexOf(" ") + 1, commandIn.length()).matches(DIGITS))
                    {
                        int levelIn = Integer.parseInt(commandIn.substring(commandIn.lastIndexOf(" ") + 1, commandIn.length()));
                        if ((levelIn >= Plane.MIN_LEVEL) && (levelIn <= Plane.MAX_LEVEL))
                        {
                            addAIEnemy(levelIn);
                            textDisplayer.output(new DisplayMessage(DisplayMessage.GAME_STATUS_MESSAGE, ((Plane) planes
                                    .get(planes.size() - 1)).getName()
                                    + " has joined the game.", DisplayMessage.STANDARD_DISPLAY_TIME));
                        }
                        else
                            textDisplayer.output(new DisplayMessage(DisplayMessage.ERROR_MESSAGE,
                                    "That is not a valid AI level. Please input a level between " + Plane.MIN_LEVEL + " and "
                                            + Plane.MAX_LEVEL + ".", DisplayMessage.STANDARD_DISPLAY_TIME));
                    }
                    else
                        textDisplayer.output(new DisplayMessage(DisplayMessage.ERROR_MESSAGE,
                                "That is not a valid AI level. Please only input numerical values for the AI level.",
                                DisplayMessage.STANDARD_DISPLAY_TIME));

                }
                else
                    textDisplayer.output(new DisplayMessage(DisplayMessage.ERROR_MESSAGE, "Only the game host may add AI enemies.",
                            DisplayMessage.STANDARD_DISPLAY_TIME));
            }
            else
                if (commandIn.startsWith(PLAYERS_COMMAND))
                {
                    // This command goes through the Vector of planes and
                    // outputs each one's name and health value to the
                    // TextDisplayer:

                    for (int i = 0; i < planes.size(); i++)
                        textDisplayer.output(new DisplayMessage(DisplayMessage.GAME_STATUS_MESSAGE, ((Plane) planes.get(i)).getName()
                                .toUpperCase()
                                + " (Health: " + ((Plane) planes.get(i)).getHealth() + ")", DisplayMessage.STANDARD_DISPLAY_TIME));
                }
                else
                    if (commandIn.startsWith(GLOBAL_COMMAND))
                    {
                        // You can only chat in a network game:
                        if (!LOCAL)
                        {
                            if (HOST)
                            {
                                // As the host you can just broadcast your chat
                                // message:
                                broadcast((CHAT_GLOBAL + PROTOCOL_SEPARATOR + ((Plane) planes.get(PLAYER)).getName() + PARTS_SEPARATOR + commandIn
                                        .substring(GLOBAL_COMMAND.length())));
                            }
                            else
                            {
                                // As a clientyou have to piggy-back off the
                                // server, and get it to broadcast it for you:
                                send(socketChannel, (CHAT_GLOBAL + PROTOCOL_SEPARATOR + ((Plane) planes.get(PLAYER)).getName()
                                        + PARTS_SEPARATOR + commandIn.substring(GLOBAL_COMMAND.length())));
                            }
                            textDisplayer.output(new DisplayMessage(DisplayMessage.GLOBAL_CHAT_FROM_PLAYER_MESSAGE, "(GLOBAL) "
                                    + ((Plane) planes.get(PLAYER)).getName() + ": " + commandIn.substring(GLOBAL_COMMAND.length()),
                                    DisplayMessage.STANDARD_DISPLAY_TIME));
                        }
                        else
                            textDisplayer.output(new DisplayMessage(DisplayMessage.ERROR_MESSAGE,
                                    "You are playing a local game. There is nobody to send a message to.",
                                    DisplayMessage.STANDARD_DISPLAY_TIME));
                    }
                    else
                        if (commandIn.startsWith(SEND_COMMAND))
                        {
                            // This is the same as the global chat command
                            // handling above, except that here a boolean is
                            // kept which tracks whether or not the message was
                            // sent successfully:

                            if (!LOCAL)
                            {
                                sentToUser = false;

                                if (HOST)
                                {
                                    sentToUser = send(commandIn.substring(commandIn.indexOf(" ") + 1, SEND_COMMAND.length()
                                            + commandIn.substring(commandIn.indexOf(" ") + 1).indexOf(" ")),
                                            (CHAT_PLAYER
                                                    + PROTOCOL_SEPARATOR
                                                    + ((Plane) planes.get(PLAYER)).getName()
                                                    + PARTS_SEPARATOR
                                                    + commandIn.substring(commandIn.indexOf(" ") + 1, SEND_COMMAND.length()
                                                            + commandIn.substring(commandIn.indexOf(" ") + 1).indexOf(" "))
                                                    + PARTS_SEPARATOR + commandIn.substring((SEND_COMMAND.length() + 1)
                                                    + commandIn.substring(commandIn.indexOf(" ") + 1).indexOf(" "))));
                                }
                                else
                                {
                                    sentToUser = send(socketChannel,
                                            (CHAT_PLAYER
                                                    + PROTOCOL_SEPARATOR
                                                    + ((Plane) planes.get(PLAYER)).getName()
                                                    + PARTS_SEPARATOR
                                                    + commandIn.substring(commandIn.indexOf(" ") + 1, SEND_COMMAND.length()
                                                            + commandIn.substring(commandIn.indexOf(" ") + 1).indexOf(" "))
                                                    + PARTS_SEPARATOR + commandIn.substring((SEND_COMMAND.length() + 1)
                                                    + commandIn.substring(commandIn.indexOf(" ") + 1).indexOf(" "))));
                                }

                                if (sentToUser)
                                    textDisplayer.output(new DisplayMessage(DisplayMessage.PRIVATE_CHAT_FROM_PLAYER_MESSAGE, "(PRIVATE TO "
                                            + commandIn.substring(commandIn.indexOf(" ") + 1, SEND_COMMAND.length()
                                                    + commandIn.substring(commandIn.indexOf(" ") + 1).indexOf(" "))
                                            + ") "
                                            + ((Plane) planes.get(PLAYER)).getName()
                                            + ": "
                                            + commandIn.substring((SEND_COMMAND.length() + 1)
                                                    + commandIn.substring(commandIn.indexOf(" ") + 1).indexOf(" ")),
                                            DisplayMessage.STANDARD_DISPLAY_TIME));
                                else
                                    textDisplayer.output(new DisplayMessage(DisplayMessage.ERROR_MESSAGE,
                                            "Sorry, the message was not sent." , DisplayMessage.STANDARD_DISPLAY_TIME));
                            }
                            else
                                textDisplayer.output(new DisplayMessage(DisplayMessage.ERROR_MESSAGE,
                                        "You are playing a local game. There is nobody to send a message to.",
                                        DisplayMessage.STANDARD_DISPLAY_TIME));
                        }
                        else
                            if (!(commandIn.length() == 0))
                                textDisplayer.output(new DisplayMessage(DisplayMessage.ERROR_MESSAGE, "That command is not recognized.",
                                        DisplayMessage.STANDARD_DISPLAY_TIME));

        if (closing)
            close(); // Call the close () method.
        else
            if (LOCAL)
            {
                if (((Plane) planes.get(PLAYER)).isAlive())
                    gameSoundPlayer.loopSound(ENGINE);

                world.animateFires(true);
            }
    }

    /**
     * This adds and AI Enemy Plane with the specified skill/strength level to
     * the game.
     * 
     * @param levelIn
     *            The level of the AI Enemy.
     */
    private void addAIEnemy(int levelIn)
    {
        // The AI Plane's will be the same model as the host's (in local and
        // network games), except the texture for AI Enemies will be from a
        // separate file:
        addPlane(new Plane(true, "AI Enemy (ID: " + aiCounter + ", Level: " + levelIn + ")", ((Plane) planes.get(PLAYER)).getModel(),
                getAITextureFilename(), new Point3d(((Plane) planes.get(PLAYER)).getLocationX()
                        + ((Math.random() - 0.5) * MAX_DISPLAY_RANGE), ((Plane) planes.get(PLAYER)).getLocationY()
                        + ((Math.random() - 0.5) * MAX_DISPLAY_RANGE), ((Plane) planes.get(PLAYER)).getLocationZ()
                        + ((Math.random() - 0.5) * MAX_DISPLAY_RANGE)), ((Plane) planes.get(PLAYER)).PITCH_CONSTANT, ((Plane) planes
                        .get(PLAYER)).ROLL_CONSTANT, ((Plane) planes.get(PLAYER)).SPEED_CONSTANT, new Matrix3d(), levelIn));
        aiCounter++;
    }

    /**
     * Adds a Plane to the World.
     * 
     * @param planeIn
     *            The Plane to add.
     */
    private void addPlane(Plane planeIn)
    {
        int index = planes.size(); // The index at which to add the Plane.

        synchronized (planes)
        {
            planes.add(index, planeIn);
        }

        // Set the capabilities for this Plane:
        ((Plane) planes.get(index)).setCapability(Shape3D.ALLOW_BOUNDS_READ);
        ((Plane) planes.get(index)).setCapability(Shape3D.ALLOW_BOUNDS_WRITE);
        ((Plane) planes.get(index)).setBounds(new BoundingSphere(((Plane) planes.get(index)).getLocation(), 1));
        PickTool.setCapabilities(((Plane) planes.get(index)), PickTool.INTERSECT_FULL);

        // Initialize the Plane's Transform3D and TransformGroup, and set the
        // capabilities and transforms:

        synchronized (planeTransformGroups)
        {
            planeTransformGroups.add(index, new TransformGroup());
        }
        synchronized (planeTransforms)
        {
            planeTransforms.add(index, new Transform3D());
        }
        ((TransformGroup) planeTransformGroups.get(index)).setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        ((TransformGroup) planeTransformGroups.get(index)).setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        ((Transform3D) planeTransforms.get(index)).set(new Vector3d(((Plane) planes.get(index)).getLocation()));
        ((Transform3D) planeTransforms.get(index)).setRotation(((Plane) planes.get(index)).getNoseRotation());
        ((TransformGroup) planeTransformGroups.get(index)).setTransform(((Transform3D) planeTransforms.get(index)));

        ((TransformGroup) planeTransformGroups.get(index)).addChild(((Plane) planes.get(index)));

        // Initialize the BranchGroup:
        BranchGroup bg = new BranchGroup();
        bg.setCapability(BranchGroup.ALLOW_DETACH);
        bg.addChild(((TransformGroup) planeTransformGroups.get(index)));

        synchronized (planeBranchGroups)
        {
            planeBranchGroups.add(index, bg);
        }

        synchronized (transform3DInitial)
        {
            transform3DInitial.add(index, new Transform3D());
        }

        synchronized (transformGroupInitial)
        {
            transformGroupInitial.add(index, new TransformGroup());
        }

        if ((!LOCAL) && (HOST))
        {
            // Let the client's know about this new player:
            broadcast(ADD_CLIENT + PROTOCOL_SEPARATOR + ((Plane) planes.get(index)).getStartString());
        }

        // Add the new Player to the World:
        world.addChild(((BranchGroup) planeBranchGroups.get(index)));
    }

    /**
     * Removes the Plane of the player whos name is passed to this method.
     * 
     * @param nameRemove
     *            The name of the Plane to remove.
     */
    private void removePlane(String nameRemove)
    {
        loop: for (int i = 0; i < planes.size(); i++)
        {
            if (((Plane) planes.get(i)).getName().equalsIgnoreCase(nameRemove))
            {
                // Remove the plane components with the specified name:

                world.removeChild(((BranchGroup) planeBranchGroups.get(i)));

                synchronized (planes)
                {
                    planes.remove(i);
                }

                synchronized (planeTransforms)
                {
                    planeTransforms.remove(i);
                }
                synchronized (planeTransformGroups)
                {
                    ((TransformGroup) planeTransformGroups.get(i)).removeAllChildren();
                    planeTransformGroups.remove(i);
                }
                synchronized (planeBranchGroups)
                {
                    ((BranchGroup) planeBranchGroups.get(i)).removeAllChildren();
                    planeBranchGroups.remove(i);
                }

                synchronized (transform3DInitial)
                {
                    transform3DInitial.remove(i);
                }

                synchronized (transformGroupInitial)
                {
                    ((TransformGroup) transformGroupInitial.get(i)).removeAllChildren();
                    transformGroupInitial.remove(i);
                }

                if (!LOCAL)
                {
                    if ((((Plane) planes.get(PLAYER)).isAlive()) && (countNetworkEnemies() == 0))
                    {
                        // If this player is the last human player alive in a
                        // netork game, then announce you are the game winner:
                        if (HOST)
                        {
                            broadcast(GAME_WINNER + PROTOCOL_SEPARATOR + ((Plane) planes.get(PLAYER)).getName() + PARTS_SEPARATOR + kills);
                        }
                        else
                        {
                            send(socketChannel, GAME_WINNER + PROTOCOL_SEPARATOR + ((Plane) planes.get(PLAYER)).getName() + PARTS_SEPARATOR
                                    + kills);
                        }
                    }
                }
                break loop;
            }
        }
    }

    /**
     * Handles the operations of the server.
     * 
     * @param operationsIn
     *            The operations to be performed.
     */
    private void handleServerOperations(int operationsIn)
    {
        if ((SelectionKey.OP_ACCEPT & operationsIn) != 0)
        {
            // If the server receives and accept request:

            clientChannel = (ServerSocketChannel) selectionKey.channel();
            try
            {
                clientSocket = clientChannel.accept();
                clientSocket.configureBlocking(false);
                clientSocket.register(selector, SelectionKey.OP_WRITE);
                clientSocket.register(selector, SelectionKey.OP_READ);

                // Add the client's SocketChannel to the vecotr of
                // SocketChannels:
                synchronized (clientSocketChannels)
                {
                    clientSocketChannels.add(clientSocket);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace(System.err);
            }
        }

        if ((SelectionKey.OP_READ & operationsIn) != 0)
        {
            // If reading from the client:

            try
            {
                clientSocket = (SocketChannel) selectionKey.channel();
                clientSocket.configureBlocking(false);
                clientSocket.register(selector, SelectionKey.OP_WRITE);

                // Read in the incoming message using ByteBuffers:
                incomingLengthInBytes = ByteBuffer.allocate(4);
                clientSocket.read(incomingLengthInBytes);
                incomingLengthInBytes.rewind();
                incomingLength = incomingLengthInBytes.getInt();
                receivedBytes += (4 + incomingLength); // Add incoming length +
                // 5 (for incoming length ByteBuffer) to receiveBytes.

                incomingData = ByteBuffer.allocate(incomingLength);
                clientSocket.read(incomingData);
                incomingData.rewind();
                incomingMessage = incomingData.asCharBuffer().toString();

                // Split the message into it's major marts:
                parts = incomingMessage.split(PARTS_SEPARATOR);
                if (parts.length > 0)
                {
                    if (parts[0].indexOf(PROTOCOL_SEPARATOR) != -1)
                    {
                        // Handle this message:
                        handleMessageFromClient(Integer.parseInt(parts[0].substring(0, parts[0].indexOf(PROTOCOL_SEPARATOR))), parts);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace(System.err);
            }
        }

        if ((SelectionKey.OP_WRITE & operationsIn) != 0)
        {
            clientSocket = (SocketChannel) selectionKey.channel();

            try
            {
                clientSocket.configureBlocking(false);
                clientSocket.register(selector, SelectionKey.OP_READ);
            }
            catch (Exception e)
            {
                e.printStackTrace(System.err);
            }

            for (int i = 0; i < planes.size(); i++)
            {
                send(clientSocket, (UPDATE_CLIENT + PROTOCOL_SEPARATOR + ((Plane) planes.get(i)).getUpdateString()));
            }
        }
    }

    /**
     * Handles a message received from a client.
     * 
     * @param messageProtocol
     *            The type of message.
     * @param messageParts
     *            the String array of data to work with.
     */
    private void handleMessageFromClient(int messageProtocol, String[] messageParts)
    {
        // Find the name of the client:
        clientName = messageParts[0].substring(messageParts[0].indexOf(PROTOCOL_SEPARATOR) + 1);

        switch (messageProtocol)
        {
            case ADD_CLIENT:
            {
                if (messageParts.length == ADD_CLIENT_PARTS)
                {
                    addClient(clientName, messageParts);

                    client_loop: for (int i = 0; i < clientSocketChannels.size(); i++)
                    {
                        String hostName = ((SocketChannel) clientSocketChannels.get(i)).socket().getInetAddress().getHostName();

                        // Getting the host name returns entire domain name,
                        // i.e.: jamesa.mshome.net instead of jamesa, so I just
                        // lop off the domain:
                        if (((hostName.indexOf(".") != -1) && (hostName.startsWith(clientName + ".")))
                                || ((hostName.indexOf(".") == -1) && (hostName.startsWith(clientName))))
                        {
                            for (int j = 0; j < planes.size(); j++)
                            {
                                // Send the details of each Plane in the game to
                                // the new client:
                                send(((SocketChannel) clientSocketChannels.get(i)), (ADD_CLIENT + PROTOCOL_SEPARATOR + ((Plane) planes
                                        .get(j)).getStartString()));
                            }
                            break client_loop;
                        }
                    }
                }

                break;
            }
            case UPDATE_CLIENT:
            {
                if (messageParts.length == UPDATE_CLIENT_PARTS)
                {
                    // Update this specific client:
                    updateClient(clientName, messageParts);
                }

                break;
            }
            case KILL_CLIENT:
            {
                // Kill this specific client:
                killClient(clientName);

                // Broadcast it to the rest of the clients too.
                broadcast(KILL_CLIENT + PROTOCOL_SEPARATOR + clientName);

                break;
            }
            case REMOVE_CLIENT:
            {
                // Remove this specific client:
                removeClient(clientName);

                break;
            }
            case GAME_WINNER:
            {
                if (messageParts.length == GAME_WINNER_PARTS)
                {
                    // Display the game winner and broadcast the result too:
                    textDisplayer.output(new DisplayMessage(DisplayMessage.GAME_STATUS_MESSAGE, clientName + " won the game with "
                            + messageParts[1] + " kills.", DisplayMessage.STANDARD_DISPLAY_TIME));
                    broadcast(GAME_WINNER + PROTOCOL_SEPARATOR + clientName + PARTS_SEPARATOR + messageParts[1]);
                }
            }
            case CHAT_GLOBAL:
            {
                if (messageParts.length == CHAT_GLOBAL_PARTS)
                {
                    // Display the global chat message and broadcast it as well:
                    textDisplayer.output(new DisplayMessage(DisplayMessage.GLOBAL_CHAT_MESSAGE, "(GLOBAL) " + clientName + ": "
                            + messageParts[1], DisplayMessage.STANDARD_DISPLAY_TIME));
                    broadcast(CHAT_GLOBAL + PROTOCOL_SEPARATOR + clientName + PARTS_SEPARATOR + messageParts[1]);
                }

                break;
            }
            case CHAT_PLAYER:
            {
                if (messageParts.length == CHAT_PLAYER_PARTS)
                {
                    // Display the private chat message if it's for you, else
                    // pass it on to the recipient:
                    if (((Plane) planes.get(PLAYER)).getName().equalsIgnoreCase(messageParts[1]))
                        textDisplayer.output(new DisplayMessage(DisplayMessage.PRIVATE_CHAT_TO_PLAYER_MESSAGE, "(PRIVATE TO "
                                + messageParts[1] + ") " + clientName + ": " + messageParts[2], DisplayMessage.STANDARD_DISPLAY_TIME));
                    else
                        send(messageParts[1], CHAT_PLAYER + PROTOCOL_SEPARATOR + clientName + PARTS_SEPARATOR + messageParts[2]);
                }

                break;
            }
        }
    }

    /**
     * Handles the operations of the client.
     * 
     * @param operationsIn
     *            The operations to be performed.
     */
    private void handleClientOperations(int operationsIn)
    {
        if ((SelectionKey.OP_WRITE & operationsIn) != 0)
        {
            // If registered to perform a write operation, then send this
            // player's getUpdateString():
            send(socketChannel, (UPDATE_CLIENT + PROTOCOL_SEPARATOR + ((Plane) planes.get(PLAYER)).getUpdateString()));
        }

        if ((SelectionKey.OP_READ & operationsIn) != 0)
        {
            // If registered to perform a read operation:

            try
            {
                incomingLengthInBytes = ByteBuffer.allocate(4);

                socketChannel.read(incomingLengthInBytes);
                incomingLengthInBytes.rewind();
                incomingLength = incomingLengthInBytes.getInt();
                if (incomingLength > 0)
                {
                    incomingData = ByteBuffer.allocate(incomingLength);
                    socketChannel.read(incomingData);
                    receivedBytes += (4 + incomingLength); // Add 4 + the
                    // incoming length to the receivedBytes counter (extre 4 is
                    // for incomingLengthInBytes ByteBuffer)

                    incomingData.rewind();

                    incomingMessage = incomingData.asCharBuffer().toString();
                }

                // Split the incoming message into major parts:
                parts = incomingMessage.split(PARTS_SEPARATOR);
                if (parts.length > 0)
                {
                    if (parts[0].indexOf(PROTOCOL_SEPARATOR) != -1)
                    {
                        // Handle the server message:
                        handleMessageFromServer(Integer.parseInt(parts[0].substring(0, parts[0].indexOf(PROTOCOL_SEPARATOR))), parts);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace(System.err);
            }
        }
    }

    /**
     * Handles a message received from the server.
     * 
     * @param messageProtocol
     *            The type of message.
     * @param messageParts
     *            the String array of data to work with.
     */
    private void handleMessageFromServer(int messageProtocol, String[] messageParts)
    {
        // Find the name of the client in the message:
        clientName = messageParts[0].substring(messageParts[0].indexOf(PROTOCOL_SEPARATOR) + 1);

        switch (messageProtocol)
        {
            case GAME_OVER:
            {
                // Tell the user that the host has quit and the game is over:
                textDisplayer.output(new DisplayMessage(DisplayMessage.GAME_STATUS_MESSAGE, "The host, " + clientName
                        + ", has left the game. The game is over.", DisplayMessage.STANDARD_DISPLAY_TIME));

                break;
            }
            case ADD_CLIENT:
            {
                if (messageParts.length == ADD_CLIENT_PARTS)
                {
                    // Adds a client to the game:
                    addClient(clientName, messageParts);
                }

                break;
            }
            case UPDATE_CLIENT:
            {
                if (messageParts.length == UPDATE_CLIENT_PARTS)
                {
                    // Update a specific client:
                    updateClient(clientName, messageParts);
                }

                break;
            }
            case KILL_CLIENT:
            {
                // Kill a specific client:
                killClient(clientName);

                break;
            }
            case REMOVE_CLIENT:
            {
                // Remove a specific client:
                removeClient(clientName);

                break;
            }
            case GAME_WINNER:
            {
                if (messageParts.length == GAME_WINNER_PARTS)
                {
                    // Output the GAME_WINNER message:
                    textDisplayer.output(new DisplayMessage(DisplayMessage.GAME_STATUS_MESSAGE, clientName + " won the game with "
                            + messageParts[1] + " kills.", DisplayMessage.STANDARD_DISPLAY_TIME));
                }
            }
            case CHAT_GLOBAL:
            {
                if (messageParts.length == CHAT_GLOBAL_PARTS)
                {
                    // If you didn't send it (otherwise it will be displayed
                    // twice):
                    if (!(((Plane) planes.get(PLAYER)).getName().equalsIgnoreCase(clientName)))
                        textDisplayer.output(new DisplayMessage(DisplayMessage.GLOBAL_CHAT_MESSAGE, "(GLOBAL) " + clientName + ": "
                                + messageParts[1], DisplayMessage.STANDARD_DISPLAY_TIME));
                }

                break;
            }
            case CHAT_PLAYER:
            {
                if (messageParts.length == CHAT_PLAYER_PARTS)
                {
                    // If the message is meant for you, then output it:
                    if (((Plane) planes.get(PLAYER)).getName().equalsIgnoreCase(clientName))
                        textDisplayer.output(new DisplayMessage(DisplayMessage.PRIVATE_CHAT_TO_PLAYER_MESSAGE, "(PRIVATE TO "
                                + messageParts[1] + ") " + clientName + ": " + messageParts[2], DisplayMessage.STANDARD_DISPLAY_TIME));
                }
                break;
            }
        }
    }

    /**
     * @param addName
     *            The name of the client to add.
     * @param addParts
     *            The String array of data to add the client with.
     */
    private void addClient(String addName, String[] addParts)
    {
        if (!((Plane) planes.get(PLAYER)).getName().equalsIgnoreCase(addName))
        {
            // Separate all the elements...

            addClientModel = addParts[1].substring(addParts[1].lastIndexOf(PROTOCOL_SEPARATOR) + 1);
            addClientTexture = addParts[2];

            addClientPitchConstant = Double.parseDouble(addParts[3]);
            addClientRollConstant = Double.parseDouble(addParts[4]);
            addClientSpeedConstant = Double.parseDouble(addParts[5]);

            addClientLocationParts = addParts[6].split(FINER_PARTS_SEPARATOR);

            if (addClientLocationParts.length == 3)
            {
                addClientStart.x = Double.parseDouble(addClientLocationParts[0]);
                addClientStart.y = Double.parseDouble(addClientLocationParts[1]);
                addClientStart.z = Double.parseDouble(addClientLocationParts[2]);
            }

            addClientRotationParts = addParts[7].split(FINER_PARTS_SEPARATOR);

            if (addClientRotationParts.length == 9)
            {
                addClientRotation = new Matrix3d(Double.parseDouble(addClientRotationParts[0]), Double
                        .parseDouble(addClientRotationParts[1]), Double.parseDouble(addClientRotationParts[2]), Double
                        .parseDouble(addClientRotationParts[3]), Double.parseDouble(addClientRotationParts[4]), Double
                        .parseDouble(addClientRotationParts[5]), Double.parseDouble(addClientRotationParts[6]), Double
                        .parseDouble(addClientRotationParts[7]), Double.parseDouble(addClientRotationParts[8]));
            }

            addClientLevel = Integer.parseInt(addParts[8]);

            // ... and add a new Plane with the parsed values:

            addPlane(new Plane(false, addName, addClientModel, addClientTexture, addClientStart, addClientPitchConstant,
                    addClientRollConstant, addClientSpeedConstant, addClientRotation, addClientLevel));

            // Tell this player about the new arrival:
            textDisplayer.output(new DisplayMessage(DisplayMessage.GAME_STATUS_MESSAGE, addName + " joined the game.",
                    DisplayMessage.STANDARD_DISPLAY_TIME));
        }
    }

    /**
     * Updates the specified client with the data constained in the String
     * array.
     * 
     * @param updateName
     *            the name of the client to update.
     * @param updateParts
     *            The String array of the message parts.
     */
    private void updateClient(String updateName, String[] updateParts)
    {
        loop: for (int i = 0; i < planes.size(); i++)
        {
            // Find the correct plane to update:
            if (((Plane) planes.get(i)).getName().equalsIgnoreCase(updateName))
            {
                updateClientHealth = Integer.parseInt(parts[1].substring(parts[1].lastIndexOf(PROTOCOL_SEPARATOR) + 1));
                if (((Plane) planes.get(i)).getHealth() > updateClientHealth)
                    ((Plane) planes.get(i)).setHealth(updateClientHealth);

                // Network users could only change this player's health, and
                // nothing else:
                if (i != PLAYER)
                {
                    // separate everything into finer parts and then parse the
                    // values:

                    updateClientLocationParts = updateParts[2].split(FINER_PARTS_SEPARATOR);

                    if (updateClientLocationParts.length == 3)
                    {
                        updateClientLocation.x = Double.parseDouble(updateClientLocationParts[0]);
                        updateClientLocation.y = Double.parseDouble(updateClientLocationParts[1]);
                        updateClientLocation.z = Double.parseDouble(updateClientLocationParts[2]);
                    }

                    ((Plane) planes.get(i)).setLocation(updateClientLocation);

                    updateClientRotationParts = updateParts[3].split(FINER_PARTS_SEPARATOR);

                    if (updateClientRotationParts.length == 9)
                    {
                        updateClientRotation.m00 = Double.parseDouble(updateClientRotationParts[0]);
                        updateClientRotation.m01 = Double.parseDouble(updateClientRotationParts[1]);
                        updateClientRotation.m02 = Double.parseDouble(updateClientRotationParts[2]);
                        updateClientRotation.m10 = Double.parseDouble(updateClientRotationParts[3]);
                        updateClientRotation.m11 = Double.parseDouble(updateClientRotationParts[4]);
                        updateClientRotation.m12 = Double.parseDouble(updateClientRotationParts[5]);
                        updateClientRotation.m20 = Double.parseDouble(updateClientRotationParts[6]);
                        updateClientRotation.m21 = Double.parseDouble(updateClientRotationParts[7]);
                        updateClientRotation.m22 = Double.parseDouble(updateClientRotationParts[8]);
                    }

                    ((Plane) planes.get(i)).setNoseRotation(updateClientRotation);
                }
                break loop;
            }
        }
    }

    /**
     * Kills the client with the specified name.
     * 
     * @param nameIn
     *            The name of the client to kill.
     */
    private void killClient(String nameIn)
    {
        loop: for (int i = 0; i < planes.size(); i++)
        {
            if (((Plane) planes.get(i)).getName().equalsIgnoreCase(nameIn))
            {
                // Sets the plane's health to 0:
                ((Plane) planes.get(i)).setHealth(0);
                break loop;
            }
        }

        // Display the message saying who died:
        if (nameIn.equalsIgnoreCase(((Plane) planes.get(PLAYER)).getName()))
            textDisplayer.output(new DisplayMessage(DisplayMessage.GAME_STATUS_MESSAGE, "You died, " + nameIn + ".",
                    DisplayMessage.STANDARD_DISPLAY_TIME));
        else
            textDisplayer.output(new DisplayMessage(DisplayMessage.GAME_STATUS_MESSAGE, nameIn + " died.",
                    DisplayMessage.STANDARD_DISPLAY_TIME));
    }

    /**
     * Removes a client with the specified name from the game.
     * 
     * @param nameIn
     *            The name of the client to remove.
     */
    private void removeClient(String nameIn)
    {
        textDisplayer.output(new DisplayMessage(DisplayMessage.GAME_STATUS_MESSAGE, nameIn + " left the game.",
                DisplayMessage.STANDARD_DISPLAY_TIME));

        removePlane(nameIn);

        if ((!LOCAL) && (HOST))
        {
            // Perform duty as host, and let all clients know:

            removeSocketChannel(nameIn);
            broadcast(REMOVE_CLIENT + PROTOCOL_SEPARATOR + nameIn);
        }
    }

    /**
     * Returns the number of network enemies currently in this game.
     * 
     * @return The number of network enemies.
     */
    public int countNetworkEnemies()
    {
        int networkCount = 0;
        // Goes through the Planes Vector and counts the non-AI controlled
        // Planes:
        for (int i = (PLAYER + 1); i < planes.size(); i++)
        {
            if ((((Plane) planes.get(i)).isAlive()) && (!(((Plane) planes.get(i)).isAI())))
                networkCount++;
        }

        return networkCount;
    }

    /**
     * Returns the number of AI enemies in the game.
     * 
     * @return The number of AI enemies in the game.
     */
    public int countAIEnemies()
    {
        int aiCount = 0;
        // Goes through the Planes Vector and counts the AI controlled Planes:
        for (int i = (PLAYER + 1); i < planes.size(); i++)
        {
            if ((((Plane) planes.get(i)).isAlive()) && ((((Plane) planes.get(i)).isAI())))
                aiCount++;
        }

        return aiCount;
    }

    /**
     * Sends a message to the client asscoiated with the specified
     * SocketChannel.
     * 
     * @param sendSocketChannel
     *            The SocketChannel to send the message to.
     * @param toSend
     *            he String to send.
     * @return true if the message was sent successfully, otherwise false.
     */
    private boolean send(SocketChannel sendSocketChannel, String toSend)
    {
        sendLength = (toSend.length() * 2); // The length to send.

        sendLengthBuffer = ByteBuffer.allocate(4); // The ByteBuffer that sends
        // the length of the outgoing
        // message.
        sendLengthBuffer.putInt(sendLength);
        sendLengthBuffer.rewind();

        sendTextBuffer = ByteBuffer.allocate(sendLength); // The ByteBuffer for
        // the outgoing data.
        sendTextBuffer.asCharBuffer().put(toSend);

        // Add teh length and data ByteBuffers to the output ByteBuffer array:
        output[0] = sendLengthBuffer;
        output[1] = sendTextBuffer;

        try
        {
            if (sendSocketChannel.isOpen())
            {
                // Write the ByteBuffer array:
                sendSocketChannel.write(output);
                sentBytes += (4 + sendLength); // add the length of the sent
                // data, plus 4 for the length of
                // the sent length.
                sent = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
            sent = false; // Did not send.
        }

        return sent;
    }

    /**
     * Send the specified message to the client with the name passed to this
     * method.
     * 
     * @param nameIn
     *            The client's name.
     * @param toSend
     *            The message to send.
     * @return true if the message was sent to the client with the specified
     *         name, otherwise false.
     */
    private boolean send(String nameIn, String toSend)
    {
        sentToName = false;

        if ((!LOCAL) && (HOST))
        {
            sendLoop: for (int i = 0; i < clientSocketChannels.size(); i++)
            {
                // Finds the right SocketChannel:
                if ((((SocketChannel) clientSocketChannels.get(i)).socket().getInetAddress().getHostName()).equalsIgnoreCase(nameIn))
                {
                    sentToName = send(((SocketChannel) clientSocketChannels.get(i)), toSend);
                    break sendLoop;
                }
            }
        }

        return sentToName;
    }

    /**
     * Allows the host to send the specified String to all clients.
     * 
     * @param toSend
     *            The String to broadcast.
     */
    private void broadcast(String toSend)
    {
        if ((!LOCAL) && (HOST))
        {
            for (int i = 0; i < clientSocketChannels.size(); i++)
            {
                if (((SocketChannel) clientSocketChannels.get(i)).isOpen())
                {
                    // Send to each client:
                    send((SocketChannel) clientSocketChannels.get(i), toSend);
                }
            }
        }
    }

    /**
     * Removes the SocketChannel which is associated with the client whose name
     * is passed to this method.
     * 
     * @param nameIn
     *            The name of the client using this SocketChannel.
     */
    private void removeSocketChannel(String nameIn)
    {
        for (int l = 0; l < clientSocketChannels.size(); l++)
        {
            // Find the right SocketChannel in the Vector:
            if (((SocketChannel) clientSocketChannels.get(l)).socket().getInetAddress().getHostName().equalsIgnoreCase(nameIn))
            {
                synchronized (clientSocketChannels)
                {
                    try
                    {
                        // Close the SocketChannel:
                        ((SocketChannel) clientSocketChannels.get(l)).close();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace(System.err);
                    }

                    // Then remove it:
                    clientSocketChannels.remove(l);
                }
            }
        }
    }

    /**
     * This method is used to clean up the application, by closing connections
     * and things like that.
     */
    private void close()
    {
        gameSoundPlayer.pauseSound(ENGINE); // Pause the looping engine sound.
        world.animateFires(false);

        if (!LOCAL)
        {
            try
            {
                if (HOST)
                {
                    // Broadcast a GAME_OVER message:
                    broadcast(GAME_OVER + PROTOCOL_SEPARATOR + ((Plane) planes.get(PLAYER)).getName());

                    // Close the ServerSocket and Selector:
                    serverSocket.close();
                    serverSocketChannel.close();
                    selector.close();

                    // Close each SocketChannel:
                    for (int i = 0; i < clientSocketChannels.size(); i++)
                        ((SocketChannel) clientSocketChannels.get(i)).close();

                    clientSocketChannels.clear();
                }
                else
                {
                    // Tell the host to remove you from the game:
                    send(socketChannel, REMOVE_CLIENT + PROTOCOL_SEPARATOR + ((Plane) planes.get(PLAYER)).getName());
                    // Close the connection to the server:
                    socketChannel.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace(System.err);
            }
        }

        this.removeAll(); // Remove all components from this one.
        System.gc(); // Suggest a GarbgeCollection.
    }

    /**
     * The FlightDisplay is an extension of the Canvas3D class. It overrides the
     * postRender() method in the Canvas3D class, and in that method it draws
     * all the on-screen information during the game.
     * 
     * @author James Attenborough
     */
    public class FlightDisplay extends Canvas3D
    {
        private final Color ENEMY_COLOUR = Color.ORANGE; // The colour to use to
        // display enemies on the radar map.
        private Image crosshair; // The Image to use as a crosshair.
        private Color displayColour; // The colour to display text in.

        // This array of colours is used to draw the health bar. It is made up
        // of horizontal lines, and a different colour is used for each line. A
        // 3D effect if created:
        private Color[] healthColours = { new Color(255, 99, 99), new Color(248, 84, 84), new Color(240, 61, 61), new Color(229, 37, 37),
                new Color(218, 15, 15), new Color(207, 0, 0), new Color(195, 0, 0), new Color(180, 0, 0), new Color(165, 0, 0),
                new Color(152, 0, 0) };

        private Image map; // The Image to use as the background for the radar
        // map.
        private int mapX, mapY; // The x and y locations to draw the map at.
        private int mapMidX, mapMidY; // The mid point of the map (used when
        // drawing enemies around player).

        private J3DGraphics2D g; // This is used to do all the drawing.
        private MediaTracker mt; // This is used to load all the Images before
        // running the application.

        // The location of the crosshair:
        private int crosshairX;
        private int crosshairY;

        // The location of the health bar:
        private int healthX;
        private int healthY;

        // The postRender() is run so often that it would be crazy not to try
        // and globalize these variables so that a new memory position does not
        // have to be assigned every frame:
        private Point3d location; // The player's location.
        private Point3d enemy; // The location of each enemy.
        // These are used to calculate the angle between this plane's nose
        // direction and the enemies in the game. More on them later:
        private Vector3d flat;
        private double angle;
        private int between;
        private int enemyX;
        private int enemyZ;

        // This String describes the current view state:
        private String viewString = new String();

        private LinkedList displayQueue = new LinkedList(); // This queue is
        // used to re-queue DisplayQueue messages for display.
        private LinkedList displayQueueTimes = new LinkedList(); // This
        // LinkedList holds the start display time of each DisplayMessage in the
        // displayQueue LinkedList.
        private DisplayMessage message; // The next DisplayMessage in the
        // TextDisplayer queue.

        private final Font PLAIN, BOLD; // A plane Font and a bold font. Once
        // again, these are changed every frame, so I would be crazy not to
        // globalize them.

        private Rectangle bounds; // The screen bounds.

        /**
         * The FlightDisplay constructor.
         * 
         * @param configurationIn
         *            The GraphicsConfiguration which is required for the
         *            Canvas3D superconstructor.
         * @param boundsIn
         *            The screen bounds.
         * @param crosshairIn
         *            The Image to be used as a crosshair.
         * @param displayColourIn
         *            The colour to use to display flight information.
         */
        public FlightDisplay(GraphicsConfiguration configurationIn, Rectangle boundsIn, Image crosshairIn, Color displayColourIn)
        {
            super(configurationIn);

            g = getGraphics2D(); // Acquire the drawing object.

            bounds = boundsIn;
            crosshair = crosshairIn;
            displayColour = displayColourIn;

            // Derive the fonts from the standard one:
            PLAIN = g.getFont().deriveFont(Font.PLAIN);
            BOLD = g.getFont().deriveFont(Font.BOLD);

            // Create the map Image:
            map = this.getToolkit().createImage("image//map.png");

            mt = new MediaTracker(this);

            // Add the Images to the MediaTracker:
            mt.addImage(crosshair, 0);
            mt.addImage(map, 0);

            try
            {
                // Wait for the Images to load properly:
                mt.waitForAll();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace(System.err);
            }

            // Calculate the positions of the display components:

            crosshairX = ((bounds.width / 2) - (crosshair.getWidth(this) / 2));
            crosshairY = ((bounds.height / 2) - (crosshair.getHeight(this) / 2));

            healthX = (bounds.width - 105);
            healthY = (bounds.height - 15);

            mapX = (bounds.width - map.getWidth(this) - 5);
            mapY = 5;

            mapMidX = (mapX + (map.getWidth(this) / 2) - 1);
            mapMidY = (mapY + (map.getHeight(this) / 2) - 1);
        }

        /**
         * This method is the overridden postRender method from Canvas3D. It
         * performs all the drawing operations on top of the 3D rendering.
         */
        public void postRender()
        {
            super.postRender();

            g.setFont(PLAIN);

            if (menu)
            {
                // Display the menu:

                for (int i = 0; i < menuItems.length; i++)
                {
                    if (i == selected)
                    {
                        // Display the selected element in blue:
                        g.setColor(Color.blue);
                    }
                    else
                        g.setColor(Color.white);

                    // Display the menu item:
                    g.drawString(menuItems[i], ((bounds.width / 2) - 20), ((bounds.height / 2) - 30 + (i * 20)));
                }
            }
            else
                if (console)
                {
                    // Display the console box:

                    g.setColor(Color.BLACK);
                    g.fillRect(0, (bounds.height - 45), bounds.width, 45);

                    g.setColor(Color.WHITE);
                    g.drawString("Console:", 10, (bounds.height - 30));
                    g.setColor(Color.ORANGE);
                    g.drawString("> " + command, 10, (bounds.height - 15));
                }
                else
                {
                    if (viewState == VIEW_FIRST_PERSON)
                    {
                        // Draw the crosshair if in First Person view mode:
                        g.drawImage(crosshair, crosshairX, crosshairY, this);
                    }
                }

            if (displayVerbose)
            {
                // Output all the flight information:

                g.setColor(displayColour);

                g.drawString("Name: " + ((Plane) planes.get(PLAYER)).getName(), 10, 20);
                g.drawString("Location: " + ((Plane) planes.get(PLAYER)).getLocation().toString(), 10, 35);
                g.drawString("Pitch: " + playerPitch, 10, 50); // The player's
                // pitch in degrees.
                g.drawString("Bearing: " + playerBearing, 10, 65); // the
                // player's bearing in degrees.
                g.drawString("Throttle: " + ((Plane) planes.get(PLAYER)).getThrottle(), 10, 80);
                g.drawString("Bullets: " + ((Plane) planes.get(PLAYER)).getNumBullets(), 10, 95);

                switch (viewState)
                {
                    case VIEW_FIRST_PERSON:
                        viewString = "First Person";
                        break;
                    case VIEW_THIRD_PERSON:
                        viewString = "Third Person";
                        break;
                    case VIEW_AERIAL:
                        viewString = "Aerial";
                        break;
                    case VIEW_FRONT:
                        viewString = "Front";
                        break;
                    case VIEW_STILL:
                        viewString = "Still Camera";
                        break;
                    case VIEW_ENEMY:
                        viewString = "Nearest Enemy";
                        break;
                }

                g.drawString("View: " + viewString, 10, 110);
                g.drawString("Hits: " + hits, 10, 125);
                g.drawString("Kills: " + kills, 10, 140);

                g.drawString("FPS: " + fps, 10, 155);
                g.drawString("Memory Usage: " + usage + "%", 10, 170);

                if (!LOCAL)
                {
                    g.drawString("Sending: " + sentKBPS + " Kb/s", 10, 185);
                    g.drawString("Receiving: " + receivedKBPS + " Kb/s", 10, 200);
                }
            }

            if (displayHealth)
            {
                // Display the health bar:

                g.setColor(Color.white);

                // The built-in drawRect() method does not draw the correct
                // rectangle, so I just draw 4 lines:

                g.drawLine(healthX - 1, healthY - 1, healthX - 1, healthY + 11);
                g.drawLine(healthX - 1, healthY - 1, healthX + 101, healthY - 1);
                g.drawLine(healthX - 1, healthY + 11, healthX + 101, healthY + 11);
                g.drawLine(healthX + 101, healthY - 1, healthX + 101, healthY + 11);

                for (int j = 0; j < healthColours.length; j++)
                {
                    // Change the colour to the next in the array, and then draw
                    // the line based on the player's health:
                    g.setColor(healthColours[j]);
                    g.drawLine(healthX, healthY + j, healthX + ((Plane) planes.get(PLAYER)).getHealth(), healthY + j);
                }
            }

            if (displayMap)
            {
                // Display the radar map:

                g.setColor(Color.white);

                // The built-in drawRect() method does not draw the correct
                // rectangle, so I just draw 4 lines:
                g.drawLine(mapX - 1, mapY - 1, mapX - 1, mapY + 201);
                g.drawLine(mapX - 1, mapY - 1, mapX + 201, mapY - 1);
                g.drawLine(mapX - 1, mapY + 201, mapX + 201, mapY + 201);
                g.drawLine(mapX + 201, mapY - 1, mapX + 201, mapY + 201);

                // Draw the map Image:
                g.drawImage(map, mapX, mapY, this);

                g.setColor(Color.white);
                // Draw the player's dot in the middle of the map:
                g.fillOval(mapMidX, mapMidY, 2, 2);

                for (int i = (PLAYER + 1); i < planes.size(); i++)
                {
                    g.setColor(ENEMY_COLOUR);

                    location = ((Plane) planes.get(PLAYER)).getLocation();
                    enemy = ((Plane) planes.get(i)).getLocation();

                    // Calculate the distance between the two points (in the
                    // horizontal plane):
                    between = ((int) Math.round(Math.sqrt(((location.x - enemy.x) * (location.x - enemy.x))
                            + ((location.z - enemy.z) * (location.z - enemy.z)))));

                    if (between < MAX_DISPLAY_RANGE)
                    {
                        if (((Plane) planes.get(i)).isAlive())
                        {
                            // Holds the vector of the enemy transposed by the
                            // location within the horizontal plane (i.e, the
                            // direction of the enemy from the player):
                            flat = new Vector3d(enemy.x - location.x, 0, enemy.z - location.z);

                            // Calculate the angle between north and the vector
                            // between the player and the enemy:
                            if (flat.x < 0)
                                angle = (360 - Math.toDegrees(NORTH.angle(flat)));
                            else
                                angle = (Math.toDegrees(NORTH.angle(flat)));

                            // The angle between your player's bearing and the
                            // nearest enemy:
                            angle = (playerBearing - angle);

                            // Calculate the amount to add on the x and z
                            // axis...
                            enemyX = (int) ((between * Math.sin(Math.toRadians(angle))) / 2);
                            enemyZ = (int) ((between * Math.cos(Math.toRadians(angle))) / 2);
                            // ... and add those amounts to the player's
                            // location (the mid point of the map):
                            g.fillOval(mapMidX - enemyX, mapMidY - enemyZ, 2, 2);
                        }
                    }
                }

                g.setColor(Color.white);
                // Display the number of enemies:
                g.drawString("Enemies: " + (planes.size() - 1), mapX + 5, mapY + 15);
            }

            // If there are any messages waiting in the queue...
            while ((message = textDisplayer.getNextMessage()) != null)
            {
                // Store the DisplayMessage and the time it was received:
                displayQueue.add(message);
                displayQueueTimes.add(String.valueOf(System.currentTimeMillis()));
            }

            // Now, display the contents of the displayQueue:
            for (int i = 0; i < displayQueue.size(); i++)
            {
                g.setFont(BOLD);

                //Set the appropriate colour:
                g.setColor(((DisplayMessage) displayQueue.get(i)).getColour());

                // Make sure we don't draw at anegative screen coordinates, and
                // draw the message at the top of the screen:
                if (((((bounds.width / 2)) - (((DisplayMessage) displayQueue.get(i)).getText().length() / 2) * 6)) >= 0)
                {
                    g.drawString(((DisplayMessage) displayQueue.get(i)).getText(), ((((bounds.width / 2)) - (((DisplayMessage) displayQueue
                            .get(i)).getText().length() / 2) * 6)), ((i + 1) * 15));
                }
                else
                    g.drawString(((DisplayMessage) displayQueue.get(i)).getText(), 0, ((i + 1) * 15));

                // If the DisplayMessage's display time has elapsed:
                if ((System.currentTimeMillis() - (Long.parseLong(String.valueOf(displayQueueTimes.get(i))))) >= ((DisplayMessage) displayQueue
                        .get(i)).getDisplayTime())
                {
                    // Remove the stored DisplayMessage and start time
                    // (synchronized just in case):
                    synchronized (displayQueue)
                    {
                        displayQueue.remove(i);
                    }
                    synchronized (displayQueueTimes)
                    {
                        displayQueueTimes.remove(i);
                    }
                }
            }

            // This is essenetial. We have to flush it, or else it will not
            // display:
            g.flush(true);
        }
    }

    /**
     * The Navigator class is a Behavior extension which implements KeyListener.
     * It handles the game loop completely, managing the input from the user,
     * the network event processing, and the graphics operations.
     * 
     * @author James Attenborough
     */
    public class Navigator extends Behavior implements KeyListener
    {
        // These key states are all set to the value of a different bit, by
        // shifting to the left:
        // e.g:
        // (1 << 0) = 2^0 = 00000001
        // (1 << 1) = 2^1 = 00000010 (moved 1 bit to the left)
        // (1 << 2) = 2^2 = 00000100 (moved 2 bits to the left), etc.
        // This makes it easy to check whether a certain state is set, as you
        // can just check to see if the bit for that state is set. It also
        // allows for multiple keys to be pressed at once.
        private static final int FORWARD = (1 << 0); // The state for the Up
        // Arrow.
        private static final int BACKWARD = (1 << 1); // The state for the Down
        // Arrow.
        private static final int LEFT = (1 << 2); // The state for the Left
        // Arrow.
        private static final int RIGHT = (1 << 3); // The state for the Right
        // Arrow.
        private static final int SPACE = (1 << 4); // The state for the Space
        // Bar.
        private static final int PLUS = (1 << 5); // The state for the Plus key.
        private static final int MINUS = (1 << 6); // The state for the Minus
        // key.

        private int frames = 0; // The number of frames that have been rendered.

        private long start = System.currentTimeMillis(); // The start time...
        private long current; //... and the current time. These are used to
        // find out if one second has elapsed, so that per-second operations can
        // be performed.

        // This vector represents the direction that the player's nose is
        // facing.
        private Vector3d playerDirection = new Vector3d();
        // This is just a copy of the above. A copy is needed to use in other
        // operations.
        private Vector3d playerDirectionFrontCopy = new Vector3d();

        private Vector3d floor; // This is used to represent the plane's nose
        // direction in the horizontal plane.

        private Matrix3d pitchRotationMatrix = new Matrix3d(); // The pitch
        // rotation matrix for the player.
        private Matrix3d yawRotationMatrix = new Matrix3d(); // The yaw rotation
        // matrix for the player.
        private Matrix3d rollRotationMatrix = new Matrix3d(); // The roll
        // rotation matrix for the player.

        private Vector3d currentLocation = new Vector3d(); // This is used in
        // location changing operations.

        private Matrix3d enemyPitchRotationMatrix;// The pitch rotation matrix
        // for the enemies.
        private Matrix3d enemyYawRotationMatrix; // The yaw rotation matrix for
        // the enemies.
        private Matrix3d enemyRollRotationMatrix;// The roll rotation matrix for
        // the enemies.

        private int keyState = 0; // The current state of the keys (nothing
        // being pressed).

        private KeyEvent keyEvent; // The KeyEvent for processing key events.

        private WakeupCriterion event; // A WakeupCriterion that has occurred

        // The criteria for wakeups:
        private WakeupCriterion keyReleasedWakeup = new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED);
        private WakeupCriterion keyPressedWakeup = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
        private WakeupOnElapsedFrames framesWakeup = new WakeupOnElapsedFrames(0);

        // The criteria are placed into an array:
        private WakeupCriterion[] conditions = { keyPressedWakeup, keyReleasedWakeup, framesWakeup };
        // And made into one WakeupCondition:
        private WakeupCondition waker = new WakeupOr(conditions);

        private Transform3D view = new Transform3D(); // The view transform.

        private final Vector3d UP = new Vector3d(0, 1, 0); // The direction
        // "up".

        // Hold the player's current roll and pitch values:
        private double currentRoll = 0;
        private double currentPitch = 0;

        private Vector3d forward;

        // The new health value of the player who has been shot.
        private int newHealth;

        private SceneGraphPath[] collisions; // The collisions of the player.
        private SceneGraphPath[] enemyCollisions; // The collisions of the AI
        // enemies.

        private Vector3d noseForward;
        private Vector3d playerDirectionCopy;

        // Used when calculating the nearest enemy to any plane, AI or human:
        private double enemyBearing;
        private double enemyAngle;

        private Vector3d enemyDirection = new Vector3d();
        private Vector3d temp;
        private Vector3d enemyDirectionForward;
        private Vector3d enemyDirectionCopy;

        private boolean viewChangeable = true; // Whether the view can be
        // changed or not.

        private Vector3d frontPoint;

        private int closest;
        private Point3d location;

        private String killed = new String(); // Used to store the name of the
        // Plane that was shot down.

        private boolean networkErrorOutputted = false; // To prevent the error
        // being outputted too
        // many times.

        private int closestEnemyToEnemy;

        private final int OUT_OF_BOUNDS_LIMIT = 30; // You only have 30 seconds
        // to return to the combat zone if you leave it.
        private boolean outOfBoundsDisplayed = false;
        private int outOfBoundsSecondCount = 1;
        private boolean alreadyOutOfBounds = false;
        private long outOfBoundsStartTime;

        /**
         * The Navigator constructor.
         * 
         * @param canvasIn
         *            The Canvas3D this Navigator operates on.
         */
        public Navigator(Canvas3D canvasIn)
        {
            canvasIn.addKeyListener(this);
            setView(VIEW_FIRST_PERSON); // Start with First Person view.
        }

        /**
         * This method had to be implemented. It allows you to perform any
         * initialization routines for this Behavior.
         */
        public void initialize()
        {
            setSchedulingBounds(new BoundingSphere(new Point3d(), 10000));
            gameSoundPlayer.loopSound(ENGINE); // Loop the engine sound.
            wakeupOn(waker); // Schedule a Wakeup.
        }

        /**
         * This method had to be implemented. It is called whenever the Behavior
         * experiences a Wakeup.
         * 
         * @param criteria
         *            The Enumeration of stiuli to process.
         */
        public void processStimulus(Enumeration criteria)
        {
            while (criteria.hasMoreElements())
            {
                event = (WakeupCriterion) criteria.nextElement();

                if (event instanceof WakeupOnElapsedFrames)
                    processViewChanges();
                else
                    if (event instanceof WakeupOnAWTEvent)
                        processEvents(((WakeupOnAWTEvent) event).getAWTEvent());
            }
            wakeupOn(waker); // Schedule a Wakeup.
        }

        /**
         * This method processes AWT events.
         * 
         * @param events
         *            The AWTEvent[] of events to process.
         */
        public void processEvents(AWTEvent[] events)
        {
            for (int i = 0; i < events.length; i++)
            {
                // We are only interested in KeyEvents for this particular
                // class:
                if (events[i] instanceof KeyEvent)
                {
                    keyEvent = ((KeyEvent) events[i]);

                    if ((keyEvent.getID() == KeyEvent.KEY_RELEASED) || (keyEvent.getID() == KeyEvent.KEY_PRESSED))
                        processKeyEvent(keyEvent);
                }
            }
        }

        /**
         * This method forms the basic game loop. It handles the network events,
         * handles the input, and calculates and sets the players to their new
         * position.
         */
        public void processViewChanges()
        {
            // Record the current time for per-second operation purposes:
            current = System.currentTimeMillis();

            try
            {
                if (!LOCAL)
                {
                    if (HOST)
                    {
                        // If running a network game as the host:

                        if (selector.isOpen())
                        {
                            numChannelRequests = selector.selectNow();

                            if (numChannelRequests > 0)
                            {
                                channelIterator = (selector.selectedKeys()).iterator();

                                while (channelIterator.hasNext())
                                {
                                    // Go through each SelectionKey in the
                                    // Iterator:

                                    selectionKey = (SelectionKey) channelIterator.next();
                                    // and handle the network operations for the
                                    // specific operations:
                                    handleServerOperations(selectionKey.interestOps());
                                    channelIterator.remove();
                                }
                            }
                        }
                    }
                    else
                    {
                        if (socketChannel.isOpen())
                        {
                            // If playing a network game as a client, then
                            // handle the client network operations:
                            handleClientOperations(socketChannel.validOps());
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace(System.err);

                if (!networkErrorOutputted)
                {
                    // If there was a network error:
                    textDisplayer.output(new DisplayMessage(DisplayMessage.ERROR_MESSAGE, "Sorry, there was a networking error.",
                            DisplayMessage.STANDARD_DISPLAY_TIME));
                    networkErrorOutputted = true; // We only want to display it
                    // once.
                }
            }

            if (((Plane) planes.get(PLAYER)).isAlive())
            {
                if ((!LOCAL) || ((LOCAL) && ((!menu) && (!console))))
                {
                    if ((!menu) && (!console))
                    {
                        // Check the key states to see which control operations
                        // need to be performed:

                        if ((keyState & FORWARD) != 0)
                            ((Plane) planes.get(PLAYER)).pitchDown();

                        if ((keyState & BACKWARD) != 0)
                            ((Plane) planes.get(PLAYER)).pitchUp();

                        if ((keyState & LEFT) != 0)
                            ((Plane) planes.get(PLAYER)).rollLeft();

                        if ((keyState & RIGHT) != 0)
                            ((Plane) planes.get(PLAYER)).rollRight();

                        if ((keyState & PLUS) != 0)
                            ((Plane) planes.get(PLAYER)).throttleUp();

                        if ((keyState & MINUS) != 0)
                            ((Plane) planes.get(PLAYER)).throttleDown();
                    }

                    currentRoll = ((Plane) planes.get(PLAYER)).getRoll();
                    currentPitch = ((Plane) planes.get(PLAYER)).getPitch();

                    // Reset the nose rotation:
                    ((Plane) planes.get(PLAYER)).setNoseRotation(new Matrix3d());

                    transform3DInitial.set(PLAYER, new Transform3D());
                    ((TransformGroup) transformGroupInitial.get(PLAYER)).getTransform(((Transform3D) transform3DInitial.get(PLAYER)));
                    ((Transform3D) transform3DInitial.get(PLAYER)).get(((Plane) planes.get(PLAYER)).getNoseRotation());

                    // Apply the pitch rotation:
                    pitchRotationMatrix = new Matrix3d();
                    pitchRotationMatrix.set(new AxisAngle4d(1.0, 0.0, 0.0,
                            ((currentPitch * ((Plane) planes.get(PLAYER)).PITCH_CONSTANT) / fps)));
                    ((Plane) planes.get(PLAYER)).getNoseRotation().mul(pitchRotationMatrix);

                    // Although the yaw is not incorporated into this game, you
                    // still need to apply the yaw rotation, as the nose
                    // rotation matrix requires the pitch, then the yaw, then
                    // the roll rotations to be applied:
                    yawRotationMatrix = new Matrix3d();
                    yawRotationMatrix.set(new AxisAngle4d(0.0, 1.0, 0.0, 0));
                    ((Plane) planes.get(PLAYER)).getNoseRotation().mul(yawRotationMatrix);

                    // Apply the roll rotation:
                    rollRotationMatrix = new Matrix3d();
                    rollRotationMatrix.set(new AxisAngle4d(0.0, 0.0, 1.0,
                            ((currentRoll * ((Plane) planes.get(PLAYER)).ROLL_CONSTANT) / fps)));
                    ((Plane) planes.get(PLAYER)).getNoseRotation().mul(rollRotationMatrix);

                    ((Transform3D) transform3DInitial.get(PLAYER)).set(((Plane) planes.get(PLAYER)).getNoseRotation());
                    ((TransformGroup) transformGroupInitial.get(PLAYER)).setTransform(((Transform3D) transform3DInitial.get(PLAYER)));

                    playerDirection = new Vector3d(0, 0, -1.0);
                    // Transfomr the noseDirection by the noseRotation:
                    ((Plane) planes.get(PLAYER)).getNoseRotation().transform(playerDirection);

                    // Scale the player's direction by a function of his
                    // throttle, the user's Frames Per Second and his plane's
                    // speed constant. The FPS is included in my equation so
                    // that no network user is at an advantage: If one player
                    // renders less frames per second, then in effect he moves
                    // forward less times per second, but with my equation,
                    // he'll move more per frame, so he may be moving forward
                    // less often, but he's moving further each time, so in
                    // effect every player would cover the same distance in a
                    // certain number of frames if they were using the same
                    // plane. This way, the SPEED_CONSTANT is the only factor
                    // affecting their speed:
                    playerDirection.scale(((double) ((Plane) planes.get(PLAYER)).getThrottle() + 1)
                            / (fps * ((Plane) planes.get(PLAYER)).SPEED_CONSTANT));

                    // Update the location of the player's Plane:
                    currentLocation = new Vector3d(((Plane) planes.get(PLAYER)).getLocation());
                    currentLocation.add(playerDirection);
                    ((Plane) planes.get(PLAYER)).setLocation(new Point3d(currentLocation));

                    // Calculate the bearing of the player by finding the angle
                    // between the two vectors: north, and the player's
                    // direction in the horizontal plane.
                    floor = new Vector3d(playerDirection.x, 0, playerDirection.z);
                    if (floor.x < 0)
                        playerBearing = (360 - Math.toDegrees(NORTH.angle(floor)));
                    else
                        playerBearing = (Math.toDegrees(NORTH.angle(floor)));

                    // Calculate the pitch of the player by finding the angle
                    // between the two vectors: player's nose direction, and the
                    // player's direction in the horizontal plane, i.e., only
                    // the y-value is different:
                    if (playerDirection.y < 0)
                        playerPitch = (360 - Math.toDegrees(playerDirection.angle(floor)));
                    else
                        playerPitch = Math.toDegrees(playerDirection.angle(floor));

                    // The noseForward Vector3d is used when shooting. This
                    // finds the point that is within MAX_SHOOT_DISTANCE from
                    // the player:
                    noseForward = new Vector3d(((Plane) planes.get(PLAYER)).getLocation());
                    playerDirectionCopy = new Vector3d(playerDirection);
                    playerDirectionCopy.scale(MAX_SHOOT_DISTANCE);
                    noseForward.add(playerDirectionCopy);

                    ((Plane) planes.get(PLAYER)).setBounds(new BoundingSphere(((Plane) planes.get(PLAYER)).getLocation(), 1));
                }
            }

            if ((!LOCAL) || ((LOCAL) && ((!menu) && (!console))))
            {
                enemyLoop: for (int i = (PLAYER + 1); i < planes.size(); i++)
                {
                    if (((Plane) planes.get(i)).isAlive())
                    {
                        if (((Plane) planes.get(i)).isAI())
                        {
                            // For AI Enemies:

                            closestEnemyToEnemy = 0;

                            // Use a simple searching algorithm to find the
                            // Plane which is closest to this AI enemy:

                            for (int k = 0; k < planes.size(); k++)
                            {
                                if (i != k)
                                {
                                    if (((Plane) planes.get(i)).getLocation().distance(((Plane) planes.get(k)).getLocation()) < ((Plane) planes
                                            .get(i)).getLocation().distance(((Plane) planes.get(closestEnemyToEnemy)).getLocation()))
                                    {
                                        closestEnemyToEnemy = k;
                                    }
                                }
                            }

                            // If the Plane's are within AI range:
                            if (((Plane) planes.get(i)).getLocation().distance(((Plane) planes.get(closestEnemyToEnemy)).getLocation()) < MAX_AI_RANGE)
                            {
                                // The player's chance of making a turning
                                // decision is based on his level: the higher
                                // his level, the better chance he has of making
                                // a decision:
                                if (((int) (Math.random() * (Plane.MAX_LEVEL - Plane.MIN_LEVEL)) + Plane.MIN_LEVEL) < ((Plane) planes
                                        .get(i)).getLevel())
                                {
                                    // We find the enemy's bearing:
                                    if (enemyDirection.x < 0)
                                        enemyBearing = (360 - Math.toDegrees(NORTH
                                                .angle(new Vector3d(enemyDirection.x, 0, enemyDirection.z))));
                                    else
                                        enemyBearing = (Math.toDegrees(NORTH.angle(new Vector3d(enemyDirection.x, 0, enemyDirection.z))));

                                    // Calculate the angle between north and the
                                    // vector between the player and the enemy:
                                    if (((Plane) planes.get(i)).getLocationX() < ((Plane) planes.get(closestEnemyToEnemy)).getLocationX())
                                        enemyAngle = (360 - Math.toDegrees(NORTH.angle(new Vector3d(((Plane) planes.get(i)).getLocationX()
                                                - ((Plane) planes.get(closestEnemyToEnemy)).getLocationX(), 0, ((Plane) planes.get(i))
                                                .getLocationZ()
                                                - ((Plane) planes.get(closestEnemyToEnemy)).getLocationZ()))));
                                    else
                                        enemyAngle = (Math.toDegrees(NORTH.angle(new Vector3d(((Plane) planes.get(i)).getLocationX()
                                                - ((Plane) planes.get(closestEnemyToEnemy)).getLocationX(), 0, ((Plane) planes.get(i))
                                                .getLocationZ()
                                                - ((Plane) planes.get(closestEnemyToEnemy)).getLocationZ()))));

                                    // Find the angle of the closest enemy,
                                    // relative to this enemy's current heading:
                                    enemyAngle = (enemyBearing - enemyAngle);

                                    // The closest enemy will be to one side of
                                    // him, so we use the angle we calculated
                                    // above to make a decision between turning
                                    // left or right:
                                    if (Math.abs(enemyAngle) < 180)
                                        ((Plane) planes.get(i)).rollLeft();
                                    else
                                        ((Plane) planes.get(i)).rollRight();

                                    // The we randomly pitch up or down (By
                                    // adding a radnom feature like this, it
                                    // makes the enemy more unpredictable, which
                                    // is always good):
                                    if (Math.random() > 0.5)
                                        ((Plane) planes.get(i)).pitchDown();
                                    else
                                        ((Plane) planes.get(i)).pitchUp();
                                }
                            }
                            else
                            {
                                // Steady the plane if it's out of range:
                                ((Plane) planes.get(i)).setRoll(0);
                                ((Plane) planes.get(i)).setPitch(0);
                            }

                            // This section is very similar to the section above
                            // where the player's new liocation and nose
                            // rotation were found. I would have liked to have
                            // made this all into one method for reusability of
                            // code, but it tended to have strange results.

                            ((Plane) planes.get(i)).setNoseRotation(new Matrix3d());
                            transform3DInitial.set(i, new Transform3D());
                            ((TransformGroup) transformGroupInitial.get(i)).getTransform((Transform3D) (transform3DInitial.get(i)));
                            ((Transform3D) transform3DInitial.get(i)).get(((Plane) planes.get(i)).getNoseRotation());

                            // Apply the pitch rotation:
                            enemyPitchRotationMatrix = new Matrix3d();
                            enemyPitchRotationMatrix.set(new AxisAngle4d(1.0, 0.0, 0.0, ((Plane) planes.get(i)).getPitch()
                                    * ((Plane) planes.get(i)).PITCH_CONSTANT / fps));
                            ((Plane) planes.get(i)).getNoseRotation().mul(enemyPitchRotationMatrix);

                            // Apply the necessary, but not used, yaw rotation:
                            enemyYawRotationMatrix = new Matrix3d();
                            enemyYawRotationMatrix.set(new AxisAngle4d(0.0, 1.0, 0.0, 0));
                            ((Plane) planes.get(i)).getNoseRotation().mul(enemyYawRotationMatrix);

                            // Apply the roll rotation:
                            enemyRollRotationMatrix = new Matrix3d();
                            enemyRollRotationMatrix.set(new AxisAngle4d(0.0, 0.0, 1.0, ((Plane) planes.get(i)).getRoll()
                                    * ((Plane) planes.get(i)).ROLL_CONSTANT / fps));
                            ((Plane) planes.get(i)).getNoseRotation().mul(enemyRollRotationMatrix);

                            ((Transform3D) transform3DInitial.get(i)).set(((Plane) planes.get(i)).getNoseRotation());
                            ((TransformGroup) transformGroupInitial.get(i)).setTransform(((Transform3D) transform3DInitial.get(i)));

                            // Find the current heading and scale it by my
                            // throttle, FPS and speed constant function:
                            enemyDirection = new Vector3d(0, 0, -1.0);
                            ((Plane) planes.get(i)).getNoseRotation().transform(enemyDirection);
                            enemyDirection
                                    .scale((((double) ((Plane) planes.get(i)).getThrottle() + 1) / (fps * ((Plane) planes.get(i)).SPEED_CONSTANT)));

                            temp = new Vector3d(((Plane) planes.get(i)).getLocation());
                            temp.add(enemyDirection);
                            // Set the new location:
                            ((Plane) planes.get(i)).setLocation(new Point3d(temp));

                            enemyDirectionForward = new Vector3d(((Plane) planes.get(i)).getLocation());
                            enemyDirectionCopy = new Vector3d(enemyDirection);
                            enemyDirectionCopy.scale(MAX_SHOOT_DISTANCE);
                            enemyDirectionForward.add(enemyDirectionCopy);

                            if (((Plane) planes.get(i)).isAlive())
                            {
                                // If AI Plane's leave the Combat Zone they die
                                // immediately:
                                if (!(isWithinBounds((Plane) planes.get(i))))
                                {
                                    textDisplayer.output(new DisplayMessage(DisplayMessage.GAME_STATUS_MESSAGE, ((Plane) planes.get(i))
                                            .getName()
                                            + " left the combat zone.", DisplayMessage.STANDARD_DISPLAY_TIME));

                                    ((Plane) planes.get(i)).setHealth(0);

                                    killPlayer(((Plane) planes.get(i)), false, true);

                                    // If we didn't do this, then it would go on
                                    // to try and access a plane which has been
                                    // removed:
                                    break enemyLoop;
                                }
                            }

                            // Retrieves the SceneGraphPath[] of objects that
                            // were
                            // picked by the specified PickShape:
                            enemyCollisions = world.pickAll(new PickPoint(((Plane) planes.get(i)).getLocation()));

                            if (enemyCollisions != null)
                            {
                                for (int j = 0; j < enemyCollisions.length; j++)
                                {
                                    if ((enemyCollisions[j].getObject() instanceof WorldObject))
                                    {
                                        // If the player hit the ground:

                                        if ((((Plane) planes.get(i)).isAI()) && (((Plane) planes.get(i)).isAlive()))
                                        {
                                            // Output the message:
                                            textDisplayer.output(new DisplayMessage(DisplayMessage.GAME_STATUS_MESSAGE, ((Plane) planes
                                                    .get(i)).getName()
                                                    + " crashed.", DisplayMessage.STANDARD_DISPLAY_TIME));

                                            // Kill the plane:
                                            ((Plane) planes.get(i)).setHealth(0);
                                            killPlayer(((Plane) planes.get(i)), true, true);

                                            // If we didn't do this, then it
                                            // would go on to try and access
                                            // a plane which has been removed:
                                            break enemyLoop;
                                        }
                                    }
                                }
                            }
                        }

                        // Translate the Transform3D by the plane's location,
                        // rotate it by the plane's new nose rotation, and then
                        // set it as the transform for this Plane's
                        // TransformGroup:
                        ((Transform3D) planeTransforms.get(i)).set(new Vector3d(((Plane) planes.get(i)).getLocation()));
                        ((Transform3D) planeTransforms.get(i)).setRotation(((Plane) planes.get(i)).getNoseRotation());
                        ((TransformGroup) planeTransformGroups.get(i)).setTransform(((Transform3D) planeTransforms.get(i)));

                        ((Plane) planes.get(i)).setBounds(new BoundingSphere(((Plane) planes.get(i)).getLocation(), 1));

                        if (((Plane) planes.get(i)).isAI())
                        {
                            if (((Plane) planes.get(i)).getLocation().distance(((Plane) planes.get(closestEnemyToEnemy)).getLocation()) < ((((Plane) planes
                                    .get(i)).getLevel() * MAX_SHOOT_DISTANCE) / Plane.MAX_LEVEL))
                            {
                                // Make the AI plane shoot:

                                if (((Plane) planes.get(i)).fireBullet())
                                    checkForHits(((Plane) planes.get(i)), world.pickAll(new PickSegment(((Plane) planes.get(i))
                                            .getLocation(), new Point3d(enemyDirectionForward))));
                            }
                        }
                    }

                }
            }

            if (((Plane) planes.get(PLAYER)).isAlive())
            {
                if ((!menu) && (!console))
                {
                    if ((keyState & SPACE) != 0)
                    {
                        // Make the player shoot:
                        if (((Plane) planes.get(PLAYER)).fireBullet())
                            checkForHits(((Plane) planes.get(PLAYER)), world.pickAll(new PickSegment(((Plane) planes.get(PLAYER))
                                    .getLocation(), new Point3d(noseForward))));
                    }
                }

                // Check for player's collisions:
                collisions = world.pickAll(new PickPoint(((Plane) planes.get(PLAYER)).getLocation()));

                if (collisions != null)
                {
                    for (int i = 0; i < collisions.length; i++)
                    {
                        // If Player hits a WorldObject:
                        if (collisions[i].getObject() instanceof WorldObject)
                        {
                            if (((Plane) planes.get(PLAYER)).isAlive())
                            {
                                // Kill player and output message:

                                textDisplayer.output(new DisplayMessage(DisplayMessage.GAME_STATUS_MESSAGE, "You crashed, "
                                        + ((Plane) planes.get(PLAYER)).getName() + ".", DisplayMessage.STANDARD_DISPLAY_TIME));

                                ((Plane) planes.get(PLAYER)).setHealth(0);

                                killPlayer(((Plane) planes.get(PLAYER)), true, false);
                            }
                        }
                    }
                }

            }
            // Translate by player's locations:
            ((Transform3D) planeTransforms.get(PLAYER)).set(new Vector3d(((Plane) planes.get(PLAYER)).getLocation()));
            // Rotate by player's nose rotation:
            ((Transform3D) planeTransforms.get(PLAYER)).setRotation(((Plane) planes.get(PLAYER)).getNoseRotation());
            // Apply the player's transform:
            ((TransformGroup) planeTransformGroups.get(PLAYER)).setTransform(((Transform3D) planeTransforms.get(PLAYER)));

            // This switch handles the view based on the view state value:
            viewSwitch: switch (viewState)
            {
                case VIEW_FIRST_PERSON:
                {
                    // This view places the position of the camera just a little
                    // bit ahead of the player, in the direction of the player's
                    // nose rotation:
                    forward = new Vector3d(((Plane) planes.get(PLAYER)).getLocation());
                    forward.add(playerDirection);
                    view.set(forward);
                    view.setRotation(((Plane) planes.get(PLAYER)).getNoseRotation());
                    break viewSwitch;
                }
                case VIEW_THIRD_PERSON:
                {
                    // This lookAt method is a simple method provided by the
                    // Transform3D class, which constructs the matrix for you
                    // based on the eye position, the lookingAt point and the
                    // "up" direction. Although doing it manually is not much
                    // harder, it is still a useful method:
                    view.lookAt(new Point3d(((Plane) planes.get(PLAYER)).getLocationX()
                            - (((((Plane) planes.get(PLAYER)).getThrottle() + 10) / 10) * Math.sin(Math.toRadians(((Plane) planes
                                    .get(PLAYER)).getRoll()))), ((Plane) planes.get(PLAYER)).getLocationY()
                            - (((((Plane) planes.get(PLAYER)).getThrottle() + 10) / 10) * Math.sin(Math.toRadians(((Plane) planes
                                    .get(PLAYER)).getPitch()))), ((Plane) planes.get(PLAYER)).getLocationZ()
                            + (((((Plane) planes.get(PLAYER)).getThrottle() + 10) / 10) * Math.cos(Math.toRadians(((Plane) planes
                                    .get(PLAYER)).getRoll())))), ((Plane) planes.get(PLAYER)).getLocation(), UP);
                    // In the above method, I also placed the camera a ceratin
                    // distance away from the plane, where the distance was =
                    // ((((Plane) planes.get(PLAYER)).getThrottle() + 10) / 10);
                    // This moves the camera closer to your plane as you slow
                    // down and further away as you speed up.

                    view.invert();
                    break viewSwitch;
                }
                case VIEW_AERIAL:
                {
                    // This looks down at the plane from above and slightly to
                    // the side:
                    view.lookAt(new Point3d(((Plane) planes.get(PLAYER)).getLocationX() - 0.001, (((Plane) planes.get(PLAYER))
                            .getLocationY() + 10), ((Plane) planes.get(PLAYER)).getLocationZ() - 0.001), ((Plane) planes.get(PLAYER))
                            .getLocation(), UP);
                    view.invert();
                    break viewSwitch;
                }
                case VIEW_FRONT:
                {
                    // This finds the point which is at most 30 units away from
                    // the player's location, and sets that as the eye point for
                    // a lookAt generated view matrix:
                    frontPoint = new Vector3d(((Plane) planes.get(PLAYER)).getLocation());
                    playerDirectionFrontCopy = new Vector3d(playerDirection);
                    playerDirectionFrontCopy.scale(30);
                    frontPoint.add(playerDirectionFrontCopy);

                    view.lookAt(new Point3d(frontPoint), ((Plane) planes.get(PLAYER)).getLocation(), UP);

                    view.invert();
                    break viewSwitch;
                }
                case VIEW_STILL:
                {
                    // The camera stays where it was last set. It stops the
                    // camera from moving relative to you.

                    break viewSwitch;
                }
                case VIEW_ENEMY:
                {
                    // Finds the closest enemy:

                    closest = 1;
                    location = ((Plane) planes.get(PLAYER)).getLocation();

                    if (planes.size() > 1)
                    {
                        for (int i = (PLAYER + 1); i < planes.size(); i++)
                        {
                            if (((Plane) planes.get(i)).isAlive())
                                if (location.distance(((Plane) planes.get(i)).getLocation()) < (location.distance(((Plane) planes
                                        .get(closest)).getLocation())))
                                    closest = i;
                        }
                        // Looks at your position from the nearest enemy:
                        view.lookAt(((Plane) planes.get(closest)).getLocation(), location, UP);
                        view.invert();
                    }
                    else
                    {
                        // Or if there are no enemies, then from a point that is
                        // 20 away on all axis:
                        view.lookAt(new Point3d(location.x + 20, location.y + 20, location.z + 20), location, UP);
                        view.invert();
                    }

                    break viewSwitch;
                }
            }

            // Sets the view (but only if the menu and console are not up if you
            // are playing a local game... i.e., the game will continue to play
            // during a network game, regardless of whether the menu or console
            // are active):
            if ((!LOCAL) || ((LOCAL) && ((!menu) && (!console))))
                viewingPlatform.getViewPlatformTransform().setTransform(view);

            if (((Plane) planes.get(PLAYER)).isAlive())
            {
                // Checks to see if the player is out of bounds:

                if (!(isWithinBounds((Plane) planes.get(PLAYER))))
                {
                    if (!alreadyOutOfBounds)
                    {
                        outOfBoundsStartTime = System.currentTimeMillis();
                        alreadyOutOfBounds = true;
                    }

                    outOfBoundsDisplayed = false;
                    if (outOfBoundsSecondCount < OUT_OF_BOUNDS_LIMIT)
                    {
                        if ((System.currentTimeMillis() - outOfBoundsStartTime) >= (outOfBoundsSecondCount * 1000))
                        {
                            if (!outOfBoundsDisplayed)
                            {
                                // Displays the number of seconds left to return
                                // to the combat zone:
                                textDisplayer.output(new DisplayMessage(DisplayMessage.WARNING_MESSAGE, "WARNING: You have "
                                        + (OUT_OF_BOUNDS_LIMIT - outOfBoundsSecondCount) + " seconds to return to the combat zone.",
                                        DisplayMessage.SECOND_DISPLAY_TIME));
                                outOfBoundsDisplayed = true;
                                outOfBoundsSecondCount++;
                            }
                        }
                    }
                    else
                    {
                        // Kills the player if he fails to return:

                        textDisplayer.output(new DisplayMessage(DisplayMessage.GAME_STATUS_MESSAGE, "You left the combat zone, "
                                + ((Plane) planes.get(PLAYER)).getName() + ".", DisplayMessage.STANDARD_DISPLAY_TIME));

                        ((Plane) planes.get(PLAYER)).setHealth(0);

                        killPlayer(((Plane) planes.get(PLAYER)), false, false);
                    }
                }
                else
                {
                    // Reset the outOfBounds variables:
                    alreadyOutOfBounds = false;
                    outOfBoundsSecondCount = 1;
                }
            }

            // Increment the number of frames rendered:
            frames++;

            // Every second:
            if ((current - start) >= 1000)
            {
                if (!LOCAL)
                {
                    // Calculate how many KiloBytes were sent and received in
                    // the last second:
                    sentKBPS = (((double) sentBytes) / 1024);
                    receivedKBPS = (((double) receivedBytes) / 1024);

                    // These need to be reset:
                    sentBytes = 0;
                    receivedBytes = 0;
                }

                // Store how many frames were rendered in the last second:
                fps = frames;
                start = current;
                frames = 0;
            }

            // Fetch memory usage statistics from the Runtime object:
            total = runtime.totalMemory();
            free = runtime.freeMemory();
            used = (total - free);
            usage = ((used * 100) / total);
        }

        /**
         * Kills a player and performs killing operations on it, such as
         * explosion sounds, perhaps adding a Fire, etc.
         * 
         * @param planeIn
         *            The Plane to kill.
         * @param fireIn
         *            Whether or not to place a Fire at the location of the
         *            kill.
         * @param removePlayer
         *            Whether or not to remove the plane from the scene after
         *            being killed.
         */
        private void killPlayer(Plane planeIn, boolean fireIn, boolean removePlayer)
        {
            gameSoundPlayer.playSound(EXPLOSION); // Play the explosion sound.

            if (fireIn)
                world.addFire(planeIn.getLocation()); // Add a Fire if
            // necessary.

            if (removePlayer)
            {

                removePlane(planeIn.getName()); // Remove the necessary Plane.
            }

            // If it was you who was killed:
            if (planeIn.getName().equalsIgnoreCase(((Plane) planes.get(PLAYER)).getName()))
            {
                // Set the view to Nearest Enemy and make the view
                // non-changeable:
                setView(VIEW_ENEMY);
                viewChangeable = false;
                // Pause the sounds:
                gameSoundPlayer.pauseSound(ENGINE);
                gameSoundPlayer.pauseSound(GUN_FIRE);
            }

            if (!LOCAL)
            {
                // Notify clients about this kill:
                if (HOST)
                    broadcast(KILL_CLIENT + PROTOCOL_SEPARATOR + planeIn.getName());
                else
                    send(socketChannel, (KILL_CLIENT + PROTOCOL_SEPARATOR + planeIn.getName()));
            }
        }

        /**
         * Checks to see if any enemy planes were hit while the player was
         * shooting.
         * 
         * @param p
         *            The Plane that did the shooting.
         * @param objectsIn
         *            The SceneGraphPath [] of objects that were picked.
         */
        private void checkForHits(Plane p, SceneGraphPath[] objectsIn)
        {
            if (objectsIn != null)
            {
                for (int i = 0; i < objectsIn.length; i++)
                {
                    // If you hit a plane:
                    if (objectsIn[i].getObject() instanceof Plane)
                    {
                        // There's the chance that you were hitting your own
                        // Plane, as the buttler originate form the dead-centre
                        // of your plane's model:
                        if (!(p.getName().equalsIgnoreCase(((Plane) objectsIn[i].getObject()).getName())))
                        {
                            if (p.getName().equalsIgnoreCase(((Plane) planes.get(PLAYER)).getName()))
                                hits++; // scored a hit.

                            plane_loop: for (int j = 0; j < planes.size(); j++)
                            {
                                if (((Plane) planes.get(j)).isAlive())
                                {
                                    if (((Plane) objectsIn[i].getObject()).getName().equalsIgnoreCase(((Plane) planes.get(j)).getName()))
                                    {
                                        // Calculate and apply the new health
                                        // value:
                                        newHealth = (((Plane) planes.get(j)).getHealth() - ((int) Math.floor(Math.random()
                                                * ((Plane.MAX_LEVEL + 1) - ((Plane) planes.get(j)).getLevel()))));
                                        ((Plane) planes.get(j)).setHealth(newHealth);

                                        if (!((Plane) planes.get(j)).isAlive())
                                        {
                                            // Perform killing operations on the
                                            // plane that ou destroyed:

                                            boolean remove = true;
                                            killed = ((Plane) planes.get(j)).getName();

                                            if (killed.equalsIgnoreCase(((Plane) planes.get(PLAYER)).getName()))
                                            {
                                                remove = false;
                                            }

                                            killPlayer(((Plane) planes.get(j)), false, remove);

                                            if (killed.equalsIgnoreCase(((Plane) planes.get(PLAYER)).getName()))
                                                textDisplayer.output(new DisplayMessage(DisplayMessage.GAME_STATUS_MESSAGE,
                                                        "You were shot down, " + killed + ".", DisplayMessage.STANDARD_DISPLAY_TIME));
                                            else
                                                textDisplayer.output(new DisplayMessage(DisplayMessage.GAME_STATUS_MESSAGE, killed
                                                        + " was shot down.", DisplayMessage.STANDARD_DISPLAY_TIME));

                                            if (p.getName().equalsIgnoreCase(((Plane) planes.get(PLAYER)).getName()))
                                                kills++;

                                            break plane_loop;
                                        }
                                        else
                                            if ((!LOCAL) && (!HOST))
                                                send(socketChannel, UPDATE_CLIENT + PROTOCOL_SEPARATOR
                                                        + ((Plane) planes.get(j)).getUpdateString());
                                    }
                                }
                            }
                        }
                    }
                    else
                        if (objectsIn[i].getObject() instanceof GroundObject)
                        {
                            if (LOCAL)
                            {
                                world.addFire(((GroundObject) objectsIn[i].getObject()).getLocation());
                            }
                        }
                }
            }
        }

        /**
         * Checks whether the specified Plane is within the boundaries of the
         * World.
         * 
         * @param planeIn
         *            The Plane who's bounds status you wish to check.
         * @return true if the player is within the World bounds, otherwise
         *         false.
         */
        private boolean isWithinBounds(Plane planeIn)
        {
            if ((planeIn.getLocationY() < (world.getMinimumHeight() * world.getScale())) || (planeIn.getLocationX() < 0)
                    || (planeIn.getLocationZ() < 0) || (planeIn.getLocationX() > (world.getBreadth() * world.getScale()))
                    || (planeIn.getLocationZ() > (world.getLength() * world.getScale())))
                return false;

            return true;
        }

        /**
         * This is the KeyListener.keyReleased implemented method.
         * 
         * @param e
         *            The KeyEvent.
         */
        public void keyReleased(KeyEvent e)
        {
            int code = e.getKeyCode();

            // Retrieve the VK code, then "bitwise and" the key state variable
            // with the composite of the specific key state, because:
            // As an example, say if your keyState variable looked like this:
            //    00001010
            // and say that UP = (1 << 1) and LEFT = (1 << 3) were the states
            // set in keyStates. Then, say if you took your finger off the up
            // key, and you needed that to disappear, then you could clear the UP
            // bit like this:
            // Find the composite of UP:
            // UP = 00000010
            // Therefore ~UP = 11111101
            // Therefore keyState &= ~UP makes keyState = 00001000 because only
            // the one bit will return 1 when it is "bitwise and"ed.

            switch (code)
            {
                case KeyEvent.VK_UP:
                    keyState &= ~FORWARD;
                    break;
                case KeyEvent.VK_DOWN:
                    keyState &= ~BACKWARD;
                    break;
                case KeyEvent.VK_LEFT:
                    keyState &= ~LEFT;
                    break;
                case KeyEvent.VK_RIGHT:
                    keyState &= ~RIGHT;
                    break;
                case KeyEvent.VK_EQUALS:
                    keyState &= ~PLUS;
                    break;
                case KeyEvent.VK_MINUS:
                    keyState &= ~MINUS;
                    break;
                case KeyEvent.VK_SPACE:
                    keyState &= ~SPACE;
                    gameSoundPlayer.pauseSound(GUN_FIRE); // stop shooting.
                    break;
            }
        }

        /**
         * This is the keyPressed implemented method.
         * 
         * @param e
         *            The KeyEvent.
         */
        public void keyPressed(KeyEvent e)
        {
            int code = e.getKeyCode();

            if (console)
            {
                // Append he sleected character to the command String to make up
                // the console command:

                switch (code)
                {
                    case KeyEvent.VK_0:
                        command += "0";
                        break;
                    case KeyEvent.VK_1:
                        command += "1";
                        break;
                    case KeyEvent.VK_2:
                        command += "2";
                        break;
                    case KeyEvent.VK_3:
                        command += "3";
                        break;
                    case KeyEvent.VK_4:
                        command += "4";
                        break;
                    case KeyEvent.VK_5:
                        command += "5";
                        break;
                    case KeyEvent.VK_6:
                        command += "6";
                        break;
                    case KeyEvent.VK_7:
                        command += "7";
                        break;
                    case KeyEvent.VK_8:
                        command += "8";
                        break;
                    case KeyEvent.VK_9:
                        command += "9";
                        break;
                    case KeyEvent.VK_A:
                        command += "a";
                        break;
                    case KeyEvent.VK_B:
                        command += "b";
                        break;
                    case KeyEvent.VK_C:
                        command += "c";
                        break;
                    case KeyEvent.VK_D:
                        command += "d";
                        break;
                    case KeyEvent.VK_E:
                        command += "e";
                        break;
                    case KeyEvent.VK_F:
                        command += "f";
                        break;
                    case KeyEvent.VK_G:
                        command += "g";
                        break;
                    case KeyEvent.VK_H:
                        command += "h";
                        break;
                    case KeyEvent.VK_I:
                        command += "i";
                        break;
                    case KeyEvent.VK_J:
                        command += "j";
                        break;
                    case KeyEvent.VK_K:
                        command += "k";
                        break;
                    case KeyEvent.VK_L:
                        command += "l";
                        break;
                    case KeyEvent.VK_M:
                        command += "m";
                        break;
                    case KeyEvent.VK_N:
                        command += "n";
                        break;
                    case KeyEvent.VK_O:
                        command += "o";
                        break;
                    case KeyEvent.VK_P:
                        command += "p";
                        break;
                    case KeyEvent.VK_Q:
                        command += "q";
                        break;
                    case KeyEvent.VK_R:
                        command += "r";
                        break;
                    case KeyEvent.VK_S:
                        command += "s";
                        break;
                    case KeyEvent.VK_T:
                        command += "t";
                        break;
                    case KeyEvent.VK_U:
                        command += "u";
                        break;
                    case KeyEvent.VK_V:
                        command += "v";
                        break;
                    case KeyEvent.VK_W:
                        command += "w";
                        break;
                    case KeyEvent.VK_X:
                        command += "x";
                        break;
                    case KeyEvent.VK_Y:
                        command += "y";
                        break;
                    case KeyEvent.VK_Z:
                        command += "z";
                        break;
                    case KeyEvent.VK_SPACE:
                        command += " ";
                        break;
                    case KeyEvent.VK_COMMA:
                        command += ",";
                        break;
                    case KeyEvent.VK_PERIOD:
                        command += ".";
                        break;
                    case KeyEvent.VK_UNDERSCORE:
                        command += "_";
                        break;
                    case KeyEvent.VK_BACK_SLASH:
                        command += "\\";
                        break;
                    case KeyEvent.VK_SLASH:
                        command += "/";
                        break;
                    case KeyEvent.VK_BACK_SPACE: // Backspace operation:
                        if (command.length() > 0)
                            command = command.substring(0, (command.length() - 1));
                        break;
                    case KeyEvent.VK_ENTER:
                    {
                        // Execute the console command:
                        handleConsoleCommand(command);
                        console = false;
                        break;
                    }
                }
            }
            else
            {

                // Retrieve the VK code, then "bitwise or" the key state
                // variable with the specific key state:

                switch (code)
                {
                    case KeyEvent.VK_UP:
                    {
                        keyState |= FORWARD;

                        if (menu)
                        {
                            selected--;
                            if (selected < 0)
                                selected = (menuItems.length - 1);
                        }
                        break;
                    }
                    case KeyEvent.VK_DOWN:
                    {
                        keyState |= BACKWARD;

                        if (menu)
                        {
                            selected++;
                            if (selected > (menuItems.length - 1))
                                selected = 0;
                        }
                        break;
                    }
                    case KeyEvent.VK_A:
                        keyState |= LEFT;
                        break;
                    case KeyEvent.VK_LEFT:
                        keyState |= LEFT;
                        break;
                    case KeyEvent.VK_D:
                        keyState |= RIGHT;
                        break;
                    case KeyEvent.VK_RIGHT:
                        keyState |= RIGHT;
                        break;
                    case KeyEvent.VK_SPACE:
                        if (((Plane) planes.get(PLAYER)).isAlive())
                        {
                            keyState |= SPACE;
                            gameSoundPlayer.loopSound(GUN_FIRE); // Fire the
                            // gun.
                        }
                        break;
                    case KeyEvent.VK_EQUALS:
                        keyState |= PLUS;
                        break;
                    case KeyEvent.VK_MINUS:
                        keyState |= MINUS;
                        break;
                    case KeyEvent.VK_ESCAPE:
                        menu = !menu;
                        if (LOCAL)
                        {
                            if (menu)
                            {
                                if (((Plane) planes.get(PLAYER)).isAlive())
                                    gameSoundPlayer.pauseSound(ENGINE);

                                world.animateFires(false); // pasue the fire
                                // animations.
                            }
                            else
                            {
                                if (((Plane) planes.get(PLAYER)).isAlive())
                                    gameSoundPlayer.loopSound(ENGINE);

                                world.animateFires(true);
                            }
                        }
                        break;
                    case KeyEvent.VK_ENTER:
                    {
                        if (menu)
                        {
                            if (menuItems[selected].equalsIgnoreCase(MENU_CONTINUE))
                            {
                                // continue with the game:

                                menu = false;

                                if (LOCAL)
                                {
                                    if (((Plane) planes.get(PLAYER)).isAlive())
                                        gameSoundPlayer.loopSound(ENGINE);

                                    world.animateFires(true);
                                }
                            }
                            else
                                if (menuItems[selected].equalsIgnoreCase(MENU_EXIT))
                                {
                                    int choice = JOptionPane.showConfirmDialog(new JPanel(), "Do you really want to quit", "Confirm quit",
                                            JOptionPane.YES_NO_OPTION);
                                    if (choice == JOptionPane.YES_OPTION)
                                        close(); // call the close() lceanup
                                    // method.
                                    break;
                                }
                        }
                        else
                        {
                            command = new String();
                            if (LOCAL)
                            {
                                gameSoundPlayer.pauseSound(ENGINE);
                                world.animateFires(false);
                            }
                            console = true;
                        }
                        break;
                    }
                    case KeyEvent.VK_F1:
                    {
                        // These ones change the view:

                        if (viewChangeable)
                            setView(VIEW_FIRST_PERSON);

                        break;
                    }
                    case KeyEvent.VK_F2:
                    {
                        if (viewChangeable)
                            setView(VIEW_THIRD_PERSON);

                        break;
                    }
                    case KeyEvent.VK_F3:
                    {
                        if (viewChangeable)
                            setView(VIEW_AERIAL);

                        break;
                    }
                    case KeyEvent.VK_F4:
                    {
                        if (viewChangeable)
                            setView(VIEW_FRONT);

                        break;
                    }
                    case KeyEvent.VK_F5:
                    {
                        if (viewChangeable)
                            setView(VIEW_STILL);

                        break;
                    }
                    case KeyEvent.VK_F6:
                    {
                        if (viewChangeable)
                            setView(VIEW_ENEMY);

                        break;
                    }
                    //These three toggle the display settings:
                    case KeyEvent.VK_V:
                    {

                        displayVerbose = !displayVerbose;
                        break;
                    }
                    case KeyEvent.VK_H:
                        displayHealth = !displayHealth;
                        break;
                    case KeyEvent.VK_M:
                        displayMap = !displayMap;
                        break;
                }
            }
        }

        /**
         * The implemented KeyListener.keyTyped.
         * 
         * @param e
         *            The keyEvent.
         */
        public void keyTyped(KeyEvent e)
        {
        }

        /**
         * Sets the view state to the value passed to it.
         * 
         * @param viewStateIn
         *            The new view state.
         */
        private void setView(int viewStateIn)
        {
            viewState = viewStateIn;
        }
    }
}