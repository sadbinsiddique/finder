package com.market.finder.rest;

import com.market.finder.dao.EmployeeDAO;
import com.market.finder.entity.Employee;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    private final EmployeeDAO employeeDAO;

    // Quick: inject employee dao (for Now)

    public EmployeeController(EmployeeDAO injection) {
       this.employeeDAO = injection;
    }
    //expose the /employee end point and return the employee List
    @GetMapping("/employee")
    public List<Employee> findAll() {
        return employeeDAO.findAll();
    }

}
