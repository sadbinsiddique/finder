package com.market.finder.service.employee;

import com.market.finder.entity.Employee;
import com.market.finder.repository.EmployeeRepository;
import com.market.finder.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends BaseServiceImpl<Employee, Integer, EmployeeRepository> implements EmployeeService {

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        super(employeeRepository);
    }
}
