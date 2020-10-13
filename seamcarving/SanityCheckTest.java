package seamcarving;

import edu.princeton.cs.algs4.Picture;
import org.junit.Test;

import java.awt.Color;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SanityCheckTest {
    @Test
    public void sanityEnergyTest() {
        /* Creates the sample array specified in the spec and checks for matching energies */
        Picture p = new Picture(3, 4);
        int[][][] exampleArray = {{{255, 101, 51}, {255, 101, 153}, {255, 101, 255}},
                                  {{255, 153, 51}, {255, 153, 153}, {255, 153, 255}},
                                  {{255, 203, 51}, {255, 204, 153}, {255, 205, 255}},
                                  {{255, 255, 51}, {255, 255, 153}, {255, 255, 255}}};
        double[][] exampleEnergySquared = {{20808.0, 52020.0, 20808.0},
                                           {20808.0, 52225.0, 21220.0},
                                           {20809.0, 52024.0, 20809.0},
                                           {20808.0, 52225.0, 21220.0}};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                int[] colorVals = exampleArray[j][i];
                p.set(i, j, new Color(colorVals[0], colorVals[1], colorVals[2]));
            }
        }

        SeamCarver sc = new AStarSeamCarver(p);
        double[][] energy = new double[3][4];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                energy[i][j] = sc.energy(i, j);
                assertEquals(Math.sqrt(exampleEnergySquared[j][i]), energy[i][j], 1e-3);
            }
        }
    }

    @Test
    public void sanityVerticalSeamTest() {
        Picture p = new Picture("data/images/6x5.png");
        SeamCarver sc = new AStarSeamCarver(p);

        int[] seam = sc.findVerticalSeam();
        int[] expected = {3, 4, 3, 2, 2};
        // Exact seam may differ depending on tie-breaking
        assertArrayEquals(expected, seam);
    }

    @Test
    public void sanityHorizontalSeamTest() {
        Picture p = new Picture("data/images/6x5.png");
        SeamCarver sc = new AStarSeamCarver(p);

        int[] seam = sc.findHorizontalSeam();
        int[] expected = {2, 2, 1, 2, 1, 2};
        // Exact seam may differ depending on tie-breaking
        assertArrayEquals(expected, seam);
    }
}
