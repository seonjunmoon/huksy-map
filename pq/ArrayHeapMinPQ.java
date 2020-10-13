package pq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {

    private PriorityNode node;
    private ArrayList<PriorityNode> contents;
    private HashMap<T, Integer> map;
    private int size;

    public ArrayHeapMinPQ() {
        this.contents = new ArrayList<>();
        this.map = new HashMap<>();
    }

    /*
    Here's a helper method and a method stub that may be useful. Feel free to change or remove
    them, if you wish.
     */

    /**
     * A helper method to create arrays of T, in case you're using an array to represent your heap.
     * You shouldn't need this if you're using an ArrayList instead.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArray(int newCapacity) {
        return (T[]) new Object[newCapacity];
    }

    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode nodeA = contents.get(a);
        PriorityNode nodeB = contents.get(b);
        contents.set(a, nodeB);
        contents.set(b, nodeA);
        map.replace(nodeA.item, b);
        map.replace(nodeB.item, a);
    }

    /**
     * Adds an item with the given priority value.
     * Assumes that item is never null.
     * Runs in O(log N) time (except when resizing).
     *
     * @throws IllegalArgumentException if item is already present in the PQ
     */
    @Override
    public void add(T item, double priority) {
        if (!contains(item)) {
            PriorityNode newNode = new PriorityNode(item, priority);
            contents.add(newNode);
            map.put(item, contents.size() - 1);
            if (contents.size() > 1 && priority < contents.get((contents.size() - 2) / 2).getPriority()) {
                swim(contents.size() - 1);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returns true if the PQ contains the given item; false otherwise.
     * Runs in O(log N) time.
     */
    @Override
    public boolean contains(T item) {
        if (map.containsKey(item)) {
            return true;
        }
        return false;
    }

    /**
     * Returns the item with the smallest priority.
     * Runs in O(log N) time.
     *
     * @throws NoSuchElementException if the PQ is empty
     */
    @Override
    public T getSmallest() {
        if (contents.size() == 0) {
            return null;
        }
        return contents.get(0).item;
    }

    /**
     * Removes and returns the item with the smallest priority.
     * Runs in O(log N) time (except when resizing).
     *
     * @throws NoSuchElementException if the PQ is empty
     */
    @Override
    public T removeSmallest() {
        if (contents.size() == 0) {
            throw new NoSuchElementException();
        }
        else {
            T min = contents.get(0).item;
            swap(0, contents.size() - 1);
            contents.remove(contents.size() - 1);
            map.remove(min);
            if (contents.size() != 0) {
                sink(0);
            }
            return min;
        }
    }

    /**
     * Changes the priority of the given item.
     * Runs in O(log N) time.
     *
     * @throws NoSuchElementException if the item is not present in the PQ
     */
    @Override
    public void changePriority(T item, double priority) {
            if (map.containsKey(item)) {
                double oldPriority = contents.get(map.get(item)).priority;
                contents.get(map.get(item)).priority = priority;
                if (oldPriority < priority) {
                    sink(map.get(item));
                } else if (contents.size() > 1 && priority < contents.get((contents.size() - 1) / 2).getPriority()) {
                    swim(map.get(item));
                }
            }
    }

    /**
     * Returns the number of items in the PQ.
     * Runs in O(log N) time.
     */
    @Override
    public int size() {
        return contents.size();
    }

    private void swim(int index) {
        int parent = (index -1) / 2;
        while (contents.get(parent).getPriority() > contents.get(index).getPriority()) {
            swap(parent, index);
            index = parent;
            parent = (index -1) / 2;
        }
    }

    private void sink(int index) {
        while (2 * index + 1 <= contents.size()) {
            int rightIndex = 2 * index + 2;
            int leftIndex = 2 * index + 1;
            if (leftIndex < contents.size() && rightIndex < contents.size() &&
                    contents.get(rightIndex).getPriority() < contents.get(leftIndex).getPriority()) {
                leftIndex = rightIndex;
            }
            if (leftIndex > contents.size() - 1) {
                return;
            }
            if (contents.get(leftIndex).getPriority() >= contents.get(index).getPriority()) {
                return;
            }
            swap(index, leftIndex);
            index = leftIndex;
        }
    }

    private class PriorityNode {
        private T item;
        private double priority;

        PriorityNode(T e, double p) {
            this.item = e;
            this.priority = p;
        }
        
        T getItem() {
            return item;
        }

        double getPriority() {
            return priority;
        }

        void setPriority(double priority) {
            this.priority = priority;
        }
    }
}