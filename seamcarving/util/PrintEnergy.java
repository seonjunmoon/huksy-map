package seamcarving.util;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import seamcarving.AStarSeamCarver;
import seamcarving.SeamCarver;

/**
 *  Print energy of each pixel as calculated by SeamCarver object.
 */
public class PrintEnergy {
    public static void main(String[] args) {
        Picture picture = new Picture("data/images/3x4.png");
        StdOut.printf("%d-by-%d image\n", picture.width(), picture.height());
        SeamCarver sc = new AStarSeamCarver(picture);
        StdOut.printf("Printing energy calculated for each pixel.\n");

        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++) {
                StdOut.printf("%9.0f ", sc.energy(col, row));
            }
            StdOut.println();
        }
    }
}
