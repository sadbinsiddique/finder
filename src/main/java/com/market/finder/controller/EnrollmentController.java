package com.market.finder.controller;

import com.market.finder.dao.CourseRepository;
import com.market.finder.dao.EnrollmentRepository;
import com.market.finder.dao.StudentRepository;
import com.market.finder.entity.Enrollment;
import com.market.finder.entity.EnrollmentId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentController(EnrollmentRepository enrollmentRepository,
                                StudentRepository studentRepository,
                                CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    // 1. Show all enrollments
    @GetMapping
    public String listEnrollments(Model model) {
        model.addAttribute("enrollments", enrollmentRepository.findAll());
        return "enrollments/list";
    }

    // 2. Show the form to enroll a student
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("enrollment", new Enrollment());
        // Pass students and courses for the dropdown menus
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("courses", courseRepository.findAll());
        return "enrollments/form";
    }

    // 3. Show the form to edit an existing enrollment (e.g., updating the enrollment date)
    @GetMapping("/edit")
    public String showEditForm(
            @RequestParam("studentId") Integer studentId,
            @RequestParam("courseId") Integer courseId,
            Model model) {

        EnrollmentId id = new EnrollmentId(studentId, courseId);
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid enrollment details"));

        model.addAttribute("enrollment", enrollment);
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("courses", courseRepository.findAll());
        return "enrollments/form";
    }

    // 4. Save the enrollment
    @PostMapping("/save")
    public String saveEnrollment(@ModelAttribute("enrollment") Enrollment enrollment) {
        // Map the selected objects' IDs to the Composite Key before saving
        if (enrollment.getId() == null) {
            enrollment.setId(new EnrollmentId());
        }
        if (enrollment.getStudent() != null) {
            enrollment.getId().setStudentId(enrollment.getStudent().getId());
        }
        if (enrollment.getCourse() != null) {
            enrollment.getId().setCourseId(enrollment.getCourse().getId());
        }

        enrollmentRepository.save(enrollment);
        return "redirect:/enrollments";
    }

    // 5. Delete an enrollment (Drop Course)
    @GetMapping("/delete")
    public String dropCourse(
            @RequestParam("studentId") Integer studentId,
            @RequestParam("courseId") Integer courseId) {

        EnrollmentId id = new EnrollmentId(studentId, courseId);
        if (enrollmentRepository.existsById(id)) {
            enrollmentRepository.deleteById(id);
        }
        return "redirect:/enrollments";
    }
}