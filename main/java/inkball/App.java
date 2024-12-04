package inkball;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import java.io.*;
import java.util.*;

/**
 * The main class that is responsible for displaying the canvas and running the game.
 */
public class App extends PApplet {

    public static final int CELLSIZE = 32; /**8;*/
    public static final int CELLHEIGHT = 32;

    public static final int CELLAVG = 32;
    public static final int TOPBAR = 64;
    public static int WIDTH = 576; /**CELLSIZE*BOARD_WIDTH;*/
    public static int HEIGHT = 640; /**BOARD_HEIGHT*CELLSIZE+TOPBAR;*/
    public static final int BOARD_WIDTH = WIDTH/CELLSIZE;
    public static final int BOARD_HEIGHT = 20;

    public static final int NUM_ROWS = (HEIGHT-TOPBAR)/CELLSIZE;
    public static final int NUM_COLS = BOARD_WIDTH;
    
    public static Tile[][] board = new Tile[NUM_ROWS][NUM_COLS];
    public static final int INITIAL_PARACHUTES = 1;

    public static final int FPS = 30;

    public String configPath;
    public JSONObject config;
    public JSONArray levels; /** array that stores the information of each level in the game */
    public Map<String, PImage> images; /** dictionary to store the images */
    protected ArrayList<List<Line>> lines; /** Arraylist to store a multiple collections of line segments */
    protected ArrayList<Line> line; /** arraylist to store one set of line segments. */

    protected ArrayList<Ball> levelBalls; /** stores all the balls active on the board*/
    protected ArrayList<Wall> levelWalls; /** stores the information of all the walls in the board */
    protected ArrayList<Hole> levelHoles; /** stores the information of all the holes in the board */
    protected Map<Integer, int[]> spawner; /** stores the location of all the spawners in the board */

