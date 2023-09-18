public class LinkedListDeque<T> implements Deque<T> {
    private IntNode sentinel;
    private int size;

    private class IntNode {
        IntNode prev;
        T item;
        IntNode next;

        public IntNode(IntNode prevNode, T thing, IntNode nextNode) {
            prev = prevNode;
            item = thing;
            next = nextNode;
        }

        public IntNode(IntNode prevNode, IntNode nextNode) {
            prev = prevNode;
            item = (T) Integer.valueOf(77);
            next = nextNode;
        }
    }

    @Override
    public void addFirst(T content) {
        IntNode oneNode = new IntNode(sentinel, content, sentinel.next);
        sentinel.next.prev = oneNode;
        sentinel.next = oneNode;
        size++;
    }

    @Override
    public void addLast(T content) {
        IntNode oneNode = new IntNode(sentinel.prev, content, sentinel);
        sentinel.prev = oneNode;
        oneNode.prev.next = oneNode;
        size++;
    }

    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        IntNode p = sentinel;
        while (p.next != sentinel) {
            p = p.next;
            System.out.print(p.item + " ");
        }
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T returnValue = sentinel.next.item;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        size--;
        return returnValue;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T returnValue = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        size--;
        return returnValue;
    }

    @Override
    public T get(int index) {
        IntNode p = sentinel.next;
        while (index > 0) {
            p = p.next;
            index--;
        }
        return p.item;
    }

    public LinkedListDeque() {
        sentinel = new IntNode(null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public T getRecursive(int index) {
        if (index == 0) {
            return sentinel.next.item;
        } else {
            return getRecursiveHelper(index - 1, sentinel.next.next);
        }
    }

    private T getRecursiveHelper(int index, IntNode p) {
        if (index == 0) {
            return p.item;
        } else {
            return getRecursiveHelper(index - 1, p.next);
        }
    }
}
