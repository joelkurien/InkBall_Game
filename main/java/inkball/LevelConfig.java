package inkball;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import inkball.Enums.Color;
import processing.data.JSONArray;
import processing.data.JSONObject;

/**
 * Class that handles setting the default values in each level, extracting data from each level's layout file
 * and sets up the board.
 */
public class LevelConfig {
    GameUI gameUI = new GameUI();
    
    /**
     * The function to setup each level of the game.
     * Here, the type of tiles each 32x32 pixel cell is determined, the spawn timer of the balls to load and setting the initial time of
     * the level is decided in this function.
     * @param levelCount
     */
    public void levelSetup(App app, int levelCount) {
        resetAppState(app);
        addScoreUpdaters(app);
        JSONObject levelData = app.levels.getJSONObject(levelCount);
        loadLevelAttributes(app, levelData);
        
        ArrayList<String> tileTypes = loadLevelLayout(levelData.getString("layout"));
        
        setupBoard(app, tileTypes);
        cleanUpBoard(app);
        setupWalls(app);

        gameUI.addBorders(App.board);
    }

    /**
    *Resets the application state for a new level. This method initializes various game-related data structures and variables to their initial state, preparing the game for a new level.
    *@param app The application object that contains the game state and other game-related data.
    */
    private void resetAppState(App app) {
        app.finishAnimate = false;
        app.updateAnimate = false;
        app.levelBalls = new ArrayList<>();
        app.lines = new ArrayList<>();
        app.line = new ArrayList<>();
        app.spawner = new HashMap<>();
        app.ballArray = new ArrayList<>();
        app.levelWalls = new ArrayList<>();
        app.levelHoles = new ArrayList<>();
        app.startSpawnTime = app.millis();
        app.startTime = app.millis();
        app.holePlusPoints = new HashMap<>();
        app.holeMinusPoints = new HashMap<>();
        gameUI.resetBorderTiles();
    }

    /** Method that adds/subtracts the score updaters for each correct hole or wrong hole a ball falls into */
    private void addScoreUpdaters(App app) {
        for(Color color: Color.values()) {
            app.holePlusPoints.put(color.name(), (Integer)app.scoreIncreaseMap.getInt(color.name()));
            app.holeMinusPoints.put(color.name(), (Integer)app.scoreDecreaseMap.getInt(color.name()));
        }
    }
    /**
    *This method loads the attributes of a level from the provided JSONObject and updates the application's state accordingly. It retrieves the array of balls, plus and minus points modifiers, spawn interval, and level time from the JSONObject and updates the application's state with these attributes.
    * @param app The application object that contains the game state and other game-related data.
    * @param levelData A JSONObject containing the attributes of the current level.
    */
    private void loadLevelAttributes(App app, JSONObject levelData) {
        JSONArray balls = levelData.getJSONArray("balls");
        app.plusPoints = levelData.getFloat("score_increase_from_hole_capture_modifier");
        app.minusPoints = levelData.getFloat("score_decrease_from_wrong_hole_modifier");

        for (int i = 0; i < balls.size(); i++) {
            app.ballArray.add(balls.getString(i));
        }

        app.spawn_time = levelData.getInt("spawn_interval");
        GameUI.spawnCountDown = app.spawn_time;

        app.level_time = levelData.getInt("time");
        GameUI.levelCountDown = app.level_time;
    }

