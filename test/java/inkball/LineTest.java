package inkball;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class LineTest {
    public int collisionBuffer = 0;
    App app;

    Line line;
    @BeforeEach
    void setup(){
        //setting up the line object.
        line = new Line(3,4, 5, 6);
    }

    @Test
    void testLineCollision(){
        //testing that the line collision function is working properly. -> the line after collision should be marked
        //as true.
        line.handleCollision(new Ball(4,5, "B0", "0"));
        assertTrue(line.collided);
    }

    @Test
    void testLineCollision_collisionBuffer(){
        //testing that the line does not get collided when the collision buffer is not divisible xspeed or yspeed of the ball.
        line.collisionBuffer = 1;

        line.handleCollision(new Ball(4,5, "B0", "0"));
        assertFalse(line.collided);
    }

    @Test
    void testLineCollision_collisionBufferNotDivisible(){
        //testing that the line does not get collided when the collision buffer is not divisible xspeed but
        //it is divisible yspeed of the ball 
        line.collisionBuffer = 1;
        Ball ball = new Ball(4,5, "B0", "0");
        ball.xspeed = 2.0f;
        ball.yspeed = 1.0f;
        line.handleCollision(ball);
        assertTrue(line.collided);
    }

    @Test
    void testLineCollision_BallNotMovingX(){
        //testing what happens when the ball is not moving in the xdirection.
        line.collisionBuffer = 1;
        Ball ball = new Ball(4,5, "B0", "0");
        ball.xspeed = 1;
        ball.yspeed = 0f;
        line.handleCollision(ball);
        assertTrue(line.collided);
    }

    @Test
    void testLineCollision_BallNotMovingY(){
        //testing what happens when the ball is not moving in the ydirection.
        line.collisionBuffer = 1;
        Ball ball = new Ball(4,5, "B0", "0");
        ball.xspeed = 0;
        ball.yspeed = 1f;
        line.handleCollision(ball);
        assertTrue(line.collided);
    }

    @Test
    public void testDraw() {
        //testing drawing the line and if the collision should be handled if and only if the ball.remove = false.
        line = new Line(1,2,3,4);
        app = new App(); 
        Ball ball1 = new Ball(1,2, "B0", "0"); 
        ball1.remove = true;
        app.levelBalls = new ArrayList<>();
        app.levelBalls.add(ball1);

        app.delay(500);
        try{
            line.draw(app);
        }
        catch(RuntimeException e){
            e.printStackTrace();
        }
        assertFalse(line.collided);
    }

}
