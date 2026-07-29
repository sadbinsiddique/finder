package com.market.finder.service.impl;

import com.market.finder.dao.GradebookRepository;
import com.market.finder.entity.Gradebook;
import com.market.finder.entity.GradebookId;
import com.market.finder.service.GradebookService;
import com.market.finder.service.base.BaseServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "gradebook", key = "'all'")
    public List<Gradebook> findAll() {
        return super.findAll();
    }

    @Override
    @Cacheable(value = "gradebook", key = "#id")
    public Optional<Gradebook> findById(GradebookId id) {
        return super.findById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "gradebook", allEntries = true)
    public Gradebook save(Gradebook gradebook) {
        return repository.saveAndFlush(gradebook);
    }

    @Override
    @Transactional
    @CacheEvict(value = "gradebook", allEntries = true)
    public void deleteById(GradebookId id) {
        super.deleteById(id);
        repository.flush();
    }

    @Override
    public boolean existsById(GradebookId id) {
        return repository.existsById(id);
    }
}
