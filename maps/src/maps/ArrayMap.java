package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.NoSuchElementException;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ArrayMap<K, V> extends AbstractIterableMap<K, V> {
    private int size;
    private static final int DEFAULT_INITIAL_CAPACITY = 10;
    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    SimpleEntry<K, V>[] entries;

    // You may add extra fields or helper methods though!

    public ArrayMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public ArrayMap(int initialCapacity) {
        this.size = 0;
        this.entries = this.createArrayOfEntries(initialCapacity);
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code Entry<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     */
    @SuppressWarnings("unchecked")
    private SimpleEntry<K, V>[] createArrayOfEntries(int arraySize) {
        /*
        It turns out that creating arrays of generic objects in Java is complicated due to something
        known as "type erasure."

        We've given you this helper method to help simplify this part of your assignment. Use this
        helper method as appropriate when implementing the rest of this class.

        You are not required to understand how this method works, what type erasure is, or how
        arrays and generics interact.
        */
        return (SimpleEntry<K, V>[]) (new SimpleEntry[arraySize]);
    }

    // return the value that is associated with the key.
    @Override
    public V get(Object key) {
        for (SimpleEntry<K, V> entry: entries) {
            if (entry != null && Objects.equals(entry.getKey(), key)) {
                return entry.getValue();
            }
        }
        return null;
    }


    @Override
    public V put(K key, V value) {
        if (this.containsKey(key)) {
            V result = this.get(key);
            for (SimpleEntry<K, V> entry: entries) {
                if (entry != null && Objects.equals(entry.getKey(), key)) {
                    entry.setValue(value);
                }
            }
            return result;
        } else {
            if (size == entries.length) {
                resize(size * 2);
            }
            entries[size] = new SimpleEntry<K, V>(key, value);
            size = size + 1;
            return null;
        }
    }

    // resize the Map to a size of given integer capacity
    private void resize(int capacity) {
        SimpleEntry<K, V>[] newMap = createArrayOfEntries(capacity);
        for (int i = 0; i < size; i++) {
            newMap[i] = entries[i];
        }
        entries = newMap;
    }

    @Override
    public V remove(Object key) {
        V result;
        for (int i = 0; i < size; i++) {
            if (Objects.equals(entries[i].getKey(), key)) {
                result = entries[i].getValue();
                entries[i] = entries[size - 1];
                entries[size - 1] = null;
                size = size - 1;
                return result;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            entries[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        for (int i = 0; i < entries.length; i++) {
            if (entries[i] != null && Objects.equals(entries[i].getKey(), key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: you won't need to change this method (unless you add more constructor parameters)
        return new ArrayMapIterator<>(this.entries, size);
    }

    private static class ArrayMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private final SimpleEntry<K, V>[] entries;
        private int current;
        private int size;
        // You may add more fields and constructor parameters

        public ArrayMapIterator(SimpleEntry<K, V>[] entries, int size) {
            this.entries = entries;
            current = 0;
            this.size = size;
        }

        @Override
        public boolean hasNext() {
            return (current < this.size);
        }

        @Override
        public Map.Entry<K, V> next() {
            current += 1;
            if (current - 1 >= this.size) {
                throw new NoSuchElementException();
            }
            return entries[current - 1];
        }
    }
}
