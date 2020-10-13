package pointset;

import org.junit.Test;

import java.util.List;

public class KDTreePointSetTest {
    @Test
    public void test() {
        Point p1 = new Point(1.1, 2.2);
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(2.9, 4.2);

        PointSet kd = new KDTreePointSet(List.of(p1, p2, p3));
        Point res = kd.nearest(3.0, 4.0);
        System.out.println(res.x());

    }

}
