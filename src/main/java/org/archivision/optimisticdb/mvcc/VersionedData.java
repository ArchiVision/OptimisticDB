package org.archivision.optimisticdb.mvcc;

import java.util.Objects;

/**
 * This class represents the versioned data in the database.
 * It stores the value and the version number of each data item.
 *
 * @param <V> the type of value stored in this class
 */
public class VersionedData<V> {

    private final int version;
    private final V value;

    public VersionedData(int version, V value) {
        this.version = version;
        this.value = value;
    }

    public int getVersion() {
        return version;
    }

    public V getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VersionedData<?> that = (VersionedData<?>) o;
        return version == that.version && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, value);
    }
}
