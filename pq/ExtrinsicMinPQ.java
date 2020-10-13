package pq;

import java.util.NoSuchElementException;

/**
 * Priority queue where objects have a priority that is provided extrinsically,
 * i.e., priorities are supplied as an argument during insertion and can be changed
 * using the changePriority method. Cannot contain duplicate items.
 */
public interface ExtrinsicMinPQ<T> {

    /**
     * Adds an item with the given priority value.
     * Assumes that item is never null.
     * @throws IllegalArgumentException if item is already present in the PQ
     */
    void add(T item, double priority);

    /** Returns true if the PQ contains the given item; false otherwise. */
    boolean contains(T item);

    /**
     * Returns the item with the smallest priority.
     * @throws NoSuchElementException if the PQ is empty
     */
    T getSmallest();

    /**
     * Removes and returns the item with the smallest priority.
     * @throws NoSuchElementException if the PQ is empty
     */
    T removeSmallest();

    /**
     * Changes the priority of the given item.
     * @throws NoSuchElementException if the item is not present in the PQ
     */
    void changePriority(T item, double priority);

    /** Returns the number of items in the PQ. */
    int size();

    /** Returns true if the PQ is empty; false otherwise. */
    default boolean isEmpty() {
        return size() == 0;
    }
}
