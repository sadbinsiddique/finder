package com.market.finder.rest;

import com.market.finder.entity.Employee;
import com.market.finder.service.EmployeeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    private  final EmployeeService employeeService;

    public EmployeeController(EmployeeService  injection) {
       this. employeeService = injection;
    }
    @GetMapping("/employee")
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

}
