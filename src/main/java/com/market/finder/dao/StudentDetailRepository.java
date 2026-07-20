package com.market.finder.dao;

import com.market.finder.entity.StudentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentDetailRepository extends JpaRepository<StudentDetail, Integer> {
    // The ID type is Integer because it shares the Student's ID
}