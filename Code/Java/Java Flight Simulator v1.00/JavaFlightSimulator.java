import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;

import com.sun.j3d.utils.image.TextureLoader;

/**
 * JavaFlightSimulator is the main class of this project. It is responsible for
 * running the user interface of the program, handling database operations of
 * the program, and of launching the game.
 * 
 * Please note that after pressing either the "Fly Now" or "Host Game" or "Join
 * Game" buttons to launch the game you will need to click on the screen to give
 * focus to the game before your key input will be recognized.
 * 
 * @author James Attenborough
 */
public class JavaFlightSimulator extends JFrame implements WindowListener
{
    // This class is an extension of the JFrame class. It also implements the
    // WindowListener interface, which allows this class to perform any
    // essential closing operations when the application is closing down.

    // The content pane for this window:
    private Container c = this.getContentPane();

    private BackgroundPanel backgroundPanel; // This BackgroundPanel is used to
    // display a specifiec background Image or Color. Other components should be
    // set to be transparent (using setOpaque(false)), and then added to this
    // panel.

    // These variables manage the database operations of the application:
    private final String DATABASE = "GameData.mdb"; // The filename of the
    // database for this application.
    private Connection connection; // The connection to the database.
    private Statement statement; // This is an all-purpose statement.
    private Statement crosshairStatement; // This is used to retrieve crosshair
    // information.
    private Statement crosshairImageStatement; // This is used to retrieve
    // crosshair image filenames.
    private Statement optionsFetchStatement; // This is used to retrieve the
    // game options from the database.
    private Statement launchPlaneStatement; // This is used to retrieve
    // the plane's details when launching the game.
    private Statement launchLocationStatement; // This is used to
    // retrieve the location's details when launching the game.
    private Statement planeStatsStatement; // This is used to retrieve the
    // statistics of the currently selected plane.
    private Statement locationStatsStatement; // This is used to retrieve the
    // statistics of the currently selected location.
    private Statement planeAvatarStatement; // This is used to retrieve the
    // avatar filename of the currently selected plane.
    private Statement locationAvatarStatement; // This is used to retrieve the
    // avatar filename of the currently selected location.
    private PreparedStatement optionsUpdateStatement; // This PreparedStatement
    // is used to update the game options.

    private GraphicsEnvironment environment; // The GraphicsEnvironment
    // associated with this application.
    private GraphicsConfiguration configuration; // The GraphicsConfiguration
    // which defines how the game's graphics are displayed.

    private final Image MAIN; // The main menu background Image.

    private JPanel mainButtons = new JPanel(); // The JPanel that holds the main
    // menu screen buttons.
    private JPanel soloButtons = new JPanel(); // The JPanel that holds the solo
    // flight screen buttons.
    private JPanel dogfightHostButtons = new JPanel(); // The JPanel that holds
    // the Host Network Dogfight screen buttons.
    private JPanel dogfightJoinButtons = new JPanel();// The JPanel that holds
    // the Join Network Dogfight screen buttons.
    private JPanel optionsButtons = new JPanel();// The JPanel that holds the
    // options screen buttons.
    private JPanel aboutButtons = new JPanel();// The JPanel that holds the
    // about screen buttons.

    private JComboBox planeComboBox = new JComboBox(); // This holds the list
    // of available planes.
    private JLabel planeImageLabel = new JLabel(); // Displays the avatar of
    // the currently selected plane.
    private JComboBox locationComboBox = new JComboBox(); // This holds the list
    // of available locations.
    private JLabel locationImageLabel = new JLabel(); // Displays the avatar of
    // the currently selected location.

    private JTextField pitchOutput, rollOutput, speedOutput; // These are used
    // to output the currently selected plane's pitch, roll and speed constants.

    // These are used to store the statistics of the currently selected
    // location:
    private JTextField theatreOutput; // The Theatre to which it belongs,
    private JTextField locationOutput; // The starting location,
    private JTextField heightRangeOutput; // The height range,
    private JTextField objectsOutput; // The number of GroundObjects,
    private JTextField waterOutput; // The water level,
    private JTextField nightOutput; // Whether this is a night location, and
    private JTextField fogOutput; // Whether this location has fog.

    private JTextField hostNameInput; // This is used to input the computer name
    // of the host whose game you wish to join.

    private boolean optionsChanged = false; // Keeps track of whether any of the
    // game options have been changed.
    private JComboBox crosshairComboBox = new JComboBox(); // Holds the list of
    // available crosshairs.
    private JLabel crosshairImageLabel = new JLabel(); // Displays the image of
    // the currently selected crosshair.
    private JTextField playerStrengthOutput; // Used to output the current
    // player strength setting.

    // This JSlider is used to input the player's strength setting:
    private JSlider strengthSlider = new JSlider(Plane.MIN_LEVEL, Plane.MAX_LEVEL);
    private JCheckBox animateWaterCheckBox; // Holds wheteher or not animated
    // water to be used.
    private JButton applyOptionsButton = new JButton("  Apply  "); // The button
    // that, when pressed, updates the game options in the database.

    private MediaTracker tracker; // The MediaTracker used to load images before
    // displaying them. Sometimes, in the case of very large images, the
    // application will not wait for the Image to load. The MediaTracker causes
    // the application to wait until it has loaded the Image completely before
    // proceeding.
    private final int MENU_IMAGE_ID = 0; // The ID for menu images.
    private final int PLANE_IMAGE_ID = 1; // The ID for plane avatars.
    private final int LOCATION_IMAGE_ID = 2; // The ID for location avatars.
    private final int CROSSHAIR_IMAGE_ID = 3; // The ID for crosshair avatars.
    private final int LAUNCH_IMAGE_ID = 4; // The ID for images needed when
    // launching the game.

    private FlightGamePanel flightGamePanel; // This FlightGamePanel is the
    // JPanel extension which manages the input, graphics, sound and networking
    // of the game.

    private final int SOLO_FLIGHT = (1 << 0); // This flag signals that the mode
    // of the game to launch is Solo Flight mode.
    private final int NETWORK_DOGFIGHT_HOST = (1 << 1); // This flag signals
    // that the mode of the game to launch is Host Network Dogfight mode.
    private final int NETWORK_DOGFIGHT_JOIN = (1 << 2); // This flag signals
    // that the mode of the game to launch is Join Network Dogfight mode.

    private final Color OUTPUT_FIELD_BACKGROUND = new Color(38, 38, 38); // This
    // holds the background colour of all the JTextField objects used to output
    // information.
    private final Color OUTPUT_FIELD_FOREGROUND = new Color(238, 238, 238); // This
    // holds the foreground colour of all the JTextField objects used to output
    // information.

    private JPanel planePanel = new JPanel(); // This JPanel is used to hold the
    // JComboBox used to select a plane and the avatar and statistics releveant
    // to the selected plane.
    private JPanel locationPanel = new JPanel(); // This JPanel is used to hold
    // the JComboBox used to select a location and the avatar and statistics
    // releveant to the selected location.
    private JPanel flightSelectionPanel = new JPanel(); // This JPanel is used
    // to hold the plane and location selection JPanel objects defined above.

    private JLabel soloHeadingLabel; // The heading for the Solo Flight screen.
    private JLabel soloInstructionsLabel; // The instructions to be placed on
    // the Solo Flight screen.
    private JPanel soloUpper = new JPanel(); // This JPanel holds all the Solo
    // Flight screen heading and instructions components.
    private JPanel lowerSolo = new JPanel(); // This JPanel is placed at the
    // bottom of the Solo Flight screen, and holds the "Main Menu" and "Fly Now"
    // buttons.

    private JLabel hostHeadingLabel; // The heading for the Host Network
    // Dogfight screen.
    private JLabel hostInstructionsLabel; // The instructions to be placed on
    // the Host Network Dogfight flight screen.
    private JPanel hostUpper = new JPanel(); // This JPanel holds all the Host
    // Network Dogfight screen heading and instructions components.
    private JPanel lowerDogfightHost = new JPanel(); // This JPanel is placed at
    // the bottom of the Host Network Dogfight screen, and holds the "Main Menu"
    // and "Host Game" buttons.

    private JLabel joinHeadingLabel; // The heading for the Join Network
    // Dogfight screen.
    private JLabel joinInstructionsLabel; // The instructions to be placed on
    // the Join Network Dogfight screen.
    private JPanel joinUpperNorthPanel = new JPanel(); // This panel is used to
    // hold the heading and instructions on the Join Network Dogfight screen.
    private JPanel joinUpper = new JPanel(); // This JPanel holds the Join
    // Network Dogfight screen heading and instructions components.
    private JPanel lowerDogfightJoin = new JPanel(); // This JPanel is placed at
    // the bottom of the Join Network Dogfight screen, and holds the "Main Menu"
    // and "Join Game" buttons.

    private JLabel optionsHeadingLabel; // The heading for the Game Options
    // screen.
    private JLabel optionsInstructionsLabel; // The instructions to be placed on
    // the Game Options screen.
    private JPanel optionsUpper = new JPanel(); //  This JPanel holds the Game
    // Options screen heading and instructions components.

    private final int AI_START_NUM = 5; // The number of AI enemies to start a
    // Solo Flight game with.

    private final Color SOLO_BACK = new Color(2, 20, 24); // The background
    // colour of the Solo Flight screen.
    private final Color SOLO_FORE = new Color(184, 207, 229); // The foreground
    // colour of the Solo Flight screen.
    private final Color HOST_BACK = new Color(57, 65, 28); // The background
    // colour of the Host Network Dogfight screen.
    private final Color HOST_FORE = new Color(145, 153, 113); // The foreground
    // colour of the Host Network Dogfight screen.
    private final Color JOIN_BACK = new Color(111, 95, 65); // The background
    // colour of the Join Network Dogfight screen.
    private final Color JOIN_FORE = new Color(229, 215, 189); // The foreground
    // colour of the Join Network Dogfight screen.
    private final Color OPTIONS_BACK = new Color(94, 0, 0); // The background
    // colour of the Game Options screen.
    private final Color OPTIONS_FORE = new Color(128, 75, 66); // The foreground
    // colour of the Game Options screen.
    private final Color ABOUT_BACK = new Color(65, 43, 5); // The background
    // colour of the About screen.
    private final Color ABOUT_FORE = new Color(35, 24, 3); // The foreground
    // colour of the About screen.

    private ActionListener actionMainMenu; // This ActionListener is used to
    // return to the Main Menu screen from all the other screens.

