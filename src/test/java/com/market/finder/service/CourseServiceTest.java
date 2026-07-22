package com.market.finder.service;

import com.market.finder.dao.CourseRepository;
import com.market.finder.entity.Course;
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
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course sampleCourse;

    @BeforeEach
    void setUp() {
        sampleCourse = new Course();
        sampleCourse.setId(101);
        sampleCourse.setTitle("Computer Science 101");
    }

    @Test
    void testFindAllCourses() {
        when(courseRepository.findAll()).thenReturn(List.of(sampleCourse));

        List<Course> courses = courseService.findAll();

        assertEquals(1, courses.size());
        assertEquals("Computer Science 101", courses.getFirst().getTitle());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Found() {
        when(courseRepository.findById(101)).thenReturn(Optional.of(sampleCourse));

        Optional<Course> courseOpt = courseService.findById(101);

        assertTrue(courseOpt.isPresent());
        assertEquals(101, courseOpt.get().getId());
        verify(courseRepository, times(1)).findById(101);
    }

    @Test
    void testSaveCourse() {
        when(courseRepository.save(sampleCourse)).thenReturn(sampleCourse);

        Course saved = courseService.save(sampleCourse);

        assertNotNull(saved);
        assertEquals("Computer Science 101", saved.getTitle());
        verify(courseRepository, times(1)).save(sampleCourse);
    }

    @Test
    void testDeleteById() {
        doNothing().when(courseRepository).deleteById(101);

        courseService.deleteById(101);

        verify(courseRepository, times(1)).deleteById(101);
    }
}
