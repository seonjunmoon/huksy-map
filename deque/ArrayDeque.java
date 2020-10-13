package deque;

/** A buggy array implementation of the Deque interface. */
public class ArrayDeque<T> implements Deque<T> {
    private T[] data;
    private int front;
    private int back;
    private int size;

    @SuppressWarnings("unchecked")
    public ArrayDeque() {
        data = (T[]) new Object[8];
        front = 0;
        back = 1;
        size = 0;
    }

    private static int increment(int i, int length) {
        if (i == length - 1) {
            return 0;
        } else {
            return i + 1;
        }
    }

    private static int decrement(int i, int length) {
        if (i == 0) {
            return length - 1;
        } else {
            return i - 1;
        }
    }

    public void addFirst(T item) {
        if (size == data.length) {
            resize(data.length * 2);
        }
        data[front] = item;
        front = decrement(front, data.length);
        size += 1;
    }

    public void addLast(T item) {
        if (size == data.length) {
            resize(data.length * 2);
        }
        data[back] = item;
        back = increment(back, data.length);
        size += 1;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        front = increment(front, data.length);
        T result = data[front];
        data[front] = null;
        size -= 1;
        if (needsDownsize()) {
            resize(data.length / 2);
        }
        return result;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        back = decrement(back, data.length);
        T result = data[back];
        data[back] = null;
        size -= 1;
        if (needsDownsize()) {
            resize(data.length / 2);
        }
        return result;
    }

    public T get(int index) {
        if (index >= size) {
            return null;
        } else {
            int place = front + 1 + index;
            return data[place % data.length];
        }
    }

    public String toString() {
        // We use a StringBuilder since it concatenates strings more efficiently
        // than using += in a loop
        StringBuilder output = new StringBuilder();
        if (size >= 0) {
            int counter = 0;
            int i = increment(front, data.length);
            while (counter < size) {
                output.append(data[i]).append(" ");
                i = increment(i, data.length);
                counter += 1;
            }
        }
        return output.toString();
    }

    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        T[] newData = (T[]) new Object[capacity];
        int i = increment(front, data.length);
        for (int newIndex = 0; newIndex < size; newIndex += 1) {
            newData[newIndex] = data[i];
            i = increment(i, data.length);
        }
        front = newData.length - 1;
        back = size;
        data = newData;
    }

    private boolean needsDownsize() {
        return ((double) size) / data.length < 0.25 && data.length >= 16;
    }
}
