package para;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import static org.junit.Assert.*;

public class FilterEmptyTest {

    public static Random RANDOM = new Random(332134);

    public static final int NUM_SMALL = 250;
    public static final int SMALL_SIZE  = 10;

    public static final int NUM_LARGE = 10;
    public static final int LARGE_SIZE  = 100000;

    public static final int MAX_STRING_LENGTH = 30;

    @Test
    public void testSmall() {
        for (int i = 0; i < NUM_SMALL; i++) {
            String[] input = makeInput(i, SMALL_SIZE);
            int[] output = filter(input);
            assertTrue(runTest(input, output));
        }
    }

    @Test
    public void testLarge() {
        for (int i = 0; i < NUM_LARGE; i++) {
            String[] input = makeRandomInput(LARGE_SIZE);
            assertTrue(runTest(input, filter(input)));
        }
    }

    private String makeStringOfLength(int length) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            str.append("" + ('a' + RANDOM.nextInt(26)));
        }
        return str.toString();
    }

    private String[] makeInput(int num, int size) {
        String[] input = new String[size];
        for (int i = size - 1; i >= 0; i--) {
            input[i] = ((num >> i) & 1) == 1 ? makeStringOfLength(RANDOM.nextInt(MAX_STRING_LENGTH) + 1) : "";
        }
        return input;
    }

    private int[] filter(String[] input) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < input.length; i++) {
            if (input[i].length() > 0) {
                list.add(input[i].length());
            }
        }
        int[] ret = new int[list.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = list.get(i);
        }
        return ret;
    }

    private String[] makeRandomInput(int size) {
        String[] input = new String[size];
        for (int i = size - 1; i >= 0; i--) {
            input[i] = RANDOM.nextBoolean() ? makeStringOfLength(RANDOM.nextInt(MAX_STRING_LENGTH) + 1) : "";
        }
        return input;
    }

    private  boolean runTest(String[] input, int[] expected) {
        String result = Arrays.toString(FilterEmpty.filterEmpty(input));
        return result != null && result.equals(Arrays.toString(expected));
    }

}
