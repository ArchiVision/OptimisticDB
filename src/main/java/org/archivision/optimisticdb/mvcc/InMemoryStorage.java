package org.archivision.optimisticdb.mvcc;

import java.util.HashMap;
import java.util.Map;

/**
 * InMemoryStorage stores data with versioning in memory.
 * The data is stored in a map where the key maps to a VersionedData object.
 * This class is used by MVCCManager to manage versions of data.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 */
public class InMemoryStorage<K, V> {

    private final Map<K, VersionedData<V>> storage;

    public InMemoryStorage() {
        this.storage = new HashMap<>();
    }

    /**
     * Gets the versioned data for a given key.
     *
     * @param key the key to retrieve data for
     * @return the versioned data, or {@code null} if not found
     */
    public VersionedData<V> get(K key) {
        return storage.get(key);
    }

    /**
     * Puts a value into the storage with a version.
     * The value is stored in a VersionedData object.
     *
     * @param key the key for the value
     * @param value the value to store
     */
    public void put(K key, V value) {
        storage.compute(key, (k, existingData) -> {
            if (existingData == null) {
                return new VersionedData<>(1, value);
            } else {
                int newVersion = existingData.getVersion() + 1;
                return new VersionedData<>(newVersion, value);
            }
        });
    }

    /**
     * Removes a key and its associated value from the storage.
     *
     * @param key the key to remove
     */
    public void remove(K key) {
        storage.remove(key);
    }

    /**
     * Checks if the storage contains a key.
     *
     * @param key the key to check
     * @return {@code true} if the key exists in the storage, otherwise {@code false}
     */
    public boolean contains(K key) {
        return storage.containsKey(key);
    }

    /**
     * Gets all data from the storage.
     *
     * @return a map of all stored key-value pairs
     */
    public Map<K, VersionedData<V>> getAll() {
        return storage;
    }
}
