package inkball;

/**
 * A class created to handle modularity when pressing a key button
 */
public class Controls {
    /**
     * Handles the actions when a key is pressed.
     * When a space is pressed, the game is paused and unpaused.
     * When 'R' is pressed, the level/game is reset.
     */
    public static void keyPress(App app, char key){
        if(key ==  ' '){
            app.paused = !app.paused;
        }
        if(key == 'r'){
            if(app.levelCount >= app.totalLevels || app.gameOver){
                app.levelCount = 0;
                app.totalScore = 0;
            }
            new LevelConfig().levelSetup(app, app.levelCount);
            app.totalScore = app.levelScore;
            app.paused = false;
            app.gameOver = false;
            app.end = false;
            new GameUI().setCurrentTile(0);
            new GameUI().resetBorderTiles();
        }
    }
}
