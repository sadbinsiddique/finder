package com.market.finder.controller;

import com.market.finder.entity.Gradebook;
import com.market.finder.entity.GradebookId;
import com.market.finder.service.course.CourseService;
import com.market.finder.service.gradebook.GradebookService;
import com.market.finder.service.student.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public String listGradebooks(Model model) {
        model.addAttribute("gradebooks", gradebookService.findAll());
        return "gradebooks/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("gradebook", new Gradebook());
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("courses", courseService.findAll());
        return "gradebooks/form";
    }

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

    @PostMapping("/save")
    public String saveGradebook(@ModelAttribute("gradebook") Gradebook gradebook) {
        gradebook.prepareId();
        gradebookService.save(gradebook);
        return "redirect:/gradebooks";
    }

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