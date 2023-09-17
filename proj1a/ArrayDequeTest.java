import static org.junit.Assert.*;

import edu.princeton.cs.algs4.Interval1D;
import org.junit.Test;

public class ArrayDequeTest {
    @Test
    public void testResize() {
        ArrayDeque<Integer> item = new ArrayDeque<>();
        ArrayDeque<Integer> new_item = new ArrayDeque();
    }

    @Test
    public void testAddFirst() {
        Integer[] first = {1, 2, 3, 4, 5, 6,};
        ArrayDeque<Integer> new_item = new ArrayDeque<>(first);
        new_item.addFirst(111);
    }

    @Test
    public void testAddLast() {
        Integer[] first = {1, 2, 3, 4, 5, 6, 7, 8};
        ArrayDeque<Integer> new_item = new ArrayDeque<>(first);
        new_item.addLast(111);
    }

    @Test
    public void testPrintDeque() {
        Integer[] first = {1, 2, 3, 4, 5, 6, 7, 8};
        ArrayDeque<Integer> new_item = new ArrayDeque<>(first);
        new_item.printDeque();
    }

    public static void main(String[] args){
        Integer[] first = {1, 2, 3, 4, 5, 6, 7, 8};
        ArrayDeque<Integer> new_item = new ArrayDeque<>(first);
        new_item.upsize();
    }
}
