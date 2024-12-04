package inkball;

import java.util.*;

import inkball.Enums.Color;
import processing.core.PApplet;

/**
 * Class that handles animation and level updating logic
 */
public class GameUI extends PApplet {
    protected static float spawnCountDown = 0; /**spawn count down timer */
    protected static int levelCountDown = 0; /**level count down timer */
    protected float timeSpent = 0; /**yellow tile animation timer of 0.067 per transition */
    protected int frameCounter = 0; /**go to the next frame for yellow tile animation */
    protected boolean changeToYellow = true; /**check if a tile needs to be switched to yellow */
    private int currentTile = 0; /**index of the current border tile that is animated */
    private static List<Tile> borderTiles0 = new ArrayList<Tile>(); /**list of border tiles from top left corner to bottom right corner  */
    private static List<Tile> borderTiles1 = new ArrayList<Tile>(); /**list of border tiles from bottom right corner to top left corner */
    public static int xOffset = 0; /**offset to push the spawned ball out of the spawner queue animation */
    private int rectWidth = 5 * 34; /** width of the spawner rectangle */
    private int rectHeight = 5+24; /** height of the spawner rectangle */

    /**
     * Set the currentTile value
     */
    public void setCurrentTile(int ct) { currentTile = ct; }

    /**
     * Reset the border tile lists so that it can store the border tiles for the next level
     */
    public void resetBorderTiles(){
        borderTiles0 = new ArrayList<Tile>();
        borderTiles1 = new ArrayList<Tile>();
    }

    /**
     * Add the border tiles from the top left corner to bottom right corner
     */
    public void addBorderTile0(Tile t) { borderTiles0.add(t); }

    /**
     * Adds the border tiles from the bottom right corner to the top left corner
     */
    public void addBorderTile1(Tile t) { borderTiles1.add(t); }

    /**
     * Add the border tiles into borderTiles0 and borderTiles1
     */
    public void addBorders(Tile[][] board){
        for(int col=0; col<App.NUM_ROWS; col++){
            addBorderTile1(board[0][col]);
        }
        for(int row=0; row<App.NUM_ROWS;row++){
            addBorderTile1(board[row][App.NUM_COLS-1]);
        }
        for(int col=App.NUM_COLS-1; col>-1; col--){
            addBorderTile0(board[App.NUM_ROWS-1][col]);
        }
        for(int row=App.NUM_ROWS-1; row>-1; row--){
            addBorderTile0(board[row][0]);
        }
    }

    /**
     * Displays the spawn queue timer.
     */
    public void displaySpawnTimer(App app) {
        
        if (!app.paused && !app.gameOver && !app.end) {
            if (app.ballArray.size() > 0 && (app.millis() - app.startSpawnTime >= 100)) {
                if (spawnCountDown >= 0) {
                    spawnCountDown -= 0.1f;
                    app.startSpawnTime += 100; 
                }
            } 
        }

        app.fill(0); 
        app.textSize(20); 
        app.textAlign(App.LEFT);
        String timerText = String.format("%.1f", spawnCountDown <=0 ? app.spawn_time: spawnCountDown); 
        app.text(timerText, rectWidth+20, 34);
    }

    /**
     * Displays the level timer.
     */
    public void displayLevelTime(App app) {
        if (!app.gameOver && !app.paused && levelCountDown > 0 && !app.end) {
            if ((app.millis() - app.startTime) >= 1000) {
                levelCountDown--;
                app.startTime += 1000; 
            }
        }

        app.fill(0);
        app.textSize(20);
        app.textAlign(App.RIGHT, App.TOP);
        String timeLeft = String.format("Time: %d", levelCountDown);
        app.text(timeLeft, App.WIDTH - 10, 10);
    }

    /**
     * Displays the overall score of the player across all levels
     */
    public void displayScore(App app) {
        app.fill(0);
        app.textSize(20);
        app.textAlign(App.RIGHT, App.TOP);
        String scoreText = String.format("Score: %d", app.totalScore);
        app.text(scoreText, App.WIDTH - 10, 34);
    }

    /**
     * Animates the spawner pushing out the left most queue ball
     */
    public void animateShift(App app){
        int selectSpawner = new Random().nextInt(app.spawn_count);
        int[] spawnerPos = app.spawner.get(selectSpawner);
        if(spawnCountDown <= 0){
            xOffset += 1;
            if(xOffset >= 34){
                xOffset = 0;
                String ballColor = null;
                if(app.ballArray.size()>0){
                    ballColor = app.ballArray.get(0);
                    app.ballArray.remove(0);
                    if(ballColor != null) 
                        app.levelBalls.add(new Ball(spawnerPos[0], spawnerPos[1], Color.valueOf(ballColor).getType(), Integer.toString(Color.valueOf(ballColor).ordinal())));
                    spawnCountDown = app.spawn_time;
                    app.startSpawnTime = app.millis();
                }
            }
        }
    }
    
