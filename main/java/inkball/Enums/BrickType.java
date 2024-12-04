package inkball.Enums;

/**
 * Enum for brick type data
 */
public enum BrickType {
    Brick0("T0"),
    Brick1("T1"),
    Brick2("T2"),
    Brick3("T3"),
    Brick4("T4");

    private final String type;

    /** Add a type for each brick color */
    BrickType(String type){
        this.type = type;
    }

    /** Return the type for each brick */
    public String getType() {
        return type;
    }
}
