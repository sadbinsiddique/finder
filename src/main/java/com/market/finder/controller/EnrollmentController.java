package com.market.finder.controller;

import com.market.finder.entity.Enrollment;
import com.market.finder.entity.EnrollmentId;
import com.market.finder.service.CourseService;
import com.market.finder.service.EnrollmentService;
import com.market.finder.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * DIP: Depends on EnrollmentService, StudentService, and CourseService abstractions.
 */
@Controller
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final CourseService courseService;

    public EnrollmentController(EnrollmentService enrollmentService,
                                StudentService studentService,
                                CourseService courseService) {
        this.enrollmentService = enrollmentService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    // 1. Show all enrollments
    @GetMapping
    public String listEnrollments(Model model) {
        model.addAttribute("enrollments", enrollmentService.findAll());
        return "enrollments/list";
    }

    // 2. Show the form to enroll a student
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("enrollment", new Enrollment());
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("courses", courseService.findAll());
        return "enrollments/form";
    }

    // 3. Show the form to edit an existing enrollment
    @GetMapping("/edit")
    public String showEditForm(
            @RequestParam("studentId") Integer studentId,
            @RequestParam("courseId") Integer courseId,
            Model model) {

        EnrollmentId id = new EnrollmentId(studentId, courseId);
        Enrollment enrollment = enrollmentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid enrollment details"));

        model.addAttribute("enrollment", enrollment);
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("courses", courseService.findAll());
        return "enrollments/form";
    }

    // 4. Save the enrollment
    @PostMapping("/save")
    public String saveEnrollment(@ModelAttribute("enrollment") Enrollment enrollment) {
        if (enrollment.getId() == null) {
            enrollment.setId(new EnrollmentId());
        }
        if (enrollment.getStudent() != null) {
            enrollment.getId().setStudentId(enrollment.getStudent().getId());
        }
        if (enrollment.getCourse() != null) {
            enrollment.getId().setCourseId(enrollment.getCourse().getId());
        }

        enrollmentService.save(enrollment);
        return "redirect:/enrollments";
    }

    // 5. Delete an enrollment (Drop Course)
    @GetMapping("/delete")
    public String dropCourse(
            @RequestParam("studentId") Integer studentId,
            @RequestParam("courseId") Integer courseId) {

        EnrollmentId id = new EnrollmentId(studentId, courseId);
        if (enrollmentService.existsById(id)) {
            enrollmentService.deleteById(id);
        }
        return "redirect:/enrollments";
    }
}