package com.market.finder.dao;

import com.market.finder.entity.Employee;

import java.util.List;

public interface EmployeeDAO {
    List<Employee> findAll();
}
