package seamcarving.util;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import seamcarving.AStarSeamCarver;
import seamcarving.SeamCarver;

/**
 * Shows 2 images: original image and the energies of each of its pixel (as a grayscale image).
 */
public class ShowEnergy {
    public static void main(String[] args) {
        Picture picture = new Picture("data/images/HJoceanSmall.png");
        StdOut.printf("%d-by-%d image\n", picture.width(), picture.height());
        picture.show();
        SeamCarver sc = new AStarSeamCarver(picture);

        StdOut.printf("Displaying energy calculated for each pixel.\n");
        PictureUtils.showEnergy(sc);
    }
}
