package com.market.finder.controller;

import com.market.finder.entity.Staff;
import com.market.finder.service.staff.StaffService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    public String listStaff(
            @RequestParam(name = "sort", required = false) String sort,
            Model model) {
        List<Staff> staffList;
        if ("age".equalsIgnoreCase(sort)) {
            staffList = staffService.findAllByOrderByAgeAsc();
        } else if ("income".equalsIgnoreCase(sort)) {
            staffList = staffService.findAllByOrderByIncomeAsc();
        } else {
            staffList = staffService.findAll();
        }
        model.addAttribute("staffList", staffList);
        model.addAttribute("currentSort", sort);
        return "staff/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("staff", new Staff());
        return "staff/form";
    }

    @PostMapping("/save")
    public String saveStaff(
            @Valid @ModelAttribute("staff") Staff staff,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "staff/form";
        }
        staffService.save(staff);
        return "redirect:/staff";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Staff staff = staffService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid staff Id: " + id));
        model.addAttribute("staff", staff);
        return "staff/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteStaff(@PathVariable("id") Integer id) {
        staffService.deleteById(id);
        return "redirect:/staff";
    }
}
