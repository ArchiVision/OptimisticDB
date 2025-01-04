package org.archivision.optimisticdb.mvcc;

/**
 * The {@code OptimisticLockingException} is a custom exception that is thrown when
 * an optimistic locking conflict occurs in the database.
 * <p>
 * Optimistic locking is used in scenarios where multiple operations try to modify
 * the same data concurrently. This exception is thrown when an attempt is made
 * to update data with an incorrect version, indicating a conflict between concurrent operations.
 * </p>
 *
 * <p>
 * This exception is a subclass of {@link RuntimeException}, meaning it is an unchecked exception
 * and does not need to be explicitly declared or handled.
 * </p>
 *
 * Example usage:
 * <pre>
 * if (existingVersion != expectedVersion) {
 *     throw new OptimisticLockingException("Version conflict for key: " + key);
 * }
 * </pre>
 *
 * @see RuntimeException
 */
public class OptimisticLockingException extends RuntimeException {
    /**
     * Constructs a new {@code OptimisticLockingException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public OptimisticLockingException(String message) {
        super(message);
    }
}
