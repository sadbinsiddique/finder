package com.market.finder.service;

import com.market.finder.entity.Instructor;

import java.util.List;
import java.util.Optional;

public interface InstructorService {

    List<Instructor> findAll();

    Optional<Instructor> findById(Integer id);

    Instructor save(Instructor instructor);

    void deleteById(Integer id);

    boolean existsById(Integer id);
}
