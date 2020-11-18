package disjointsets;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    private final HashMap<T, Integer> map;
    List<Integer> pointers;
    private int size;

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {
        this.map = new HashMap<>();
        this.pointers = new ArrayList<>();
        this.size = 0;
    }

    @Override
    public void makeSet(T item) {
        this.map.put(item, this.size);
        this.size++;
        this.pointers.add(-1);
    }

    @Override
    public int findSet(T item) {
        if (!map.containsKey((item))) {
            throw new IllegalArgumentException();
        }
        int root = map.get(item);
        while (pointers.get(root) > -1) {
            root = pointers.get(root);
        }
        int k = map.get(item);
        while (pointers.get(k) > 0) {
            int parent = pointers.get(k);
            pointers.set(k, root);
            k = parent;
        }
        return root;
    }

    @Override
    public boolean union(T item1, T item2) {
        if (!map.containsKey(item1) || !map.containsKey(item2)) {
            throw new IllegalArgumentException();
        }
        int first = findSet(item1);
        int second = findSet(item2);
        if (first == second) {
            return false;
        }
        int weightA = -1 * pointers.get(first);
        int weightB = -1 * pointers.get(second);
        if (weightA < weightB) {
            pointers.set(second, -1 * Math.abs(weightA + weightB));
            pointers.set(findSet(item1), findSet(item2));
        } else {
            pointers.set(first, -1 * Math.abs(weightA + weightB));
            pointers.set(findSet(item2), findSet(item1));
        }
        return true;
    }
}
