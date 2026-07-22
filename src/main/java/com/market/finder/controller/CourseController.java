package com.market.finder.controller;

import com.market.finder.dao.CourseRepository;
import com.market.finder.entity.Course;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // 1. Show all courses
    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        return "courses/list";
    }

    // 2. Show the form to create a new course
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        return "courses/form";
    }

    // 3. Show the form to edit an existing course
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id: " + id));
        model.addAttribute("course", course);
        return "courses/form";
    }

    // 4. Save the course (Handles both Create and Update)
    @PostMapping("/save")
    public String saveCourse(@ModelAttribute("course") Course course) {
        courseRepository.save(course);
        return "redirect:/courses";
    }

    // 5. Delete a course
    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Integer id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
        }
        return "redirect:/courses";
    }
}