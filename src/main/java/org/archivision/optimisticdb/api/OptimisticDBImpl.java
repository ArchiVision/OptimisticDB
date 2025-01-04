package org.archivision.optimisticdb.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.archivision.optimisticdb.mvcc.MVCCManager;
import org.archivision.optimisticdb.mvcc.VersionedData;
import org.archivision.optimisticdb.versioning.CommitManager;
import org.springframework.stereotype.Component;

/**
 * OptimisticDBImpl provides an implementation of a versioned database
 * with optimistic locking and Multi-Version Concurrency Control (MVCC) support.
 * This class interacts with the {@link MVCCManager} and {@link CommitManager} to handle
 * data operations such as put, get, getVersion, delete, and commit, ensuring consistent
 * handling of concurrent operations in a multithreaded environment.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OptimisticDBImpl<K, V> implements OptimisticDB<K, V> {
    private final MVCCManager<K, V> mvccManager;
    private final CommitManager<K, V> commitManager;

    /**
     * Stores or updates the value associated with the specified key.
     * If the key already exists, it checks the current version and applies
     * the optimistic locking approach to ensure no version conflict.
     *
     * @param key the key to associate the value with
     * @param value the value to store or update
     */
    @Override
    public void put(K key, V value) {
        VersionedData<V> currentVersion = mvccManager.get(key);
        int version = (currentVersion != null) ? currentVersion.getVersion() : 0;
        log.info("Putting value for key: {}. Version: {}", key, version);
        mvccManager.put(key, value, version);
    }

    /**
     * Retrieves the latest value associated with the specified key.
     *
     * @param key the key whose value is to be retrieved
     * @return the value associated with the key, or null if not found
     */
    @Override
    public V get(K key) {
        VersionedData<V> versionedData = mvccManager.get(key);
        V value = (versionedData != null) ? versionedData.getValue() : null;
        log.info("Getting value for key: {}. Found: {}", key, value != null ? "Yes" : "No");
        return value;
    }

    /**
     * Retrieves a specific version of the value associated with the specified key.
     *
     * @param key the key whose value is to be retrieved
     * @param version the version of the value to retrieve
     * @return the value associated with the key and version, or null if not found
     */
    @Override
    public V getVersion(K key, int version) {
        VersionedData<V> versionedData = mvccManager.get(key);
        V value = (versionedData != null && versionedData.getVersion() == version)
                ? versionedData.getValue() : null;
        log.info("Getting version {} for key: {}. Found: {}", version, key, value != null ? "Yes" : "No");
        return value;
    }

    /**
     * Deletes the value associated with the specified key.
     *
     * @param key the key whose associated value is to be deleted
     */
    @Override
    public void delete(K key) {
        log.info("Deleting key: {}", key);
        mvccManager.delete(key);
    }

    /**
     * Commits all changes made to the database, finalizing the changes
     * and ensuring that they are persisted in the system.
     */
    @Override
    public void commit() {
        log.info("Committing all changes.");
        commitManager.commit();
    }
}
