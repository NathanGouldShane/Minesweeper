/**
 * This is the enum for the different field-contents
 * @author Jonathan Matscheko
 * @version 1.0
 */
public enum FieldContent {

    FREE("0"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    MINE("M"),
    FLAG("F"),
    UNDISCOVERED(" ");

    private final String name;

    /**
     * This is the constructor method
     * @param name The name of the FieldContent
     */
    FieldContent(String name) {
        this.name = name;
    }

    /**
     * This method parses the given string into a FieldContent
     * @param integer The given integer
     * @return The returned FieldContent
     */
    public static FieldContent toFieldContent(int integer) {
        String numberAsString = String.valueOf(integer);
        for (FieldContent fieldContent: FieldContent.values()) {
            if (numberAsString.equals(fieldContent.name)) {
                return fieldContent;
            }
        }
        return FREE;
    }
}