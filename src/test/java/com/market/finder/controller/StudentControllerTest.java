package com.market.finder.controller;

import com.market.finder.entity.Student;
import com.market.finder.service.DepartmentService;
import com.market.finder.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private Model model;

    @InjectMocks
    private StudentController studentController;

    private Student sampleStudent;

    @BeforeEach
    void setUp() {
        sampleStudent = new Student();
        sampleStudent.setId(1);
        sampleStudent.setFirstName("Alice");
        sampleStudent.setLastName("Johnson");
        sampleStudent.setEmail("alice@student.edu");
    }

    @Test
    void testListStudents() {
        when(studentService.findAll()).thenReturn(List.of(sampleStudent));

        String view = studentController.listStudents(model);

        assertEquals("students/list", view);
        verify(model, times(1)).addAttribute("students", List.of(sampleStudent));
    }

    @Test
    void testShowCreateForm() {
        when(departmentService.findAll()).thenReturn(List.of());

        String view = studentController.showCreateForm(model);

        assertEquals("students/form", view);
        verify(model, times(1)).addAttribute(eq("student"), any(Student.class));
    }

    @Test
    void testShowEditForm_Success() {
        when(studentService.findById(1)).thenReturn(Optional.of(sampleStudent));
        when(departmentService.findAll()).thenReturn(List.of());

        String view = studentController.showEditForm(1, model);

        assertEquals("students/form", view);
        verify(model, times(1)).addAttribute("student", sampleStudent);
    }

    @Test
    void testSaveStudent() {
        when(studentService.save(sampleStudent)).thenReturn(sampleStudent);

        String view = studentController.saveStudent(sampleStudent);

        assertEquals("redirect:/students", view);
        verify(studentService, times(1)).save(sampleStudent);
    }

    @Test
    void testDeleteStudent() {
        doNothing().when(studentService).deleteById(1);

        String view = studentController.deleteStudent(1);

        assertEquals("redirect:/students", view);
        verify(studentService, times(1)).deleteById(1);
    }
}
