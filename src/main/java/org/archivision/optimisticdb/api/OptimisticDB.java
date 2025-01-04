package org.archivision.optimisticdb.api;

import org.springframework.lang.Nullable;

/**
 * Interface representing a versioned database with support for optimistic locking
 * and multi-version concurrency control (MVCC).
 * <p>
 * This database allows storing and retrieving values associated with keys,
 * supports versioning for historical data, and provides mechanisms to
 * commit changes as atomic operations.
 *
 * @param <K> the type of keys maintained by this database
 * @param <V> the type of values maintained by this database
 */
public interface OptimisticDB<K, V> {

    /**
     * Adds or updates a value associated with the specified key.
     * If the key already exists, the value will be updated in the latest version.
     *
     * @param key   the key to associate the value with
     * @param value the value to store
     * @throws NullPointerException if the key or value is null
     */
    void put(K key, V value);

    /**
     * Retrieves the latest value associated with the specified key.
     * If no version exists for the given key, this method returns {@code null}.
     *
     * @param key the key whose latest value is to be retrieved
     * @return the latest value associated with the key, or {@code null} if no such value exists
     * @throws NullPointerException if the key is null
     */
    @Nullable
    V get(K key);

    /**
     * Retrieves a specific version of the value associated with the specified key.
     * If the version does not exist for the given key, this method returns {@code null}.
     *
     * @param key     the key whose value is to be retrieved
     * @param version the version number to retrieve
     * @return the value associated with the key and version, or {@code null} if no such version exists
     * @throws NullPointerException if the key is null
     * @throws IllegalArgumentException if the version is negative
     */
    @Nullable
    V getVersion(K key, int version);

    /**
     * Deletes the specified key and all associated versions.
     * If the key does not exist, this method has no effect.
     *
     * @param key the key to delete
     * @throws NullPointerException if the key is null
     */
    void delete(K key);

    /**
     * Commits all pending changes, creating a new version in the database.
     * <p>
     * This method ensures atomicity of operations performed since the last commit.
     */
    void commit();
}
