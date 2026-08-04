package com.market.finder.service.cache;

import java.util.Map;

public interface CacheBackupService {
    Map<String, Object> exportCacheData();
    String exportCacheDataAsJson();
    boolean importCacheDataFromJson(String jsonContent);
}
