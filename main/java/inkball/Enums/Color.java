package inkball.Enums;

public enum Color {
    grey("B0"),
    orange("B1"), 
    blue("B2"),
    green("B3"), 
    yellow("B4");

    private final String type;

    /**
     * Sets the color of a ball
     */
    Color(String type){
        this.type = type;
    }

    /**
     * Returns the color of a ball.
     */
    public String getType(){
        return type;
    }
}
