package com.market.finder.service.gradebook;

import com.market.finder.entity.Gradebook;
import com.market.finder.entity.GradebookId;
import com.market.finder.repository.GradebookRepository;
import com.market.finder.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class GradebookServiceImpl extends BaseServiceImpl<Gradebook, GradebookId, GradebookRepository> implements GradebookService {

    public GradebookServiceImpl(GradebookRepository gradebookRepository) {
        super(gradebookRepository);
    }

    @Override
    public boolean existsById(GradebookId id) {
        return repository.existsById(id);
    }
}