    public int startSpawnTime; /** used to set the start spawn time */
    public int startTime; /** used to set the start level time */
    protected int spawn_count; /** used to count the number of spawners available. */
    protected int spawn_time; /** used to set the spawn time for each level */
    protected int level_time; /** used to set the level time for each level */
    public ArrayList<String> ballArray; /**used to store the balls in the spawn queue */
    public int levelCount = 0; /**used to keep track of the current level */
    public int totalLevels = 0; /**used to keep track of the total number of levels */
    public boolean paused = false; /**check if the game is paused */
    public boolean gameOver = false; /**check if the game is over */
    public boolean end = false; /** check if the game has been completed */
    public int totalScore = 0; /** stores the overall score of the player */
    public float plusPoints; /** additive points  */
    public float minusPoints; /** reductive points */
    protected boolean updateAnimate; /** used to check if the score animation is completed */
    protected boolean finishAnimate; /** used to check if the tile animation is completed */
    protected int levelScore = 0; /** used to store the score for each level */
    protected Map<String, Integer> holePlusPoints; /** stores the positive points when a ball falls into the correct hole */
    protected Map<String, Integer> holeMinusPoints; /** stores the negative points when a ball falls into the wrong hole */
    protected JSONObject scoreIncreaseMap; /** JSON object for getting the score increase for each hole value */
    protected JSONObject scoreDecreaseMap; /** JSON object for getting the score decrease for each hole value */
    protected GameUI gameUI = new GameUI(); 
    protected LevelConfig levelConfig = new LevelConfig();
	/**
     * Construct for the Inkball game, loaded the App window. and initializes the necessary overvall variables
     * such as assigns the path for the config file and creates the images hashmap object.
     */
    public App() {
        this.configPath = "config.json";
        images = new HashMap<>();
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Method to set the framerate of the game.
     * In this method the initial level of the game is setup.
     * Also all the images that are used in the game is loaded in this function
     */
	@Override
    public void setup() {
        frameRate(FPS);
        //See PApplet javadoc:
		config = loadJSONObject(configPath);
        levels = config.getJSONArray("levels");
        scoreDecreaseMap = config.getJSONObject("score_decrease_from_wrong_hole");
        scoreIncreaseMap = config.getJSONObject("score_increase_from_hole_capture");
        totalLevels = levels.size();

        //#region load images
        try{
            for(int i=0; i<5; i++){
                String filenameW = "wall"+i;
                String filenameH = "hole"+i;
                String filenameB = "ball"+i;
                String filenameWB = "wallB"+i;
                images.put(filenameW, loadImage(URLDecoder.decode(this.getClass().getResource(filenameW+".png").getPath(), StandardCharsets.UTF_8.name())));
                images.put(filenameB, loadImage(URLDecoder.decode(this.getClass().getResource(filenameB+".png").getPath(), StandardCharsets.UTF_8.name())));
                images.put(filenameH, loadImage(URLDecoder.decode(this.getClass().getResource(filenameH+".png").getPath(), StandardCharsets.UTF_8.name())));
                images.put(filenameWB, loadImage(URLDecoder.decode(this.getClass().getResource(filenameWB+".png").getPath(), StandardCharsets.UTF_8.name())));
            }
            images.put("tile", loadImage(URLDecoder.decode(this.getClass().getResource("tile.png").getPath(), StandardCharsets.UTF_8.name())));
            images.put("spawn", loadImage(URLDecoder.decode(this.getClass().getResource("entrypoint.png").getPath(), StandardCharsets.UTF_8.name())));
        }
        catch(UnsupportedEncodingException e){
            throw new RuntimeException(e);
        }
        //#endregion

        //#region level setup
        levelConfig.levelSetup(this, levelCount);
        //#endregion level setup
    }

    /**
     * Receive key pressed signal from the keyboard.
     * This function handles the logic of what pressing different keys does to the inkball game
     * The functionalities include: Pausing the game when spacebar is clicked
     *                              Reseting the game when 'r' key is pressed
     */
	@Override
    public void keyPressed(KeyEvent event){
        char key = event.getKey();
        Controls.keyPress(this,key);
    }

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(){
        
    }

    /**
     * The function handles the operation of what the application should do when the mouse is clicked
     * In this game: When the left mouse button is clicked, the user is able to draw a line and when the 
     * right mouse button the user drawn lines are removed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // create a new player-drawn line object
        if(!paused && !gameOver && !end){
            if(mouseButton == LEFT){
                Line l = new Line(mouseX, mouseY, pmouseX, pmouseY);
                line.add(l);
            }
        }
        if(mouseButton == RIGHT || (mouseButton == LEFT && keyPressed && (key == CODED && keyCode == CONTROL))){
            lines = new ArrayList<>();
            line = new ArrayList<>();
        }
        
    }

    /**
     * User is able to draw continuous lines while dragging the cursor across the screen. within the game window.
     */
	@Override
    public void mouseDragged(MouseEvent e) {
        // add line segments to player-drawn line object if left mouse button is held
        if(mouseButton == LEFT && !paused && !gameOver && !end)
            line.add(new Line(mouseX, mouseY, pmouseX, pmouseY));
    }

    /**
     * Stops the line been drawn when the user releases the left mouse button.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if(mouseButton == LEFT && !paused && !gameOver && !end)
            lines.add(line);
        line = new ArrayList<>();
    }
    
    /**
     * Draw all elements in the game by current frame.
     * Draws all the elements in each level of the game.
     * Loads the balls, walls and holes of the game and is responsible to handle what happens when the game is paused, completed and 
     * lost.
     * Other logic that this function supports is loading the balls in the spawner queue, display spawn time, level time and total score of the game.
     */
    float timeSpent = 0;
	@Override
    public void draw() {
        //----------------------------------
        //display Board for current level:
        //----------------------------------
        //TODO
        background(204);
               
        for(Tile[] row: board){
            for(Tile tile: row){
                switch(tile.getTileType()){
                    case "S":
                        image(images.get("spawn"), tile.x, tile.y);
                        break;
                    default:
                        image(images.get("tile"), tile.x, tile.y);
                        break;
                }
            }
        }

        for(Wall wall: levelWalls){
            wall.draw(this);
        }

        for(Hole hole: levelHoles){
            hole.draw(this);
        }
        
        if(line != null)
            for(Line l: line){
                l.draw(this);
            }
        
        for(List<Line> lineSegments: lines){
            if(lineSegments != null){
                for(Line lineSegment: lineSegments){
                    lineSegment.draw(this);
               }
            }
        }
        
        for(Ball ball:levelBalls){
            if(!ball.remove)
                ball.draw(this);
        }

        gameUI.displaySpawnTimer(this);
        
        gameUI.draw(this);

        gameUI.removeLines(this);

        //----------------------------------
        //display score
        //----------------------------------
        //TODO
        gameUI.displayLevelTime(this);
        gameUI.displayScore(this);
        
		//----------------------------------
        //----------------------------------
		//display game end message
        gameUI.displayGameOver(this);

        gameUI.displayPaused(this);

        gameUI.displayLevelComplete(this);
    }


    public static void main(String[] args) {
        PApplet.main("inkball.App");
    }

}
