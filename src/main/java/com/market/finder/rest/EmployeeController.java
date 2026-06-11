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

    @GetMapping("/employee")  // it is get method
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    @PostMapping("/employee")
    public Employee save(@RequestBody Employee theEmployee) {// request body use for binding with JSON
       theEmployee.setId(0);
        return employeeService.save(theEmployee);
    }

    @PutMapping("/employee")
    public Employee updateEmployee(@RequestBody Employee theEmployee) {
        return employeeService.save(theEmployee);
    }

    @PatchMapping("/employee/{employeeId}") // partial Update for employee
    public Employee patchEmployee (@PathVariable int employeeId, @RequestBody Map<String, Object> patchPaylode) {
        Employee tempEmployee = employeeService.findById(employeeId);

        //throw exception if value Is null
        if (tempEmployee == null) {
            throw new RuntimeException("Employee is not found - "+ employeeId );
        }

        //throw exception if request body contain "id" key
        if (patchPaylode.containsKey("id")) {
            throw new RuntimeException("Employee id in not Allowed - " + employeeId );
        }

        // actual Logic for single variable Update
        Employee patchEmployee = jsonMapper.updateValue(tempEmployee, patchPaylode);

        return employeeService.save(patchEmployee);
    }

}
