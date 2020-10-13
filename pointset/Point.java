package pointset;

import java.util.Objects;

public class Point {

    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the Euclidean distance (L2 norm) squared between two points.
     * Note: This is the square of the Euclidean distance, i.e. there's no
     * square root.
     */
    public double distanceSquaredTo(Point p) {
        return this.distanceSquaredTo(p.x, p.y);
    }

    /**
     * Returns the Euclidean distance (L2 norm) squared between two points.
     * Note: This is the square of the Euclidean distance, i.e. there's no
     * square root.
     */
    public double distanceSquaredTo(double px, double py) {
        return distanceSquaredBetween(this.x, px, this.y, py);
    }

    /**
     * Returns the Euclidean distance (L2 norm) squared between two points
     * (x1, y1) and (x2, y2).
     */
    private static double distanceSquaredBetween(double x1, double x2, double y1, double y2) {
        return Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2);
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 &&
                Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("Point x: %.10f, y: %.10f", x, y);
    }
}
