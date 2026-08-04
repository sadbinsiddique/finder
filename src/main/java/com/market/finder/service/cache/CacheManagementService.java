package com.market.finder.service.cache;

import java.util.List;

public interface CacheManagementService extends CacheInspectorService, CacheOperatorService, CacheBackupService {

    record CacheDetailDTO(
            String name,
            int keyCount,
            String nativeStoreType,
            List<String> keys
    ) {
        public String getName() { return name; }
        public int getKeyCount() { return keyCount; }
        public String getNativeStoreType() { return nativeStoreType; }
        public List<String> getKeys() { return keys; }
    }
}
