package com.market.finder.service;

import com.market.finder.dao.EnrollmentRepository;
import com.market.finder.entity.Enrollment;
import com.market.finder.entity.EnrollmentId;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "enrollment", key = "'all'")
    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }

    @Override
    @Cacheable(value = "enrollment", key = "#id")
    public Optional<Enrollment> findById(EnrollmentId id) {
        return enrollmentRepository.findById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "enrollment", allEntries = true)
    public Enrollment save(Enrollment enrollment) {
        return enrollmentRepository.saveAndFlush(enrollment);
    }

    @Override
    @Transactional
    @CacheEvict(value = "enrollment", allEntries = true)
    public void deleteById(EnrollmentId id) {
        enrollmentRepository.deleteById(id);
        enrollmentRepository.flush();
    }

    @Override
    public boolean existsById(EnrollmentId id) {
        return enrollmentRepository.existsById(id);
    }
}
