package com.market.finder.rest;

import com.market.finder.entity.Student;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        isStudent.add(new Student("Kori", "Islam", "Korimislam@gmail.com"));
        isStudent.add(new Student("Nakir", "Hannan", "nakirhannan@gmail.com"));
        isStudent.add(new Student("Roman","Insider","rohomansikder@gmail.com"));
    }

    @GetMapping("/student")
    public List<Student> getStudents() {
        return isStudent;
    }

    @GetMapping("/student/{studentId}")
    public Student getStudent(@PathVariable int studentId) {
        if ((studentId >= isStudent.size()) || (studentId < 0)) {
            throw new StudentNotFoundExaptions("Not Exist id of " + studentId);
        }
        return isStudent.get(studentId);
    }
}