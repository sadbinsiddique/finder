package com.market.finder.service;

import com.market.finder.dao.InstructorRepository;
import com.market.finder.entity.Instructor;
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
class InstructorServiceTest {

    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private InstructorServiceImpl instructorService;

    private Instructor sampleInstructor;

    @BeforeEach
    void setUp() {
        sampleInstructor = new Instructor();
        sampleInstructor.setId(1);
        sampleInstructor.setFirstName("John");
        sampleInstructor.setLastName("Doe");
        sampleInstructor.setEmail("john.doe@university.edu");
    }

    @Test
    void testFindAllInstructors() {
        when(instructorRepository.findAll()).thenReturn(List.of(sampleInstructor));

        List<Instructor> instructors = instructorService.findAll();

        assertEquals(1, instructors.size());
        assertEquals("John", instructors.get(0).getFirstName());
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Found() {
        when(instructorRepository.findById(1)).thenReturn(Optional.of(sampleInstructor));

        Optional<Instructor> result = instructorService.findById(1);

        assertTrue(result.isPresent());
        assertEquals("Doe", result.get().getLastName());
        verify(instructorRepository, times(1)).findById(1);
    }

    @Test
    void testSaveInstructor() {
        when(instructorRepository.save(sampleInstructor)).thenReturn(sampleInstructor);

        Instructor saved = instructorService.save(sampleInstructor);

        assertNotNull(saved);
        assertEquals("John", saved.getFirstName());
        verify(instructorRepository, times(1)).save(sampleInstructor);
    }

    @Test
    void testDeleteById() {
        doNothing().when(instructorRepository).deleteById(1);

        instructorService.deleteById(1);

        verify(instructorRepository, times(1)).deleteById(1);
    }
}
