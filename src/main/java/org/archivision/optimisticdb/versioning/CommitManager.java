package org.archivision.optimisticdb.versioning;

import lombok.RequiredArgsConstructor;
import org.archivision.optimisticdb.mvcc.MVCCManager;
import org.archivision.optimisticdb.mvcc.VersionedData;
import org.archivision.optimisticdb.mvcc.InMemoryStorage;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CommitManager<K, V> {
    private final MVCCManager<K, V> mvccManager;
    private final InMemoryStorage inMemoryStorage;

    public void commit() {
        Map<K, VersionedData<V>> allData = inMemoryStorage.getAll();
        for (Map.Entry<K, VersionedData<V>> entry : allData.entrySet()) {
            // TODO: sinking into real DB storage
        }
    }
}
