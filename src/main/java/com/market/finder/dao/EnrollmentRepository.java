package com.market.finder.dao;

import com.market.finder.entity.Enrollment;
import com.market.finder.entity.EnrollmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId> {
}