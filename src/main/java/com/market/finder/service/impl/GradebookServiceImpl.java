package com.market.finder.service.impl;

import com.market.finder.dto.GradebookRepository;
import com.market.finder.entity.Gradebook;
import com.market.finder.entity.GradebookId;
import com.market.finder.service.GradebookService;
import com.market.finder.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GradebookServiceImpl extends BaseServiceImpl<Gradebook, GradebookId, GradebookRepository> implements GradebookService {

    public GradebookServiceImpl(GradebookRepository gradebookRepository) {
        super(gradebookRepository);
    }

    @Override
    public List<Gradebook> findAll() {
        return super.findAll();
    }

    @Override
    public Optional<Gradebook> findById(GradebookId id) {
        return super.findById(id);
    }

    @Override
    @Transactional
    public Gradebook save(Gradebook gradebook) {
        return repository.saveAndFlush(gradebook);
    }

    @Override
    @Transactional
    public void deleteById(GradebookId id) {
        super.deleteById(id);
        repository.flush();
    }

    @Override
    public boolean existsById(GradebookId id) {
        return repository.existsById(id);
    }
}
