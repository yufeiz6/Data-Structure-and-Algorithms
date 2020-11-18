package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ChainedHashMap<K, V> extends AbstractIterableMap<K, V> {
    private static final double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = 1;
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 5;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 5;

    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */

    AbstractIterableMap<K, V>[] chains;

    // You're encouraged to add extra fields (and helper methods) though!
    private int size;
    private double resizingLoadFactorThreshold;

    public ChainedHashMap() {
        this(DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD, DEFAULT_INITIAL_CHAIN_COUNT, DEFAULT_INITIAL_CHAIN_CAPACITY);
    }

    public ChainedHashMap(double resizingLoadFactorThreshold, int initialChainCount, int chainInitialCapacity) {
        size = 0;
        chains = createArrayOfChains(initialChainCount);
        for (int i = 0; i < initialChainCount; i++) {
            chains[i] = createChain(chainInitialCapacity);
        }
        this.resizingLoadFactorThreshold = resizingLoadFactorThreshold;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code AbstractIterableMap<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     * @see ArrayMap createArrayOfEntries method for more background on why we need this method
     */
    @SuppressWarnings("unchecked")
    private AbstractIterableMap<K, V>[] createArrayOfChains(int arraySize) {
        return (AbstractIterableMap<K, V>[]) new AbstractIterableMap[arraySize];
    }

    /**
     * Returns a new chain.
     *
     * This method will be overridden by the grader so that your ChainedHashMap implementation
     * is graded using our solution ArrayMaps.
     *
     * Note: You do not need to modify this method.
     */
    protected AbstractIterableMap<K, V> createChain(int initialSize) {
        return new ArrayMap<>(initialSize);
    }

    @Override
    public V get(Object key) {
        int hash;
        if (key == null) {
            hash = 0;
        } else {
            hash = key.hashCode();
        }
        hash = Math.abs(hash);
        return chains[hash % chains.length].get(key);
    }

    @Override
    public V put(K key, V value) {
        int hashIndex;
        boolean ifContainsKey = containsKey(key);
        if (key == null) {
            hashIndex = 0;
        } else {
            hashIndex = Math.abs(key.hashCode());
            hashIndex = hashIndex % chains.length;
        }
        V result = chains[hashIndex].put(key, value);
        if (!ifContainsKey) {
            this.size = this.size + 1;
        }
        if (this.size / chains.length > resizingLoadFactorThreshold) {
            resize(chains.length * 2);
        }
        return result;
    }

    //resize the length of chains to be equal to the integer capacity
    private void resize(int capacity) {
        AbstractIterableMap<K, V>[] newChains = createArrayOfChains(capacity);
        for (int i = 0; i < newChains.length; i++) {
            newChains[i] = createChain(DEFAULT_INITIAL_CHAIN_CAPACITY);
        }
        for (int i = 0; i < chains.length; i++) {
            Iterator<Map.Entry<K, V>> iterator = chains[i].iterator();
            while (iterator.hasNext()) {
                Map.Entry<K, V> entry = iterator.next();
                int hashIndex = Math.abs(entry.getKey().hashCode()) % capacity;
                newChains[hashIndex].put(entry.getKey(), entry.getValue());
            }
        }
        chains = newChains;
    }

    @Override
    public V remove(Object key) {
        int hash;
        if (this.containsKey(key)) {
            if (key == null) {
                hash = 0;
            } else {
                hash = key.hashCode();
            }
            hash = Math.abs(hash);
            size = size - 1;
            return chains[hash % chains.length].remove(key);
        }
        return null;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            chains[i] = createChain(DEFAULT_INITIAL_CHAIN_CAPACITY);
        }
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        int hash;
        if (key == null) {
            hash = 0;
        } else {
            hash = key.hashCode();
        }
        hash = Math.abs(hash);
        AbstractIterableMap<K, V> newChains = chains[hash % chains.length];
        if (newChains != null && newChains.containsKey(key)) {
            return true;
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
        return new ChainedHashMapIterator<>(this.chains);
    }

    /*
    See the assignment webpage for tips and restrictions on implementing this iterator.
     */
    private static class ChainedHashMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private AbstractIterableMap<K, V>[] chains;
        private int current;
        private Iterator<Map.Entry<K, V>> iterator;
        // You may add more fields and constructor parameters

        public ChainedHashMapIterator(AbstractIterableMap<K, V>[] chains) {
            this.current = 0;
            this.chains = chains;
            if (chains[current] != null) {
                iterator = chains[current].iterator();
            }
        }

        @Override
        public boolean hasNext() {
            while (current < chains.length) {
                if (iterator.hasNext()) {
                    return true;
                } else {
                    current++;
                    if (current == chains.length) {
                        return false;
                    } else if (chains[current] != null) {
                        iterator = chains[current].iterator();
                    }
                }
            }
            return false;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            } else {
                return iterator.next();
            }
        }
    }
}
