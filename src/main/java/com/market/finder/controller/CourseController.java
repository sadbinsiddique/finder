package com.market.finder.controller;

import com.market.finder.entity.Course;
import com.market.finder.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * DIP: Depends on CourseService abstraction rather than CourseRepository directly.
 */
@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // 1. Show all courses
    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.findAll());
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
        Course course = courseService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id: " + id));
        model.addAttribute("course", course);
        return "courses/form";
    }

    // 4. Save the course (Handles both Create and Update)
    @PostMapping("/save")
    public String saveCourse(@ModelAttribute("course") Course course) {
        courseService.save(course);
        return "redirect:/courses";
    }

    // 5. Delete a course
    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Integer id) {
        if (courseService.existsById(id)) {
            courseService.deleteById(id);
        }
        return "redirect:/courses";
    }
}