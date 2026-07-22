package com.market.finder.service;

import com.market.finder.dao.EnrollmentRepository;
import com.market.finder.entity.Enrollment;
import com.market.finder.entity.EnrollmentId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }

    @Override
    public Optional<Enrollment> findById(EnrollmentId id) {
        return enrollmentRepository.findById(id);
    }

    @Override
    @Transactional
    public Enrollment save(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    @Override
    @Transactional
    public void deleteById(EnrollmentId id) {
        enrollmentRepository.deleteById(id);
    }

    @Override
    public boolean existsById(EnrollmentId id) {
        return enrollmentRepository.existsById(id);
    }
}
