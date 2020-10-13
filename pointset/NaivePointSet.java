package pointset;

import java.util.List;

/**
 * Naive nearest neighbor implementation using a linear scan.
 */
public class NaivePointSet implements PointSet {
    private final List<Point> points;

    /**
     * Instantiates a new NaivePointSet with the given points.
     * @param points a non-null, non-empty list of points to include
     *               (makes a defensive copy of points, so changes to the list
     *               after construction don't affect the point set)
     */
    public NaivePointSet(List<Point> points) {
        this.points = points;
    }

    /**
     * Returns the point in this set closest to (x, y) in O(N) time,
     * where N is the number of points in this set.
     */
    @Override
    public Point nearest(double x, double y) {
        if (points == null) {
            return null;
        }
        Point closest = points.get(0);
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).distanceSquaredTo(x, y) < closest.distanceSquaredTo(x, y)) {
                closest = points.get(i);
            }
        }
        return closest;
    }
}
