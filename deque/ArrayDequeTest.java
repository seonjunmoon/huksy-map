package deque;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArrayDequeTest {

    @Test
    public void testTricky() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.addFirst(0);
        assertEquals(0, (int) deque.get(0));

        deque.addLast(1);
        assertEquals(1, (int) deque.get(1));

        deque.addFirst(-1);
        deque.addLast(2);
        assertEquals(2, (int) deque.get(3));

        deque.addLast(3);
        deque.addLast(4);

        // Test that removing and adding back is okay
        assertEquals(-1, (int) deque.removeFirst());
        deque.addFirst(-1);
        assertEquals(-1, (int) deque.get(0));

        deque.addLast(5);
        deque.addFirst(-2);
        deque.addFirst(-3);

        // Test a tricky sequence of removes
        assertEquals(-3, (int) deque.removeFirst());
        assertEquals(5, (int) deque.removeLast());
        assertEquals(4, (int) deque.removeLast());
        assertEquals(3, (int) deque.removeLast());
        assertEquals(2, (int) deque.removeLast());
        // Failing test
        int actual = deque.removeLast();
        assertEquals(1, actual);
    }
}
