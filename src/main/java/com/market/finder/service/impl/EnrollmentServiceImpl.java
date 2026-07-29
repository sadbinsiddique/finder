package com.market.finder.service.impl;

import com.market.finder.dao.EnrollmentRepository;
import com.market.finder.entity.Enrollment;
import com.market.finder.entity.EnrollmentId;
import com.market.finder.service.EnrollmentService;
import com.market.finder.service.base.BaseServiceImpl;
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
    public List<Enrollment> findAll() {
        return super.findAll();
    }

    @Override
    public Optional<Enrollment> findById(EnrollmentId id) {
        return super.findById(id);
    }

    @Override
    @Transactional
    public Enrollment save(Enrollment enrollment) {
        return repository.saveAndFlush(enrollment);
    }

    @Override
    @Transactional
    public void deleteById(EnrollmentId id) {
        super.deleteById(id);
        repository.flush();
    }

    @Override
    public boolean existsById(EnrollmentId id) {
        return repository.existsById(id);
    }
}
