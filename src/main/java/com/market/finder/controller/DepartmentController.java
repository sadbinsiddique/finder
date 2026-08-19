package com.market.finder.controller;

import com.market.finder.entity.Department;
import com.market.finder.service.department.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public String listDepartments(Model model) {
        model.addAttribute("departments", departmentService.findAll());
        return "departments/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("department", new Department());
        return "departments/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Department department = departmentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid department Id: " + id));
        model.addAttribute("department", department);
        return "departments/form";
    }

    @PostMapping("/save")
    public String saveDepartment(
            @Valid @ModelAttribute("department") Department department,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "departments/form";
        }
        departmentService.save(department);
        return "redirect:/departments";
    }


    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Integer id) {
        if (departmentService.existsById(id)) {
            departmentService.deleteById(id);
        }
        return "redirect:/departments";
    }
}