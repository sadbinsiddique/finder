package com.market.finder.service;

import com.market.finder.dao.EmployeeDAO;
import com.market.finder.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public Employee findById(int theId) {
        return employeeDAO.findById(theId);
    }

    @Override
    @Transactional  // we modify the dataset (don't use in DAO) it only apply Service layer
    public Employee save(Employee theEmployee) {
        return employeeDAO.save(theEmployee);
    }

    @Override
    @Transactional // we modify the dataset (don't use in DAO) it only apply Service layer
    public void deleteById(int theId) {
        employeeDAO.deleteById(theId);
    }
}
