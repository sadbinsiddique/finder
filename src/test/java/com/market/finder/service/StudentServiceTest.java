package com.market.finder.service;

import com.market.finder.dao.StudentRepository;
import com.market.finder.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

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
    void testFindAllStudents() {
        when(studentRepository.findAll()).thenReturn(List.of(sampleStudent));

        List<Student> students = studentService.findAll();

        assertEquals(1, students.size());
        assertEquals("Alice", students.get(0).getFirstName());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Found() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(sampleStudent));

        Optional<Student> studentOpt = studentService.findById(1);

        assertTrue(studentOpt.isPresent());
        assertEquals("Johnson", studentOpt.get().getLastName());
        verify(studentRepository, times(1)).findById(1);
    }

    @Test
    void testSaveStudent() {
        when(studentRepository.save(sampleStudent)).thenReturn(sampleStudent);

        Student saved = studentService.save(sampleStudent);

        assertNotNull(saved);
        assertEquals("Alice", saved.getFirstName());
        verify(studentRepository, times(1)).save(sampleStudent);
    }

    @Test
    void testDeleteById() {
        doNothing().when(studentRepository).deleteById(1);

        studentService.deleteById(1);

        verify(studentRepository, times(1)).deleteById(1);
    }
}
