package seamcarving;

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class AStarSeamCarver implements SeamCarver {
    private Picture picture;
    private SeamCarver carver;

    public AStarSeamCarver(Picture picture) {
        if (picture == null) {
            throw new NullPointerException("Picture cannot be null.");
        }
        this.picture = new Picture(picture);
    }

    public Picture picture() {
        return new Picture(picture);
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    public Color get(int x, int y) {
        return picture.get(x, y);
    }


    public int[] findHorizontalSeam() {
        Picture trans = new Picture(picture.height(), picture.width());
        for (int i = 0; i < picture.height(); i++) {
            for (int j = 0; j < picture.width(); j++) {
                trans.set(i, j, picture.get(j, i));
            }
        }
        picture = trans;
        int[] horSeam = findVerticalSeam();

        Picture origin = new Picture(picture.height(), picture.width());
        for (int i = 0; i < picture.height(); i++) {
            for (int j = 0; j < picture.width(); j++) {
                origin.set(i, j, picture.get(j, i));
            }
        }
        picture = origin;

        return horSeam;
    }


    public int[] findVerticalSeam() {
        carver = new AStarSeamCarver(picture);
        double[][] energy = toEnergyMatrix(carver);
        int[]verSeam = new int[picture.height()];
        double[][]distTo = new double[picture.width()][picture.height()];
        int[][]edgeTo = new int[picture.width()][picture.height()];

        for (int i = 0; i < picture.width(); i++) {
            for (int j = 0; j < picture.height(); j++) {
                distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        for (int i = 0; i< picture.width(); i++) {
            edgeTo[i][0]= Integer.MIN_VALUE;
            distTo[i][0]= energy[i][0];
        }

        for (int c = 0; c < picture.height() - 1; c++) {
            for (int r = 0; r < picture.width(); r++) {
                if (distTo[r][c + 1] > distTo[r][c] + energy[r][c + 1]) {
                    distTo[r][c + 1] = distTo[r][c] + energy[r][c + 1];
                    edgeTo[r][c + 1] = r;
                }
                if (r > 0) {
                    if (distTo[r - 1][c + 1] > distTo[r][c] + energy[r - 1][c + 1]) {
                        distTo[r - 1][c + 1] = distTo[r][c] + energy[r - 1][c + 1];
                        edgeTo[r - 1][c + 1] = r;
                    }
                }
                if (r < picture.width() - 1) {
                    if (distTo[r + 1][c + 1] > distTo[r][c] + energy[r + 1][c + 1]) {
                        distTo[r + 1][c + 1] = distTo[r][c] + energy[r + 1][c + 1];
                        edgeTo[r + 1][c + 1] = r;
                    }
                }
            }
        }

        int edgeToBot = Integer.MAX_VALUE;
        double energyToBot = Double.POSITIVE_INFINITY;

        for (int r = 0; r < picture.width(); r++) {
            if (energyToBot > distTo[r][picture.height() - 1]) {
                energyToBot = distTo[r][picture.height() - 1];
                edgeToBot = r;
            }
        }

        for (int c = picture.height() - 1; c >= 0; c--) {
            verSeam[c] = edgeToBot;
            edgeToBot = edgeTo[edgeToBot][c];
        }
        return verSeam;
    }

    private double[][] toEnergyMatrix(SeamCarver sc) {
        double[][] a = new double[sc.width()][sc.height()];
        for (int col = 0; col < sc.width(); col++) {
            for (int row = 0; row < sc.height(); row++) {
                a[col][row] = sc.energy(col, row);
            }
        }
        return a;
    }
}
