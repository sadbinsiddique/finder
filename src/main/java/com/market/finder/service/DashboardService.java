package com.market.finder.service;

import java.util.Map;

/**
 * SRP: Extracts dashboard stat-counting logic into its own service,
 * so AdminController doesn't have to know about every repository.
 */
public interface DashboardService {

    /**
     * Returns system statistics as a map of label→count.
     */
    Map<String, Long> getSystemStats();
}
