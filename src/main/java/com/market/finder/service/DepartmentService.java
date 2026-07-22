package com.market.finder.service;

import com.market.finder.entity.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    List<Department> findAll();

    Optional<Department> findById(Integer id);

    Department save(Department department);

    void deleteById(Integer id);

    boolean existsById(Integer id);
}
