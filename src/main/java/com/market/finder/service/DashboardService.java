package com.market.finder.service;

import java.util.Map;
public interface DashboardService {
    /**
     * Returns system statistics as a map of label→count.
     */
    Map<String, Long> getSystemStats();
}
