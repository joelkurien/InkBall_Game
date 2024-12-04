package inkball;

import inkball.Enums.BallType;
import inkball.Enums.BrickType;
import inkball.Enums.HoleType;
import inkball.Enums.WallType;

/**
 * This class is responsible for giving a default board when the game is initally called.
 */
public class Tile {
    protected int x; /**x position of a tile*/
    protected int y; /**y position of a tile*/

    protected String tileType = "X"; /**default tile value*/

    /**
     * Constructor of the Tile class, used to specify the type of the
     * tile on the board
     */
    public Tile(int x, int y, String tileType){
        this.x = x;
        this.y = y;
        this.tileType = tileType;
    }

    /**
     * Returns the type of the tile.
     */
    public String getTileType(){
        return tileType;
    }

    /**
     * Sets the type of a tile
     */
    public void setTileType(String tt){
        tileType = tt;
    }

    /**
     * Checks if a tile is a hole or not
     */
    public boolean isHoleType(){
        try {
            HoleType.valueOf(this.tileType);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Checks given a tileType is it a hole or not
     * @param tileType
     */
    public static boolean isHoleType(String tileType){
        try {
            HoleType.valueOf(tileType);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Checks if the tile is a ball or not
     */
    public boolean isBallType(){
        try {
            BallType.valueOf(this.tileType);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Checks if given a tileType, if the tile is a ball or not
     * @param tileType
     */
    public static boolean isBallType(String tileType){
        try {
            BallType.valueOf(tileType);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Checks if the tile is a wall or not.
     */
    public boolean isWallType(){
        try {
            for(WallType wall: WallType.values()){
                if(wall.getWallType().equals(tileType)){
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the tile is a brick or not.
     */
    public boolean isBrickType(){
        try {
            for(BrickType brick: BrickType.values()){
                if(brick.getType().equals(tileType)){
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }   
    }
}
