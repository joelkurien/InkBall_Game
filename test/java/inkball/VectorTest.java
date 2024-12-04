package inkball;

import org.junit.jupiter.api.Test;

import processing.core.PVector;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class VectorTest {
    Vector vector;
    @BeforeEach
    void setup(){
        //creating a new vector
        vector = new Vector();
    }

    @Test
    void testVelocityChange(){
        //testing the velocityChange method with a given normal and velocity vector
        PVector normal = new PVector(1,2);
        PVector v = new PVector(3,4);

        PVector result = vector.velocityChange(normal, v);
        assertEquals(-19, result.x);
        assertEquals(-40, result.y);
    }

    @Test
    void testSelectNormalRight(){
        //testing the selection method with a given normal, when the ball is on the right side of the line
        PVector start = new PVector(0, 0);
        PVector end = new PVector(10, 0);
        PVector p = new PVector(5, 2); 
        PVector expectedNormal = new PVector(0, 1); 
        PVector selectedNormal = vector.selectNormal(start, end, p);
        
        assertEquals(expectedNormal.x, Math.abs(selectedNormal.x));
        assertEquals(expectedNormal.y, selectedNormal.y);
    }

    @Test
    void testSelectNormalLeft(){
        //testing the selection method with a given normal, when the ball is on the left side of the line
        PVector start = new PVector(0, 0);
        PVector end = new PVector(10, 0);
        PVector p = new PVector(5, -2); 
        PVector expectedNormal = new PVector(0, -1); 
        PVector selectedNormal = vector.selectNormal(start, end, p);
        
        assertEquals(expectedNormal.x, selectedNormal.x, 0.001);
        assertEquals(expectedNormal.y, selectedNormal.y, 0.001);
    }
}
