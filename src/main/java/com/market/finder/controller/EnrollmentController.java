package com.market.finder.controller;

import com.market.finder.entity.Enrollment;
import com.market.finder.entity.EnrollmentId;
import com.market.finder.service.course.CourseService;
import com.market.finder.service.enrollment.EnrollmentService;
import com.market.finder.service.student.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public String listEnrollments(Model model) {
        model.addAttribute("enrollments", enrollmentService.findAll());
        return "enrollments/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("enrollment", new Enrollment());
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("courses", courseService.findAll());
        return "enrollments/form";
    }

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

    @PostMapping("/save")
    public String saveEnrollment(@ModelAttribute("enrollment") Enrollment enrollment) {
        enrollment.prepareId();
        enrollmentService.save(enrollment);
        return "redirect:/enrollments";
    }

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