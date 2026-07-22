package com.market.finder.service;

import com.market.finder.entity.Gradebook;
import com.market.finder.entity.GradebookId;

import java.util.List;
import java.util.Optional;

public interface GradebookService {

    List<Gradebook> findAll();

    Optional<Gradebook> findById(GradebookId id);

    Gradebook save(Gradebook gradebook);

    void deleteById(GradebookId id);

    boolean existsById(GradebookId id);
}
