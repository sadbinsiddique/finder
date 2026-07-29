package com.market.finder.service.impl;

import com.market.finder.dao.EnrollmentRepository;
import com.market.finder.entity.Enrollment;
import com.market.finder.entity.EnrollmentId;
import com.market.finder.service.EnrollmentService;
import com.market.finder.service.base.BaseServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentServiceImpl extends BaseServiceImpl<Enrollment, EnrollmentId, EnrollmentRepository> implements EnrollmentService {

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository) {
        super(enrollmentRepository);
    }

    @Override
    @Cacheable(value = "enrollment", key = "'all'")
    public List<Enrollment> findAll() {
        return super.findAll();
    }

    @Override
    @Cacheable(value = "enrollment", key = "#id")
    public Optional<Enrollment> findById(EnrollmentId id) {
        return super.findById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "enrollment", allEntries = true)
    public Enrollment save(Enrollment enrollment) {
        return repository.saveAndFlush(enrollment);
    }

    @Override
    @Transactional
    @CacheEvict(value = "enrollment", allEntries = true)
    public void deleteById(EnrollmentId id) {
        super.deleteById(id);
        repository.flush();
    }

    @Override
    public boolean existsById(EnrollmentId id) {
        return repository.existsById(id);
    }
}