    // This String holds the playing instructions for the game, and uses HTML
    // (this String is appended to JLabel objects which use HTML):
    private final String CONTROLS_TEXT = "Menu - <b>Escape</b>" + "<li>Console - <b>Enter</b>" + "<li><u><b>Flight Controls:</b></u>"
            + "<li>Pitch Up - <b>Up Arrow</b>" + "<li>Pitch Down - <b>Down Arrow</b>" + "<li>Roll Right - <b>Right Arrow</b>"
            + "<li>Roll Left - <b>Left Arrow</b>" + "<li>Shoot - <b>Space</b>" + "<li><u><b>View:</b></u>"
            + "<li>First Person - <b>F1</b>, Third Person - <b>F2</b>," + " Aerial - <b>F3</b>, Front - <b>F4</b>,"
            + " Still Camera - <b>F5</b>, Nearest Enemy - <b>F6</b>" + "<li><u><b>Important Console Commands:</b></u>"
            + "<li><b>addenemy %</b> - Where % is an integer value from " + Plane.MIN_LEVEL + " to " + Plane.MAX_LEVEL
            + ", adds an AI enemy to the game with the specified skill level"
            + "<li><b>players</b> - Displays the health of all players in the game.";

    /**
     * The JavaFlightSimulator constructor. This is used when launching a new
     * JavaFlightSimulationor application.
     * 
     * @author James Attenborough
     */
    public JavaFlightSimulator()
    {
        super("Java Flight Simulator"); // Call the superclass' constructor, in
        // this case, that of the JFrame.

        addWindowListener(this); // Adds the implemented and instantiated
        // WindowListener to this application.

        MAIN = this.getToolkit().createImage("image//intro.jpg"); // Creates the
        // Image used as the background for the Main Menu screen.

        // There is no point in continuing unless the database was initialized
        // successfully:
        if (initializeDatabase())
        {
            tracker = new MediaTracker(this); // Initializes the MediaTracker
            // using this Component.

            tracker.addImage(MAIN, MENU_IMAGE_ID); // Add the Man Menu
            // background Image to the tracker.

            try
            {
                tracker.waitForID(MENU_IMAGE_ID); // Causes this application to
                // first load the Image before doing anything else.
            }
            catch (InterruptedException e)
            {
                // If an InterruptedException was thrown, then print the stack
                // trace of that exception:
                e.printStackTrace(System.err);
            }

            configuration = initializeGraphicsConfiguration(); // Initializes
            // the necessary graphics objects, and returns the best
            // GraphicsConfiguration to use for the game.

            initializeUserInterface(); // Initializes all the User Interface
            // elements of this application.
        }
        else
        {
            // Tell the user that there is a problem with their database:
            JOptionPane.showMessageDialog(this, "The database could not be initialized.");
        }
    }

    /**
     * This method initializes the GraphicsEnvironment member, and returns the
     * best Graphicsconfiguration object for this computer.
     * 
     * @return the best GraphicsConfiguration for this particular computer.
     */
    private GraphicsConfiguration initializeGraphicsConfiguration()
    {
        // A GraphicsConfiguration object is needed to initialize a Canvas3D
        // object, and there are four steps to getting one:

        // 1. Get the local GraphicsEnvironment, which holds the
        // available GraphicsDevice and Font objects:
        environment = GraphicsEnvironment.getLocalGraphicsEnvironment();

        // 2. Get the default GraphicsDevice from our local GraphicsEnvironment
        // object:
        GraphicsDevice device = environment.getDefaultScreenDevice();

        // 3. Initialize a GraphicsConfigTemplate3D object and specify
        // anything over and above the default that is required:
        GraphicsConfigTemplate3D template = new GraphicsConfigTemplate3D();
        template.setSceneAntialiasing(GraphicsConfigTemplate3D.PREFERRED);

        // Use the GraphicsDevice object to return the best
        // GraphicsConfiguration object based on the GraphicsConfigTemplate3D:
        return device.getBestConfiguration(template);
    }

    /**
     * This method initializes the database, and creates the Connection to it
     * and all the necessary Statement and PreparedStatement objects.
     * 
     * @return true if the database was initialized successfully, otherwise
     *         false.
     */
    private boolean initializeDatabase()
    {
        boolean worked = true; // Keeps track of whether the database was
        // initialize successfully. It is true by default. If the initialization
        // fails, then it is set to false.

        try
        {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); // Returns the Class
            // object associated with the required database driver.

            // Creates the Connection to the database:
            connection = DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + DATABASE
                    + ";DriverID=22;READONLY=true}");

            // Initialize the following statements using the Connection member:

            statement = connection.createStatement(); // This is an all-purpose
            // statement.
            crosshairStatement = connection.createStatement(); // This is used
            // to retrieve crosshair information.
            crosshairImageStatement = connection.createStatement(); // This is
            // used to retrieve crosshair image filenames.
            optionsFetchStatement = connection.createStatement(); // This is
            // used to retrieve the game options from the database.
            launchPlaneStatement = connection.createStatement(); // This is used
            // to retrieve the plane's details when launching the game.
            launchLocationStatement = connection.createStatement(); // This is
            // used to retrieve the location's details when launching the game.
            planeAvatarStatement = connection.createStatement(); // This is used
            // to retrieve the statistics of the currently selected plane.
            locationAvatarStatement = connection.createStatement(); // This is
            // used to retrieve the statistics of the currently selected
            // location.
            planeStatsStatement = connection.createStatement(); // This is used
            // to retrieve the avatar filename of the currently selected plane.
            locationStatsStatement = connection.createStatement(); // This is
            // used to retrieve the avatar filename of the currently selected
            // location.

