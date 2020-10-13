package pointset;

public interface PointSet {
    /** Returns the point in this set closest to (x, y). */
    Point nearest(double x, double y);
}
