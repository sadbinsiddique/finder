package com.market.finder.service;

import com.market.finder.dao.GradebookRepository;
import com.market.finder.entity.Gradebook;
import com.market.finder.entity.GradebookId;
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
    public List<Gradebook> findAll() {
        return gradebookRepository.findAll();
    }

    @Override
    public Optional<Gradebook> findById(GradebookId id) {
        return gradebookRepository.findById(id);
    }

    @Override
    @Transactional
    public Gradebook save(Gradebook gradebook) {
        return gradebookRepository.save(gradebook);
    }

    @Override
    @Transactional
    public void deleteById(GradebookId id) {
        gradebookRepository.deleteById(id);
    }

    @Override
    public boolean existsById(GradebookId id) {
        return gradebookRepository.existsById(id);
    }
}
