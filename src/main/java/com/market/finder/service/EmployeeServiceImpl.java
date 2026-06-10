package com.market.finder.service;

import com.market.finder.dao.EmployeeDAO;
import com.market.finder.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeDAO employeeDAO;

    @Autowired
    public EmployeeServiceImpl (EmployeeDAO injection) {
        employeeDAO = injection;
    }
    @Override
    public List<Employee> findAll() {
        return employeeDAO.findAll();
    }
}
