package com.market.finder.service;

import com.market.finder.dao.GradebookRepository;
import com.market.finder.entity.Gradebook;
import com.market.finder.entity.GradebookId;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GradebookServiceImpl implements GradebookService {

    private final GradebookRepository gradebookRepository;

    public GradebookServiceImpl(GradebookRepository gradebookRepository) {
        this.gradebookRepository = gradebookRepository;
    }

    @Override
    @Cacheable(value = "gradebook", key = "'all'")
    public List<Gradebook> findAll() {
        return gradebookRepository.findAll();
    }

    @Override
    @Cacheable(value = "gradebook", key = "#id")
    public Optional<Gradebook> findById(GradebookId id) {
        return gradebookRepository.findById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "gradebook", allEntries = true)
    public Gradebook save(Gradebook gradebook) {
        return gradebookRepository.saveAndFlush(gradebook);
    }

    @Override
    @Transactional
    @CacheEvict(value = "gradebook", allEntries = true)
    public void deleteById(GradebookId id) {
        gradebookRepository.deleteById(id);
        gradebookRepository.flush();
    }

    @Override
    public boolean existsById(GradebookId id) {
        return gradebookRepository.existsById(id);
    }
}
