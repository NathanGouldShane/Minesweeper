import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * This is the main class
 *
 * @author Jonathan Matscheko
 * @version 1.0
 */
public class Main {
    // The window that opens when starting the game
    private static JFrame window;
    // The amount of flags remaining in the player's inventory
    private static int flagsRemaining;
    // The map that has all the values filled
    private static FieldContent[][] hiddenMap;
    // The map the player gets to see ( redundant with the button-array in the Window.java class but I decided to leave it so the Window-class isn't needed for functionality (  in case a new interface is wanted )
    private static FieldContent[][] shownMap;
    // The emoticon that is currently displayed on the top of the window, indicating the current state of the game
    public static String currentEmoticon = Constants.BUTTON_STRING_RESET_DEFAULT;
    // Values for the hint functionality
    private static LinkedList<String> safeCoordinates = new LinkedList<>();
    // The horizontal position of the hint that is currently shown
    private static int currentHintX;
    // The vertical position of the hint that is currently shown
    private static int currentHintY;
    // Whether or not the field of the current hint had a flag on it or not
    private static boolean hintHadFlag;
    // Values for the auto-fill functionality
    // This linked list contains all the fields that still need to be checked
    private static LinkedList<String> coordinatesToCheck = new LinkedList<>();
    // This linked list contains all the fields that were already checked
    private static LinkedList<String> checkedCoordinates = new LinkedList<>();

    /**
     * The main method
     */
    public static void main(String[] args) {
        // to check if a game start could lead to problems
        if (checkIfValuesInvalid()) {
            return;
        }
        startNewGame();
        initializeWindow();
    }


    // initialisation methods

    /**
     * This method starts a new game
     */
    public static void startNewGame() {
        flagsRemaining = Constants.AMOUNT_OF_FLAGS;
        initializeShownMap();
        initializeHiddenMap();
    }

    /**
     * This method creates a new, free map for the map that is seen by the player
     */
    private static void initializeShownMap() {
        // Creates a new FieldContent-array and fills it with undiscovered fields
        shownMap = new FieldContent[Constants.MAP_HEIGHT][Constants.MAP_WIDTH];
        for (int i = 0; i < Constants.MAP_HEIGHT; i++) {
            for (int j = 0; j < Constants.MAP_WIDTH; j++) {
                shownMap[i][j] = FieldContent.UNDISCOVERED;
            }
        }
    }

    /**
     * This method creates a new, free map for the map that is not seen by the player
     */
    private static void initializeHiddenMap() {
        // Creates a new FieldContent-array and fills it with free fields
        hiddenMap = new FieldContent[Constants.MAP_HEIGHT][Constants.MAP_WIDTH];
        for (int i = 0; i < Constants.MAP_HEIGHT; i++) {
            for (int j = 0; j < Constants.MAP_WIDTH; j++) {
                hiddenMap[i][j] = FieldContent.FREE;
            }
        }
    }

