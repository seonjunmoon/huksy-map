package seamcarving.util;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import seamcarving.AStarSeamCarver;
import seamcarving.SeamCarver;

/**
 *  Prints energies of pixels, a vertical seam, and a horizontal seam.
 *
 *  The table gives the dual-gradient energies of each pixel.
 *  The asterisks denote a minimum-energy vertical or horizontal seam.
 */
public class PrintSeams {
    private static final boolean HORIZONTAL   = true;
    private static final boolean VERTICAL     = false;

    private static void printSeam(SeamCarver carver, int[] seam, boolean direction) {
        double totalSeamEnergy = 0.0;

        for (int row = 0; row < carver.height(); row++) {
            for (int col = 0; col < carver.width(); col++) {
                double energy = carver.energy(col, row);
                String marker = " ";
                if ((direction == HORIZONTAL && row == seam[col]) ||
                    (direction == VERTICAL   && col == seam[row])) {
                    marker = "*";
                    totalSeamEnergy += energy;
                }
                StdOut.printf("%7.2f%s ", energy, marker);
            }
            StdOut.println();
        }
        StdOut.printf("Total energy = %f\n", totalSeamEnergy);
        StdOut.println();
        StdOut.println();
    }

    public static void main(String[] args) {
        Picture picture = new Picture("data/images/4x6.png");
        StdOut.printf("%d-by-%d image\n", picture.width(), picture.height());
        StdOut.println();
        StdOut.println("The table gives the dual-gradient energies of each pixel.");
        StdOut.println("The asterisks denote a minimum energy vertical or horizontal seam.");
        StdOut.println();

        SeamCarver carver = new AStarSeamCarver(picture);

        StdOut.printf("Vertical seam: { ");
        int[] verticalSeam = carver.findVerticalSeam();
        for (int x : verticalSeam) {
            StdOut.print(x + " ");
        }
        StdOut.println("}");
        printSeam(carver, verticalSeam, VERTICAL);

        StdOut.printf("Horizontal seam: { ");
        int[] horizontalSeam = carver.findHorizontalSeam();
        for (int y : horizontalSeam) {
            StdOut.print(y + " ");
        }
        StdOut.println("}");
        printSeam(carver, horizontalSeam, HORIZONTAL);
    }
}
