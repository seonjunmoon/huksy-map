package deque;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** Performs some basic linked deque tests. */
public class LinkedDequeTest {

    /** Adds a few strings to the deque, checking isEmpty() and size() are correct. */
    @Test
    public void addIsEmptySizeTest() {
        LinkedDeque<String> lld = new LinkedDeque<>();
        assertTrue(lld.isEmpty());

        lld.addFirst("front");
        assertEquals(1, lld.size());
        assertFalse(lld.isEmpty());

        lld.addLast("middle");
        assertEquals(2, lld.size());

        lld.addLast("back");
        assertEquals(3, lld.size());
    }

    /** Adds an item, then removes an item, and ensures that the deque is empty afterwards. */
    @Test
    public void addRemoveTest() {
        LinkedDeque<Integer> lld = new LinkedDeque<>();
        assertTrue(lld.isEmpty());

        lld.addFirst(10);
        assertFalse(lld.isEmpty());

        lld.removeFirst();
        assertTrue(lld.isEmpty());
    }

    /** Adds an item or removes an item when the deque is empty. */
    @Test
    public void addRemoveTest2() {
        LinkedDeque<Integer> lld = new LinkedDeque<>();

        lld.addLast(10);
        assertFalse(lld.isEmpty());

        lld.removeFirst();
        assertTrue(lld.isEmpty());
        lld.removeFirst();
        assertTrue(lld.isEmpty());
    }

    /** Gets an item at the given index, and  when it's empty. */
    @Test
    public void getTest() {
        LinkedDeque<Integer> lld = new LinkedDeque<>();
        assertTrue(lld.isEmpty());
        assertEquals(null, lld.get(0));
        lld.addFirst(5);
        lld.addLast(20);
        lld.addFirst(10);
        assertEquals(10, (long) (lld.get(0)));
        lld.addLast(30);
        lld.addLast(40);
        lld.addLast(50);
        assertEquals(40, (long) lld.get(4));
        lld.removeFirst();
        assertEquals(50, (long) lld.get(4));
        assertEquals(40, (long) lld.get(3));
        lld.removeLast();
        assertEquals(null, (lld.get(8)));
        assertEquals(40, (long) lld.get(3));
    }
}
