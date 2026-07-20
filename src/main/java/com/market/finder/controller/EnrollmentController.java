package com.market.finder.controller;

import com.market.finder.dao.EnrollmentRepository;
import com.market.finder.entity.Enrollment;
import com.market.finder.entity.EnrollmentId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentController(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    @GetMapping
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @GetMapping("/student/{studentId}")
    public List<Enrollment> getEnrollmentsByStudent(@PathVariable Integer studentId) {
        return enrollmentRepository.findByStudent_Id(studentId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Enrollment enrollStudent(@RequestBody Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    @DeleteMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<Void> dropCourse(@PathVariable Integer studentId, @PathVariable Integer courseId) {
        EnrollmentId id = new EnrollmentId(studentId, courseId);
        if (!enrollmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        enrollmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}