package com.market.finder.service;

import com.market.finder.dao.DepartmentRepository;
import com.market.finder.entity.Department;
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
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department sampleDept;

    @BeforeEach
    void setUp() {
        sampleDept = new Department();
        sampleDept.setId(1);
        sampleDept.setName("Computer Science");
    }

    @Test
    void testFindAllDepartments() {
        when(departmentRepository.findAll()).thenReturn(List.of(sampleDept));

        List<Department> list = departmentService.findAll();

        assertEquals(1, list.size());
        assertEquals("Computer Science", list.getFirst().getName());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Found() {
        when(departmentRepository.findById(1)).thenReturn(Optional.of(sampleDept));

        Optional<Department> opt = departmentService.findById(1);

        assertTrue(opt.isPresent());
        assertEquals("Computer Science", opt.get().getName());
        verify(departmentRepository, times(1)).findById(1);
    }

    @Test
    void testSaveDepartment() {
        when(departmentRepository.save(sampleDept)).thenReturn(sampleDept);

        Department saved = departmentService.save(sampleDept);

        assertNotNull(saved);
        assertEquals("Computer Science", saved.getName());
        verify(departmentRepository, times(1)).save(sampleDept);
    }

    @Test
    void testDeleteById() {
        doNothing().when(departmentRepository).deleteById(1);

        departmentService.deleteById(1);

        verify(departmentRepository, times(1)).deleteById(1);
    }
}
