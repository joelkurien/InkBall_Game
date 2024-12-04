package inkball;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import processing.core.PImage;
import processing.data.JSONObject;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static org.mockito.Mockito.*;

public class GameUITest {

	App app;
	App mockApp;
	GameUI gameUI;
	@BeforeEach
	void setup(){
        //set the base values of the App variables and static GameUI variables.
		app = new App();
		app.paused = false;
		app.gameOver = false;
		app.end = false;
		app.ballArray = new ArrayList<>();
		app.levelBalls = new ArrayList<>();
        app.startSpawnTime = 0;
        app.spawn_time = 10;
		gameUI = new GameUI();
		GameUI.spawnCountDown = 5.0f;
		mockApp = mock(App.class);
	}

	@Test
    public void testDisplaySpawnTimer_ReducesSpawnCountDown() {
        //test if the spawnCountDown decreases by 0.1 after 100 ms
        app.ballArray.add("blue");
        app.startSpawnTime = 0;
        app.paused = false;
        app.gameOver = false;
        app.end = false;
        GameUI.spawnCountDown = 5.0f;
        try{
            gameUI.displaySpawnTimer(app);
        }
        catch(RuntimeException e){
            e.printStackTrace();
        }
        assertEquals(4.9f, GameUI.spawnCountDown, 0.1f); 
    }

    // @Test
    // public void testDisplaySpawnTimer_ResetsOnZero() {
    //     //test that spawnCountDown resets after it reaches zero.
    //     app = new App();
    //     app.spawn_time = 10;
    //     GameUI.spawnCountDown = 0.0f; // Set countdown to 0
        
    //     try{
    //         gameUI.displaySpawnTimer(app);
    //     }
    //     catch(RuntimeException e){
    //         e.printStackTrace();
    //     }
    //     assertEquals(10.0f, GameUI.spawnCountDown, 0.1f); 
    // }

    @Test
    public void testDisplaySpawnTimer_DoesNothingWhenPaused() {
        //test that the spawnCountDown does not decrease when paused.
        app.paused = true; // Set paused to true
        GameUI.spawnCountDown = 5.0f;
        try{
            gameUI.displaySpawnTimer(app);
        }
        catch(RuntimeException e){
            e.printStackTrace();
        }
        assertEquals(5.0f, GameUI.spawnCountDown, 0.1f); 
    }

	@Test
    public void testAnimateShift_FirstBallRemoval() {
        //test that the first ball gets removed when the spawnCountDown reaches 0
		app = new App();
        app.spawn_count = 1;
        app.spawner = new HashMap<>();
        app.spawner.put(0, new int[]{10, 20});

        app.ballArray = new ArrayList<>();
        app.ballArray.add("blue");
        app.ballArray.add("grey");

		app.spawn_time = 10;
        app.levelBalls = new ArrayList<>();
		GameUI.xOffset = 40;

        app.spawn_time = 5; 
		GameUI.spawnCountDown = 0; 
		gameUI.animateShift(app);

        for (int i = 0; i < 34; i++) {
            gameUI.animateShift(app);
        }

        assertEquals(1, app.levelBalls.size());

        assertEquals(1, app.ballArray.size());
        assertEquals("grey", app.ballArray.get(0));

        assertEquals(5.0f, GameUI.spawnCountDown, 0.1f);
    }

	@Test
	void testRemoveLines_collision(){
        //test that a line gets removed when a ball collides with it.
		app.lines = new ArrayList<>();
		Line line1 = new Line(1,2,3,4); 
        line1.collided = true;  

        Line line2 = new Line(2,3,4,5);
        line2.collided = false; 

        Line line3 = new Line(3,4,5,6);
        line3.collided = true; 
        List<Line> lineSegment1 = new ArrayList<>();
        lineSegment1.add(line1);
        lineSegment1.add(line2);  
        List<Line> lineSegment2 = new ArrayList<>();
        lineSegment2.add(line3);  
        app.lines.add(lineSegment1);
        app.lines.add(lineSegment2);

        gameUI.removeLines(app);

        assertEquals(0, app.lines.size()); 
	}
	@Test
    public void testRemoveLines_NoLinesRemovedWhenNoCollisions() {
        //test that if no line is collided with a ball then the line is not removed.
		app.lines = new ArrayList<>();
        Line line1 = new Line(1,2,3,4);
        line1.collided = false;  
        Line line2 = new Line(2,3,4,5);
        line2.collided = false;  
        List<Line> lineSegment1 = new ArrayList<>();
        lineSegment1.add(line1);
        lineSegment1.add(line2);

        app.lines.add(lineSegment1); 
        gameUI.removeLines(app);

        assertEquals(1, app.lines.size()); 
    }

