import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This is the class that handles the game window
 *
 * @author Jonathan Matscheko
 * @version 1.0
 */
public class Window extends JFrame {

    /**
     * The height of the map
     */
    private static int mapHeight;
    /**
     * The width of the map
     */
    private static int mapWidth;
    /**
     * The current game-state
     */
    private static GameState gameState = GameState.DEFAULT;
    /**
     * The dimensions of the field-buttons
     */
    private static final Dimension FIELD_BUTTON_DIMENSION = new Dimension(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
    /**
     * The gridBagConstraints
     */
    private static GridBagConstraints gbc;
    // components
    /**
     * The panel that contains the menu buttons
     */
    private static JPanel menuPanel;
    /**
     * The panel that contains the map buttons
     */
    private static JPanel mapPanel;
    // sub components
    /**
     * The hint button
     */
    private static JButton hintButton;
    /**
     * The reset button
     */
    private static JButton resetButton;
    /**
     * The flag label
     */
    private static JLabel flagLabel;
    /**
     * The two-dimensional array of all the field-buttons
     */
    private static SquareJButton[][] mineMap;
    // miscellaneous value
    /**
     * Whether or not the next / current click is the first one this round
     */
    private static boolean firstClick = true;

    /**
     * This is the main window method, which creates the game-window
     *
     * @param title     The title of the window
     * @param mapHeight The height of the map
     * @param mapWidth  The width of the map
     */
    public Window(String title, int mapHeight, int mapWidth) {
        super(title);
        Window.mapHeight = mapHeight;
        Window.mapWidth = mapWidth;
        mineMap = new SquareJButton[mapHeight][mapWidth];

        // Set Layout manager
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        gbc = new GridBagConstraints();

        // setup of the panels
        // setup of the menu panel
        menuPanel = new JPanel();
        initializeMenuPanel();
        // setup of the map panel
        mapPanel = new JPanel();
        initializeMapPanel();

        // placement of the panels
        // placement of the menu panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        add(menuPanel, gbc);
        gbc.insets = new Insets(5, 0, 0, 0);
        // placement of the map panel
        gbc.gridwidth = mapWidth;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(mapPanel, gbc);
    }

    /**
     * This method adds the given component at the given location to the given panel
     *
     * @param component          The given component
     * @param horizontalLocation The given horizontal location
     * @param verticalLocation   The given vertical location
     * @param panel              The given panel
     */
    private void placeComponentOnCoordinatesWithGBC(JComponent component, int horizontalLocation, int verticalLocation, JPanel panel) {
        gbc.gridx = horizontalLocation;
        gbc.gridy = verticalLocation;
        panel.add(component, gbc);
        resetGBCValues();
    }

    /**
     * This method resets the values of the gridBagConstraints to zero
     */
    private void resetGBCValues() {
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
    }

    // These methods help in the creation of the window

    /**
     * This method initializes the menu panel
     */
    private void initializeMenuPanel() {
        // setup of the menu layout
        GridBagLayout menuLayout = new GridBagLayout();
        menuPanel.setLayout(menuLayout);
        // placement of the hint button
        hintButton = new JButton();
        initializeHintButton();
        placeComponentOnCoordinatesWithGBC(hintButton, 0, 0, menuPanel);
        // placement of the reset button
        resetButton = new JButton();
        initializeResetButton();
        placeComponentOnCoordinatesWithGBC(resetButton, 1, 0, menuPanel);
        // Placement of the flag label
        flagLabel = new JLabel();
        initializeFlagLabel();
        placeComponentOnCoordinatesWithGBC(flagLabel, 2, 0, menuPanel);
    }

    /**
     * This method initializes the map panel
     */
    private void initializeMapPanel() {
        // creates and assigns a layout to the map-panel
        GridLayout panelLayout = new GridLayout(mapHeight, mapWidth);
        mapPanel.setLayout(panelLayout);
        // fills the mine-map with buttons, which are initialized before being added to the map-panel
        for (int x = 0; x < mapHeight; x++) {
            for (int y = 0; y < mapWidth; y++) {
                SquareJButton button = new SquareJButton();
                mineMap[x][y] = button;
                initializeFieldButton(x, y);
                placeComponentOnCoordinatesWithGBC(button, x, y, mapPanel);
            }
        }
    }

    /**
     * This method initializes the hint button
     */
    private void initializeHintButton() {
        // assigns the correct values and a mouse-listener to the hint-button
        hintButton.setSize(FIELD_BUTTON_DIMENSION);
        hintButton.setBackground(Constants.COLOR_HINT);
        hintButton.setText(Constants.BUTTON_STRING_HINT);
        hintButton.setFocusPainted(false);
        hintButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (gameState != GameState.LOST && gameState != GameState.WON && !firstClick) {
                    Main.showOrHideHint(true);
                    updateMap();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (gameState != GameState.LOST && gameState != GameState.WON && !firstClick) {
                    Main.showOrHideHint(false);
                    updateMap();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    /**
     * This method initializes the reset button
     */
    private void initializeResetButton() {
        // assigns the correct values and an action-listener to the reset-button
        resetButton.setSize(FIELD_BUTTON_DIMENSION);
        resetButton.setBackground(Constants.COLOR_EMOTE_DEFAULT);
        resetButton.setText(Constants.BUTTON_STRING_RESET_DEFAULT);
        resetButton.setFocusPainted(false);
        resetButton.addActionListener(e -> pressResetButton());
    }

    /**
     * This method initializes the flag label
     */
    private void initializeFlagLabel() {
        flagLabel.setSize(new Dimension());
        updateFlagLabel();
        flagLabel.setBackground(Constants.COLOR_FLAG_NORMAL);
    }

    /**
     * This method initializes a field button
     */
    private void initializeFieldButton(int x, int y) {
        // creates a button, places it on the map and assigns the correct values and a mouse-listener to it
        SquareJButton button = mineMap[x][y];
        button.setSize(FIELD_BUTTON_DIMENSION);
        button.setText(Constants.DISPLAY_UNDISCOVERED);
        button.setBackground(Constants.COLOR_FIELD_UNDISCOVERED);
        button.setFocusPainted(false);
        // the mouse-listener is added in a separate method to keep this one smaller
        button.addMouseListener(setupFieldMouseListener(x, y));
    }

    /**
     * This method gives back the mouse-listener for a field button
     *
     * @param x The vertical position of the field button on the field
     * @param y The horizontal position of the field button on the field
     * @return The mouse-listener of the field button
     */
    private MouseListener setupFieldMouseListener(int x, int y) {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    pressFieldButtonLeft(x, y);
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    pressFieldButtonRight(x, y);
                }
                updateEmoticonButton();
                updateMap();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    releaseFieldButtonLeft(x, y);
                }
                updateMap();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }

    // input methods

    /**
     * This method handles the left mouse click of the field button on the given coordinates
     *
     * @param x The vertical position of the field button on the field
     * @param y The horizontal position of the field button on the field
     */
    private void pressFieldButtonLeft(int x, int y) {
        SquareJButton button = mineMap[x][y];
        if (firstClick) {
            // the field is calculated after the first click to ensure that the first click isn't on a mine
            Main.placeMinesAfterClick(x, y);
            firstClick = false;
        } else {
            // to only change the face into a scared one, when it is needed
            if (gameState == GameState.DEFAULT && !button.getText().matches(Constants.REGEX_NOT_SCARED)) {
                gameState = GameState.CLICKED;
                updateEmoticonButton();
            }
        }
    }

    private void pressFieldButtonRight(int x, int y) {
        Main.placeRemoveFlag(x, y);
    }

    /**
     * This method handles the left mouse click of the field button on the given coordinates
     *
     * @param x The vertical position of the field button on the field
     * @param y The horizontal position of the field button on the field
     */
    private void releaseFieldButtonLeft(int x, int y) {
        if (gameState != GameState.LOST && gameState != GameState.WON) {
            // the default case
            gameState = Main.walkOnField(x, y);
            // to indicate the mine that exploded
            if (gameState.equals(GameState.LOST)) {
                mineMap[x][y].setForeground(Constants.COLOR_TEXT_EXPLOSION);
            }
        } else {
            // if a new game has to be started
            pressResetButton();
        }
    }

    /**
     * This method issues the start of a new game
     */
    private void pressResetButton() {
        Main.startNewGame();
        gameState = GameState.DEFAULT;
        firstClick = true;
        updateMap();
    }

    /**
     * This method updates the Reset / Emoticon button depending on the current game-state
     */
    private void updateEmoticonButton() {
        switch (gameState) {
            case DEFAULT -> {
                resetButton.setText(Constants.BUTTON_STRING_RESET_DEFAULT);
                resetButton.setBackground(Constants.COLOR_EMOTE_DEFAULT);
            }
            case CLICKED -> {
                resetButton.setText(Constants.BUTTON_STRING_RESET_CLICKED);
                resetButton.setBackground(Constants.COLOR_EMOTE_CLICKED);
            }
            case WON -> {
                resetButton.setText(Constants.BUTTON_STRING_RESET_WON);
                resetButton.setBackground(Constants.COLOR_EMOTE_WON);
            }
            case LOST -> {
                resetButton.setText(Constants.BUTTON_STRING_RESET_LOST);
                resetButton.setBackground(Constants.COLOR_EMOTE_LOST);
            }
        }
    }

    /**
     * This method updates the flag label
     */
    private static void updateFlagLabel() {
        int flagsRemainingInt = Main.getFlagsRemaining();
        String flagsRemainingString = String.valueOf(flagsRemainingInt);
        String flagOrFlags = Constants.BUTTON_STRING_FLAG_PLURAL;
        // to change the text to reflect, that there is a single flag remaining
        if (flagsRemainingInt == 1) {
            flagOrFlags = Constants.BUTTON_STRING_FLAG_SINGULAR;
        }
        // pads the string before setting it as the text of the flag label
        flagLabel.setText("   " + padString(flagsRemainingString, "  ", 2) + " " + flagOrFlags + "     ");
        // changes the background color of the flag label to indicate that no more flags are remaining if there are none left
        if (flagsRemainingInt == 0) {
            menuPanel.setBackground(Constants.COLOR_FLAG_EMPTY);
        } else {
            menuPanel.setBackground(Constants.COLOR_FLAG_NORMAL);
        }
    }

    /**
     * This method updates the contents of every field of the map
     */
    private void updateMap() {
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                updateField(i, j);
            }
        }
        updateEmoticonButton();
        // to let the player see in the finished screen how many flags they placed
        if (!gameState.equals(GameState.LOST) && !gameState.equals(GameState.WON)) {
            updateFlagLabel();
        }
    }

