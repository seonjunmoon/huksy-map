/* *****************************************************************************
 *  Compilation:  javac-algs4 Hawk.java
 *  Execution:    none
 *  Dependencies: none
 *  
 *  Implementation of a hawk for the boid simulator.
 *      
 **************************************************************************** */

package pointset.boids;

import edu.princeton.cs.algs4.Vector;
import edu.princeton.cs.algs4.StdDraw;
import pointset.Point;

public class Hawk {
    private Point position;
    private Vector velocity;

    // create a hawk at (x, y) with zero velocity
    public Hawk(double x, double y) {
        position = new Point(x, y);
        velocity = new Vector(2);
    }

    public Point position() {
        return position;
    }

    public double x() {
        return position.x();
    }

    public double y() {
        return position.y();
    }

    // compare by y-coordinate, breaking ties by x-coordinate
    public int compareTo(Hawk that) {
        if (this.position.y() < that.position.y()) {
            return -1;
        }
        if (this.position.y() > that.position.y()) {
            return +1;
        }
        if (this.position.x() < that.position.x()) {
            return -1;
        }
        if (this.position.x() > that.position.x()) {
            return +1;
        }
        return 0;
    }

    // compare by y-coordinate, breaking ties by x-coordinate
    public double distanceSquaredTo(Hawk that) {
        return position.distanceSquaredTo(that.position);
    }

    public Vector returnToWorld() {
        Vector center = new Vector(0.5, 0.5);
        Vector myPosition = new Vector(x(), y());
        return center.minus(myPosition);
    }

    public Vector eatBoid(Boid boid) {
        Vector boidPosition = new Vector(boid.x(), boid.y());
        Vector myPosition = new Vector(x(), y());
        return boidPosition.minus(myPosition);
    }           

    public void updatePositionAndVelocity(Boid nearest) {
        double x = x() + velocity.cartesian(0);
        double y = y() + velocity.cartesian(1);
        position = new Point(x, y);
        Vector desire = eatBoid(nearest).direction().scale(0.0003);
        velocity = velocity.plus(desire);
    }

    public void draw() {
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.point(x(), y());
    }

}
