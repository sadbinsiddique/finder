package com.market.finder.rest;

import com.market.finder.entity.Employee;
import com.market.finder.service.EmployeeService;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    private  final EmployeeService employeeService;
    private final JsonMapper jsonMapper;

    public EmployeeController(EmployeeService  injection, JsonMapper theJsonMapper) {
       this. employeeService = injection;
       this.jsonMapper = theJsonMapper;
    }

    @GetMapping("/employee")
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

    //add mapping for PATCH
    @PatchMapping("/employee/{employeeId}")
    public Employee pathEmployee(@PathVariable int employeeId, @RequestBody Map<String, Object> PathPayload) {
        Employee tempEmployee = employeeService.findById(employeeId);

        // throw exception if Employee id not found
        if (tempEmployee == null) {
            throw new RuntimeException("Employee id not found - " + employeeId);
        }

        // throw exception if request body contains "id" key
        if (PathPayload.containsKey("id")) {
            throw new RuntimeException("Employee id not allowed in request body - " + employeeId);
        }

        // apply the patch binding
        Employee patchedEmployee = jsonMapper.updateValue(tempEmployee, PathPayload);

        return employeeService.save(patchedEmployee);
    }

}
