package com.market.finder.service;

import com.market.finder.entity.Gradebook;
import com.market.finder.entity.GradebookId;
import com.market.finder.service.base.BaseService;

public interface GradebookService extends BaseService<Gradebook, GradebookId> {
    boolean existsById(GradebookId id);
}
