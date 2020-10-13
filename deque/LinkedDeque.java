package deque;

public class LinkedDeque<T> implements Deque<T> {
    private int size;
    private Node front;
    private Node back;

    public LinkedDeque() {
        size = 0;
    }

    private class Node {

        public int size;
        private T value;
        private Node next;
        private Node prev;

        Node(T value) {
            this.value = value;
        }
    }

    public void addFirst(T item) {
        Node node = new Node(item);
        node.next = front;
        if (isEmpty()) {
            back = node;
        } else {
            front.prev = node;
        }
        front = node;
        size += 1;
    }

    public void addLast(T item) {
        Node node = new Node(item);
        node.prev = back;
        if (isEmpty()) {
            front = node;
        } else {
            back.next = node;
        }
        back = node;
        size += 1;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        Node node = front;
        front = node.next;
        if (front != null) {
            front.prev = null;
        }
        size -= 1;
        return node.value;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Node node = back;
        back = node.prev;
        if (back != null) {
            back.next = null;
        }
        size -= 1;
        return node.value;
    }

    public T get(int index) {
        if ((index >= size) || (index < 0)) {
            return null;
        }
        Node node = front;
        while (index != 0) {
            if (node.next == null) {
                node.next = back;
            } else {
                node = node.next;
                index--;
            }
        }
        return node.value;
    }

    public int size() {
        return size;
    }
}
