public class ArrayDeque<T> {
    private T[] item;
    private int size;
    private int length;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        item = (T[]) new Object[8];
        size = 0;
        length = 8;
        nextFirst = 3;
        nextLast = 4;
    }

    private int getFirst() {
        int first;
        if (nextFirst == length - 1) {
            first = 0;
        } else {
            first = nextFirst + 1;
        }
        return first;
    }

    private int getLast() {
        int last;
        if (nextLast == 0) {
            last = length - 1;
        } else {
            last = nextLast - 1;
        }
        return last;
    }

    private void upsize() {
        T[] newItem = (T[]) new Object[2 * length];
        int first = getFirst();
        int last = getLast();
        System.arraycopy(item, first, newItem, length / 2, length - first);
        System.arraycopy(item, 0, newItem, length * 3 / 2 - first, first);
        nextFirst = length / 2 - 1;
        nextLast = length * 3 / 2;
        length *= 2;
        item = newItem;
    }

    private void downsize() {
        T[] newItem = (T[]) new Object[length / 2];
        int first = getFirst();
        int last = getLast();
        System.arraycopy(item, first, newItem, length / 8, length / 4);
        nextFirst = length / 8 - 1;
        nextLast = length * 3 / 8 + 1;
        length /= 2;
        item = newItem;
    }

    public void addFirst(T newFirst) {
        if (size == length) {
            upsize();
            item[nextFirst] = newFirst;
            nextFirst--;
        } else {
            item[nextFirst] = newFirst;
            if (nextFirst == 0) {
                nextFirst = length - 1;
            } else {
                nextFirst--;
            }
        }
        size++;
    }

    public void addLast(T newLast) {
        if (size == length) {
            upsize();
            item[nextLast] = newLast;
            nextLast++;
        } else {
            item[nextLast] = newLast;
            if (nextLast == length - 1) {
                nextLast = 0;
            } else {
                nextLast++;
            }
        }
        size++;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int first = getFirst();
        int last = getLast();

        if (first < last) {
            for (int i = first; i <= last; i++) {
                System.out.print(item[i] + " ");
            }
        } else {
            for (int i = first; i < length; i++) {
                System.out.print(item[i] + " ");
            }
            for (int i = 0; i <= last; i++) {
                System.out.print(item[i] + " ");
            }
        }
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        int first = getFirst();
        double useRatio = (double) size / length;
        final int minChangeSize = 16;
        if (useRatio == 0.25 && size >= minChangeSize) {
            downsize();
        }
        T returnValue = item[first];
        item[first] = null;
        nextFirst = first;
        size--;
        return returnValue;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        int last = getLast();
        double useRatio = (double) size / length;
        final int minChangeSize = 16;
        if (useRatio == 0.25 && size >= minChangeSize) {
            downsize();
        }
        T returnValue = item[last];
        item[last] = null;
        nextLast = last;
        size--;
        return returnValue;
    }

    public T get(int index) {
        int first = getFirst();
        int last = getLast();
        index = (index + first) % length;
        if (first < last) {
            if (first <= index && index <= last) {
                return item[index];
            } else {
                return null;
            }
        } else {
            if (index >= first || index <= last) {
                return item[index];
            } else {
                return null;
            }
        }
    }
}
