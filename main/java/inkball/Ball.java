package inkball;

import java.util.Random;

import processing.core.PImage;

/**
 * Class for creating the balls and their functionalities.
 */
public class Ball extends Objects {
    protected float xspeed, yspeed; /**sets the speed of the ball in x and y axes  */
    public int[] speeds = {2, -2}; /**the inital two options of the ball speed */
    public int radius = 12; /**radius of the ball */
    protected float xFactor = 2*radius; /**x-scale of the ball image */
    protected float yFactor = 2*radius; /**y-scale of the ball imate */
    public boolean remove = false; /**checks if the ball has to be removed from the board or not */
    public PImage ballImage; /**stores the image of the ball */

    Random random = new Random();

    /**
     * Constructor for the ball class
     */
    public Ball(int x, int y, String ballType, String ballNo){
        super(x, y, ballType, ballNo);
        int speed = speeds[random.nextInt(2)];
        xspeed = speed;
        yspeed = speed;
    }

    /**
     * Updates the location of the ball based on the speed
     */
    public void updateSpeed(){
        x += xspeed;
        y += yspeed;
    }

    /**
     * Method to handle board collision so that the ball does not go out of the screen.
     */
    public void boardCollision(){
        if (x < 0 || x + 2 * radius > App.WIDTH) {
            xspeed *= -1;
          }
          if (y < App.TOPBAR || y + 2 * radius > App.HEIGHT) {
            yspeed *= -1;
          }
    }

    /**
     * Draws the ball onto the canvas
     */
    public void draw(App app){
        ballImage = app.images.get("ball"+objectNo);

        if(!app.paused && !app.gameOver)
            updateSpeed();
        boardCollision();
        app.image(ballImage, x, y, xFactor, yFactor);
    }
}