    private static void updateField(int i, int j) {
        SquareJButton button = mineMap[i][j];
        Color backGroundColor;
        Color foreGroundColor = Constants.COLOR_TEXT_DEFAULT;
        String fieldText;
        // sets the appearance of the field, depending on the content of the field
        switch (Main.getFieldOfShownMap(i, j)) {
            case MINE -> {
                backGroundColor = Constants.COLOR_FIELD_MINE;
                fieldText = Constants.DISPLAY_MINE;
            }
            case FREE -> {
                backGroundColor = Constants.COLOR_FIELD_FREE;
                fieldText = Constants.DISPLAY_FREE;
            }
            case ONE -> {
                backGroundColor = Constants.COLOR_FIELD_ONE;
                fieldText = Constants.DISPLAY_ONE;
            }
            case TWO -> {
                backGroundColor = Constants.COLOR_FIELD_TWO;
                fieldText = Constants.DISPLAY_TWO;
            }
            case THREE -> {
                backGroundColor = Constants.COLOR_FIELD_THREE;
                fieldText = Constants.DISPLAY_THREE;
            }
            case FOUR -> {
                backGroundColor = Constants.COLOR_FIELD_FOUR;
                fieldText = Constants.DISPLAY_FOUR;
            }
            case FIVE -> {
                backGroundColor = Constants.COLOR_FIELD_FIVE;
                fieldText = Constants.DISPLAY_FIVE;
            }
            case SIX -> {
                backGroundColor = Constants.COLOR_FIELD_SIX;
                fieldText = Constants.DISPLAY_SIX;
            }
            case SEVEN -> {
                backGroundColor = Constants.COLOR_FIELD_SEVEN;
                fieldText = Constants.DISPLAY_SEVEN;
            }
            case EIGHT -> {
                backGroundColor = Constants.COLOR_FIELD_EIGHT;
                fieldText = Constants.DISPLAY_EIGHT;
            }
            case FLAG -> {
                backGroundColor = Constants.COLOR_FIELD_FLAG;
                fieldText = Constants.DISPLAY_FLAG;
            }
            default -> {
                backGroundColor = Constants.COLOR_FIELD_UNDISCOVERED;
                fieldText = Constants.DISPLAY_UNDISCOVERED;
            }
        }
        // if the game is over, every flag that was placed is indicated by
        // either a white text color of the numbers and the mines or a black text color in case of free fields
        if (gameState.equals(GameState.LOST) || gameState.equals(GameState.WON)) {
            if (button.getText().equals(Constants.DISPLAY_FLAG)) {
                foreGroundColor = Constants.COLOR_TEXT_CONTRAST;
                if (fieldText.equals(Constants.DISPLAY_FREE)) {
                    foreGroundColor = Constants.COLOR_TEXT_DEFAULT;
                    fieldText = Constants.DISPLAY_FLAG;
                }
            }
            // to not change the color of the field with the exploding bomb
            if (button.getForeground().equals(Constants.COLOR_TEXT_EXPLOSION)) {
                foreGroundColor = Constants.COLOR_TEXT_EXPLOSION;
            }
        }
        // updates the button with the final values
        button.setBackground(backGroundColor);
        button.setForeground(foreGroundColor);
        button.setText(fieldText);
    }

    // Miscellaneous Methods
    // This method pads a given string with a given char to a given length
    public static String padString(String string, String padding, int length) {
        // to check if the string is too short and even needs padding
        if (string.length() < length) {
            StringBuilder stringBuilder = new StringBuilder(string);
            // as long as the string isn't long enough yet, the padding-string is added in front of the string
            for (int i = stringBuilder.length(); i < length; i++) {
                stringBuilder.insert(0, padding);
            }
            string = stringBuilder.toString();
        }
        return string;
    }

}