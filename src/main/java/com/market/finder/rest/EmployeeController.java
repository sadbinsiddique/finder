package com.market.finder.rest;

import com.market.finder.entity.Employee;
import com.market.finder.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    private  final EmployeeService employeeService;

    public EmployeeController(EmployeeService  injection) {
       this. employeeService = injection;
    }

    @GetMapping("/employee")  // it is get method
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    @PostMapping("/employee")
    public Employee save(@RequestBody Employee theEmployee) {// request body use for binding with JSON
       theEmployee.setId(0);
        return employeeService.save(theEmployee);
    }

    //update methode
    @PutMapping("/employee")
    public Employee updateEmployee(@RequestBody Employee theEmployee) {
        return employeeService.save(theEmployee);
    }

}
