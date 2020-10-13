package deque;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class LargeScaleLinkedDequeTest {
    @Test
    public void testAddLastThenRemoveLast() {
        StringBuilder operations = new StringBuilder();
        try {
            // Create a new ArrayDeque (reference implementation)
            operations.append("ArrayDeque<Integer> expectedDeque = new ArrayDeque<>();\n");
            ArrayDeque<Integer> expectedDeque = new ArrayDeque<>();
            // Create a new LinkedDeque (testing implementation)
            operations.append("LinkedDeque<Integer> testingDeque = new LinkedDeque<>();\n");
            LinkedDeque<Integer> testingDeque = new LinkedDeque<>();

            // Add random integers to both the expectedDeque and testingDeque implementations
            for (int i = 0; i < 1000; i += 1) {
                operations.append("expectedDeque.addLast(").append(i).append(");\n");
                expectedDeque.addLast(i);
                operations.append("testingDeque.addLast(").append(i).append(");\n");
                testingDeque.addLast(i);
            }
            // Check that testingDeque matches expectedDeque on all integers
            for (int i = 0; i < 1000; i += 1) {
                operations.append("expectedDeque.removeLast();\n");
                int expectedValue = expectedDeque.removeLast();
                operations.append("testingDeque.removeLast();\n");
                int testingValue = testingDeque.removeLast();
                assertEquals("removeLast() failed on iteration " + i, expectedValue, testingValue);
                // To debug a particular iteration, set a conditional breakpoint
                // Add a regular breakpoint but right click and set a condition like i == 578
            }
        } catch (Throwable e) {
            System.err.append(operations);
            throw e;
        }
    }

    @Test
    public void testRandomOperations() {
        StringBuilder operations = new StringBuilder();
        try {
            // Random seed ensures that each test run is reproducible
            int seed = 332; // or your favorite number
            operations.append("Random random = new Random(").append(seed).append(");\n");
            Random random = new Random(seed);

            // Create a new ArrayDeque (reference implementation)
            operations.append("ArrayDeque<Integer> expectedDeque = new ArrayDeque<>();\n");
            ArrayDeque<Integer> expectedDeque = new ArrayDeque<>();
            // Create a new LinkedDeque (testing implementation)
            operations.append("LinkedDeque<Integer> testingDeque = new LinkedDeque<>();\n");
            LinkedDeque<Integer> testingDeque = new LinkedDeque<>();

            // For 1000 iterations...
            for (int i = 0; i < 1000; i += 1) {
                // Roll a six-sided dice returning a value 0, 1, 2, 3, 4, or 5
                int choice = random.nextInt(6);

                // Choose to perform one of six different actions
                if (choice == 0) {
                    int n = random.nextInt();
                    operations.append("expectedDeque.addFirst(").append(n).append(");\n");
                    expectedDeque.addFirst(n);
                    operations.append("testingDeque.addFirst(").append(n).append(");\n");
                    testingDeque.addFirst(n);
                } else if (choice == 1) {
                    int n = random.nextInt();
                    operations.append("expectedDeque.addLast(").append(n).append(");\n");
                    expectedDeque.addLast(n);
                    operations.append("testingDeque.addLast(").append(n).append(");\n");
                    testingDeque.addLast(n);
                } else if (choice == 2) {
                    operations.append("expectedDeque.size();\n");
                    int expectedSize = expectedDeque.size();
                    operations.append("testingDeque.size();\n");
                    int testingSize = testingDeque.size();
                    assertEquals("size() failed on iteration " + i, expectedSize, testingSize);
                } else if (choice == 3) {
                    if (expectedDeque.isEmpty()) {
                        i -= 1;
                        continue;
                    }
                    operations.append("expectedDeque.removeFirst();\n");
                    int expectedValue = expectedDeque.removeFirst();
                    operations.append("testingDeque.removeFirst();\n");
                    int testingValue = testingDeque.removeFirst();
                    assertEquals("removeFirst() failed on iteration " + i, expectedValue, testingValue);
                } else if (choice == 4) {
                    if (expectedDeque.isEmpty()) {
                        i -= 1;
                        continue;
                    }
                    operations.append("expectedDeque.removeLast();\n");
                    int expectedValue = expectedDeque.removeLast();
                    operations.append("testingDeque.removeLast();\n");
                    int testingValue = testingDeque.removeLast();
                    assertEquals("removeLast() failed on iteration " + i, expectedValue, testingValue);
                } else {
                    if (expectedDeque.isEmpty()) {
                        i -= 1;
                        continue;
                    }
                    int maxSize = expectedDeque.size();
                    int index = random.nextInt(maxSize);
                    operations.append("expectedDeque.get(").append(index).append(");\n");
                    int expectedValue = expectedDeque.get(index);
                    operations.append("testingDeque.get(").append(index).append(");\n");
                    int testingValue = testingDeque.get(index);
                    assertEquals("get(" + index + ") failed on iteration " + i, expectedValue, testingValue);
                }
            }
        } catch (Throwable e) {
            System.err.append(operations);
            throw e;
        }
    }
}
