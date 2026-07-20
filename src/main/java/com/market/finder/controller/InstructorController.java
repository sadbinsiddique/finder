package com.market.finder.controller;

import com.market.finder.dao.InstructorRepository;
import com.market.finder.entity.Instructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
public class InstructorController {

    private final InstructorRepository instructorRepository;

    public InstructorController(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @GetMapping
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Instructor> getInstructorById(@PathVariable Integer id) {
        return instructorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Instructor createInstructor(@RequestBody Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstructor(@PathVariable Integer id) {
        if (!instructorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        instructorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}