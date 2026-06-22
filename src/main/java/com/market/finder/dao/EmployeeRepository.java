package com.market.finder.dao;

import com.market.finder.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

@SuppressWarnings("unused")
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // No Need To Manual Code
}
