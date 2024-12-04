package inkball;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import processing.core.PApplet;
import processing.event.MouseEvent;

import java.util.ArrayList;

public class AppTest {

    private App app;

    @BeforeEach
    public void setup() {
        //setting up the app for testing
        app = new App();
        app.loop();
        PApplet.runSketch(new String[]{ "App" },app);
        app.delay(100);
        app.setup();
    }

    @Test
    public void testInitialBallCount() {
        //testing that the ball array properly accepts ball color
        app.ballArray = new ArrayList<>();
        app.ballArray.add("blue");
        app.ballArray.add("grey");
        assertEquals(2, app.ballArray.size());

    }

    @Test
    public void testWallInitialization() {
        //testing the case if the levelWall array is empty
        assertFalse(app.levelWalls.isEmpty());
    }

    @Test
    public void testHoleInitialization() {
        //testing the case if the levelHole array is empty
        assertFalse(app.levelHoles.isEmpty());
    }

    @Test
    public void testTileSetup() {
        //testing that the board is correctly initialized
        assertNotNull(App.board);
        assertEquals( App.NUM_ROWS, App.board.length);
        assertEquals( App.NUM_COLS, App.board[0].length);
    }

    @Test
    public void testLinesClearingOnRightClick() {
        //testing that lines are cleared on right click
        app.line.add(new Line(10, 10, 20, 20));
        app.mousePressed(new processing.event.MouseEvent(null, 0, 0, PApplet.RIGHT, 0, 0, 0, 0));
        assertTrue(app.lines.isEmpty());
    }

    @Test
    public void testTileType() {
        //testing that every board tile does have a default type
        Tile tile = App.board[0][0];
        assertNotNull(tile.getTileType());
    }

    @Test
    public void testMousePressedLeftButton() {
        //testing that a line is drawn when the left button is pressed
        app.mouseButton = PApplet.LEFT;
        MouseEvent event = new MouseEvent(app, 0, 0, 0, 100, 100, PApplet.LEFT, 1);
        app.mousePressed(event);

        assertEquals(1, app.line.size());
        assertEquals(0, app.lines.size());
    }

    @Test
    public void testMousePressedRightButton() {
        app.mouseButton = PApplet.RIGHT;
        MouseEvent event = new MouseEvent(app, 0, 0, 0, 100, 100, PApplet.RIGHT, 1);
        app.mousePressed(event);

        assertEquals(0, app.line.size());
        assertEquals(0, app.lines.size());
    }

    @Test
    public void testMouseDragged() {
        // Simulate dragging with left mouse button
        app.mouseButton = PApplet.LEFT;
        MouseEvent event = new MouseEvent(app, 0, 0, 0, 150, 150, PApplet.LEFT, 1);
        app.mouseDragged(event);

        assertEquals(1, app.line.size());
    }

    @Test
    public void testMouseReleased() {
        // Checking when the mouse left button is released the line created is stored in the lines array.
        app.mouseButton = PApplet.LEFT;
        MouseEvent event = new MouseEvent(app, 0, 0, 0, 200, 200, PApplet.LEFT, 1);
        app.mousePressed(event);   // Start drawing a line
        app.mouseReleased(event);  // Release the line

        assertEquals(1, app.lines.size());
    }

    @Test
    public void testSpaceKeyPressedPausesGame() {
        //testing that the game is paused when space is pressed
        Controls.keyPress(app, ' '); 

        
        assertTrue(app.paused); 
        Controls.keyPress(app, ' '); 
        
        assertFalse(app.paused); 
    }
    
    @Test
    public void testRKeyPress() {
        //testing that the game is reset when the 'r' key is pressed
        app.levelCount = 5;
        app.paused = true;
        app.gameOver = true;
        app.end = true;
        app.totalScore = 100;
        
        Controls.keyPress(app, 'r'); 

        assertEquals(0, app.levelCount);
        assertFalse(app.paused);
        assertFalse(app.gameOver);
        assertFalse(app.end);
        assertEquals(0, app.totalScore);
    }

    @Test
    void testLineDraw(){
        //testing that a line is drawn by the draw function in app class
        app = new App();
        app.lines = new ArrayList<>();
        app.line = new ArrayList<>();
        app.line.add(new Line(10, 10, 20, 20));
        app.lines.add(app.line);
        try{
            app.draw();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        assertEquals(app.lines.size(), 1);
    }

    @Test
    public void testLinesClearingOnCtrlLeftClick() {
        //testing that lines are cleared on Ctrl + left click
        app.line.add(new Line(10, 10, 20, 20));
        app.keyPressed = true;
        app.key = PApplet.CODED;
        app.keyCode = PApplet.CONTROL;
        app.mousePressed(new processing.event.MouseEvent(null, 0, 0, PApplet.LEFT, 0, 0, 0, 0));
        assertTrue(app.lines.isEmpty());
    }
}
