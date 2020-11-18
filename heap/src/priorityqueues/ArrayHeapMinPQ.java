package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 1;
    List<PriorityNode<T>> items;
    private Map<T, Integer> map;
    private int size;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        for (int i = 0; i < START_INDEX; i++) {
            items.add(null);
        }
        map = new HashMap<>();
        size = 0;
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> nodeAtA = items.get(a);
        items.set(a, items.get(b));
        items.set(b, nodeAtA);
        map.put(items.get(a).getItem(), a);
        map.put(items.get(b).getItem(), b);
    }

    @Override
    public void add(T item, double priority) {
        if (item == null || this.contains(item)) {
            throw new IllegalArgumentException();
        }
        items.add(new PriorityNode<>(item, priority));
        size = size + 1;
        int currentNode = size;
        map.put(item, currentNode);
        int index = perculateUp(currentNode, priority);
    }

    private int perculateUp(int currentNode, double priority) {
        while (currentNode > 1 && items.get(currentNode / 2).getPriority() > priority) {
            swap(currentNode / 2, currentNode);
            currentNode = currentNode / 2;
        }
        return currentNode;
    }

    @Override
    public boolean contains(T item) {
        return map.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return items.get(1).getItem();
    }

    @Override
    public T removeMin() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        T result = items.get(1).getItem();
        this.swap(1, items.size() - 1);
        items.remove(items.size() - 1);
        map.remove(result);
        size = size - 1;
        if (size > 0) {
            int index = percolateDown(1);
        }
        return result;
    }

    private int percolateDown(int currentNode) {
        while (2 * currentNode <= size) {
            int childNode = 2 * currentNode;
            if (childNode < size && items.get(childNode).getPriority() > items.get(childNode + 1).getPriority()) {
                childNode = childNode + 1;
            }
            if (items.get(childNode).getPriority() < items.get(currentNode).getPriority()) {
                swap(childNode, currentNode);
                currentNode = childNode;
            } else {
                break;
            }
        }
        return currentNode;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!this.contains(item)) {
            throw new NoSuchElementException();
        }
        int index = map.get(item);
        double prev = items.get(index).getPriority();
        items.get(index).setPriority(priority);
        int newIndex;
        newIndex = perculateUp(index, priority);
        newIndex = percolateDown(index);
    }

    @Override
    public int size() {
        return size;
    }
}
