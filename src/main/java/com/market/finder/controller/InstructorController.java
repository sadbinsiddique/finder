package com.market.finder.controller;

import com.market.finder.entity.Instructor;
import com.market.finder.service.InstructorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping
    public String listInstructors(Model model) {
        model.addAttribute("instructors", instructorService.findAll());
        return "instructors/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("instructor", new Instructor());
        return "instructors/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Instructor instructor = instructorService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid instructor Id: " + id));
        model.addAttribute("instructor", instructor);
        return "instructors/form";
    }

    @PostMapping("/save")
    public String saveInstructor(@ModelAttribute("instructor") Instructor instructor) {
        instructorService.save(instructor);
        return "redirect:/instructors";
    }

    @GetMapping("/delete/{id}")
    public String deleteInstructor(@PathVariable Integer id) {
        if (instructorService.existsById(id)) {
            instructorService.deleteById(id);
        }
        return "redirect:/instructors";
    }
}