    /**
     * This method reads the layout file for a level and returns an ArrayList of strings representing the tile types for each cell on the game board.
     * The layout file is expected to contain a series of characters, each representing a tile type.
     * The method reads each line of the file and adds each character to the ArrayList as a separate string.
     * If the layout file cannot be found or read, the method prints an error message to the console.
     *
     * @param layoutFilePath The path to the layout file for the level
     * @return An ArrayList of strings representing the tile types for each cell on the game board
     * @throws IOException If the layout file cannot be found or read
     */
    private ArrayList<String> loadLevelLayout(String layoutFilePath) {
        ArrayList<String> tileTypes = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(layoutFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (char c : line.toCharArray()) {
                    tileTypes.add(Character.toString(c));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return tileTypes;
    }

    /**
     * This method sets up the game board by placing tiles at specified coordinates (x, y) based on the tile types provided in the tileTypes array list.
     * It iterates through each row and column of the board and assigns the appropriate tile type to each cell.
     * If the tile type is a ball or a hole, a new Ball or Hole object is created and added to the app's levelBalls or levelHoles lists, respectively.
     * If the tile type is "S", a new Wall object is created and added to the app's spawner with the specified coordinates (x, y).
     * @param app The application object that contains the board and other game-related data.
     * @param tileTypes An array list containing the tile types for each cell on the game board.
     */
    private void setupBoard(App app, ArrayList<String> tileTypes) {
        int count = 0;
        app.spawn_count = 0;

        for (int row = 0; row < App.NUM_ROWS; row++) {
            for (int col = 0; col < App.NUM_COLS; col++) {
                String tileType = (count < tileTypes.size()) ? tileTypes.get(count++) : " ";
                int x = col * App.CELLSIZE;
                int y = row * App.CELLSIZE + App.TOPBAR;

                if (tileType.equals("H") || tileType.equals("B") || tileType.equals("T")) {
                    tileType = tileTypes.get(count - 1) + tileTypes.get(count);
                }

                handleTileType(app, tileType, x, y);
                App.board[row][col] = new Tile(x, y, tileType);
            }
        }
    }

    /**
     * This method handles the type of tile at the specified coordinates (x, y) on the game board.
     * It creates and adds appropriate game objects to the application (app) based on the tile type.
     * If the tile type is a ball or a hole, a new Ball or Hole object is created and added to the app's levelBalls or levelHoles lists, respectively.
     * If the tile type is "S", a new Wall object is created and added to the app's spawner with the specified coordinates (x, y).
     * @param app The application object that contains the board and other game-related data.
     * @param tileType The type of tile at the specified coordinates (x, y).
     * @param x The x-coordinate of the tile on the game board.
     * @param y The y-coordinate of the tile on the game board.
     */
    private void handleTileType(App app, String tileType, int x, int y) {
        if (Tile.isBallType(tileType)) {
            app.levelBalls.add(new Ball(x, y, tileType, tileType.substring(1)));
        } else if (Tile.isHoleType(tileType)) {
            app.levelHoles.add(new Hole(x, y, tileType, tileType.substring(1)));
        } else if (tileType.equals("S")) {
            app.spawner.put(app.spawn_count++, new int[]{x, y});
        }
    }

    /**
    *This method cleans up the board by removing any tiles that represent balls, holes, or bricks.
    *It iterates through each row and column of the board and checks if a tile is of a type that needs to be cleaned up.
    *If it is, the method sets the tile at the next column to the left to an empty space.
    *@param app The application object that contains the board and other game-related data.
    */
    private void cleanUpBoard(App app) {
        for (int row = 0; row < App.NUM_ROWS; row++) {
            for (int col = 0; col < App.NUM_COLS; col++) {
                Tile tile = App.board[row][col];
                if (tile.isBallType() || tile.isHoleType() || tile.isBrickType()) {
                    App.board[row][col + 1].setTileType(" ");
                }
            }
        }
    }

    /**
     * This method sets up the walls on the game board. It iterates through each row and column of the board and checks if a tile is of a type that represents a wall or a brick. If it is, a new Wall object is created and added to the application's levelWalls list.
     * @param app The application object that contains the board and other game-related data.
     */
    private void setupWalls(App app) {
        for (int row = 0; row < App.NUM_ROWS; row++) {
            for (int col = 0; col < App.NUM_COLS; col++) {
                Tile tile = App.board[row][col];
                if (tile.isWallType()) {
                    String objectNo = !tile.getTileType().equals("X") ? tile.getTileType() : "0";
                    app.levelWalls.add(new Wall(tile.x, tile.y, tile.getTileType(), objectNo));
                }
                if (tile.isBrickType()){
                    String objectNo = Character.toString(tile.getTileType().charAt(1));
                    app.levelWalls.add(new Wall(tile.x, tile.y, tile.getTileType(), objectNo));
                }
            }
        }
    }

}
