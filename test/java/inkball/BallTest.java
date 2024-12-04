package inkball;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class BallTest {
    Ball ball;
    @BeforeEach
    void setup(){
        //setting up the ball object
        ball = new Ball(0,0,"B1","1");
    }

    @Test
    public void testCollisionLeftBoundary() {
        //testing that the ball speed changes direction in x axis on left wall collision
        ball.xspeed = 2;
        ball.x = -1; 
        ball.boardCollision();
        assertEquals(-2, ball.xspeed);
    }

    @Test
    public void testCollisionRightBoundary() {
        //testing that the ball speed changes direction in x axis on right wall collision
        ball.xspeed = 2;
        ball.x = App.WIDTH - 24 + 1; 
        ball.boardCollision();
        assertEquals(-2, ball.xspeed);
    }

    @Test
    public void testCollisionTopBoundary() {
        //testing that the ball speed changes direction in y axis on top wall collision
        ball.y = 5; 
        ball.yspeed = 2;
        ball.boardCollision();
        assertEquals(-2, ball.yspeed);
    }

    @Test
    public void testCollisionBottomBoundary() {
        //testing that the ball speed changes direction in y axis on bottom wall collision
        ball.y = App.HEIGHT - 24 + 1;
        ball.yspeed = 2; 
        ball.boardCollision();
        assertEquals(-2, ball.yspeed);
    }
}
