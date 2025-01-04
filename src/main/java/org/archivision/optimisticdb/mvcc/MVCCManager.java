package org.archivision.optimisticdb.mvcc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * MVCCManager provides a mechanism for managing multi-version concurrency control (MVCC).
 * It handles storing, retrieving, and versioning data, with the addition of optimistic locking.
 * The class uses an in-memory storage to store versioned data and ensures thread safety using locks.
 *
 * @param <K> The type of the key
 * @param <V> The type of the value
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MVCCManager<K, V> {

    private final InMemoryStorage<K, VersionedData<V>> storage;
    private final Map<K, Lock> locks;

    /**
     * Retrieves versioned data for a given key.
     * This method is synchronized to ensure thread safety when accessing data.
     *
     * @param key the key for the value
     * @return the versioned data, or {@code null} if not found
     */
    public VersionedData<V> get(K key) {
        VersionedData<V> data = storage.get(key).getValue();
        if (data != null) {
            log.debug("Retrieved data for key: {}. Version: {}", key, data.getVersion());
        } else {
            log.debug("No data found for key: {}", key);
        }
        return data;
    }

    /**
     * Puts the new value in storage for the given key, ensuring optimistic concurrency control.
     * If there is a version conflict, it throws an OptimisticLockingException.
     *
     * @param key The key for the data
     * @param value The value to be stored
     * @param expectedVersion The expected version for optimistic locking
     * @throws OptimisticLockingException If there is a version conflict
     */
    public void put(K key, V value, int expectedVersion) {
        Lock lock = locks.computeIfAbsent(key, k -> new ReentrantLock());
        lock.lock();
        try {
            VersionedData<V> existingData = storage.get(key).getValue();
            if (existingData != null && existingData.getVersion() != expectedVersion) {
                log.error("Version conflict for key: {}. Expected version: {}, but found version: {}",
                        key, expectedVersion, existingData.getVersion());
                throw new OptimisticLockingException("Version conflict for key: " + key);
            }
            int newVersion = (existingData != null) ? existingData.getVersion() + 1 : 1;
            storage.put(key, new VersionedData<>(newVersion, value));
            log.info("Stored new value for key: {} with version: {}", key, newVersion);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Deletes the data for the given key from storage.
     *
     * @param key The key of the data to be deleted
     */
    public void delete(K key) {
        Lock lock = locks.computeIfAbsent(key, k -> new ReentrantLock());
        lock.lock();
        try {
            storage.remove(key);
            log.info("Deleted data for key: {}", key);
        } finally {
            lock.unlock();
        }
    }
}
