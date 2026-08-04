package com.market.finder.service.enrollment;

import com.market.finder.entity.Enrollment;
import com.market.finder.entity.EnrollmentId;
import com.market.finder.service.base.BaseService;

public interface EnrollmentService extends BaseService<Enrollment, EnrollmentId> {
    boolean existsById(EnrollmentId id);
}
