package com.market.finder.dao;

import com.market.finder.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByEmail(String email);

    Optional<Student> findByUser_Username(String username);

    List<Student> findByDepartment_Id(Integer departmentId);
}