    /**
     * This method handles the window-setup
     */
    private static void initializeWindow() {
        // calculates the needed dimensions and position for the window
        GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int screenWidth = screen.getDisplayMode().getWidth();
        int screenHeight = screen.getDisplayMode().getHeight();
        int windowWidth = (int) (screenWidth * (0.01 * Constants.WINDOW_SCALING_FACTOR));
        int windowHeight = (int) (screenHeight * (0.01 * Constants.WINDOW_SCALING_FACTOR));
        int windowLocationX = (screenWidth - windowWidth) / 2;
        int windowLocationY = (screenHeight - windowHeight) / 2;
        // Creates the new window and assigns the values to it
        SwingUtilities.invokeLater(() -> {
            window = new Window("Minesweeper by Jonathan Matscheko", Constants.MAP_HEIGHT, Constants.MAP_WIDTH);
            window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);
            window.setResizable(false);
            ImageIcon windowIcon = new ImageIcon(Constants.FILE_LOCATION_ICON);
            window.setIconImage(windowIcon.getImage());
            window.setSize(windowWidth, windowHeight);
            window.setLocation(windowLocationX, windowLocationY);
            window.setVisible(true);
        });
    }

    /**
     * This method calculates a playing field after the first click, by calling the corresponding methods
     *
     * @param x The x-coordinate of the first click
     * @param y The y-coordinate of the first click
     */
    public static void placeMinesAfterClick(int x, int y) {
        placeMinesRandomly(x, y);
        calculateFreeFields();
    }

    /**
     * This method places mines randomly on the hidden map
     * The location of the click is used to avoid the placement of mines on the field the player clicked on
     *
     * @param clickX The x-coordinate of the click
     * @param clickY The y-coordinate of the click
     */
    private static void placeMinesRandomly(int clickX, int clickY) {
        LinkedList<Integer> viableMineLocations = new LinkedList<>();
        LinkedList<Integer> mineLocations = new LinkedList<>();
        int k = 0;
        // Fills the list of viable mine locations with every field, except the one, the player clicked on
        for (int i = 0; i < Constants.MAP_WIDTH*Constants.MAP_HEIGHT; i++) {
                if (i != clickX * Constants.MAP_WIDTH + clickY) {
                    viableMineLocations.add(k);
                }
                k++;
        }
        // as long a mines are remaining, a free field is chosen at random and removed from the list of viable locations and added to the list of mine locations
        for (int m = 0; m < Constants.AMOUNT_OF_MINES; m++) {
            int currentMineIndexInList = returnRandomInt(viableMineLocations.size() - 1);
            int currentMineLocation = viableMineLocations.remove(currentMineIndexInList);
            mineLocations.add(currentMineLocation);
        }
        // Transforms the integers into x and y coordinates on which it places mines
        for (int mineLocation : mineLocations) {
            int mineX = mineLocation / Constants.MAP_WIDTH;
            int mineY = mineLocation % Constants.MAP_WIDTH;
            hiddenMap[mineX][mineY] = FieldContent.MINE;
        }
    }

    /**
     * This method calculates the number values of the fields which don't have a mine in them
     * It then assigns the fields those values
     */
    private static void calculateFreeFields() {
        safeCoordinates = new LinkedList<>();
        for (int x = 0; x < Constants.MAP_HEIGHT; x++) {
            for (int y = 0; y < Constants.MAP_WIDTH; y++) {
                if (!hiddenMap[x][y].equals(FieldContent.MINE)) {
                    int number = 0;
                    // the fields around the field are getting inspected to count the number of mines, adjacent to the field
                    for (int k = -1; k < 2; k++) {
                        for (int l = -1; l < 2; l++) {
                            // checks if the coordinates are even on the map
                            if (checkIfInBounds(x + k, y + l)) {
                                if (hiddenMap[x + k][y + l].equals(FieldContent.MINE)) {
                                    number++;
                                }
                            }
                        }
                    }
                    hiddenMap[x][y] = FieldContent.toFieldContent(number);
                    safeCoordinates.add(x + Constants.SEPARATION_TOKEN + y);
                }
            }
        }
    }


    // methods that handle the input

    /**
     * This method places or removes the flag on the field
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     */
    public static void placeRemoveFlag(int x, int y) {
        // removing the flag, if one is placed on the coordinates
        if (shownMap[x][y].equals(FieldContent.FLAG)) {
            shownMap[x][y] = FieldContent.UNDISCOVERED;
            flagsRemaining += 1;
        } else {
            // if no flag is placed on the coordinates, the field is undiscovered and there are flags remaining, a flag is placed
            if (flagsRemaining > 0 && shownMap[x][y] == FieldContent.UNDISCOVERED) {
                shownMap[x][y] = FieldContent.FLAG;
                flagsRemaining -= 1;
            }
        }
    }

    /**
     * This method emulates the player walking on the field with the given coordinates
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return The game-state the game has after the click on the map
     */
    public static GameState walkOnField(int x, int y) {
        if (!shownMap[x][y].equals(FieldContent.FLAG)) {
            if (hiddenMap[x][y].equals(FieldContent.MINE)) {
                // if the click is on a mine, the game ends
                revealWholeMap();
                return GameState.LOST;
            } else {
                // the field is getting revealed
                revealMapCoordinates(x, y);
                //if it is a field with zero neighboring mines, the whole free area is revealed
                if (shownMap[x][y].equals(FieldContent.FREE)) {
                    coordinatesToCheck = new LinkedList<>();
                    checkedCoordinates = new LinkedList<>();
                    revealAdjacentFreeCells(x, y);
                }
            }
        }
        // checks if the game is finished
        if (checkIfFinished()) {
            currentEmoticon = Constants.BUTTON_STRING_RESET_WON;
            revealWholeMap();
            return GameState.WON;
        }
        return GameState.DEFAULT;
    }

    /**
     * This method reveals all adjacent fields, for the block of free fields given
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     */
    private static void revealAdjacentFreeCells(int x, int y) {
        coordinatesToCheck.add(x + Constants.SEPARATION_TOKEN + y);
        // iterates over every coordinates in the list of coordinates it still has to check,removes it from the list it has to check
        // and adds it to the list of coordinates it already checked
        while (!coordinatesToCheck.isEmpty()) {
            String currentCoordinates = coordinatesToCheck.removeFirst();
            // the coordinates got checked and are getting removed from the coordinates that have to be checked and added to the checked ones
            // this is done now to save time by not checking the current coordinates
            checkedCoordinates.add(currentCoordinates);
            String[] splitCurrentCoordinates = currentCoordinates.split(Constants.SEPARATION_TOKEN);
            x = Integer.parseInt(splitCurrentCoordinates[0]);
            y = Integer.parseInt(splitCurrentCoordinates[1]);
            // the adjacent fields are added to the list with coordinates it still has to check, if it is free and if it isn't in the list of already checked coordinates
            // however it always is getting revealed, to show the border of the free zone
            // the already checked list is there so it doesn't run into an infinite loop
            for (int k = -1; k < 2; k++) {
                for (int l = -1; l < 2; l++) {
                    int a = x+k;
                    int b = y+l;
                    String fieldCoordinates = a + Constants.SEPARATION_TOKEN + b;
                    // the check on whether or not it already checked the coordinates
                    if (!checkedCoordinates.contains(fieldCoordinates)) {
                        // the check whether the coordinates are even on the field
                        if (checkIfInBounds(a, b)) {
                            // to check whether the field is not yet revealed
                            if (shownMap[a][b].equals(FieldContent.UNDISCOVERED)) {
                                revealMapCoordinates(a, b);
                                // if the field is also free, it is getting added to the coordinates it has to check
                                if (shownMap[a][b].equals(FieldContent.FREE)) {
                                    coordinatesToCheck.add(fieldCoordinates);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * This method shows or hides the field, depending on the input
     *
     * @param show Whether or not the field should be shown or hidden
     */
    public static void showOrHideHint(boolean show) {
        // whether the hint is getting shown or hidden
        if (show) {
            // the next viable hint coordinates are used
            for (String hintCoordinates : safeCoordinates) {
                String[] splitHintCoordinates = hintCoordinates.split(Constants.SEPARATION_TOKEN);
                int hintX = Integer.parseInt(splitHintCoordinates[0]);
                int hintY = Integer.parseInt(splitHintCoordinates[1]);
                if (shownMap[hintX][hintY] == FieldContent.UNDISCOVERED) {
                    // the content of the current hint is shown
                    revealMapCoordinates(hintX, hintY);
                    currentHintX = hintX;
                    currentHintY = hintY;
                    return;
                } else if (shownMap[hintX][hintY] == FieldContent.FLAG) {
                    // if the current hint is a field with a flag, it removes the flag, to show, that it is not necessary
                    hintHadFlag = true;
                    shownMap[hintX][hintY] = FieldContent.UNDISCOVERED;
                    currentHintX = hintX;
                    currentHintY = hintY;
                    return;
                }
            }
        } else {
            if (!hintHadFlag) {
                // the content is hidden again
                shownMap[currentHintX][currentHintY] = FieldContent.UNDISCOVERED;
            } else {
                // the flag is placed back down again
                shownMap[currentHintX][currentHintY] = FieldContent.FLAG;
                hintHadFlag = false;
            }
        }
    }


    // map reveal methods

    /**
     * This method assigns the value of the hidden map to the shown map, for the given coordinates
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     */
    private static void revealMapCoordinates(int x, int y) {
        // to give flags back, when a field is revealed
        // this only happens when a larger area is revealed
        if (shownMap[x][y].equals(FieldContent.FLAG)) {
            flagsRemaining += 1;
        }
        shownMap[x][y] = hiddenMap[x][y];
    }

    /**
     * This method assigns the value of the hidden map to the shown map
     */
    private static void revealWholeMap() {
        for (int i = 0; i < Constants.MAP_HEIGHT; i++) {
            for (int j = 0; j < Constants.MAP_WIDTH; j++) {
                revealMapCoordinates(i, j);
            }
        }
    }

    // getter methods

    /**
     * This method gives back the content of the field on the given coordinates
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return The content of the field
     */
    public static FieldContent getFieldOfShownMap(int x, int y) {
        return shownMap[x][y];
    }

    /**
     * This method returns the amount of the remaining flags
     *
     * @return The amount of remaining flags
     */
    public static int getFlagsRemaining() {
        return flagsRemaining;
    }


    // Miscellaneous methods

    /**
     * This method checks, if the values in the Constants.java file do not allow a game to get started
     *
     * @return Whether the game-start has to be cancelled or not
     */
    private static boolean checkIfValuesInvalid() {
        // The following checks prevent the game from running if the values in the Constants-class would lead to problems and to prevent tampering with the hard limits, "magic numbers" are used
        // This check is to avoid the game from running of the height of the map is unreasonable
        boolean inValidHeight = Constants.MAP_HEIGHT < 5 || Constants.MAP_HEIGHT > 32;
        // This check is to avoid the game from running of the width of the map is unreasonable
        boolean inValidWidth = Constants.MAP_WIDTH < 5 || Constants.MAP_WIDTH > 32;
        // This check is to avoid that the game can't set the map up properly because more mines than fields exist
        boolean inValidAmountOfMines = Constants.AMOUNT_OF_MINES < 0 || Constants.AMOUNT_OF_MINES >= Constants.MAP_HEIGHT * Constants.MAP_WIDTH;
        // This check is to avoid the window being bigger than the monitor
        boolean inValidScalingFactor = Constants.WINDOW_SCALING_FACTOR < 0 || Constants.WINDOW_SCALING_FACTOR > 100;
        // note: coded in such a way to allow for further checks to be added in the future if needed
        return inValidHeight && inValidWidth && inValidAmountOfMines && inValidScalingFactor;
    }

    /**
     * This method checks, whether or not a given coordinate is on the field or not
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return Is in bounds
     */
    private static boolean checkIfInBounds(int x, int y) {
        boolean outOfBoundsHeight = x < 0 || x > Constants.MAP_HEIGHT - 1;
        boolean outOfBoundsWidth = y < 0 || y > Constants.MAP_WIDTH - 1;
        // note: coded in such a way to allow for further checks to be added in the future if needed
        return !outOfBoundsHeight && !outOfBoundsWidth;
    }

    /**
     * This method checks, whether or not the round has been successfully completed
     *
     * @return Whether or not the map has been successfully completed
     */
    private static boolean checkIfFinished() {
        int numberOfRemainingMines = Constants.AMOUNT_OF_MINES;
        // checks if there is an equal amount of undiscovered fields and mines, if true, every safe field has been discovered and the game is won
        for (int i = 0; i < Constants.MAP_HEIGHT; i++) {
            for (int j = 0; j < Constants.MAP_WIDTH; j++) {
                if (shownMap[i][j] == FieldContent.UNDISCOVERED || shownMap[i][j] == FieldContent.FLAG) {
                    numberOfRemainingMines -= 1;
                }
            }
        }
        return numberOfRemainingMines >= 0;
    }

    /**
     * This method gives back a random integer between zero and the given integer
     *
     * @param maximum The highest value, the return can have
     * @return The random integer in the given interval
     */
    private static int returnRandomInt(int maximum) {
        double randomDouble = Math.random() * (maximum + 1) + 0;
        return (int) randomDouble;
    }

}