            // This PreparedStatement is used to update the game options:
            optionsUpdateStatement = connection
                    .prepareStatement("UPDATE GameOptions SET crosshairName = ?, playerStrength = ?, animateWater = ?");
        }
        catch (Exception e)
        {
            worked = false; // Signal that the database initialization failed.

            e.printStackTrace(System.err);
        }

        // Return whether or not the database was initialized successfully:
        return worked;
    }

    /**
     * This method initializes the User Interface of this application.
     */
    private void initializeUserInterface()
    {
        setUndecorated(true); // Removes the window decorations.
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Set this window's closing
        // operation.

        // Set the size of this window to the maximum window bounds of the local
        // GraphicsEnvironment, in other words, make this window fullscreen:
        setSize(environment.getMaximumWindowBounds().width, environment.getMaximumWindowBounds().height);
        setResizable(false); // Make this window non-resizable.

        c.setLayout(new BorderLayout()); // Set the layout of this window's
        // content pane to a BorderLayout.

        // Initialize the application's BackgroundPanel with a BorderLayout and
        // the Main Menu image:
        backgroundPanel = new BackgroundPanel(MAIN, environment.getMaximumWindowBounds().width, environment.getMaximumWindowBounds().height);
        backgroundPanel.setLayout(new BorderLayout());

        // Initialize the ActionListener which is used to return to the Main
        // Menu screen from any other one:
        actionMainMenu = (new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                backgroundPanel.setBackground(MAIN); // Set the background to
                // the Main Menu image.
                backgroundPanel.removeAll(); // Clear the BackgroundPanel of all
                // components.
                backgroundPanel.add(mainButtons, BorderLayout.NORTH); // Add the
                // Main Menu button JPanel to the application's BackgroundPanel.

                // Make sure that the change in components is made visible by
                // validating and repainting the necessary components:
                c.repaint();
                validate();
                repaint();
            }
        });

        initializeFlightSelectionPanel(); // Initializes all the elements and
        // components that make up the flight selection JPanel.

        // The folowing methods are responsible for initializing all the
        // elements and components that make up the respective screens:
        initializeMainMenuPanel(); // The Main Menu screen.
        initializeSoloPanelComponents(); // The Solo Flight screen.
        initializeHostPanelComponents(); // The Host Network Dogfight screen.
        initializeJoinPanelComponents(); // The Join Network Dogfight screen.
        initializeOptionsPanel(); // The Game Options screen.
        initializeAboutPanel(); // The About screen.

        // Add the Main Menu button JPanel to the application's BackgroundPanel:
        backgroundPanel.add(mainButtons, BorderLayout.NORTH);

        c.add(backgroundPanel); // Add the BackgroundPanel to this application's
        // content pane.

        setVisible(true); // Show the application window.

        // Make sure that the change in components is made visible by validating
        // and repainting the necessary components:
        c.repaint();
        validate();
        repaint();
    }

    /**
     * Initializes all the components of the Main Menu screen JPanel.
     */
    private void initializeMainMenuPanel()
    {
        // Initialize the buttons on the Main Menu screen:
        JButton solo = new JButton("Solo Flight");
        solo.setToolTipText("Click here to fly alone or against AI enemies.");
        JButton dogfightHost = new JButton("Host Network Dogfight");
        dogfightHost.setToolTipText("Click here to host a network game.");
        JButton dogfightJoin = new JButton("Join Network Dogfight");
        dogfightJoin.setToolTipText("Click here to join a network game.");
        JButton options = new JButton("Game Options");
        options.setToolTipText("Click here to change any of the game options.");
        JButton about = new JButton("About");
        about.setToolTipText("Click here to read more about this game.");
        JButton exit = new JButton("Exit");
        exit.setToolTipText("Click here to exit the game.");

        // This is the ActionListener for the "Solo Flight" button on the Main
        // Menu screen:
        solo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                populatePlaneList(); // Retrieves the most up-to-the-minute list
                // of available planes.
                populateLocationList();// Retrieves the most up-to-the-minute
                // list of available locations.

                backgroundPanel.setBackground(SOLO_BACK); // Changes the
                // BackgroundPanel's background to the Solo Flight background
                // colour.
                backgroundPanel.removeAll(); // Removes all the existing
                // components from the BackgroundPanel.

                // Clear the Solo Flight screen main component panel:
                soloButtons.removeAll();

                // Clear the panel that holds the flight details selection
                // panels, and then add rebuild it with whichever flight
                // selection panels are needed, in this case, both the plane and
                // location panels:
                flightSelectionPanel.removeAll();
                flightSelectionPanel.setOpaque(false); // Must be transparent so
                // that the BackgroundPanel can be seen clearly.
                flightSelectionPanel.setLayout(new FlowLayout());
                flightSelectionPanel.add(planePanel); // Add the plane selection
                // panel.
                flightSelectionPanel.add(locationPanel); // Add the location
                // selection panel.

                // Create a new JPanel to hold all the Solo Flight screen
                // components:
                JPanel soloAllPanel = new JPanel();
                soloAllPanel.setOpaque(false);
                soloAllPanel.setLayout(new BorderLayout());
                soloAllPanel.add(soloUpper, BorderLayout.NORTH); // Add the
                // panel that contains the heading and instructions.
                soloAllPanel.add(flightSelectionPanel); // Add the panel that
                // allows users to select their plane and location.
                soloAllPanel.add(lowerSolo, BorderLayout.SOUTH); // Add the
                // panel that holds the buttons for the Solo Flight screen.

                soloButtons.add(soloAllPanel, BorderLayout.CENTER); // Add the
                // panel that holds all the necessary components to the main
                // Solo Flight screen panel.

                backgroundPanel.add(soloButtons); // Add the main Solo Flight
                // screen panel to the BackgroundPanel (This is essentially just
                // laying this panel on top of the BackgroundPanel, as all the
                // components have transparent backgrounds).

                // Make sure that the changes are visible:
                validate();
                repaint();
            }
        });

        // This is the ActionListener for the "Host Network Dogfight" button on
        // the Main Menu screen:
        dogfightHost.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                populatePlaneList(); // Retrieves the most up-to-the-minute list
                // of available planes.
                populateLocationList();// Retrieves the most up-to-the-minute
                // list of available locations.

                backgroundPanel.setBackground(HOST_BACK); // Changes the
                // BackgroundPanel's background to the Host Network Dogfight
                // background colour.
                backgroundPanel.removeAll(); // Removes all the existing
                // components from the BackgroundPanel.

                // Clear the Host Network Dogfight screen main component panel:
                dogfightHostButtons.removeAll();

                // Clear the panel that holds the flight details selection
                // panels, and then add rebuild it with whichever flight
                // selection panels are needed, in this case, only the plane
                // selection panel:
                flightSelectionPanel.removeAll();
                flightSelectionPanel.setOpaque(false);
                flightSelectionPanel.setLayout(new FlowLayout());
                flightSelectionPanel.add(planePanel);

                // Create a new JPanel to hold all the Host Network Dogfight
                // screen components:
                JPanel dogfightHostPanel = new JPanel();
                dogfightHostPanel.setOpaque(false);
                dogfightHostPanel.setLayout(new BorderLayout());
                dogfightHostPanel.add(hostUpper, BorderLayout.NORTH);// Add the
                // panel that contains the heading and instructions.
                dogfightHostPanel.add(flightSelectionPanel); // Add the panel
                // that allows users to select their plane.
                dogfightHostPanel.add(lowerDogfightHost, BorderLayout.SOUTH); // Add
                // the panel that holds the buttons for the Host Network
                // Dogfight screen.

                // Add the panel that holds all the necessary components to the
                // main Host Network Dogfight screen panel:
                dogfightHostButtons.add(dogfightHostPanel, BorderLayout.CENTER);

                backgroundPanel.add(dogfightHostButtons);// Add the main Host
                // Network Dogfight screen panel to the BackgroundPanel (This is
                // equivalent laying this panel on top of the BackgroundPanel,
                // as all the components have transparent backgrounds).

                // Make sure that the changes are visible:
                validate();
                repaint();
            }
        });

        // This is the ActionListener for the "Join Network Dogfight" button on
        // the Main Menu screen:
        dogfightJoin.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                populatePlaneList(); // Retrieve the most up-to-the-minute list
                // of available planes.

                backgroundPanel.setBackground(JOIN_BACK); // Change the
                // BackgroundPanel's background to the Join Network Dogfight
                // screen background colour.
                backgroundPanel.removeAll(); // Removes all the existing
                // components from the BackgroundPanel.

                // Clear the Join Network Dogfight screen main component panel:
                dogfightJoinButtons.removeAll();

                // Clear the panel that holds the flight details selection
                // panels, and then add rebuild it with whichever flight
                // selection panels are needed, in this case, only the plane
                // selection panel:
                flightSelectionPanel.removeAll();
                flightSelectionPanel.setOpaque(false);
                flightSelectionPanel.setLayout(new FlowLayout());
                flightSelectionPanel.add(planePanel);

                // Construct the JPanel that holds the components needed to
                // input the values needed to join a network dogfight:
                JPanel joinInputPanel = new JPanel();
                joinInputPanel.setOpaque(false);
                joinInputPanel.setLayout(new FlowLayout());
                JLabel hostNameInputLabel = new JLabel("Enter the host's name: ");
                hostNameInputLabel.setForeground(Color.white);
                hostNameInput.setText("");
                hostNameInput.setColumns(14);
                joinInputPanel.add(hostNameInputLabel);
                joinInputPanel.add(hostNameInput); // Add the JTextField that
                // allows the user to input the host's computer name.

                // Clear the upper panel of all of it's components and rebuild
                // it with the heading, instructions and input components:
                joinUpper.removeAll();
                joinUpper.add(joinUpperNorthPanel, BorderLayout.NORTH); // Add
                // the heading and instructions.
                joinUpper.add(joinInputPanel, BorderLayout.SOUTH); // Add the
                // panel for input.

                // Create a new JPanel to hold all the Join Network Dogfight
                // screen components:
                JPanel dogfightJoinPanel = new JPanel();
                dogfightJoinPanel.setOpaque(false);
                dogfightJoinPanel.setLayout(new BorderLayout());

                dogfightJoinPanel.add(joinUpper, BorderLayout.NORTH); // Add the
                // panel that contains the heading and instructions.
                dogfightJoinPanel.add(flightSelectionPanel); // Add the panel
                // that allows users to select their plane.
                dogfightJoinPanel.add(lowerDogfightJoin, BorderLayout.SOUTH); // Add
                // the panel that holds the buttons for the Join Network
                // Dogfight screen.

                // Add the panel that holds all the necessary components to the
                // main Join Network Dogfight screen panel:
                dogfightJoinButtons.add(dogfightJoinPanel, BorderLayout.CENTER);

                backgroundPanel.add(dogfightJoinButtons); // Add the main Join
                // Network Dogfight screen panel to the BackgroundPanel (This is
                // equivalent laying this panel on top of the BackgroundPanel,
                // as all the components have transparent backgrounds).

                // Make sure that the changes are visible:
                validate();
                repaint();
            }
        });

        // This is the ActionListener for the "Game Options" button on
        // the Main Menu screen:
        options.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                retrieveCurrentOptions(); // Retrieve the most up-to-the-minute
                // game options data from the database.

                backgroundPanel.setBackground(OPTIONS_BACK); // Change the
                // BackgroundPanel's background to the Game Options screen
                // background colour.
                backgroundPanel.removeAll(); // Removes all the existing
                // components from the BackgroundPanel.

                backgroundPanel.add(optionsButtons); // Add the main Game
                // Options screen panel to the BackgroundPanel (This is
                // equivalent laying this panel on top of the BackgroundPanel,
                // as all the components have transparent backgrounds).

                // Make sure that the changes are visible:
                validate();
                repaint();
            }
        });

        // This is the ActionListener for the "About" button on
        // the Main Menu screen:
        about.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                backgroundPanel.setBackground(ABOUT_BACK); // Change the
                // BackgroundPanel's background to the About screen background
                // colour.
                backgroundPanel.removeAll(); // Removes all the existing
                // components from the BackgroundPanel.

                backgroundPanel.add(aboutButtons); // Add the main About screen
                // panel to the BackgroundPanel (This is equivalent laying this
                // panel on top of the BackgroundPanel, as all the components
                // have transparent backgrounds).

                // Make sure that the changes are visible:
                validate();
                repaint();
            }
        });

        // This is the ActionListener for the "Exit" button on
        // the Main Menu screen:
        exit.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // Prompt the user to decide whether he really does want to
                // exit:
                int choice = JOptionPane.showConfirmDialog(c, "Are you sure you want to quit?", "Really?", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION)
                {
                    // If the user chooses to exit, then run the method which
                    // cleans up after the program:
                    cleanUp();
                }
            }
        });

        // Add the Main Menu buttons using a GridLayout:
        mainButtons.setLayout(new GridLayout(1, 6));
        mainButtons.add(solo);
        mainButtons.add(dogfightHost);
        mainButtons.add(dogfightJoin);
        mainButtons.add(options);
        mainButtons.add(about);
        mainButtons.add(exit);
    }

    /**
     * Initializes all the components of the flight selection panel.
     */
    private void initializeFlightSelectionPanel()
    {
        // This is the ActionListener for the JComboBox which holds the list of
        // available planes:
        planeComboBox.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // Change's the image displayed to the currently selected
                // plane's avatar:
                changePlaneAvatar(String.valueOf(planeComboBox.getSelectedItem()));

                // Updates the displayed plane details to those of the
                // currently selected plane:
                fetchPlaneDetails(String.valueOf(planeComboBox.getSelectedItem()));
            }
        });

        // This is the ActionListener for the JComboBox which holds the list of
        // available locations:
        locationComboBox.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // Change's the image displayed to the currently selected
                // location's avatar:
                changeLocationAvatar(String.valueOf(locationComboBox.getSelectedItem()));

                // Updates the displayed location details to those of the
                // currently selected location:
                fetchLocationDetails(String.valueOf(locationComboBox.getSelectedItem()));
            }
        });

        // This is the ActionListener for the JComboBox which holds the list of
        // available crosshairs:
        crosshairComboBox.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // Change's the image displayed to that of the currently
                // selected crosshair:
                changeCrosshairImage(String.valueOf(crosshairComboBox.getSelectedItem()));

                optionsChanged = true; // Signal to the program that a game
                // option
                // has been changed.
                applyOptionsButton.setEnabled(true); // Allow the user to apply
                // the changed options.
            }
        });

        // Initialize the JPanels which are used to hold all the components
        // associated with plane selection:
        planePanel.setLayout(new BorderLayout());
        JPanel planeTop = new JPanel();
        planeTop.setLayout(new BorderLayout());
        JPanel planeBottom = new JPanel();
        planeBottom.setLayout(new BorderLayout());
        JPanel planeStatsPanel = new JPanel();
        planeStatsPanel.setLayout(new GridLayout(7, 2));

        // Initialize the JTextField objects used to output the statistics of
        // the currently selected plane (they are all set to be non-editable and
        // have their colour changed to distinguish them from fields where input
        // it expected):
        pitchOutput = new JTextField();
        pitchOutput.setEditable(false);
        pitchOutput.setBackground(OUTPUT_FIELD_BACKGROUND);
        pitchOutput.setForeground(OUTPUT_FIELD_FOREGROUND);
        rollOutput = new JTextField();
        rollOutput.setEditable(false);
        rollOutput.setBackground(OUTPUT_FIELD_BACKGROUND);
        rollOutput.setForeground(OUTPUT_FIELD_FOREGROUND);
        speedOutput = new JTextField();
        speedOutput.setEditable(false);
        speedOutput.setBackground(OUTPUT_FIELD_BACKGROUND);
        speedOutput.setForeground(OUTPUT_FIELD_FOREGROUND);

        // Set up the panels which allow the user to choose a plane:
        JLabel selectPlaneLabel = new JLabel("Select Your Plane: ");
        selectPlaneLabel.setForeground(Color.white);
        planeTop.add(selectPlaneLabel, BorderLayout.NORTH);
        planeImageLabel.setToolTipText("The avatar of your selected plane.");
        planeTop.add(planeImageLabel, BorderLayout.SOUTH);
        planeComboBox.setToolTipText("Choose a plane from this list of available ones.");
        planeBottom.add(planeComboBox);

        // Set up the plane statistics output panel (when adding a new
        // JLabel(""), a visually blank component is being used to fill a
        // space):
        JLabel pitchLabel = new JLabel("Pitch Constant: ");
        pitchLabel.setForeground(Color.white);
        JLabel rollLabel = new JLabel("Roll Constant: ");
        rollLabel.setForeground(Color.white);
        JLabel speedLabel = new JLabel("Speed Constant: ");
        speedLabel.setForeground(Color.white);
        planeStatsPanel.add(new JLabel(""));
        planeStatsPanel.add(new JLabel(""));
        planeStatsPanel.add(pitchLabel);
        planeStatsPanel.add(pitchOutput);
        planeStatsPanel.add(new JLabel(""));
        planeStatsPanel.add(new JLabel(""));
        planeStatsPanel.add(rollLabel);
        planeStatsPanel.add(rollOutput);
        planeStatsPanel.add(new JLabel(""));
        planeStatsPanel.add(new JLabel(""));
        planeStatsPanel.add(speedLabel);
        planeStatsPanel.add(speedOutput);
        planeStatsPanel.add(new JLabel(""));
        planeStatsPanel.add(new JLabel(""));

        planeBottom.add(planeStatsPanel, BorderLayout.SOUTH);

        // Add the separate component holder panels to the main plane selection
        // panel:
        planePanel.add(planeTop, BorderLayout.NORTH);
        planePanel.add(planeBottom, BorderLayout.SOUTH);

        // Initialize the JPanels which are used to hold all the components
        // associated with location selection:
        locationPanel.setLayout(new BorderLayout());
        JPanel locationTop = new JPanel();
        locationTop.setLayout(new BorderLayout());
        JPanel locationBottom = new JPanel();
        locationBottom.setLayout(new BorderLayout());
        JPanel locationStatsPanel = new JPanel();
        locationStatsPanel.setLayout(new GridLayout(7, 2));

        // Initialize the JTextField objects used to output the statistics of
        // the currently selected location (they are all set to be non-editable
        // and have their colour changed to distinguish them from fields where
        // input it expected):
        theatreOutput = new JTextField();
        theatreOutput.setEditable(false);
        theatreOutput.setBackground(OUTPUT_FIELD_BACKGROUND);
        theatreOutput.setForeground(OUTPUT_FIELD_FOREGROUND);
        locationOutput = new JTextField();
        locationOutput.setEditable(false);
        locationOutput.setBackground(OUTPUT_FIELD_BACKGROUND);
        locationOutput.setForeground(OUTPUT_FIELD_FOREGROUND);
        heightRangeOutput = new JTextField();
        heightRangeOutput.setEditable(false);
        heightRangeOutput.setBackground(OUTPUT_FIELD_BACKGROUND);
        heightRangeOutput.setForeground(OUTPUT_FIELD_FOREGROUND);
        objectsOutput = new JTextField();
        objectsOutput.setEditable(false);
        objectsOutput.setBackground(OUTPUT_FIELD_BACKGROUND);
        objectsOutput.setForeground(OUTPUT_FIELD_FOREGROUND);
        waterOutput = new JTextField();
        waterOutput.setEditable(false);
        waterOutput.setBackground(OUTPUT_FIELD_BACKGROUND);
        waterOutput.setForeground(OUTPUT_FIELD_FOREGROUND);
        nightOutput = new JTextField();
        nightOutput.setEditable(false);
        nightOutput.setBackground(OUTPUT_FIELD_BACKGROUND);
        nightOutput.setForeground(OUTPUT_FIELD_FOREGROUND);
        fogOutput = new JTextField();
        fogOutput.setEditable(false);
        fogOutput.setBackground(OUTPUT_FIELD_BACKGROUND);
        fogOutput.setForeground(OUTPUT_FIELD_FOREGROUND);

        // Set up the panels which allow the user to choose a location:
        JLabel selectLocationLabel = new JLabel("Select Your Location: ");
        selectLocationLabel.setForeground(Color.white);
        locationTop.add(selectLocationLabel);
        locationImageLabel.setToolTipText("The avatar of your selected location.");
        locationTop.add(locationImageLabel, BorderLayout.SOUTH);
        locationComboBox.setToolTipText("Choose a location from this list of available ones.");
        locationBottom.add(locationComboBox);

        // Set up the location statistics output panel:
        JLabel theatreLabel = new JLabel("Theatre: ");
        theatreLabel.setForeground(Color.white);
        JLabel locationLabel = new JLabel("Start Location: ");
        locationLabel.setForeground(Color.white);
        JLabel heightRangeLabel = new JLabel("Height Range: ");
        heightRangeLabel.setForeground(Color.white);
        JLabel objectsLabel = new JLabel("Ground Objects: ");
        objectsLabel.setForeground(Color.white);
        JLabel waterLabel = new JLabel("Water Level: ");
        waterLabel.setForeground(Color.white);
        JLabel nightLabel = new JLabel("Night Time: ");
        nightLabel.setForeground(Color.white);
        JLabel fogLabel = new JLabel("Fog: ");
        fogLabel.setForeground(Color.white);
        // Add the statistics output components to this panel:
        locationStatsPanel.add(theatreLabel);
        locationStatsPanel.add(theatreOutput);
        locationStatsPanel.add(locationLabel);
        locationStatsPanel.add(locationOutput);
        locationStatsPanel.add(heightRangeLabel);
        locationStatsPanel.add(heightRangeOutput);
        locationStatsPanel.add(objectsLabel);
        locationStatsPanel.add(objectsOutput);
        locationStatsPanel.add(waterLabel);
        locationStatsPanel.add(waterOutput);
        locationStatsPanel.add(nightLabel);
        locationStatsPanel.add(nightOutput);
        locationStatsPanel.add(fogLabel);
        locationStatsPanel.add(fogOutput);

        locationBottom.add(locationStatsPanel, BorderLayout.SOUTH);

        // Add the separate component holder panels to the main location
        // selection panel:
        locationPanel.add(locationTop, BorderLayout.NORTH);
        locationPanel.add(locationBottom, BorderLayout.SOUTH);

        // Set the ToolTip text of each of the JTextFields used to output
        // statistics:
        pitchOutput.setToolTipText("The pitch constant of this plane.");
        rollOutput.setToolTipText("The roll constant of this plane.");
        speedOutput.setToolTipText("The speed constant of this plane.");
        theatreOutput.setToolTipText("The Theatre of this location.");
        locationOutput.setToolTipText("Your starting location.");
        heightRangeOutput.setToolTipText("The height range of this location.");
        objectsOutput.setToolTipText("The number of ground objects.");
        waterOutput.setToolTipText("The water level of this location.");
        nightOutput.setToolTipText("The time setting of this location.");
        fogOutput.setToolTipText("The fog setting of this location.");

        // Make sure that all the component's used on this panel (and the panel
        // itself) have transparent backgrounds so that the BackgroundPanel will
        // show up behind:
        planePanel.setOpaque(false);
        planeTop.setOpaque(false);
        planeBottom.setOpaque(false);
        planeStatsPanel.setOpaque(false);
        locationPanel.setOpaque(false);
        locationTop.setOpaque(false);
        locationBottom.setOpaque(false);
        locationStatsPanel.setOpaque(false);
        flightSelectionPanel.setOpaque(false);
    }

    /**
     * Initializes the Solo Flight screen components which will only need to be
     * initialized once.
     */
    private void initializeSoloPanelComponents()
    {
        soloButtons.setLayout(new BorderLayout());

        // Initialize the heading and instructions components...
        soloHeadingLabel = new JLabel("Solo Flight");
        soloHeadingLabel.setForeground(Color.WHITE);
        soloHeadingLabel.setFont(soloHeadingLabel.getFont().deriveFont(35.0f));
        soloInstructionsLabel = new JLabel("<html>In Solo Flight mode, you start off flying your chosen"
                + " plane in the location you have selected against <b>" + AI_START_NUM + "</b> AI enemies (which are red)."
                + " Here you can hone your flying" + " skills, and if you wish for a more exciting flight,"
                + " then you can add more planes to the game dynamically" + " using the console who will join your free-for-all"
                + " dogfight. These are the important controls which you will" + " need in order to play:" + "<li><li>" + CONTROLS_TEXT
                + "</html>");
        soloInstructionsLabel.setForeground(Color.WHITE);
        soloInstructionsLabel.setFont(soloInstructionsLabel.getFont().deriveFont(Font.ITALIC));
        // ...then add those components to the panel which will be positioned at
        // the top of the screen:
        JPanel soloUpperNorth = new JPanel();
        soloUpperNorth.setBackground(SOLO_FORE);
        soloUpperNorth.add(soloHeadingLabel);
        soloUpper.setLayout(new BorderLayout());
        soloUpper.add(soloUpperNorth, BorderLayout.NORTH);
        soloUpper.add(soloInstructionsLabel, BorderLayout.SOUTH);

        // Initialize a button that will allow the user to return to the Main
        // Menu screen from the Solo Flight screen:
        JButton mainFromSolo = new JButton("Main Menu");
        mainFromSolo.addActionListener(actionMainMenu);
        mainFromSolo.setToolTipText("Return to the main menu.");

        // Initialize the button used to launch a Solo Flight game:
        JButton flyButton = new JButton("   Fly Now   ");
        flyButton.setToolTipText("Fly now using the selected plane and location.");
        flyButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // Launch a new game using Solo Flight mode:
                launchGame(SOLO_FLIGHT);
            }
        });

        // Set up the JPanel which is used to hold the buttons on the Solo
        // Flight screen:
        lowerSolo.setBackground(SOLO_FORE);
        lowerSolo.setLayout(new FlowLayout());
        lowerSolo.add(mainFromSolo);
        lowerSolo.add(flyButton);

        // Make sure that the components have transparent backgrounds:
        soloUpper.setOpaque(false);
        soloButtons.setOpaque(false);
    }

    /**
     * Initializes the Host Network Dogfight screen components which will only
     * need to be initialized once.
     */
    private void initializeHostPanelComponents()
    {
        dogfightHostButtons.setLayout(new BorderLayout());

        // Initialize the heading and instructions components...
        hostHeadingLabel = new JLabel("Host Network Dogfight");
        hostHeadingLabel.setForeground(Color.WHITE);
        hostHeadingLabel.setFont(soloHeadingLabel.getFont().deriveFont(35.0f));
        hostInstructionsLabel = new JLabel("<html>When hosting a Network Dogfight,"
                + " you will fight against other users who have joined your game."
                + " You also have the option, as host, to add AI enemies" + " (which are red) to this dogfight using the console."
                + " The last non-AI player left alive will win." + " If you, as the host, leave the game, then the game will"
                + " end for all clients too. These are the important controls" + " which you will need in order to play:" + "<li><li>"
                + CONTROLS_TEXT + "</html>");
        hostInstructionsLabel.setForeground(Color.WHITE);
        hostInstructionsLabel.setFont(hostInstructionsLabel.getFont().deriveFont(Font.ITALIC));
        // ...then add those components to the panel which will be positioned at
        // the top of the screen:
        hostUpper.setLayout(new BorderLayout());
        JPanel hostUpperNorth = new JPanel();
        hostUpperNorth.setBackground(HOST_FORE);
        hostUpperNorth.add(hostHeadingLabel);
        hostUpper.add(hostUpperNorth, BorderLayout.NORTH);
        hostUpper.add(hostInstructionsLabel, BorderLayout.SOUTH);

        // Initialize a button that will allow the user to return to the Main
        // Menu screen from the Host Network Dogfight screen:
        JButton mainFromHost = new JButton("Main Menu");
        mainFromHost.setToolTipText("Return to the main menu.");
        mainFromHost.addActionListener(actionMainMenu);

        // Initialize the button used to launch a Network Dogfight as the host:
        JButton hostButton = new JButton("Host Game");
        hostButton.setToolTipText("Host the gamand fly with your chosen plane.");
        hostButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // Launch a new game using Host Network Dogfight mode:
                launchGame(NETWORK_DOGFIGHT_HOST);
            }
        });

        // Set up the JPanel which is used to hold the buttons on the Host
        // Network Dogfight screen:
        lowerDogfightHost.setBackground(HOST_FORE);
        lowerDogfightHost.setLayout(new FlowLayout());
        lowerDogfightHost.add(mainFromHost);
        lowerDogfightHost.add(hostButton);

        // Make sure that the components have transparent backgrounds:
        hostUpper.setOpaque(false);
        dogfightHostButtons.setOpaque(false);
    }

    /**
     * Initializes the Join Network Dogfight screen components which will only
     * need to be initialized once.
     */
    private void initializeJoinPanelComponents()
    {
        dogfightJoinButtons.setLayout(new BorderLayout());

        // Set up the JTextField that is used to input the host's computer name:
        hostNameInput = new JTextField();
        hostNameInput.setToolTipText("Enter the computer name of the host of the game you wish to join.");

        // Initialize the heading and instructions components...
        joinHeadingLabel = new JLabel("Join Network Dogfight");
        joinHeadingLabel.setForeground(Color.WHITE);
        joinHeadingLabel.setFont(soloHeadingLabel.getFont().deriveFont(35.0f));
        joinInstructionsLabel = new JLabel(
                "<html>When joining a Network Dogfight, you will fight against the host of the game, other users who have joined the game, and any AI enemies added by the host. AI enemies are red. The last non-AI player left alive will win. These are the important controls which you will need in order to play:"
                        + "<li><li>" + CONTROLS_TEXT + "</html>");
        joinInstructionsLabel.setForeground(Color.WHITE);
        joinInstructionsLabel.setFont(joinInstructionsLabel.getFont().deriveFont(Font.ITALIC));
        // ...then add those components to the panel which will be positioned at
        // the top of the screen:
        joinUpper.setLayout(new BorderLayout());
        JPanel joinUpperNorth = new JPanel();
        joinUpperNorth.setBackground(JOIN_FORE);
        joinUpperNorth.add(joinHeadingLabel);
        joinUpperNorthPanel.setLayout(new BorderLayout());
        joinUpperNorthPanel.add(joinUpperNorth, BorderLayout.NORTH);
        joinUpperNorthPanel.add(joinInstructionsLabel, BorderLayout.SOUTH);

        // Initialize a button that will allow the user to return to the Main
        // Menu screen from the Join Network Dogfight screen:
        JButton mainFromJoin = new JButton("Main Menu");
        mainFromJoin.setToolTipText("Return to the main menu.");
        mainFromJoin.addActionListener(actionMainMenu);

        // Initialize the button used to launch a Network Dogfight as a client:
        JButton joinButton = new JButton("Join Game");
        joinButton.setToolTipText("Join the specified game with your chosen plane.");
        joinButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // If the user has entered the name of the game host:
                if (hostNameInput.getText().length() > 0)
                {
                    // Launch a new game using Join Network Dogfight mode:
                    launchGame(NETWORK_DOGFIGHT_JOIN);
                }
                else
                {
                    // Promt the user to enter a name:
                    JOptionPane.showMessageDialog(c, "Please enter the name of the game host.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Set up the JPanel which is used to hold the buttons on the Join
        // Network Dogfight screen:
        lowerDogfightJoin.setBackground(JOIN_FORE);
        lowerDogfightJoin.setLayout(new FlowLayout());
        lowerDogfightJoin.add(mainFromJoin);
        lowerDogfightJoin.add(joinButton);

        // Make sure that the components have transparent backgrounds:
        joinUpper.setOpaque(false);
        joinUpperNorthPanel.setOpaque(false);
        dogfightJoinButtons.setOpaque(false);
    }

    /**
     * Initializes all the components of the Game Options screen JPanel.
     */
    private void initializeOptionsPanel()
    {
        optionsButtons.setLayout(new BorderLayout());

        // Initialize the heading and instructions components...
        optionsHeadingLabel = new JLabel("Game Options");
        optionsHeadingLabel.setForeground(Color.WHITE);
        optionsHeadingLabel.setFont(soloHeadingLabel.getFont().deriveFont(35.0f));
        optionsInstructionsLabel = new JLabel("<html>" + "Here you can change the options of the game to personalize it more."
                + " You can select the crosshair that you wish to use, whether your game"
                + " will have animated water or still water. (Warning: Only use animated"
                + " water on a computer with lots of memory and on a location that does not"
                + " cover a large area) and the player strength of your player" + " (how much damage you will take when shot by enemies)."
                + " Simply change the necessary options, then press"
                + " the \"Apply Options\" button to update the game options:<p><p></html>");
        optionsInstructionsLabel.setForeground(Color.WHITE);
        optionsInstructionsLabel.setFont(optionsInstructionsLabel.getFont().deriveFont(Font.ITALIC));
        // ...then add the heading to the panel which will be positioned at the
        // very top of the screen:
        JPanel optionsUpperNorth = new JPanel();
        optionsUpperNorth.setBackground(OPTIONS_FORE);
        optionsUpperNorth.add(optionsHeadingLabel);

        // Initialize the components used to display some screenshots of the
        // game:
        JPanel optionsScreenshotPanel = new JPanel();
        optionsScreenshotPanel.setLayout(new FlowLayout());
        JLabel optionsScreenshotOneLabel = new JLabel();
        optionsScreenshotOneLabel.setIcon(new ImageIcon("image\\options_screen_game_1.jpg"));
        JLabel optionsScreenshotTwoLabel = new JLabel();
        optionsScreenshotTwoLabel.setIcon(new ImageIcon("image\\options_screen_game_2.jpg"));
        optionsScreenshotPanel.add(optionsScreenshotOneLabel);
        optionsScreenshotPanel.add(optionsScreenshotTwoLabel);

        // Set up the panel used to hold the components that must be positioned
        // at the top of the screen:
        optionsUpper.setLayout(new BorderLayout());
        optionsUpper.add(optionsUpperNorth, BorderLayout.NORTH); // Add the
        // heading.
        optionsUpper.add(optionsScreenshotPanel); // Add the screenshots.
        optionsUpper.add(optionsInstructionsLabel, BorderLayout.SOUTH); // Add
        // the instructions.

        // Create some JLabel objects for the option panels:
        JLabel crosshairLabel = new JLabel("Select Crosshair: ");
        crosshairLabel.setForeground(Color.WHITE);
        JLabel strengthLabel = new JLabel("Player Strength: ");
        strengthLabel.setForeground(Color.WHITE);
        JLabel animateWaterLabel = new JLabel("Animate Water: ");
        animateWaterLabel.setForeground(Color.WHITE);

        crosshairImageLabel.setToolTipText("Your selected crosshair.");
        crosshairComboBox.setToolTipText("Select a crosshair from this list of available ones.");

        // Initialize the player's strength output JTextField, and set the
        // colours of it to the standard output ones for this game:
        playerStrengthOutput = new JTextField();
        playerStrengthOutput.setColumns(6);
        playerStrengthOutput.setBackground(OUTPUT_FIELD_BACKGROUND);
        playerStrengthOutput.setForeground(OUTPUT_FIELD_FOREGROUND);

        // Initialize the JSlider used to change the player's strength:
        strengthSlider.setSnapToTicks(true);
        strengthSlider.setToolTipText("Use this to change your player's strength in the game."
                + " It determines how much damage your player will take when he is shot.");
        strengthSlider.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent l)
            {
                // Output the changed player strength:
                int value = strengthSlider.getValue();
                if (value == Plane.MAX_LEVEL)
                    playerStrengthOutput.setText("Invincible");
                else
                    playerStrengthOutput.setText(String.valueOf(value));

                optionsChanged = true; // Signal to the program that a game
                // option has been changed.
                applyOptionsButton.setEnabled(true); // Allow the user to apply
                // the changed options.
            }
        });

        // Set up the panel that holds the components that deal with changing
        // the player's strength:
        JPanel strengthPanel = new JPanel();
        //strengthPanel.setBackground(new Color(94, 0, 0));
        strengthPanel.setLayout(new BorderLayout());
        strengthPanel.add(strengthLabel, BorderLayout.NORTH);
        strengthPanel.add(strengthSlider);
        playerStrengthOutput.setToolTipText("The current player strength."
                + "This determines how much damage your player will take if he is shot.");
        strengthPanel.add(playerStrengthOutput, BorderLayout.EAST);

        // Initialize the JCheckBox used to keep track of whether the game will
        // run with animated or still water:
        animateWaterCheckBox = new JCheckBox();
        animateWaterCheckBox.setToolTipText("Whether the game has animated or still water.");
        animateWaterCheckBox.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // If it has been changed:
                optionsChanged = true; // Signal to the program that a game
                // option has been changed.
                applyOptionsButton.setEnabled(true); // Allow the user to apply
                // the changed options;
            }
        });

        // Initialize the panel that holds the animate water option components:
        JPanel animateWaterPanel = new JPanel();
        //animateWaterPanel.setBackground(new Color(94, 0, 0));
        animateWaterPanel.setLayout(new FlowLayout());
        animateWaterPanel.add(animateWaterLabel);
        animateWaterPanel.add(animateWaterCheckBox);

        // Initialize the left options panel:
        JPanel optionsLeftPanel = new JPanel();
        //optionsLeftPanel.setBackground(new Color(94, 0, 0));
        optionsLeftPanel.setLayout(new BorderLayout());
        optionsLeftPanel.add(crosshairLabel, BorderLayout.NORTH);
        optionsLeftPanel.add(crosshairImageLabel);
        optionsLeftPanel.add(crosshairComboBox, BorderLayout.SOUTH);

        // Initialize the right options panel:
        JPanel optionsRightPanel = new JPanel();
        //optionsRightPanel.setBackground(new Color(94, 0, 0));
        optionsRightPanel.setLayout(new BorderLayout());
        optionsRightPanel.add(animateWaterPanel, BorderLayout.NORTH);
        optionsRightPanel.add(strengthPanel);

        // Initialize the panel used to hold the left and right options panels:
        JPanel optionsAllPanel = new JPanel();
        //optionsAllPanel.setBackground(new Color(94, 0, 0));
        optionsAllPanel.setLayout(new FlowLayout());
        optionsAllPanel.add(optionsLeftPanel);
        optionsAllPanel.add(optionsRightPanel);

        // Initializes the button used to return to the Main Menu screen from
        // the Game Options screen:
        JButton mainFromOptions = new JButton("Main Menu");
        mainFromOptions.setToolTipText("Return to the main menu.");
        mainFromOptions.addActionListener(actionMainMenu);

        // Initializes the button used to apply the changed Game Options:
        applyOptionsButton.setToolTipText("Apply the changed options.");
        applyOptionsButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // If any of the game options have been changed:
                if (optionsChanged)
                {
                    updateOptions(); // Update the game options in the database.
                    ((JButton) e.getSource()).setEnabled(false); // Disable the
                    // "Apply Options" button (this all done like this to
                    // prevent unnecessary calls to the database).
                }
            }
        });

        // Initialize the JPanel used to hold the buttons on the Join Network
        // Dogfight screen:
        JPanel optionsLower = new JPanel();
        //optionsLower.setBackground(OPTIONS_FORE);
        optionsLower.setLayout(new FlowLayout());
        optionsLower.add(mainFromOptions);
        optionsLower.add(applyOptionsButton);

        // Add all of the separate component panels to one global one so that it
        // can be positioned without allowing it to deviate north or south:
        JPanel allOptionButtonsPanel = new JPanel();
        //allOptionButtonsPanel.setBackground(new Color(94, 0, 0));
        allOptionButtonsPanel.setLayout(new BorderLayout());
        allOptionButtonsPanel.add(optionsUpper, BorderLayout.NORTH);
        allOptionButtonsPanel.add(optionsAllPanel);
        allOptionButtonsPanel.add(optionsLower, BorderLayout.SOUTH);

        optionsButtons.add(allOptionButtonsPanel, BorderLayout.CENTER); // Add
        // the panel that holds all the separate components to the main Game
        // Options screen panel.

        // Make sure that all the components have transparent backgrounds:
        animateWaterCheckBox.setOpaque(false);
        animateWaterPanel.setOpaque(false);
        strengthSlider.setOpaque(false);
        strengthPanel.setOpaque(false);
        optionsScreenshotPanel.setOpaque(false);
        optionsLeftPanel.setOpaque(false);
        optionsRightPanel.setOpaque(false);
        optionsAllPanel.setOpaque(false);
        optionsLower.setOpaque(false);
        optionsUpper.setOpaque(false);
        allOptionButtonsPanel.setOpaque(false);
        optionsButtons.setOpaque(false);
    }

    private void initializeAboutPanel()
    {
        aboutButtons.setLayout(new BorderLayout());

        // Initialize the panel used to hold the components which need to be
        // nearer the top of the screen:
        JPanel aboutUpper = new JPanel();
        aboutUpper.setLayout(new BorderLayout());

        // Create the heading for the About screen:
        JLabel aboutHeadingLabel = new JLabel("About Java Flight Simulator");
        aboutHeadingLabel.setForeground(Color.WHITE);
        aboutHeadingLabel.setFont(soloHeadingLabel.getFont().deriveFont(35.0f));

        // Initialize the JPanel that will allow us to position the heading in
        // exactly the right place:
        JPanel aboutUpperNorthPanel = new JPanel();
        aboutUpperNorthPanel.setLayout(new FlowLayout());
        aboutUpperNorthPanel.add(aboutHeadingLabel, BorderLayout.NORTH);
        aboutUpperNorthPanel.setBackground(ABOUT_FORE);

        // These are the JPanel's that will hold the text contents and
        // screenshots on this screen:
        JPanel aboutContentPanel = new JPanel();
        aboutContentPanel.setLayout(new BorderLayout());
        JPanel aboutContentUpperPanel = new JPanel();
        aboutContentUpperPanel.setLayout(new FlowLayout());

        // Initialize the JPanel that must display the text contents of the
        // About screen:
        JPanel aboutContentLowerPanel = new JPanel();
        JLabel aboutContentLabel = new JLabel("<html>Java Flight Simulator is a 3D flight simulator that was programmed"
                + " by James Attenborough," + " Diocesan College, 2005." + "<p><p>It features the following:"
                + "<li>Solo Flight and Networked Dogfight modes." + "<li>A console which can be used to perform dynamic"
                + " in-game operations, such as adding AI enemies to the" + " game or displaying the health of all the other players"
                + " in the game." + "<li>In-game global and private chatting when playing" + " in Network dogfight mode."
                + "<li>A sound manager to control all the sounds in the game." + "<li>Different scenery elements and effects, including:"
                + "<ul><li>Terrain." + "<li>Still Water." + "<li>Animated Water." + "<li>Sky." + "<li>Night sky filled with stars."
                + "<li>Fog." + "<li>Fire (using a particle system)." + "</html>");
        aboutContentLabel.setForeground(Color.WHITE);
        aboutContentLabel.setFont(aboutContentLabel.getFont().deriveFont(Font.ITALIC));
        aboutContentLowerPanel.add(aboutContentLabel);

        // Initialize the screenshot JLabel objects:
        JLabel aboutScreenshotOneLabel = new JLabel();
        aboutScreenshotOneLabel.setIcon(new ImageIcon("image\\about_screen_game_1.jpg"));
        JLabel aboutScreenshotTwoLabel = new JLabel();
        aboutScreenshotTwoLabel.setIcon(new ImageIcon("image\\about_screen_game_2.jpg"));

        // Set up the panels which are used to hold the content of the About
        // screen:
        aboutContentUpperPanel.add(aboutScreenshotOneLabel);
        aboutContentUpperPanel.add(aboutScreenshotTwoLabel);
        aboutContentPanel.add(aboutContentUpperPanel, BorderLayout.NORTH);
        aboutContentPanel.add(aboutContentLowerPanel);

        // Position the components that need to be at the top of the screen:
        aboutUpper.add(aboutUpperNorthPanel, BorderLayout.NORTH);
        aboutUpper.add(aboutContentPanel);

        // Initialize the button at is used to return to the Main Menu screen
        // from the About screen:
        JButton mainFromAbout = new JButton("Main Menu");
        mainFromAbout.setToolTipText("Return to the main menu.");
        mainFromAbout.addActionListener(actionMainMenu);

        // Initialize the JPanel that is used to hold the button on the About
        // screen:
        JPanel aboutLower = new JPanel();
        aboutLower.setLayout(new FlowLayout());
        aboutLower.setBackground(ABOUT_FORE);
        aboutLower.add(mainFromAbout);

        // Initialize the panel which holds all the separate components of the
        // About screen:
        JPanel aboutAll = new JPanel();
        aboutAll.setLayout(new BorderLayout());
        aboutAll.add(aboutUpper, BorderLayout.NORTH);
        aboutAll.add(aboutLower, BorderLayout.SOUTH);

        aboutButtons.add(aboutAll, BorderLayout.CENTER); // Add
        // the panel that holds all the separate components to the main About
        // screen panel.

        // Make sure that all the components have transparent backgrounds so
        // that the BackgroundPanel will show through:
        aboutUpper.setOpaque(false);
        aboutContentPanel.setOpaque(false);
        aboutContentUpperPanel.setOpaque(false);
        aboutContentLowerPanel.setOpaque(false);
        aboutAll.setOpaque(false);
        aboutButtons.setOpaque(false);
    }

    /**
     * This method is used to launch the game whose type specified by the value
     * passed to this method.
     * 
     * @param type
     *            The game type. This must be equal to SOLO_FLIGHT,
     *            NETWORK_DOGFIGHT_HOST or NETWORK_DOGFIGHT_JOIN.
     */
    private void launchGame(int type)
    {
        try
        {
            // Get the computer name of this machine:
            String name = InetAddress.getLocalHost().getHostName();

            // These are used to hold the game options:
            String crosshairImage = new String();
            boolean animateWater = false;
            int playerStrength = Plane.MIN_LEVEL;
            String networkLocation = new String();
            String aiTexture = new String();

            // These are used to hold the plane details:
            String model = new String();
            String texture = new String();
            double pitchConstant = 0;
            double rollConstant = 0;
            double speedConstant = 0;

            // These are used to hold the location details:
            Point3d start = new Point3d();
            Matrix3d rotation = new Matrix3d();
            int scale = 0;
            double minHeight = 0;
            double maxHeight = 0;
            boolean hasWater = false;
            double waterLevel = 0;
            boolean hasFog = false;
            String skyTexture = new String();
            String terrainImageString = new String();
            boolean night = false;

            // Retrieve the current game options from the database:
            ResultSet optionsSet = statement
                    .executeQuery("SELECT * FROM GameOptions INNER JOIN Crosshairs ON GameOptions.crosshairName=Crosshairs.crosshairName");
            while (optionsSet.next())
            {
                // Set the values of the game options variables:
                animateWater = optionsSet.getBoolean("animateWater");
                playerStrength = optionsSet.getInt("playerStrength");
                crosshairImage = optionsSet.getString("crosshairImage");
                networkLocation = optionsSet.getString("networkLocation");
                aiTexture = optionsSet.getString("aiTexture"); // Texture used
                // for AI enemy planes.
            }

            // Retrieve the details of the currently selected plane:
            ResultSet planeSet = launchPlaneStatement.executeQuery("SELECT * FROM Planes WHERE model = '"
                    + String.valueOf(planeComboBox.getSelectedItem()) + "'");

            while (planeSet.next())
            {
                // Set the values of the plane details variables:
                model = planeSet.getString("3dModel");
                texture = planeSet.getString("texture");
                pitchConstant = planeSet.getDouble("pitchConstant");
                rollConstant = planeSet.getDouble("rollConstant");
                speedConstant = planeSet.getDouble("speedConstant");
            }

            // Make the selected location the networking location, unless it is
            // a Solo Flight game:
            String selectedLocation = networkLocation;
            if (type == SOLO_FLIGHT)
                selectedLocation = String.valueOf(locationComboBox.getSelectedItem());

            // Retrieve the details of the currently selected location:
            ResultSet locationSet = launchLocationStatement.executeQuery("SELECT * FROM Theatres INNER JOIN Locations"
                    + " ON Theatres.theatreName=Locations.theatreName" + " WHERE locationName = '" + selectedLocation + "'");

            while (locationSet.next())
            {
                // Set the values of the location details variables:

                scale = locationSet.getInt("scaleFactor");
                // The location is the only variable that is not scaled later,
                // so it must be scaled now:
                start.x = (scale * locationSet.getDouble("startLocationX"));
                start.y = (scale * locationSet.getDouble("startLocationY"));
                start.z = (scale * locationSet.getDouble("startLocationZ"));

                minHeight = locationSet.getDouble("minHeight");
                maxHeight = locationSet.getDouble("maxHeight");

                hasWater = locationSet.getBoolean("hasWater");
                if (hasWater)
                    waterLevel = locationSet.getDouble("waterLevel");

                hasFog = locationSet.getBoolean("hasFog");

                skyTexture = locationSet.getString("skyTexture");
                terrainImageString = locationSet.getString("terrainGenerationImage");

                night = locationSet.getBoolean("night");
            }

            rotation = new Matrix3d(); // Set the rotation to be a zero matrix
            // initially, i.e., no nose rotation.

            MediaTracker launchTracker = new MediaTracker(this); // This
            // MediaTracker is used specifically for loading Images needed for
            // launching the game.

            // This flags variable keeps track of all the elements required for
            // this game's World object (Terrain is by default here):
            int flags = World.TERRAIN;

            Color canvasOutputColour; // This is the colour to use for text
            // output during the game.

            // If the location is a night level:
            if (night)
            {
                // Use white to display text and add a SkyWithStars object to
                // the world:
                canvasOutputColour = Color.WHITE;
                flags |= World.SKY_STARS;
            }
            else
            {
                // Use black to display text and add a standard Sky object to
                // the world:
                canvasOutputColour = Color.BLACK;
                flags |= World.SKY;
            }

            // If this location needs water, then check the game options setting
            // to see if it must be animated or still:
            if (hasWater)
            {
                if (animateWater)
                    flags |= World.WATER_ANIMATED;
                else
                    flags |= World.WATER_STILL;
            }

            // Add a Fog element to the World if it is required:
            if (hasFog)
                flags |= World.FOG;

            // Create the crosshair Image and add it to the MediaTracker:
            Image crosshair = this.getToolkit().createImage(crosshairImage);
            launchTracker.addImage(crosshair, LAUNCH_IMAGE_ID);

            // Create the terrain Image and add it to the MediaTracker:
            Image terrainImage = this.getToolkit().createImage(terrainImageString);
            launchTracker.addImage(terrainImage, LAUNCH_IMAGE_ID);

            launchTracker.waitForAll(); // Wait for the Images to finish loading
            // before continuing.

            // If the Images were loaded successfully:
            if (!(launchTracker.isErrorID(LAUNCH_IMAGE_ID)))
            {
                // Create the World for the game:
                World world = new World(flags, scale, terrainImage.getWidth(this), terrainImage.getHeight(this), minHeight, maxHeight);

                // Set the optional World fields...

                world.setTerrainImage(terrainImage);
                world.setSkyTexture(new TextureLoader(skyTexture, this).getTexture());
                if (hasWater)
                    world.setWaterLevel(waterLevel);

                // ...then generate the world.
                if (world.generateWorld())
                {
                    // If it was successfully generated:

                    // These are used to hold the GroundObject details:
                    Point3d point = new Point3d();
                    int locationX = 0;
                    int locationZ = 0;
                    double objectScale = 0;
                    double rotateBy = 0;

                    // Retrieve the GroundObjects for this location:
                    ResultSet objectsSet = statement
                            .executeQuery("SELECT * FROM Locations INNER JOIN GroundObjects ON Locations.locationName=GroundObjects.locationName WHERE Locations.locationName='"
                                    + String.valueOf(locationComboBox.getSelectedItem()) + "'");

                    while (objectsSet.next())
                    {
                        // Set the GroundObject variables:
                        locationX = objectsSet.getInt("locationX");
                        locationZ = objectsSet.getInt("locationZ");
                        point = new Point3d();
                        // Apply the scale to the location:
                        point.x = (scale * locationX);
                        point.y = 1 + (scale * world.getTerrainHeightAtPoint(locationX, locationZ));
                        point.z = (scale * locationZ);

                        objectScale = objectsSet.getDouble("objectScaleFactor");
                        rotateBy = objectsSet.getDouble("rotateBy");

                        // Add the new GroundObject to the World of the game:
                        world.addGroundObject(new GroundObject(objectsSet.getString("name"), objectsSet.getString("3dModel"),
                                new TextureLoader(objectsSet.getString("texture"), this).getTexture(), point, objectsSet
                                        .getBoolean("pickable"), objectScale, rotateBy));
                    }

                    if ((type == SOLO_FLIGHT) || (type == NETWORK_DOGFIGHT_HOST)
                            || ((type == NETWORK_DOGFIGHT_JOIN) && (hostNameInput.getText().length() > 0)))
                    {
                        // Initialize the game using the specified mode:

                        if (type == SOLO_FLIGHT)
                        {
                            // Launch a Solo Flight game:
                            flightGamePanel = new FlightGamePanel(new Plane(false, name, model, texture, start, pitchConstant,
                                    rollConstant, speedConstant, rotation, playerStrength), aiTexture, AI_START_NUM, world, crosshair,
                                    canvasOutputColour, configuration, environment.getMaximumWindowBounds());
                        }
                        if (type == NETWORK_DOGFIGHT_HOST)
                        {
                            // Host a Network Dogfight:
                            flightGamePanel = new FlightGamePanel(name, new Plane(false, name, model, texture, start, pitchConstant,
                                    rollConstant, speedConstant, rotation, playerStrength), aiTexture, world, crosshair,
                                    canvasOutputColour, configuration, environment.getMaximumWindowBounds());
                        }
                        else
                            if ((type == NETWORK_DOGFIGHT_JOIN) && (hostNameInput.getText().length() > 0))
                            {
                                // Join a Network Dogfight:
                                flightGamePanel = new FlightGamePanel(hostNameInput.getText(), new Plane(false, name, model, texture,
                                        start, pitchConstant, rollConstant, speedConstant, rotation, playerStrength), aiTexture, world,
                                        crosshair, canvasOutputColour, configuration, environment.getMaximumWindowBounds());
                            }

                        // Make sure that the screen is refreshed so the game
                        // can be seen:
                        c.add(flightGamePanel);
                        validate();
                        repaint();
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * This method is used to retrieve all the available planes from the
     * database.
     */
    private void populatePlaneList()
    {
        // Clear the JComboBox that holds the available planes:
        planeComboBox.removeAllItems();

        try
        {
            // Retrieve the models and put them into alphabetical order:

            ResultSet rs = statement.executeQuery("SELECT model FROM Planes ORDER BY model");

            while (rs.next())
            {
                planeComboBox.addItem(rs.getString("model"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
        }

        // Update the location display components:
        planeComboBox.setSelectedIndex(0);
        changePlaneAvatar(String.valueOf(planeComboBox.getSelectedItem()));
        fetchPlaneDetails(String.valueOf(planeComboBox.getSelectedItem()));
    }

    /**
     * This method is used to retrieve all the available locations from the
     * database.
     */
    private void populateLocationList()
    {
        // Clear the JComboBox that holds the available locations:
        locationComboBox.removeAllItems();

        try
        {
            // Retrieve the locations and put them into alphabetical order:

            ResultSet locationSet = statement.executeQuery("SELECT locationName FROM Locations ORDER BY locationName");

            while (locationSet.next())
            {
                locationComboBox.addItem(locationSet.getString("locationName"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
        }

        // Update the location display components:
        locationComboBox.setSelectedIndex(0);
        changeLocationAvatar(String.valueOf(locationComboBox.getSelectedItem()));
        fetchLocationDetails(String.valueOf(locationComboBox.getSelectedItem()));
    }

    /**
     * This method is used to fetch the current options from the database.
     */
    private void retrieveCurrentOptions()
    {
        // Clear the JComboBox that holds the list of available crosshairs:
        crosshairComboBox.removeAllItems();

        try
        {
            // Populate the crosshair JComboBox:

            ResultSet crosshairNameSet = crosshairStatement.executeQuery("SELECT crosshairName FROM Crosshairs ORDER BY crosshairName");

            while (crosshairNameSet.next())
            {
                crosshairComboBox.addItem(crosshairNameSet.getString("crosshairName"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
        }

        try
        {
            // Retrieve the game options:
            ResultSet optionSet = optionsFetchStatement.executeQuery("SELECT * FROM GameOptions");

            // This should only return one element, so we can just check if the
            // marker could be moved to the first index:
            if (optionSet.next())
            {
                // Update the game options display components:

                String crossHairName = optionSet.getString("crosshairName");
                crosshairComboBox.setSelectedItem(crossHairName);
                changeCrosshairImage(crossHairName);

                strengthSlider.setValue(optionSet.getInt("playerStrength"));

                animateWaterCheckBox.setSelected(optionSet.getBoolean("animateWater"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
        }

        optionsChanged = false; // Signal to the application that the game
        // options are unchanged.
        applyOptionsButton.setEnabled(false); // Disable the "Apply" button.
    }

    /**
     * This method is used to update the game options in the database.
     */
    private void updateOptions()
    {
        try
        {
            // Set the parameters for the update PreparedStatement to the values
            // retrieved from the Game Options screen:
            optionsUpdateStatement.setString(1, String.valueOf(crosshairComboBox.getSelectedItem()));
            optionsUpdateStatement.setInt(2, strengthSlider.getValue());
            optionsUpdateStatement.setBoolean(3, animateWaterCheckBox.isSelected());

            // Update the database:
            optionsUpdateStatement.executeUpdate();
        }
        catch (Exception ex)
        {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * This method is used to change the plane avatar being displayed to that of
     * the plane whose name is passed to this method.
     * 
     * @param modelIn
     *            The model name of the plane whose avatar must be fetched and
     *            displayed.
     */
    private void changePlaneAvatar(String modelIn)
    {
        try
        {
            // Retrieve the avatar filename from the database:
            ResultSet avatarSet = planeAvatarStatement.executeQuery("SELECT planeAvatar FROM Planes WHERE model='" + modelIn + "'");

            // This should only return one element, so we can just check if the
            // marker could be moved to the first index:
            if (avatarSet.next())
            {
                // Create the Image:
                Image avatar = this.getToolkit().createImage(avatarSet.getString("planeAvatar"));

                // To make sure that the Image is loaded completely:
                tracker.addImage(avatar, PLANE_IMAGE_ID); // Add the Image to
                // the Image MediaTracker.
                tracker.waitForID(PLANE_IMAGE_ID); // Wait for the Image to
                // finish loading.

                // Change the avatar being displayed:
                planeImageLabel.setIcon(new ImageIcon(avatar));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * This method is used to change the location avatar being displayed to that
     * of the location whose name is passed to this method.
     * 
     * @param nameIn
     *            The name of the location whose avatar must be fetched and
     *            displayed.
     */
    private void changeLocationAvatar(String nameIn)
    {
        try
        {
            // Retrieve the avatar filename from the database:
            ResultSet avatarSet = locationAvatarStatement.executeQuery("SELECT locationAvatar FROM Locations WHERE locationName='" + nameIn
                    + "'");

            // This should only return one element, so we can just check if the
            // marker could be moved to the first index:
            if (avatarSet.next())
            {
                // Create the Image:
                Image avatar = this.getToolkit().createImage(avatarSet.getString("locationAvatar"));

                // To make sure that the Image is loaded completely:
                tracker.addImage(avatar, LOCATION_IMAGE_ID);// Add the Image to
                // the Image MediaTracker.
                tracker.waitForID(LOCATION_IMAGE_ID);// Wait for the Image to
                // finish loading.

                // Change the avatar being displayed:
                locationImageLabel.setIcon(new ImageIcon(avatar));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * This method is used to change the crosshair Image displayed to that of
     * the crosshair whose name is passed to this method.
     * 
     * @param nameIn
     *            The name of the crosshair whose Image must be fetched and
     *            displayed.
     */
    private void changeCrosshairImage(String nameIn)
    {
        try
        {
            // Fetch the filename of the crosshair's Image from the database
            // using the crosshair name passed to this method:
            ResultSet crosshairAvatarSet = crosshairImageStatement
                    .executeQuery("SELECT crosshairImage FROM Crosshairs WHERE crosshairName='" + nameIn + "'");

            // This should only return one element, so we can just check if the
            // marker could be moved to the first index:
            if (crosshairAvatarSet.next())
            {
                // Create the Image:
                Image crosshair = this.getToolkit().createImage(crosshairAvatarSet.getString("crosshairImage"));

                // To make sure that the Image is loaded completely:
                tracker.addImage(crosshair, CROSSHAIR_IMAGE_ID); // Add the
                // Image to the Image MediaTracker.
                tracker.waitForID(CROSSHAIR_IMAGE_ID); // Wait for the Image to
                // finish loading.

                // Change the Image displayed:
                crosshairImageLabel.setIcon(new ImageIcon(crosshair));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * This method is used to fetch the details of the plane passed to it. It
     * updates the JTextField objects used to output the chosen plane's details.
     * 
     * @param modelIn
     *            The model name of the plane to fetch the details for.
     */
    private void fetchPlaneDetails(String modelIn)
    {
        try
        {
            // Fetches the pitch, roll and speed constants, of the plane passed
            // to this method, from the database:
            ResultSet planeSet = planeStatsStatement
                    .executeQuery("SELECT pitchConstant, rollConstant, speedConstant FROM Planes WHERE model='" + modelIn + "'");

            // This should only return one element, so we can just check if the
            // marker could be moved to the first index:
            if (planeSet.next())
            {
                pitchOutput.setText(String.valueOf(planeSet.getDouble("pitchConstant")));
                rollOutput.setText(String.valueOf(planeSet.getDouble("rollConstant")));
                speedOutput.setText(String.valueOf(planeSet.getDouble("speedConstant")));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * This method is used to fetch the details of the location passed to it. It
     * updates the JTextField objects used to output the chosen location's
     * details.
     * 
     * @param locationIn
     *            The location to fetch the details for.
     */
    private void fetchLocationDetails(String locationIn)
    {
        try
        {
            // Execute a query on the database that links three tables using
            // inner join statements, and fetches the details for the
            // location passed to this method (all of the fields had to be
            // explicitly stated so that they could be used in a GROUP BY
            // statement, so that a Count() operation could be performed using
            // the same query):
            ResultSet locationSet = locationStatsStatement.executeQuery("SELECT Locations.locationName,"
                    + " Count(GroundObjects.objectID) AS ObjectCount, Theatres.hasWater,"
                    + " Locations.theatreName, Locations.minHeight, Locations.maxHeight,"
                    + " Locations.startLocationX, Locations.startLocationY, Locations.startLocationZ,"
                    + " Locations.waterLevel, Locations.hasFog, Locations.night, Locations.scaleFactor"
                    + " FROM (Theatres INNER JOIN Locations ON Theatres.theatreName=Locations.theatreName)"
                    + " INNER JOIN GroundObjects ON Locations.locationName=GroundObjects.locationName"
                    + " GROUP BY Locations.locationName, Theatres.hasWater, Locations.theatreName,"
                    + " Locations.minHeight, Locations.maxHeight, Locations.startLocationX,"
                    + " Locations.startLocationY, Locations.startLocationZ, Locations.waterLevel,"
                    + " Locations.hasFog, Locations.night, Locations.scaleFactor HAVING (Locations.locationName='" + locationIn + "')");

            // This should only return one element, so we can just check if the
            // marker could be moved to the first index:
            if (locationSet.next())
            {
                // Retrieve the data from the fields and then update the details
                // output fields:

                theatreOutput.setText(locationSet.getString("theatreName"));

                int scale = locationSet.getInt("scaleFactor");

                objectsOutput.setText(String.valueOf(locationSet.getInt("ObjectCount")));
                nightOutput.setText(String.valueOf(locationSet.getBoolean("night")));
                fogOutput.setText(String.valueOf(locationSet.getBoolean("hasFog")));

                // The following fields are all multiplied by the scale:
                locationOutput.setText((scale * locationSet.getDouble("startLocationX")) + "; "
                        + (scale * locationSet.getDouble("startLocationY")) + "; " + (scale * locationSet.getDouble("startLocationZ")));
                heightRangeOutput.setText(String.valueOf((scale * locationSet.getDouble("minHeight"))) + " to "
                        + String.valueOf((scale * locationSet.getDouble("maxHeight"))));
                if (locationSet.getBoolean("hasWater"))
                    waterOutput.setText(String.valueOf((scale * locationSet.getDouble("waterLevel"))));
                else
                    waterOutput.setText("N/A");
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * This method needs to be implemented for the WindowListener interface. It
     * is not used in this class.
     * 
     * @param e
     *            The WindowEvent passed to this method.
     */
    public void windowActivated(WindowEvent e)
    {
    }

    /**
     * This method needs to be implemented for the WindowListener interface. It
     * is not used in this class.
     * 
     * @param e
     *            The WindowEvent passed to this method.
     */
    public void windowClosed(WindowEvent e)
    {
    }

    /**
     * This method needs to be implemented for the WindowListener interface. It
     * is called by the application when it is busy closing.
     * 
     * @param e
     *            The WindowEvent passed to this method.
     */
    public void windowClosing(WindowEvent e)
    {
        cleanUp(); // Call the cleanUp() method.
    }

    /**
     * This method needs to be implemented for the WindowListener interface. It
     * is not used in this class.
     * 
     * @param e
     *            The WindowEvent passed to this method.
     */
    public void windowDeactivated(WindowEvent e)
    {
    }

    /**
     * This method needs to be implemented for the WindowListener interface. It
     * is not used in this class.
     * 
     * @param e
     *            The WindowEvent passed to this method.
     */
    public void windowDeiconified(WindowEvent e)
    {
    }

    /**
     * This method needs to be implemented for the WindowListener interface. It
     * is not used in this class.
     * 
     * @param e
     *            The WindowEvent passed to this method.
     */
    public void windowIconified(WindowEvent e)
    {
    }

    /**
     * This method needs to be implemented for the WindowListener interface. It
     * is not used in this class.
     * 
     * @param e
     *            The WindowEvent passed to this method.
     */
    public void windowOpened(WindowEvent e)
    {
    }

    /**
     * This method is used to perform the cleanup operations of this
     * application.
     */
    private void cleanUp()
    {
        try
        {
            // Close all the Statements:
            crosshairStatement.close();
            crosshairImageStatement.close();
            optionsFetchStatement.close();
            launchPlaneStatement.close();
            launchLocationStatement.close();
            planeStatsStatement.close();
            locationStatsStatement.close();
            planeAvatarStatement.close();
            locationAvatarStatement.close();
            optionsUpdateStatement.close();
            statement.close();

            // Terminate the Connection to the database:
            connection.close();
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
        }

        // Exit the program with "normal" status:
        System.exit(NORMAL);
    }

    /**
     * The application's main launcher method.
     * 
     * @param args
     *            The arguments passed to the application. These arguments are
     *            not used in this application.
     */
    public static void main(String args[])
    {
        new JavaFlightSimulator();
    }

}