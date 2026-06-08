package com.market.finder.rest;

import com.market.finder.entity.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentRestController {
    @GetMapping("/student")
    public List<Student> getStudents() {
        List<Student>  isStudent = new ArrayList<>();

        isStudent.add(new Student("Sad Bin", "Siddiqur", "sadbinsiddique@gmail.com"));
        isStudent.add(new Student("Jannatun","Nayem", "jannatunnayem38214@gmail.com"));
        isStudent.add(new Student("Md", "Rahad", "rahadrohoman644@gmail.com"));

        return isStudent;
    }
}

// in /api/student/{studentId} to return student data for the given studentId.