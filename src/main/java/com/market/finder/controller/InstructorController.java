package com.market.finder.controller;

import com.market.finder.dao.InstructorRepository;
import com.market.finder.entity.Instructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/instructors")
public class InstructorController {

    private final InstructorRepository instructorRepository;

    public InstructorController(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    // 1. Show all instructors
    @GetMapping
    public String listInstructors(Model model) {
        model.addAttribute("instructors", instructorRepository.findAll());
        return "instructors/list";
    }

    // 2. Show the form to create a new instructor
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("instructor", new Instructor());
        return "instructors/form";
    }

    // 3. Show the form to edit an existing instructor
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid instructor Id: " + id));
        model.addAttribute("instructor", instructor);
        return "instructors/form";
    }

    // 4. Save the instructor (Handles both Create and Update)
    @PostMapping("/save")
    public String saveInstructor(@ModelAttribute("instructor") Instructor instructor) {
        instructorRepository.save(instructor);
        return "redirect:/instructors";
    }

    // 5. Delete an instructor
    @GetMapping("/delete/{id}")
    public String deleteInstructor(@PathVariable Integer id) {
        if (instructorRepository.existsById(id)) {
            instructorRepository.deleteById(id);
        }
        return "redirect:/instructors";
    }
}