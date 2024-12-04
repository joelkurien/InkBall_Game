package inkball;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import processing.core.PVector;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.HashMap;
import static org.mockito.Mockito.*;

class HoleTest {

    private App app;
    private Hole hole;
    private Ball ball;

    @BeforeEach
    void setUp() {
        //sets up the hole, ball and app for testing.
        app = new App(); 
        app.images = new HashMap<>();
        app.images.put("hole1", new PImage()); 
        app.levelBalls = new ArrayList<>();

        hole = new Hole(100, 100, "H0", "0");
        ball = new Ball(100, 100, "B0", "0"); 
        ball.xspeed = 2.0f; 
        ball.yspeed = 2.0f; 
        app.levelBalls.add(ball); 
    }

    @Test
    void testHoleAttraction_WithinAttractionDistance() {
        //testing that the ball gets attracted to the hole and if the hole-ball relation is satisified
        // the ball.remove should be marked as true.
        app = new App();
        hole = new Hole(100, 100, "H0", "0");
        ball = new Ball(100, 100, "B0", "0"); 

        ball.x = hole.x+App.CELLSIZE; 
        ball.y = hole.y+App.CELLSIZE;
        ball.xspeed = 2.0f;
        ball.yspeed = 2.0f;
        ball.xFactor = 24;
        ball.yFactor = 24;
        app.ballArray = new ArrayList<>();
        ball.ballImage = mock(PImage.class);
        app.totalScore = 0;
        app.minusPoints = 1;
        app.plusPoints = 1;
        app.holePlusPoints = new HashMap<>();
        app.holePlusPoints.put("grey",0);

        hole.holeAttraction(app, ball);

        assertEquals(0.0f, ball.xspeed, 0.001f);
        assertEquals(0.0f, ball.yspeed, 0.001f);
        assertTrue(ball.remove); 
        assertTrue(app.ballArray.isEmpty()); 
    }

    @Test
    void testHoleAttraction_AttractWrongBall() {
        //testing that the ball is not removed when it falls into the wrong hole.
        app = new App();
        
        hole = new Hole(100, 100, "H1", "1");
        ball = new Ball(100, 100, "B2", "2"); 

        ball.x = hole.x+App.CELLSIZE; 
        ball.y = hole.y+App.CELLSIZE;
        ball.ballImage = mock(PImage.class);
        ball.xspeed = 2.0f;
        ball.yspeed = 2.0f;
        ball.xFactor = 24;
        ball.yFactor = 24;
        ball.remove = false;
        app.ballArray = new ArrayList<>();
        app.totalScore = 0;
        app.minusPoints = 1;
        app.plusPoints = 1;
        app.holeMinusPoints = new HashMap<>();
        app.holeMinusPoints.put("orange", 25);

        hole.holeAttraction(app, ball);

        assertEquals(0.0f, ball.xspeed, 0.001f);
        assertEquals(0.0f, ball.yspeed, 0.001f);
        assertTrue(ball.remove); 
        assertFalse(app.ballArray.isEmpty()); 
        assertEquals(-25, app.totalScore);
    }

    @Test
    void testHoleAttraction_OutsideAttractionDistance() {
        //testing that the ball does not get attracted when it falls outside the attraction distance.
        app = new App();

        hole = new Hole(100, 100, "H0", "0");
        ball = new Ball(100, 100, "B0", "0"); 
        
        ball.x = hole.x+App.CELLSIZE+100; 
        ball.y = hole.y+App.CELLSIZE+100;
        ball.ballImage = mock(PImage.class);
        ball.xspeed = 2.0f;
        ball.yspeed = 2.0f;
        ball.xFactor = 24;
        ball.yFactor = 24;
        ball.remove = false;
        app.ballArray = new ArrayList<>();
        hole.holeAttraction(app, ball);

        assertFalse(ball.remove); 
        assertTrue(app.ballArray.isEmpty());
    }

    @Test
    void testBallScalingCloseToHole() {
        //testing that the ball's size decreases as it gets closer to the hole.
        ball.x = hole.x+App.CELLSIZE - 10; 
        ball.y = hole.y+App.CELLSIZE;
        ball.ballImage = mock(PImage.class);
        ball.ballImage.width = 20;
        app.holePlusPoints = new HashMap<>();
        app.holePlusPoints.put("grey",0);

        hole.holeAttraction(app, ball);

        float distance = PVector.dist(new PVector(ball.x, ball.y), new PVector(hole.x+App.CELLSIZE, hole.y+App.CELLSIZE));
        float size = Math.max(3, map(distance, 0, App.CELLSIZE, 3, ball.ballImage.width));

        assertEquals(size, ball.xFactor);
        assertEquals(size, ball.yFactor);
    }

    private float map(float value, float start1, float stop1, float start2, float stop2) {
        //helper function to handle resizing the ball image.
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }

    @Test
    public void holeAttraction() {
        //testing if the holeAttraction function is working properly.
        Hole h = new Hole(123, 123, "H0", "0");
        App app = new App();
        Ball ball = new Ball(123, 123, "B0", "0");
        h.holeAttraction(app, ball);
    }
}
