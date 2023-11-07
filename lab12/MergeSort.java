import edu.princeton.cs.algs4.Queue;

public class MergeSort {
    /**
     * Removes and returns the smallest item that is in q1 or q2.
     *
     * The method assumes that both q1 and q2 are in sorted order, with the smallest item first. At
     * most one of q1 or q2 can be empty (but both cannot be empty).
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      The smallest item that is in q1 or q2.
     */
    private static <Item extends Comparable> Item getMin(
            Queue<Item> q1, Queue<Item> q2) {
        if (q1.isEmpty()) {
            return q2.dequeue();
        } else if (q2.isEmpty()) {
            return q1.dequeue();
        } else {
            // Peek at the minimum item in each queue (which will be at the front, since the
            // queues are sorted) to determine which is smaller.
            Comparable q1Min = q1.peek();
            Comparable q2Min = q2.peek();
            if (q1Min.compareTo(q2Min) <= 0) {
                // Make sure to call dequeue, so that the minimum item gets removed.
                return q1.dequeue();
            } else {
                return q2.dequeue();
            }
        }
    }

    /** Returns a queue of queues that each contain one item from items. */
    private static <Item extends Comparable> Queue<Queue<Item>>
            makeSingleItemQueues(Queue<Item> items) {
        Queue<Queue<Item>> itemQueues = new Queue<>();
        for (Item x : items) {
            Queue<Item> nextItem = new Queue<>();
            nextItem.enqueue(x);
            itemQueues.enqueue(nextItem);
        }
        return itemQueues;
    }

    /**
     * Returns a new queue that contains the items in q1 and q2 in sorted order.
     *
     * This method should take time linear in the total number of items in q1 and q2.  After
     * running this method, q1 and q2 will be empty, and all of their items will be in the
     * returned queue.
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      A Queue containing all of the q1 and q2 in sorted order, from least to
     *              greatest.
     *
     */
    private static <Item extends Comparable> Queue<Item> mergeSortedQueues(
            Queue<Item> q1, Queue<Item> q2) {
        Queue<Item> sortedQueue = new Queue<>();
        while (!q1.isEmpty() || !q2.isEmpty()) {
            sortedQueue.enqueue(getMin(q1, q2));
        }
        return sortedQueue;
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> mergeSort(
            Queue<Item> items) {
        //        if (items.size() == 1) {
        //            return items;
        //        }
        //        Queue<Item> q1 = new Queue<>();
        //        Queue<Item> q2 = new Queue<>();
        //        int itemSize = items.size() / 2;
        //        for (int i = 0; i < itemSize; i++) {
        //            q1.enqueue(items.dequeue());
        //        }
        //        while (!items.isEmpty()) {
        //            q2.enqueue(items.dequeue());
        //        }
        //        q1 = mergeSort(q1);
        //        q2 = mergeSort(q2);
        //        Queue<Item> returnQueue = mergeSortedQueues(q1, q2);
        //        return returnQueue;

        if (items.size() <= 1) {
            return items;
        }
        Queue<Queue<Item>> tmp = makeSingleItemQueues(items);
        while (tmp.size() != 1) {
            Queue<Queue<Item>> tmpp = new Queue<>();
            while (!tmp.isEmpty()) {
                Queue<Item> q1 = tmp.dequeue();
                Queue<Item> q2 = tmp.isEmpty() ? new Queue<>() : tmp.dequeue();
                tmpp.enqueue(mergeSortedQueues(q1, q2));
            }
            tmp = tmpp;
        }
        return tmp.dequeue();
    }

    public static void main(String[] args) {
        Queue<Integer> numQueue = new Queue<>();
        numQueue.enqueue(6);
        numQueue.enqueue(5);
        numQueue.enqueue(7);
        numQueue.enqueue(2);
        numQueue.enqueue(4);

        Queue<Integer> sortedNumQueue = mergeSort(numQueue);
        System.out.println(numQueue);
        System.out.println(sortedNumQueue);

        //        Queue<Integer> q1 = new Queue<>();
        //        Queue<Integer> q2 = new Queue<>();
        //        q1.enqueue(2);
        //        q2.enqueue(3);
        //        q1.enqueue(4);
        //        q2.enqueue(5);
        //        q1.enqueue(6);
        //        q2.enqueue(7);
        //        Queue<Integer> q3= mergeSortedQueues(q1, q2);
        //        System.out.println(q3);
    }
}
