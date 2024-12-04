package inkball;

import org.junit.jupiter.api.Test;

import processing.core.PImage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

import static org.mockito.Mockito.*;

public class WallTest {
    App app;
    Wall wall;
    @Test
    void testDraw_simpleWall(){
        //testing what happens when the ball collides with a normal wall.
        Wall wall = new Wall(1,2, "B0", "0");
        App app = new App();
        app.images = new HashMap<>();
        wall.objectNo = "1"; 
        wall.collideCount = 0;
        app.images.put("wall1", mock(PImage.class)); 
        wall.x = 50; 
        wall.y = 50;
        app.levelBalls = new ArrayList<>();
        try{
            wall.draw(app);
        }
        catch(RuntimeException e) {
            e.printStackTrace();
        }
        assertTrue(wall.collidable);
    }

    @Test
    void testDraw_brick(){
        //testing what happens when the ball collides with a brick wall twice.
        Wall wall = new Wall(50, 50, "T1", "1");
        App app = new App();
        app.images = new HashMap<>();
        wall.collideCount = 2;
        app.images.put("wall1", mock(PImage.class));
        app.images.put("wallB1", mock(PImage.class)); 
        Ball ball = new Ball(50, 50, "B1", "1");
        PImage mockTileImage = mock(PImage.class);
        app.images.put("tile", mockTileImage);

        app.delay(500);
        wall.handleCollision(ball);
        assertFalse(wall.collidable);
    }

    @Test
    void testDraw_brick_firstCollide(){
        //testing what happens when the ball collides with a brick wall for the first time.
        Wall wall = new Wall(50, 50, "T1", "1");
        App app = new App();
        app.images = new HashMap<>();
        wall.collideCount = 0;
        app.images.put("wall1", mock(PImage.class));
        app.images.put("wallB1", mock(PImage.class)); 
        Ball ball = new Ball(50, 50, "B1", "1");
        PImage mockTileImage = mock(PImage.class);
        app.images.put("tile", mockTileImage);

        app.delay(500);
        wall.handleCollision(ball);
        assertTrue(wall.collidable);
    }
    @Test
    void testDraw_brickone(){
        //testing what happens when the ball collides with a brick wall once.
        Wall wall = new Wall(50, 50, "T1", "T1");
        App app = new App();
        app.images = new HashMap<>();
        wall.collideCount = 1;
        app.images.put("wall1", mock(PImage.class));
        app.images.put("wallB1", mock(PImage.class)); 

        PImage mockTileImage = mock(PImage.class);
        app.images.put("tile", mockTileImage);

        app.delay(1000);
        try{
            wall.draw(app);
        }
        catch(RuntimeException e) {
            e.printStackTrace();
        }
        assertTrue(wall.collidable);
    }

    @Test
    void testhandleCollision_simpleWall(){
        //testing that the ball speed changes when the ball collides with the wall/bricks.
        Wall wall = new Wall(3,4,"1", "1");
        Ball ball = new Ball(2,3, "B1", "1");
        ball.xspeed = 2.0f;
        ball.yspeed = 2.0f;

        wall.handleCollision(ball);

        assertEquals(4.0, ball.x);

    }

    @Test
    void testhandleCollision_simpleWall_notCollidable(){
        //testing that the ball speed changes when the ball collides with the wall/bricks.
        Wall wall = new Wall(3,4,"1", "1");
        Ball ball = new Ball(2,3, "B1", "1");
        wall.collidable = false;
        ball.xspeed = 2.0f;
        ball.yspeed = 2.0f;

        wall.handleCollision(ball);

        assertEquals(2.0, ball.x);

    }
    @Test
    void testhandleCollision_colorChange(){
        //testing - when the ball collides with a wall of different color the ball changes its color.
        Wall wall = new Wall(3,4,"1", "1");
        Ball ball = new Ball(3+12,4+12, "B0", "0");
        ball.xspeed = 2.0f;
        ball.yspeed = 2.0f;

        wall.handleCollision(ball);
        assertEquals("1", ball.objectNo);
    }

}
