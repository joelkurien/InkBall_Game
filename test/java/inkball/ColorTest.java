package inkball;

import org.junit.jupiter.api.Test;

import inkball.Enums.Color;

import static org.junit.jupiter.api.Assertions.*;

public class ColorTest {

    @Test
    public void testEnumValues() {
        //testing that the enum values are in the correct order.
        Color[] eColors = {Color.grey, Color.orange, Color.blue, Color.green, Color.yellow};
        Color[] colors = Color.values();
        
        assertArrayEquals(eColors, colors);
    }

    @Test
    public void testGetType() {
        //testing that given a ball color you can correctly get its type.
        assertEquals("B0", Color.grey.getType());
        assertEquals("B1", Color.orange.getType());
        assertEquals("B2", Color.blue.getType());
        assertEquals("B3", Color.green.getType());
        assertEquals("B4", Color.yellow.getType());
    }
}
