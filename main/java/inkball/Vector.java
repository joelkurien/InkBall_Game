package inkball;

import processing.core.*;

/**
 * Class responsible for mathematical vector calculations.
 */
public class Vector {
    /**
     * Method to calculate the normals of a line
     * @param p1
     * @param p2
     */
    private PVector[] calcNormals(PVector p1, PVector p2){
        float dx = p2.x - p1.x;
        float dy = p2.y - p1.y;
        PVector n1 = new PVector(-dy, dx);
        n1.normalize();
        PVector n2 = new PVector(dy, -dx);
        n2.normalize();
        return new PVector[]{n1, n2};
    }

    /**
     * Select the appropriate normal of a line when a ball collides
     * with it.
     * @param start
     * @param end
     * @param p
     */
    public PVector selectNormal(PVector start, PVector end, PVector p){
        PVector[] normals = calcNormals(start, end);
        PVector n1 = normals[0];
        PVector n2 = normals[1];
        float midX = (start.x+end.x)/2;
        float midY = (start.y+end.y)/2;
        PVector midP = new PVector(midX, midY);
        PVector m1 = addVector(midP, n1);
        PVector m2 = addVector(midP, n2);
        float n1Dist = PVector.dist(p, m1);
        float n2Dist = PVector.dist(p, m2);
        PVector n =  n1Dist > n2Dist ? n2 : n1;
        return n;
    }

    /**
     * Change the direction of the ball/ reflect the ball in another direction.
     * @param n
     * @param v
     */
    public PVector velocityChange(PVector n, PVector v){
        PVector x = PVector.sub(v,n.mult(2*v.dot(n)));
        return x;
    }

    /**
     * Method to add two vectors.
     */
    private PVector addVector(PVector p, PVector p1) {
        return PVector.add(p, p1);
    }
}