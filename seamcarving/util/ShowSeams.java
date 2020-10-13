package seamcarving.util;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import seamcarving.AStarSeamCarver;
import seamcarving.SeamCarver;

/**
 *  Shows 3 images: original image and the horizontal and vertical seams of that image.
 */
public class ShowSeams {

    private static void showHorizontalSeam(SeamCarver sc) {
        Picture ep = PictureUtils.toEnergyPicture(sc);
        int[] horizontalSeam = sc.findHorizontalSeam();
        Picture epOverlay = PictureUtils.seamOverlay(ep, true, horizontalSeam);
        epOverlay.show();
    }

    private static void showVerticalSeam(SeamCarver sc) {
        Picture ep = PictureUtils.toEnergyPicture(sc);
        int[] verticalSeam = sc.findVerticalSeam();
        Picture epOverlay = PictureUtils.seamOverlay(ep, false, verticalSeam);
        epOverlay.show();
    }

    public static void main(String[] args) {
        Picture picture = new Picture("data/images/HJoceanSmall.png");
        StdOut.printf("%d-by-%d image\n", picture.width(), picture.height());
        picture.show();
        SeamCarver sc = new AStarSeamCarver(picture);

        StdOut.printf("Displaying horizontal seam calculated.\n");
        showHorizontalSeam(sc);

        StdOut.printf("Displaying vertical seam calculated.\n");
        showVerticalSeam(sc);
    }
}