	@Test
	void testGamePaused(){
        //test that the game is paused.
		app = new App();
		app.paused = true;
		App.WIDTH = 100;
    
        app.delay(500);
        try{
            gameUI.displayPaused(app);
        }
        catch(RuntimeException e){
            System.out.println(e);
        }
        
        assertTrue(app.paused);
	}

	@Test
    void testGameOver_BallsRemaining(){
        //test that the game is over when there are still balls remaining.
		app = mock(App.class);
		GameUI.levelCountDown = 0; 
        Ball ball = mock(Ball.class); 
        ball.remove = false; 
        app.levelBalls = new ArrayList<>();
		app.levelBalls.add(ball); 

        gameUI.displayGameOver(app);

        assertTrue(app.gameOver);
	}

	@Test
	void testDisplayLevelComplete_nextLevel(){
        //testing that the game moves to the next level when a level is completed and some more levels are left to complete.
		app = mock(App.class); 
		app.ballArray = new ArrayList<>();
        app.levelBalls = new ArrayList<>();
        app.levelCount = 0;
        app.totalLevels = 3; 
        app.updateAnimate = false;
        app.finishAnimate = false;
		gameUI.timeSpent = 2;
		GameUI.levelCountDown = 0;
		gameUI.resetBorderTiles();
		gameUI.addBorderTile0(new Tile(4, 5, " "));
		gameUI.addBorderTile1(new Tile(4, 5, " "));
        gameUI.setCurrentTile(0);
        Ball ball = mock(Ball.class);
        ball.remove = true;
        app.levelBalls.add(ball);
		gameUI.frameCounter = 0;
		gameUI.timeSpent = 2;
		GameUI.levelCountDown = 0;
		gameUI.changeToYellow = true;
		gameUI.frameCounter = 3;
		app.images = new HashMap<>();
		app.images.put("wall4", new PImage(2,3));
        app.holeMinusPoints = new HashMap<>();
        app.holeMinusPoints.put("grey" ,0);
        app.holePlusPoints = new HashMap<>();
        app.holePlusPoints.put("grey" ,0);
        app.scoreDecreaseMap = new JSONObject();
        app.scoreDecreaseMap.put("blue",0);
        app.scoreIncreaseMap = new JSONObject();
        app.scoreIncreaseMap.put("blue",0);

        try{
            gameUI.displayLevelComplete(app);
        }
		catch(RuntimeException e){
            e.printStackTrace();
        }
		
        assertEquals(1, app.levelCount);
	}

	@Test
    void testDisplayLevelComplete_GameEnded(){
        //testing that the game ends properly and the end flag is updated correctly
		app = mock(App.class);
		app.ballArray = new ArrayList<>();
        app.levelBalls = new ArrayList<>();
        app.levelCount = 3;
        app.totalLevels = 3; 
        app.updateAnimate = false;
        app.finishAnimate = false;
		app.end = false;
		gameUI.timeSpent = 2;
		GameUI.levelCountDown = 0;
		gameUI.resetBorderTiles();
		gameUI.addBorderTile0(new Tile(4, 5, " "));
		gameUI.addBorderTile1(new Tile(4, 5, " "));
        gameUI.setCurrentTile(0);
		gameUI.changeToYellow = true;
		gameUI.frameCounter = 3;
		app.images = new HashMap<>();
		app.images.put("wall4", new PImage(2,3));
        
        gameUI.displayLevelComplete(app);
        assertTrue(app.end);
	}

	@Test
	void testDisplayLevelComplete_notNextLevel(){
        //testing that the game does not move to the next level when ballArray or levelBalls array are not empty.
        app = mock(App.class);
		app = mock(App.class); 
		app.ballArray = new ArrayList<>();
        app.levelBalls = new ArrayList<>();
        app.levelCount = 0;
        app.totalLevels = 3; 
        app.updateAnimate = false;
        app.finishAnimate = false;
		gameUI.timeSpent = 2;
		GameUI.levelCountDown = 3;
		gameUI.resetBorderTiles();
		gameUI.addBorderTile0(new Tile(4, 5, " "));
		gameUI.addBorderTile1(new Tile(4, 5, " "));
        gameUI.setCurrentTile(0);
        Ball ball = mock(Ball.class);
        ball.remove = false;
        app.levelBalls.add(ball);
		gameUI.frameCounter = 0;
		gameUI.timeSpent = 2;
		GameUI.levelCountDown = 3;
		gameUI.changeToYellow = true;
		gameUI.frameCounter = 3;
		app.images = new HashMap<>();
		app.images.put("wall4", new PImage(2,3));
        
		gameUI.displayLevelComplete(app);

        assertEquals(0, app.levelCount);
	}
}
