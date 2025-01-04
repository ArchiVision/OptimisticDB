package org.archivision.optimisticdb.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.archivision.optimisticdb.api.OptimisticDBImpl;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing optimistic database operations.
 * Provides endpoints to interact with the OptimisticDB implementation.
 * <p>
 * This controller exposes methods to store, retrieve, update, and delete data,
 * as well as commit changes to the database.
 * </p>
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
@RestController
@RequestMapping("/db")
@RequiredArgsConstructor
@Slf4j
public class OptimisticDBController<K, V> {
    private final OptimisticDBImpl<K, V> optimisticDB;

    /**
     * Stores or updates the value associated with the specified key.
     *
     * @param key the key to associate the value with
     * @param value the value to store
     */
    @PostMapping("/put")
    public void put(@RequestParam K key, @RequestParam V value) {
        log.info("Received PUT request for key: {}, value: {}", key, value);
        optimisticDB.put(key, value);
        log.info("Data successfully stored or updated for key: {}", key);
    }

    /**
     * Retrieves the latest value associated with the specified key.
     *
     * @param key the key whose value is to be retrieved
     * @return the value associated with the key
     */
    @GetMapping("/get")
    public V get(@RequestParam K key) {
        log.info("Received GET request for key: {}", key);
        V value = optimisticDB.get(key);
        log.info("Retrieved value for key: {}: {}", key, value);
        return value;
    }

    /**
     * Retrieves a specific version of the value associated with the specified key.
     *
     * @param key the key whose value is to be retrieved
     * @param version the version of the value to retrieve
     * @return the value associated with the key and version
     */
    @GetMapping("/getVersion")
    public V getVersion(@RequestParam K key, @RequestParam int version) {
        log.info("Received GET request for key: {} and version: {}", key, version);
        V value = optimisticDB.getVersion(key, version);
        log.info("Retrieved value for key: {}, version: {}: {}", key, version, value);
        return value;
    }

    /**
     * Deletes the value associated with the specified key.
     *
     * @param key the key whose associated value is to be deleted
     */
    @DeleteMapping("/delete")
    public void delete(@RequestParam K key) {
        log.info("Received DELETE request for key: {}", key);
        optimisticDB.delete(key);
        log.info("Successfully deleted value for key: {}", key);
    }

    /**
     * Commits the current changes to the database, creating a new version.
     */
    @PostMapping("/commit")
    public void commit() {
        // TODO: Is used to move data from in memory to in drive. Don't know how to make commitment rn
        log.info("Received COMMIT request.");
        optimisticDB.commit();
        log.info("Changes committed successfully.");
    }
}
