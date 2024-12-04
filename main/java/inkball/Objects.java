package inkball;

import processing.core.*;

/**
 * This is the overall head of almost all the classes in the game. 
 * It specifies the features that each game object share.
 */
public abstract class Objects extends PApplet {
    public float x; /** x position of the game object*/
    public float y; /** y position of the game object*/
    public String objectType; /** type of the game object*/
    public String objectNo; /** object no of the game object*/
    
    /**
     * Constructor of the game object
     */
    public Objects(int x, int y, String objectType, String objectNo){
        this.x = x;
        this.y = y;
        this.objectType = objectType;
        this.objectNo = objectNo;
    }

    /**
     * Method that returns the object type.
     */
    public String getTileType(){
        return objectType;
    }
    
    /**
     * Abstract method that handles drawing a game object
     */
    public abstract void draw(App app);
}
