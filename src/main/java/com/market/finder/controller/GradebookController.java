package com.market.finder.controller;

import com.market.finder.entity.Gradebook;
import com.market.finder.entity.GradebookId;
import com.market.finder.service.CourseService;
import com.market.finder.service.GradebookService;
import com.market.finder.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * DIP: Depends on GradebookService, StudentService, and CourseService abstractions.
 */
@Controller
@RequestMapping("/gradebooks")
public class GradebookController {

    private final GradebookService gradebookService;
    private final StudentService studentService;
    private final CourseService courseService;

    public GradebookController(GradebookService gradebookService,
                               StudentService studentService,
                               CourseService courseService) {
        this.gradebookService = gradebookService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    // 1. Show all grades
    @GetMapping
    public String listGradebooks(Model model) {
        model.addAttribute("gradebooks", gradebookService.findAll());
        return "gradebooks/list";
    }

    // 2. Show the form to record a new grade
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("gradebook", new Gradebook());
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("courses", courseService.findAll());
        return "gradebooks/form";
    }

    // 3. Show the form to edit an existing grade
    @GetMapping("/edit")
    public String showEditForm(
            @RequestParam("studentId") Integer studentId,
            @RequestParam("courseId") Integer courseId,
            @RequestParam("assignmentName") String assignmentName,
            Model model) {

        GradebookId id = new GradebookId(studentId, courseId, assignmentName);
        Gradebook gradebook = gradebookService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid gradebook Id"));

        model.addAttribute("gradebook", gradebook);
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("courses", courseService.findAll());
        return "gradebooks/form";
    }

    // 4. Save the gradebook entry
    @PostMapping("/save")
    public String saveGradebook(@ModelAttribute("gradebook") Gradebook gradebook) {
        if (gradebook.getId() == null) {
            gradebook.setId(new GradebookId());
        }
        if (gradebook.getStudent() != null) {
            gradebook.getId().setStudentId(gradebook.getStudent().getId());
        }
        if (gradebook.getCourse() != null) {
            gradebook.getId().setCourseId(gradebook.getCourse().getId());
        }

        gradebookService.save(gradebook);
        return "redirect:/gradebooks";
    }

    // 5. Delete a gradebook entry
    @GetMapping("/delete")
    public String deleteGradebook(
            @RequestParam("studentId") Integer studentId,
            @RequestParam("courseId") Integer courseId,
            @RequestParam("assignmentName") String assignmentName) {

        GradebookId id = new GradebookId(studentId, courseId, assignmentName);
        if (gradebookService.existsById(id)) {
            gradebookService.deleteById(id);
        }
        return "redirect:/gradebooks";
    }
}