package pq;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class ArrayHeapMinPQTest {
    /* Be sure to write randomized tests that can handle millions of items. To
     * test for runtime, compare the runtime of NaiveMinPQ vs ArrayHeapMinPQ on
     * a large input of millions of items. */
    ArrayHeapMinPQ contents = new ArrayHeapMinPQ();

    @Test
    public void testAdd() {
        contents.add("a", 2);
        contents.removeSmallest();
        assertEquals(contents.getSmallest(), null);
        contents.add("a", 2);
        contents.add("b", 1);
        contents.add("c", 7);
        contents.add("d", 6);
        contents.add("e", 4);
        contents.add("f", 5);
        contents.add("g", 3);
        assertTrue(contents.contains("b"));
        contents.removeSmallest();
    }

    @Test
    public void testContains() {
        assertFalse(contents.contains("a"));
        contents.add("a", 2);
        contents.add("b", 1);
        contents.add("c", 7);
        contents.add("d", 6);
        assertTrue(contents.contains("a"));
        assertTrue(contents.contains("d"));
        assertFalse(contents.contains("h"));
    }

    @Test
    public void testGetSmallest() {
        contents.add("a", 4);
        contents.add("b", 5);
        contents.add("c", 6);
        contents.add("d", 7);
        assertEquals(contents.getSmallest(), "a");
    }

    @Test
    public void testRemoveSmallest() {
        contents.add("a", 2);
        contents.add("b", 1);
        contents.add("c", 7);
        contents.add("d", 6);
        assertTrue(contents.contains("a"));
        assertEquals(contents.removeSmallest(), "b");
        assertEquals(contents.getSmallest(), "a");
        contents.removeSmallest();
        assertEquals(contents.getSmallest(), "d");
        contents.removeSmallest();
        assertEquals(contents.getSmallest(), "c");
        contents.removeSmallest();

        contents.add("b", 1);
        assertEquals(contents.getSmallest(), "b");
        contents.removeSmallest();
        assertEquals(contents.getSmallest(), null);
    }

    @Test
    public void testChangePriority() {
        contents.changePriority("c", 0);
        contents.add("a", 2);
        contents.add("b", 1);
        contents.add("c", 7);
        contents.add("d", 6);
        assertEquals(contents.getSmallest(), "b");
        contents.changePriority("c", 0);
        assertEquals(contents.getSmallest(), "c");
        contents.removeSmallest();
        assertEquals(contents.getSmallest(), "b");
        contents.changePriority("b", 3);
        assertEquals(contents.getSmallest(), "a");
        contents.changePriority("c", 0);
        assertEquals(contents.getSmallest(), "a");
        contents.changePriority("b", 2000000);
        assertEquals(contents.getSmallest(), "a");
        contents.changePriority("b", -300000);
        assertEquals(contents.getSmallest(), "a");
    }

    @Test
    public void testChangePriority2() {
        contents.add("a", 6);
        contents.add("b", 5);
        contents.add("c", 4);
        contents.add("d", 3);
        assertEquals(contents.getSmallest(), "d");
        contents.changePriority("d", 10);
        assertEquals(contents.getSmallest(), "c");
        contents.removeSmallest();
        contents.removeSmallest();
        assertEquals(contents.getSmallest(), "a");
        contents.removeSmallest();
        assertEquals(contents.getSmallest(), "d");

    }

    @Test
    public void testTime() {

        ArrayHeapMinPQ<Integer> list = new ArrayHeapMinPQ<>();
        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < 200000; i += 1) {
            Random rand = new Random();
            double priority = rand.nextDouble();
            list.add(i, priority);
        }
        System.out.println("Total time elapsed: " + sw.elapsedTime() +  " seconds.");
    }

    @Test
    public void testTime2() {

        ArrayHeapMinPQ<Integer> list2 = new ArrayHeapMinPQ<>();
        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < 200000; i += 1) {
            Random rand = new Random();
            int item = rand.nextInt(200000);
            double priority = rand.nextDouble();
            list2.changePriority(item, priority);
        }
        System.out.println("Total time elapsed: " + sw.elapsedTime() +  " seconds.");
    }
}
