package inkball.Enums;

/**
 * Enum for setting the types of walls in the game
 * These include both rigid walls and bricks
 */
public enum WallType {
    Wall0("X"),
    Wall1("1"),
    Wall2("2"),
    Wall3("3"),
    Wall4("4");

    private final String type;

    /**
     * Returns the type of a wall
     */
    WallType(String type){
        this.type = type;
    }

    /**
     * Returns the rigid wall types
     */
    public String getWallType(){
        return type;
    }
}
