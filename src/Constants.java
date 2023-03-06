import java.awt.*;

/**
 * This is the class for the constant values.
 * Mainly for avoiding "magic numbers"
 *
 * @author Jonathan Matscheko
 * @version 1.0
 */
public class Constants {

    /**
     * Private constructor to avoid object generation.
     */
    private Constants() {
        throw new IllegalStateException("Utility-class constructor.");
    }


    //-------------------------------------------------User-Settings----------------------------------------------------
    // Game values
    /*
      The default values for Minesweeper are:

      Beginner:
      MAP_HEIGHT = 9;
      MAP_WIDTH = 9;
      AMOUNT_OF_MINES = 10;
      AMOUNT_OF_FLAGS = 10;

      Intermediate:
      MAP_HEIGHT = 16;
      MAP_WIDTH = 16;
      AMOUNT_OF_MINES = 40;
      AMOUNT_OF_FLAGS = 40;

      Expert:
      MAP_HEIGHT = 16;
      MAP_WIDTH = 32;
      AMOUNT_OF_MINES = 99;
      AMOUNT_OF_FLAGS = 99;
     */
    /**
     * The height of the map
     * minimum: 5
     * maximum: 16
     */
    public static final int MAP_HEIGHT = 16;
    /**
     * The width of the map
     * minimum: 5
     * maximum: 32
     */
    public static final int MAP_WIDTH = 16;
    /**
     * The amount of mines that are getting placed on the map
     * <p>Depending on desired difficulty and map-size ( MAP_HEIGHT * MAP_WIDTH ), this number should be adjusted:
     * <blockquote><pre>{@code
     * Easy: 0.15625 * map-size
     * Medium: 0.15625 * map-size
     * Hard: 0.20625 * map-size
     * }</pre></blockquote>
     * maximum: ( MAP_HEIGHT * MAP_WIDTH ) - 1
     */
    public static final int AMOUNT_OF_MINES = 40;
    /**
     * The amount of flags the player can use at once ( default is same as AMOUNT_OF_MINES )
     */
    public static final int AMOUNT_OF_FLAGS = 40;
    // Window values
    /**
     * The factor that determines how big the window is compared to the screen
     * The default value is 80
     * Since the value is a percentage, it should be higher than "0" and shouldn't exceed "100"
     * Recommended values are in the range of:
     * "80" to "100" for 1920x1080 resolution
     * "40" to "50" for 3840x2160 resolution
     */
    public static final int WINDOW_SCALING_FACTOR = 40;
    //------------------------------------------------------------------------------------------------------------------

    // Warning!
    // The following values should not be changed with unless you know what you're doing

    /**
     * The location of the Minesweeper.png File
     */
    public static final String FILE_LOCATION_ICON = "Minesweeper.png";

    // Window values
    /**
     * The regular expression for any field-text except mine and undiscovered
     */
    public static String REGEX_NOT_SCARED = "[F12345678 ]";
    /**
     * The text that is displayed on a field, when no mine is next to it
     */
    public static String DISPLAY_FREE = " ";
    /**
     * The text that is displayed on a field, when one mine is next to it
     */
    public static String DISPLAY_ONE = "1";
    /**
     * The text that is displayed on a field, when two mines are next to it
     */
    public static String DISPLAY_TWO = "2";
    /**
     * The text that is displayed on a field, when three mines are next to it
     */
    public static String DISPLAY_THREE = "3";
    /**
     * The text that is displayed on a field, when four mines are next to it
     */
    public static String DISPLAY_FOUR = "4";
    /**
     * The text that is displayed on a field, when five mines are next to it
     */
    public static String DISPLAY_FIVE = "5";
    /**
     * The text that is displayed on a field, when six mines are next to it
     */
    public static String DISPLAY_SIX = "6";
    /**
     * The text that is displayed on a field, when seven mines are next to it
     */
    public static String DISPLAY_SEVEN = "7";
    /**
     * The text that is displayed on a field, when eight mines are next to it
     */
    public static String DISPLAY_EIGHT = "8";
    /**
     * The text that is displayed on a field, when a mine is on it
     */
    public static String DISPLAY_MINE = "*";
    /**
     * The text that is displayed on a field, when a flag is placed on it
     */
    public static String DISPLAY_FLAG = "F";
    /**
     * The text that is displayed on a field, when its not yet revealed
     */
    public static String DISPLAY_UNDISCOVERED = "   ";

