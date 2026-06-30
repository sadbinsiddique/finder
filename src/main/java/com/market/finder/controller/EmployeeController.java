package com.market.finder.controller;

import com.market.finder.entity.Employee;
import com.market.finder.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/mvcemployees")
public class EmployeeController {
    private EmployeeService employeeService;


    public void EmployeeService(EmployeeService theEmployeeService) {
        this.employeeService = theEmployeeService;
    }

    @GetMapping("/list")
    public String listEmployees(Model theModel) {
        //get employee from the db
        List<Employee> theEmployees = employeeService.findAll();
        //add to the spring model
        theModel.addAttribute("employees", theEmployees);
        return "list-employees";
    }

}
