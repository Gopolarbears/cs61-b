public class LinkedListDeque<T> {
    private IntNode sentinel;
    private int size;

    public class IntNode {
        IntNode prev;
        T item;
        IntNode next;

        public IntNode(IntNode prev_node, T thing, IntNode next_node) {
            prev = prev_node;
            item = thing;
            next = next_node;
        }

        public IntNode(IntNode prev_node, IntNode next_node) {
            prev = prev_node;
            item = (T) Integer.valueOf(77);
            next =next_node;
        }
    }

    public LinkedListDeque(T content) {
        sentinel = new IntNode(null, null);
        IntNode first_node = new IntNode(sentinel, content, sentinel);
        sentinel.prev = first_node;
        sentinel.next = first_node;
        size = 1;
    }

    public void addFirst(T content) {
        IntNode one_node = new IntNode(sentinel, content, sentinel.next);
        sentinel.next.prev = one_node;
        sentinel.next = one_node;
        size++;
    }

    public void addLast(T content) {
        IntNode one_node = new IntNode(sentinel.prev, content, sentinel);
        sentinel.prev = one_node;
        one_node.prev.next = one_node;
        size++;
    }

    public boolean isEmpty() {
        if (size == 0){
            return true;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        IntNode p = sentinel;
        while (p.next != sentinel) {
            p = p.next;
            System.out.print(p.item + " ");
        }
    }

    public T removeFirst() {
        T return_value = sentinel.next.item;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        size--;
        return return_value;
    }

    public T removeLast() {
        T return_value = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        size--;
        return return_value;
    }

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
    }

    public T getRecursive(int index) {
        if (index == 0) {
            return sentinel.next.item;
        }
        else{
            return getRecursive_helper(index - 1, sentinel.next.next);
        }
    }

    public T getRecursive_helper(int index, IntNode p){
        if (index == 0) {
            return p.item;
        }
        else{
            return getRecursive_helper(index - 1, p.next);
        }
    }
}