    // Button texts
    /**
     * The text of the hint-button
     */
    public static String BUTTON_STRING_HINT = "Hint";
    // The emote-button texts
    /**
     * The text that is displayed on the emote-button, when nothing happens
     */
    public static String BUTTON_STRING_RESET_DEFAULT = "^.^";
    /**
     * The text that is displayed on the emote-button, when the player clicks on a field
     */
    public static String BUTTON_STRING_RESET_CLICKED = "o.o";
    /**
     * The text that is displayed on the emote-button, when the game is won
     */
    public static String BUTTON_STRING_RESET_WON = "^o^";
    /**
     * The text that is displayed on the emote-button, when the game is lost
     */
    public static String BUTTON_STRING_RESET_LOST = "x.x";
    /**
     * The text of the flag-button, if the plural is needed
     */
    public static String BUTTON_STRING_FLAG_PLURAL = "Flags";
    /**
     * The text of the flag-button, if the singular is needed
     */
    public static String BUTTON_STRING_FLAG_SINGULAR = "Flag";

    // Colors
    /**
     * The color the symbol on a field has by default
     */
    public static final Color COLOR_TEXT_DEFAULT = new Color(0, 0, 0);
    /**
     * The color the symbol on a field has, when the game was lost and a flag was placed on that field
     */
    public static final Color COLOR_TEXT_CONTRAST = new Color(252, 252, 252);
    /**
     * The color the symbol on a field has, when it housed the mine that exploded
     */
    public static final Color COLOR_TEXT_EXPLOSION = new Color(252, 0, 0);
    /**
     * The color of the hint-button
     */
    public static final Color COLOR_HINT = new Color(252, 252, 252);

    // Emote button colors
    /**
     * The color of the emote-button, when nothing happens
     */
    public static final Color COLOR_EMOTE_DEFAULT = new Color(252, 252, 252);
    /**
     * The color of the emote-button, when the player clicks on a field
     */
    public static final Color COLOR_EMOTE_CLICKED = new Color(252, 175, 0);
    /**
     * The color of the emote-button, when the game is won
     */
    public static final Color COLOR_EMOTE_WON = new Color(252, 0, 252);
    /**
     * The color of the emote-button, when the game is lost
     */
    public static final Color COLOR_EMOTE_LOST = new Color(252, 0, 0);

    // Flag button colors
    /**
     * The color of the flag-button, when at least one flag is available
     */
    public static final Color COLOR_FLAG_NORMAL = new Color(252, 252, 252);
    /**
     * The color of the flag-button, when no more flags are available
     */
    public static final Color COLOR_FLAG_EMPTY = new Color(252, 0, 0);

    // Field button colors
    /**
     * The color a field has, when no mine is next to it
     */
    public static final Color COLOR_FIELD_FREE = new Color(252, 252, 252);
    /**
     * The color a field has, when one mine is next to it
     */
    public static final Color COLOR_FIELD_ONE = new Color(63, 126, 252);
    /**
     * The color a field has, when two mines are next to it
     */
    public static final Color COLOR_FIELD_TWO = new Color(0, 252, 0);
    /**
     * The color a field has, when three mines are next to it
     */
    public static final Color COLOR_FIELD_THREE = new Color(252, 0, 0);
    /**
     * The color a field has, when four mines are next to it
     */
    public static final Color COLOR_FIELD_FOUR = new Color(126, 0, 252);
    /**
     * The color a field has, when five mines are next to it
     */
    public static final Color COLOR_FIELD_FIVE = new Color(252, 126, 0);
    /**
     * The color a field has, when six mines are next to it
     */
    public static final Color COLOR_FIELD_SIX = new Color(0, 252, 252);
    /**
     * The color a field has, when seven mines are next to it
     */
    public static final Color COLOR_FIELD_SEVEN = new Color(252, 252, 0);
    /**
     * The color a field has, when eight mines are next to it
     */
    public static final Color COLOR_FIELD_EIGHT = new Color(252, 0, 126);
    /**
     * The color a field has, when a mine is on it
     */
    public static final Color COLOR_FIELD_MINE = new Color(0, 0, 0);
    /**
     * The color a field has, when its not yet revealed
     */
    public static final Color COLOR_FIELD_UNDISCOVERED = new Color(180, 180, 180);
    /**
     * The color a field has, when it's not yet discovered and a flag is placed on top of it
     * Default: Same value as COLOR_FIELD_UNDISCOVERED
     */
    public static final Color COLOR_FIELD_FLAG = new Color(180, 180, 180);

    // Miscellaneous Values
    /**
     * The character used to separate the x- and y-coordinates, when saved in a string
     */
    public static final String SEPARATION_TOKEN = ";";
    /**
     * The width of each field-button
     */
    public static final int BUTTON_WIDTH = 0;
    /**
     * The height of each field-button
     */
    public static final int BUTTON_HEIGHT = 0;

}