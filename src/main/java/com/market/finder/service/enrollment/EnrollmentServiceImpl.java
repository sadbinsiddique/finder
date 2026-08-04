package com.market.finder.service.enrollment;

import com.market.finder.entity.Enrollment;
import com.market.finder.entity.EnrollmentId;
import com.market.finder.repository.EnrollmentRepository;
import com.market.finder.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentServiceImpl extends BaseServiceImpl<Enrollment, EnrollmentId, EnrollmentRepository> implements EnrollmentService {

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository) {
        super(enrollmentRepository);
    }

    @Override
    public boolean existsById(EnrollmentId id) {
        return repository.existsById(id);
    }
}
