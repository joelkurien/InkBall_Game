package inkball;

import processing.core.PVector;

/**
 * This class is responsible for drawing the line segments on to the canvas and 
 * it handles ball to line segment collision detection and reflection.
 */
public class Line extends Vector implements Collisions{
    protected int x1, y1, x2, y2; /**starting and ending points of the line segment*/
    public boolean collided = false; /**checks if a line a colllided or not*/
    public int collisionBuffer = 0; /**buffer to handle internal line collisions*/
    
    /**
     * Constructor for creating a line segment.
     */
    public Line(int x1, int y1, int x2, int y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }


    /**
     * This method checks for collisions between the line represented by the current instance and the balls in the provided list.
     * If a collision is detected, the ball's velocity is adjusted accordingly, and the collisionBuffer and collided flags are updated.
     *
     * @param ball the ball to check for collisions with
     */
    public void handleCollision(Ball ball){
        if(ball.xspeed != 0 || ball.yspeed!= 0){
            PVector v = new PVector(ball.xspeed, ball.yspeed);
            PVector p = new PVector(ball.x, ball.y);
            PVector p1 = new PVector(x1, y1);
            PVector p2 = new PVector(x2, y2);
            PVector n = selectNormal(p1, p2, p);
            if(collisionBuffer % ball.xspeed == 0 || collisionBuffer % ball.yspeed == 0){
                if(PVector.dist(p1, p)+PVector.dist(p2,p) < PVector.dist(p1,p2)+ball.radius){
                    v = velocityChange(n, v);
                    ball.xspeed = v.x;
                    ball.yspeed = v.y;
                    ball.updateSpeed();
                    collisionBuffer = 1;
                    collided = true;
                }
            } else collisionBuffer++;
        }
    }

    /**
     * Draws each line segment with a stroke color of black and thickness of 10.
     */
    public void draw(App app) {
        app.stroke(0);
        app.strokeWeight(10);
        app.line(x1, y1, x2, y2);  
        for(Ball ball: app.levelBalls)
            if(!ball.remove) handleCollision(ball);
    }
}

