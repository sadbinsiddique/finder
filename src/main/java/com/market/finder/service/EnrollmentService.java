package com.market.finder.service;

import com.market.finder.entity.Enrollment;
import com.market.finder.entity.EnrollmentId;

import java.util.List;
import java.util.Optional;

public interface EnrollmentService {

    List<Enrollment> findAll();

    Optional<Enrollment> findById(EnrollmentId id);

    Enrollment save(Enrollment enrollment);

    void deleteById(EnrollmentId id);

    boolean existsById(EnrollmentId id);
}
