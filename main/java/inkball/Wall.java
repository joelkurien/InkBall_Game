package inkball;

/**
 * Class for loading walls of a particular level in Inkball and handle ball collisions on these walls.
 */
public class Wall extends Objects implements Collisions {
    public int collideCount = 0; /** Count the number of times a ball has collided with the wall */
    public boolean collidable = true; /** Checks if the wall should act as a rigid body or as a blank tile */

    /**
     * Wall Constructor: Initializes the wall object with
     * its top left corner coordinates, wall type and identifier 
     *
     * @param x the x-coordinate of the wall
     * @param y the y-coordinate of the wall
     * @param wallType the type of the wall
     * @param wallNo the identifier number for the wall
     */
    public Wall(int x, int y, String wallType, String wallNo){
        super(x, y, wallType, wallNo);
    }

     /**
     * Detects collision between the ball and the wall. And changes color of the ball based on the 
     * type of wall the ball collided with.
     *
     * @param ball the ball to check for collision with the wall
     */
    public void handleCollision(Ball ball){
        if(collidable){
            float centerX = ball.x + ball.radius;
            float centerY = ball.y + ball.radius;
        
            float wallLeft = x;
            float wallRight = x + App.CELLSIZE;
            float wallTop = y;
            float wallBottom = y + App.CELLSIZE;

            float nearestX = max(x, min(centerX, wallRight));
            float nearestY = max(y, min(centerY, wallBottom));
        
            float deltaX = centerX - nearestX;
            float deltaY = centerY - nearestY;
            float distanceSquared = deltaX * deltaX + deltaY * deltaY;
        
            if (distanceSquared < ball.radius * ball.radius) {
                if ((centerX < wallLeft && centerY < wallTop) ||     
                    (centerX > wallRight && centerY < wallTop) ||    
                    (centerX < wallLeft && centerY > wallBottom) ||  
                    (centerX > wallRight && centerY > wallBottom)) { 
                    ball.xspeed *= -1;
                    ball.yspeed *= -1;
                } else {
                    if (centerX < wallLeft || centerX > wallRight) {
                        ball.xspeed *= -1;
                    } else if (centerY < wallTop || centerY > wallBottom) {
                        ball.yspeed *= -1;
                    }
                }
                ball.updateSpeed();

                if(ball.objectNo.equals(objectNo) || objectNo.equals("0")){
                    ++collideCount;
                    if(collideCount >= 2) collidable = false;
                }
                
                int ordinal = Integer.parseInt(objectNo);
                if(ordinal > 0 && ordinal < 5){
                    ball.objectType = "B"+ordinal;
                    ball.objectNo = objectNo;
                }    
            }
        }
    }

    /**
     * Draws the wall on the screen.
     *
     * @param app the application object to draw the wall
     */
    public void draw(App app) {
        if(objectNo != null){
             app.image(app.images.get("wall"+objectNo), x, y);
            
            
            if(objectType.charAt(0) == 'T'){
                if(collideCount == 1)
                    app.image(app.images.get("wallB"+objectNo), x, y);
            
                if(collideCount == 2){
                    app.image(app.images.get("tile"), x, y);
                }
            }
        }
        for(Ball ball: app.levelBalls)
            handleCollision(ball);
            
    }
}
