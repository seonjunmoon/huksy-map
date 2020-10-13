package seamcarving.util;

import edu.princeton.cs.algs4.Picture;
import seamcarving.AStarSeamCarver;
import seamcarving.SeamCarver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Uses SeamCarver to remove number of rows or columns specified.
 * Shows the sequence of seams being removed.
 */
public class SeamCarverVisualizer {
    private static final String IMAGE_PATH = "data/images/HJoceanSmall.png";
    private static final int NUM_PIXELS = 150;
    private static final boolean HORIZONTAL_SEAM = true;
    JFrame frame;

    public void visualizeHorizontalCarve(SeamCarver sc, int N) {
        for (int i = 0; i < N; i++) {
            int[] minSeam = sc.findHorizontalSeam();
            Picture p = sc.picture();
            paintHorizontalSeam(p, minSeam);
            show(p);
            sc.removeHorizontalSeam(minSeam);
        }

        show(sc.picture());
    }

    public void visualizeVerticalCarve(SeamCarver sc, int N) {
        for (int i = 0; i < N; i++) {
            int[] minSeam = sc.findVerticalSeam();
            Picture p = sc.picture();
            paintVerticalSeam(p, minSeam);
            show(p);
            sc.removeVerticalSeam(minSeam);
        }

        show(sc.picture());
        sc.picture().save("output.png");
    }

    private void paintHorizontalSeam(Picture p, int[] seam) {

        for (int i = 0; i < seam.length; i++) {
            p.set(i, seam[i], new Color(255, 0, 0));
        }
    }

    private void paintVerticalSeam(Picture p, int[] seam) {
        for (int i = 0; i < seam.length; i++) {
            p.set(seam[i], i, new Color(255, 0, 0));
        }
    }

    public void show(Picture img) {
        if (frame == null) {
            frame = new JFrame();

            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            menuBar.add(menu);
            JMenuItem menuItem1 = new JMenuItem(" Save...   ");
            menuItem1.addActionListener(img);
            menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
            menu.add(menuItem1);
            frame.setJMenuBar(menuBar);

            frame.setContentPane(img.getJLabel());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setTitle("Output");
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
         }

         // draw
         frame.setContentPane(img.getJLabel());
         frame.revalidate();
         frame.repaint();
    }

    public static void main(String[] args) {
        Picture samplePicture = new Picture(IMAGE_PATH);
        SeamCarver sc = new AStarSeamCarver(samplePicture);

        SeamCarverVisualizer scv = new SeamCarverVisualizer();
        if (HORIZONTAL_SEAM) {
            scv.visualizeHorizontalCarve(sc, NUM_PIXELS);
        } else {
            scv.visualizeVerticalCarve(sc, NUM_PIXELS);
        }
    }
}
