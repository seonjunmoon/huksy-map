package pointset;

import org.w3c.dom.Node;

import java.util.List;

public class KDTreePointSet implements PointSet {

    private List<Point> points;
    private Node root;

    class Node {

        private Point point;
        private Node leftChild;
        private Node rightChild;
        boolean axis;

        public Node(Point point, boolean axis) {
            this.point = point;
            this.axis = axis;
        }
    }

    /**
     * Instantiates a new KDTree with the given points.
     *
     * @param points a non-null, non-empty list of points to include
     *               (makes a defensive copy of points, so changes to the list
     *               after construction don't affect the point set)
     */
    public KDTreePointSet(List<Point> points) {
        for (Point point : points) {
            root = insert(root, point, true);
        }
    }

    /**
     * Returns the point in this set closest to (x, y) in (usually) O(log N) time,
     * where N is the number of points in this set.
     */
    @Override
    public Point nearest(double x, double y) {
        return nearest(root, new Point(x, y), root).point;
    }

    public Node nearest(Node n, Point goal, Node best) {
        Node goodSide = null;
        Node badSide = null;
        if (n == null) {
            return best;
        }
        if (n.point.distanceSquaredTo(goal) < best.point.distanceSquaredTo(goal)) {
            best = n;
        }
        if (n.axis) {
            if (goal.x() <= n.point.x()) {
                goodSide = n.leftChild;
                badSide = n.rightChild;
            } else {
                goodSide = n.rightChild;
                badSide = n.leftChild;
            }
        } else {
            if (goal.y() <= n.point.y()) {
                goodSide = n.leftChild;
                badSide = n.rightChild;
            } else {
                goodSide = n.rightChild;
                badSide = n.leftChild;
            }
        }
        best = nearest(goodSide, goal, best);

        Point point;
        if (n.axis) {
            point = new Point(n.point.x(), goal.y());
        } else {
            point = new Point(goal.x(), n.point.y());
        }
        //if badSide could have a better nearest neighbor
        if (point.distanceSquaredTo(goal) < best.point.distanceSquaredTo(goal)) {
            best = nearest(badSide, goal, best);
        }
        return best;
    }

    public Node insert(Node curr, Point point, boolean axis) {
        if (curr == null) {
            return new Node(point, axis);
        }
        if (curr.point.x() == point.x() && curr.point.y() == point.y()) {
            return curr;
        }
        if (axis) {
            axis = false;
            if (curr.point.x() >= point.x()) {
                curr.leftChild = insert(curr.leftChild, point, axis);
            } else {
                curr.rightChild = insert(curr.rightChild, point, axis);
            }
        } else {
            axis = true;
            if (curr.point.y() >= point.y()) {
                curr.leftChild = insert(curr.leftChild, point, axis);
            } else {
                curr.rightChild = insert(curr.rightChild, point, axis);
            }
        }
        return curr;
    }
}