/* *****************************************************************************
 *  Read points from a file (specified as a command-line argument) and
 *  draw to standard draw. Highlight the closest point to the mouse.
 *
 *  The nearest neighbor according to the brute-force algorithm is drawn
 *  in red; the nearest neighbor using the kd-tree algorithm is drawn in blue.
 *
 **************************************************************************** */

package pointset;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class NearestNeighborVisualizer {

    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);

        // initialize the two data structures with point from standard input
        List<Point> points = new ArrayList<>();
        for (int i = 0; !in.isEmpty(); i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            points.add(new Point(x, y));
        }
        PointSet naive = new NaivePointSet(points);
        PointSet kdtree = new KDTreePointSet(points);

        // process nearest neighbor queries
        StdDraw.enableDoubleBuffering();
        while (true) {

            // the location (x, y) of the mouse
            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();

            // draw all of the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            for (Point p : points) {
                StdDraw.point(p.x(), p.y());
            }

            // draw in red the nearest neighbor according to the brute-force algorithm
            StdDraw.setPenRadius(0.03);
            StdDraw.setPenColor(StdDraw.RED);
            Point naiveNearest = naive.nearest(x, y);
            if (naiveNearest != null) {
                StdDraw.point(naiveNearest.x(), naiveNearest.y());
            }
            StdDraw.setPenRadius(0.02);

            // draw in blue the nearest neighbor according to the kd-tree algorithm
            StdDraw.setPenColor(StdDraw.BLUE);
            Point kdtreeNearest = kdtree.nearest(x, y);
            if (kdtreeNearest != null) {
                StdDraw.point(kdtreeNearest.x(), kdtreeNearest.y());
            }
            StdDraw.show();
            StdDraw.pause(20);
        }
    }
}