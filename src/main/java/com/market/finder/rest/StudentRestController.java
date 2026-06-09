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
        isStudent.add(new Student("Mis", "Nasrin","nasrin@hotmail.com"));
        isStudent.add(new Student("Abu", "Baker","abubaker@gmail.com"));
        isStudent.add(new Student("Sumiya", "Amir", "Sumiyaamir@outlook.com"));
        isStudent.add(new Student("Korim", "Islam", "Korimislam@gmail.com"));
        isStudent.add(new Student("Nakir", "Hannan", "nakirhannan@gmail.com"));
        isStudent.add(new Student("Rohaman","Sikder","rohomansikder@gmail.com"));
    }

    @GetMapping("/student")
    public List<Student> getStudents() {
        return isStudent;
    }

    @GetMapping("/student/{studentId}")
    public Student getStudent(@PathVariable int studentId) {
        // check The StudentId
        if ((studentId >= isStudent.size()) || (studentId < 0)) {
            throw new StudentNotFoundExaptions("Not Exist id of '" + studentId +"'");
        }

        if (studentId == 5 ) {
            throw new RuntimeException("Something went wrong"); // It Is for bad Request Triger
        }
        return isStudent.get(studentId);
    }

    // add ExceptionHandler

    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleException(StudentNotFoundExaptions exc) {
        StudentErrorResponse error = new StudentErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value()); // For 404 error
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleException(Exception exc) {
        StudentErrorResponse e = new StudentErrorResponse();

        e.setStatus(HttpStatus.BAD_REQUEST.value()); // For 400 error
        e.setMessage(exc.getMessage());
        e.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }
}