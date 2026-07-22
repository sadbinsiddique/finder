package com.market.finder.service;

import com.market.finder.entity.Employee;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Employee operations.
 * ISP: Only methods that clients actually use.
 */
public interface EmployeeService {

    List<Employee> findAll();

    Optional<Employee> findById(int theId);

    Employee save(Employee theEmployee);

    void deleteById(int theId);
}