    /**
     * Displaying the game is over and stopping the game.
     */
    public void displayGameOver(App app){
        if(levelCountDown <=0 && app != null){
            boolean ballLeft = false;
            for(Ball ball: app.levelBalls){
                if(!ball.remove) {
                    ballLeft = true;
                    break;
                }
            }
            if(ballLeft){
                app.gameOver = true;
                app.fill(0);
                app.textSize(20);
                app.textAlign(LEFT, TOP);
                app.text("===TIME'S UP===", App.WIDTH/4 + 120, 16);
            }
        }
    }
    
    /**
     * Animation timer for updating the score if the level timer is non zero by 0.067 frames.
     */
    private boolean updateTimeSpent(){
        timeSpent += 1.0/App.FPS;
        return timeSpent >= 0.067;
    }

    /**
     * Method to increment the score by 1 for every second left in the timer.
     */
    private void timeScoreAnimate(App app){
        if(levelCountDown > 0){
            levelCountDown--;
            app.totalScore++;
        } else {
            app.updateAnimate = true;
            app.levelScore = app.totalScore;
        }
    }

    /**
     * Method that handles the border yellow tile switching animation.
     */
    private void changeYellowTiles(App app){
        for (int i = 0; i < borderTiles0.size(); i++) {
            Tile tile0 = borderTiles0.get(i);
            Tile tile1 = borderTiles1.get(i);
            if (i == currentTile && changeToYellow) {
              app.image(app.images.get("wall4"), tile0.x, tile0.y);
              app.image(app.images.get("wall4"), tile1.x, tile1.y); 
            } 
        }
    }

    /**
     * Method responsible for the sequencial tile switching animation
     */
    private void checkFinishTiles(App app){
        frameCounter++;
        if (frameCounter >= (int)(App.FPS * 0.067)) {
            frameCounter = 0;
            changeToYellow = !changeToYellow;  
            
            if (!changeToYellow) {
                currentTile++;
                if (currentTile >= borderTiles0.size()) {
                    app.finishAnimate = true;  
                    currentTile = 0;
                }
            }
        }
    }

    /**
     * Method for updating the level once all the balls in the spawn queue and on the board
     * has been removed.
     */
    private void updateLevel(App app){
        app.levelCount++;
        if(app.levelCount < app.totalLevels){
            new LevelConfig().levelSetup(app, app.levelCount);
        }
        else{
            app.fill(0);
            app.textSize(20);
            app.textAlign(LEFT, TOP);
            app.text("===ENDED===", App.WIDTH/4 + 120, 20);
            resetBorderTiles();
            app.end = true;
        }
    }

    /**
     * Method responsible for displaying the level complete message and updating the level
     * once all the balls in the spawn queue and on the board has been removed.
     */
    public void displayLevelComplete(App app){
        boolean nextFlag = true;
        if(app.ballArray.size()<=0){
            for(Ball ball: app.levelBalls){
                if(!ball.remove) nextFlag = false;
            }
            if(nextFlag){
                if(updateTimeSpent()){
                    timeScoreAnimate(app);
                }

                if(app.updateAnimate){
                    changeYellowTiles(app);
                    checkFinishTiles(app);
                }
                
                if(app.finishAnimate){
                    updateLevel(app);
                }   
            }
        }
    }

    /**
     * Removes the lines from the board which has been collided by a ball.
     */
    public void removeLines(App app){
        Set<List<Line>> removeLines = new HashSet<>();
        for(List<Line> lineSegments: app.lines){
            for(Line lineSegment: lineSegments){
                if(lineSegment.collided){
                    if(!removeLines.contains(lineSegments)){
                        removeLines.add(lineSegments);
                    }
                }
            }
        }
        
        app.lines.removeAll(removeLines);
    }

    /** 
     * Method to pause the game 
     * */
    public void displayPaused(App app){
        if(app.paused){
            app.fill(0);
            app.textSize(20);
            app.textAlign(LEFT, TOP);
            app.text("***PAUSED***", App.WIDTH/4 + 120, 16);
        }
    }

    /**
     * Method to draw the game objects such as the spawn queue and do the spawning animation
     */
    public void draw(App app){
        app.fill(0); 
        app.rect(0, 10, rectWidth, rectHeight); 
        int bc = min(5, app.ballArray.size());
        for (int i=0; i<bc; ++i){
            if(i < app.ballArray.size())
                app.image(app.images.get("ball"+Color.valueOf(app.ballArray.get(i)).ordinal()), (i)*34-xOffset, 12, 24, 24);
        }
        animateShift(app);
    }
}
