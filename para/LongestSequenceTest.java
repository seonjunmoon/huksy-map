package para;

import java.util.Random;
import org.junit.Test;

import static org.junit.Assert.*;

public class LongestSequenceTest {
    public static Random RANDOM = new Random(332134);

    public static final int FULLY_SEQUENTIAL = Integer.MAX_VALUE;
    public static final int REASONABLE_CUTOFF = 100;
    public static final int FULLY_PARALLEL = 1;

    public static final int NUM_SMALL_TESTS = 500;
    public static final int NUM_LARGE_TESTS = 5;


    public static final int SMALL_SIZE  = 100;
    public static final int LARGE_SIZE  = 100000;
    public static final int HUGE_SIZE   = 300000000;

    @Test
    public void testSmallSequential() {
        for (int i = 0; i < NUM_SMALL_TESTS; i++) {
            assertTrue(testSmallBitArray(i, FULLY_SEQUENTIAL, 0));
            assertTrue(testSmallBitArray(i, FULLY_SEQUENTIAL, 1));
        }
    }
    @Test
    public void testSmallParallel() {
        for (int i = 0; i < NUM_SMALL_TESTS; i++) {
            assertTrue(testSmallBitArray(i, FULLY_PARALLEL, 0));
            assertTrue(testSmallBitArray(i, FULLY_PARALLEL, 1));
        }
    }

    @Test
    public void testLarge() {
        for (int i = 0; i < NUM_LARGE_TESTS; i++) {
            assertTrue(testRandomBitArray(LARGE_SIZE, REASONABLE_CUTOFF, 0));
            assertTrue(testRandomBitArray(LARGE_SIZE, REASONABLE_CUTOFF, 1));
        }
    }

    @Test
    public void testParallelism() {
        int best = 0;
        int conseq = 0;
        int[] bits = new int[HUGE_SIZE];
        for (int i = 0; i < HUGE_SIZE; i++) {
            bits[i] = RANDOM.nextBoolean() ? 1 : 0;
            if (bits[i] == 0) {
                conseq++;
            }
            else {
                conseq = 0;
            }
            best = Math.max(best, conseq);
        }

        long seqTime, reasonableTime, paraTime = 0;
        long start = System.currentTimeMillis();

        assertTrue(runTest(0, bits, FULLY_SEQUENTIAL, best));
        seqTime = System.currentTimeMillis() - start;

        assertTrue(runTest(0, bits, REASONABLE_CUTOFF, best));
        reasonableTime = System.currentTimeMillis() - (seqTime + start);

        assertTrue(runTest(0, bits, FULLY_PARALLEL, best));
        paraTime = System.currentTimeMillis() - (reasonableTime + seqTime + start);

        assertTrue(paraTime > seqTime && seqTime > reasonableTime);
    }

    private boolean testSmallBitArray(int num, int cutoff, int match) {
        int best = 0;
        int conseq = 0;
        int[] bits = new int[SMALL_SIZE];
        for (int i = SMALL_SIZE - 1; i >= 0; i--) {
            bits[i] = (num >> i) & 1;
            if (bits[i] == match) {
                conseq++;
            }
            else {
                conseq = 0;
            }
            best = Math.max(best, conseq);
        }
        return runTest(match, bits, cutoff, best);
    }

    private boolean testRandomBitArray(int size, int cutoff, int match) {
        int best = 0;
        int conseq = 0;
        int[] bits = new int[size];
        for (int i = 0; i < size; i++) {
            bits[i] = RANDOM.nextBoolean() ? 1 : 0;
            if (bits[i] == match) {
                conseq++;
            }
            else {
                conseq = 0;
            }
            best = Math.max(best, conseq);
        }
        return runTest(match, bits, cutoff, best);
    }

    private boolean runTest(int num, int[] array, int cutoff, int expected) {
        return LongestSequence.getLongestSequence(num, array, cutoff) == expected;
    }

}
