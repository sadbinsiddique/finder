package com.market.finder.rest;

import com.market.finder.entity.Student;
import com.market.finder.entity.StudentErrorResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentRestController {
    private List<Student> isStudent;

    @PostConstruct
    public void init(){
        isStudent = new ArrayList<>();

        isStudent.add(new Student("Sad Bin", "Siddiqur", "sadbinsiddique@gmail.com"));
        isStudent.add(new Student("Jannatun","Nayem", "jannatunnayem38214@gmail.com"));
        isStudent.add(new Student("Md", "Rahad", "rahadrohoman644@gmail.com"));
    }

    @GetMapping("/student")
    public List<Student> getStudents() {
        return isStudent;
    }

    @GetMapping("/student/{studentId}")
    public Student getStudent(@PathVariable int studentId) {
        // check The StudentId
        if ((studentId >= isStudent.size()) || (studentId < 0)) {
            throw new StudentNotFoundExaptions("Student id not found - " + studentId);
        }
        return isStudent.get(studentId);
    }

    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleException(StudentNotFoundExaptions exc) {
        StudentErrorResponse error = new StudentErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}