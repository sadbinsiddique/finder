package com.market.finder.controller;

import com.market.finder.entity.Department;
import com.market.finder.service.DepartmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * DIP: Depends on DepartmentService abstraction rather than DepartmentRepository directly.
 */
@Controller
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // 1. Show all departments
    @GetMapping
    public String listDepartments(Model model) {
        model.addAttribute("departments", departmentService.findAll());
        return "departments/list";
    }

    // 2. Show the form to create a new department
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("department", new Department());
        return "departments/form";
    }

    // 3. Show the form to edit an existing department
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Department department = departmentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid department Id: " + id));
        model.addAttribute("department", department);
        return "departments/form";
    }

    // 4. Save the department (Handles both Create and Update)
    @PostMapping("/save")
    public String saveDepartment(@ModelAttribute("department") Department department) {
        departmentService.save(department);
        return "redirect:/departments";
    }

    // 5. Delete a department
    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Integer id) {
        if (departmentService.existsById(id)) {
            departmentService.deleteById(id);
        }
        return "redirect:/departments";
    }
}