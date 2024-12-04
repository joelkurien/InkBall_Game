package inkball;

import inkball.Enums.Color;
import processing.core.PVector;

/**
 * Class for creating holes in the game. The holes are represented as 64x64 pixel images.
 * When a ball reaches near a hole, the ball is pulled towards the hole.
 * The class also handles what happens when an ball enters an incorrect hole.
 */
public class Hole extends Objects{
    
    private float centerX; /**find the x-axis center of the hole  */
    private float centerY; /**find the y-axis center of the hole */
    
    /**
     * Constructor for the Hole class: it initializes the top left corner coordinates of the hole
     * and the type of hole and its identifier
     * @param holeNo
     */
    public Hole(int x, int y, String holeType, String holeNo) {
        super(x, y, holeType, holeNo);
        centerX = x+App.CELLSIZE;
        centerY = y+App.CELLSIZE;
    }

    /**
     * Method to attract the ball into the hole with some force of attraction and 
     * handles what happens when a ball enters an incorrect hole.
     */
    public void holeAttraction(App app, Ball ball){

        PVector ballPos = new PVector(ball.x, ball.y);
        PVector ballVel = new PVector(ball.xspeed, ball.yspeed);
        PVector holePos = new PVector(centerX, centerY);
        
        PVector force = PVector.sub(holePos, ballPos);
        force.mult(0.005f);
        float distance = PVector.dist(ballPos, holePos);
        if(distance < App.CELLSIZE){
            ballVel.add(force);
            float sizeFactor = map(distance, 0, App.CELLSIZE, 3, ball.ballImage.width); 
            float newSize = constrain(sizeFactor, 3, ball.ballImage.width);
            ball.xFactor = newSize;
            ball.yFactor = newSize;
            if(distance < 12){
                ballVel.set(0,0);
                if((!ball.objectNo.equals("0") & !ball.objectNo.equals(objectNo) & !objectNo.equals("0"))){
                    String ballColor = Color.values()[Integer.parseInt(ball.objectNo)].toString();
                    app.ballArray.add(ballColor);
                    float decrement = app.minusPoints * app.holeMinusPoints.get(Color.values()[Integer.parseInt(objectNo)].toString());
                    app.totalScore -= (int)decrement;
                }
                else{
                    float increment = app.plusPoints * app.holePlusPoints.get(Color.values()[Integer.parseInt(objectNo)].toString());
                    app.totalScore += (int)increment;
                }
                ball.remove = true;
            }
        }
        ball.xspeed = ballVel.x;
        ball.yspeed = ballVel.y;
    }

    /**
     * Method to draw the hole on the canvas
     */
    public void draw(App app) {
        for(Ball ball: app.levelBalls){
            if(!ball.remove){
                holeAttraction(app, ball);
            }
        }
        app.image(app.images.get("hole"+objectNo), x, y);
    }
}
