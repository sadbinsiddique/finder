package com.market.finder.controller;

import com.market.finder.dao.CourseRepository;
import com.market.finder.dao.GradebookRepository;
import com.market.finder.dao.StudentRepository;
import com.market.finder.entity.Gradebook;
import com.market.finder.entity.GradebookId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/gradebooks")
public class GradebookController {

    private final GradebookRepository gradebookRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public GradebookController(GradebookRepository gradebookRepository,
                               StudentRepository studentRepository,
                               CourseRepository courseRepository) {
        this.gradebookRepository = gradebookRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    // 1. Show all grades
    @GetMapping
    public String listGradebooks(Model model) {
        model.addAttribute("gradebooks", gradebookRepository.findAll());
        return "gradebooks/list";
    }

    // 2. Show the form to record a new grade
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("gradebook", new Gradebook());
        // Pass students and courses for the dropdown menus
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("courses", courseRepository.findAll());
        return "gradebooks/form";
    }

    // 3. Show the form to edit an existing grade (Using RequestParams for Composite Key)
    @GetMapping("/edit")
    public String showEditForm(
            @RequestParam("studentId") Integer studentId,
            @RequestParam("courseId") Integer courseId,
            @RequestParam("assignmentName") String assignmentName,
            Model model) {

        GradebookId id = new GradebookId(studentId, courseId, assignmentName);
        Gradebook gradebook = gradebookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid gradebook Id"));

        model.addAttribute("gradebook", gradebook);
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("courses", courseRepository.findAll());
        return "gradebooks/form";
    }

    // 4. Save the gradebook entry
    @PostMapping("/save")
    public String saveGradebook(@ModelAttribute("gradebook") Gradebook gradebook) {
        // Because Gradebook uses @MapsId, we must ensure the EmbeddedId
        // has the correct data populated before saving it to the database.
        if (gradebook.getId() == null) {
            gradebook.setId(new GradebookId());
        }
        if (gradebook.getStudent() != null) {
            gradebook.getId().setStudentId(gradebook.getStudent().getId());
        }
        if (gradebook.getCourse() != null) {
            gradebook.getId().setCourseId(gradebook.getCourse().getId());
        }

        gradebookRepository.save(gradebook);
        return "redirect:/gradebooks";
    }

    // 5. Delete a gradebook entry
    @GetMapping("/delete")
    public String deleteGradebook(
            @RequestParam("studentId") Integer studentId,
            @RequestParam("courseId") Integer courseId,
            @RequestParam("assignmentName") String assignmentName) {

        GradebookId id = new GradebookId(studentId, courseId, assignmentName);
        if (gradebookRepository.existsById(id)) {
            gradebookRepository.deleteById(id);
        }
        return "redirect:/gradebooks";
    }
}