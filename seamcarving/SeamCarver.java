package seamcarving;

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public interface SeamCarver {

    /** Returns the current image. (This updates whenever a seam is removed.) */
    Picture picture();

    /** Sets the current image. */
    void setPicture(Picture picture);

    /** Returns the width of the current image, in pixels. */
    int width();

    /** Returns the height of the current image, in pixels. */
    int height();

    /** Returns the color of pixel (x, y) in the current image. */
    Color get(int x, int y);

    /** Returns the energy of pixel (x, y) in the current image. */
    default double energy(int x, int y) {
        if (!inBounds(x, y)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        int xr = x + 1;
        int xl = x - 1;
        int yt = y - 1;
        int yb = y + 1;
        if (x == 0) {
            xl = this.width() - 1;
        }
        if (x == this.width() - 1) {
            xr = 0;
        }
        if (y == 0) {
            yt = this.height() - 1;
        }
        if (y == this.height() - 1) {
            yb = 0;
        }
        Color right = this.get(xr, y);
        Color left = this.get(xl, y);
        Color top = this.get(x, yt);
        Color bottom = this.get(x, yb);

        double xDeltaRed = Math.abs(right.getRed() - left.getRed());
        double xDeltaBlue = Math.abs(right.getBlue() - left.getBlue());
        double xDeltaGreen = Math.abs(right.getGreen() - left.getGreen());
        double yDeltaRed = Math.abs(bottom.getRed() - top.getRed());
        double yDeltaBlue = Math.abs(bottom.getBlue() - top.getBlue());
        double yDeltaGreen = Math.abs(bottom.getGreen() - top.getGreen());

        double xSum = Math.pow(xDeltaRed, 2) + Math.pow(xDeltaBlue, 2) + Math.pow(xDeltaGreen, 2);
        double ySum = Math.pow(yDeltaRed, 2) + Math.pow(yDeltaBlue, 2) + Math.pow(yDeltaGreen, 2);

        return Math.sqrt(xSum + ySum);
    }

    /** Returns true iff pixel (x, y) is in the current image. */
    default boolean inBounds(int x, int y) {
        return (x >= 0) && (x < width()) && (y >= 0) && (y < height());
    }

    /**
     * Calculates and returns a minimum-energy horizontal seam in the current image.
     * The returned array will have the same length as the width of the image.
     * A value of v at index i of the output indicates that pixel (i, v) is in the seam.
     */
    int[] findHorizontalSeam();

    /**
     * Calculates and returns a minimum-energy vertical seam in the current image.
     * The returned array will have the same length as the height of the image.
     * A value of v at index i of the output indicates that pixel (v, i) is in the seam.
     */
    int[] findVerticalSeam();

    /** Calculates and removes a minimum-energy horizontal seam from the current image. */
    default void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new NullPointerException("Input seam array cannot be null.");
        } else if (width() == 1) {
            throw new IllegalArgumentException("Image width is 1.");
        } else if (seam.length != width()) {
            throw new IllegalArgumentException("Seam length does not match image width.");
        }

        for (int i = 0; i < seam.length - 2; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException(
                        "Invalid seam, consecutive vertical indices are greater than one apart.");
            }
        }

        Picture carvedPicture = new Picture(width(), height() - 1);
        /* Copy over the all indices besides the index specified by the seam */
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < seam[i]; j++) {
                carvedPicture.set(i, j, get(i, j));
            }

            for (int j = seam[i] + 1; j < height(); j++) {
                carvedPicture.set(i, j - 1, get(i, j));
            }
        }

        setPicture(carvedPicture);
    }

    /** Calculates and removes a minimum-energy vertical seam from the current image. */
    default void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new NullPointerException("Input seam array cannot be null.");
        } else if (height() == 1) {
            throw new IllegalArgumentException("Image height is 1.");
        } else if (seam.length != height()) {
            throw new IllegalArgumentException("Seam length does not match image height.");
        }

        for (int i = 0; i < seam.length - 2; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException(
                        "Invalid seam, consecutive horizontal indices are greater than one apart.");
            }
        }

        Picture carvedPicture = new Picture(width() - 1, height());
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < seam[i]; j++) {
                carvedPicture.set(j, i, get(j, i));
            }

            for (int j = seam[i] + 1; j < width(); j++) {
                carvedPicture.set(j - 1, i, get(j, i));
            }
        }

        setPicture(carvedPicture);
    }
}
