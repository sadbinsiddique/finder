package com.market.finder.service;

import com.market.finder.dao.StaffRepository;
import com.market.finder.entity.Staff;
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
class StaffServiceTest {

    @Mock
    private StaffRepository staffRepository;

    @InjectMocks
    private StaffServiceImpl staffService;

    private Staff sampleStaff;

    @BeforeEach
    void setUp() {
        sampleStaff = new Staff();
        sampleStaff.setId(1);
        sampleStaff.setFirstName("Sarah");
        sampleStaff.setLastName("Conner");
        sampleStaff.setAge(30);
        sampleStaff.setIncome(50000.0);
    }

    @Test
    void testFindAll() {
        when(staffRepository.findAll()).thenReturn(List.of(sampleStaff));

        List<Staff> list = staffService.findAll();

        assertEquals(1, list.size());
        assertEquals("Sarah", list.getFirst().getFirstName());
        verify(staffRepository, times(1)).findAll();
    }

    @Test
    void testFindAllByOrderByAgeAsc() {
        when(staffRepository.findAllByOrderByAgeAsc()).thenReturn(List.of(sampleStaff));

        List<Staff> list = staffService.findAllByOrderByAgeAsc();

        assertEquals(1, list.size());
        assertEquals(30, list.getFirst().getAge());
        verify(staffRepository, times(1)).findAllByOrderByAgeAsc();
    }

    @Test
    void testFindAllByOrderByIncomeAsc() {
        when(staffRepository.findAllByOrderByIncomeAsc()).thenReturn(List.of(sampleStaff));

        List<Staff> list = staffService.findAllByOrderByIncomeAsc();

        assertEquals(1, list.size());
        assertEquals(50000.0, list.getFirst().getIncome());
        verify(staffRepository, times(1)).findAllByOrderByIncomeAsc();
    }

    @Test
    void testFindById() {
        when(staffRepository.findById(1)).thenReturn(Optional.of(sampleStaff));

        Optional<Staff> opt = staffService.findById(1);

        assertTrue(opt.isPresent());
        assertEquals("Conner", opt.get().getLastName());
        verify(staffRepository, times(1)).findById(1);
    }

    @Test
    void testSave() {
        when(staffRepository.save(sampleStaff)).thenReturn(sampleStaff);

        Staff saved = staffService.save(sampleStaff);

        assertNotNull(saved);
        assertEquals("Sarah", saved.getFirstName());
        verify(staffRepository, times(1)).save(sampleStaff);
    }

    @Test
    void testDeleteById() {
        doNothing().when(staffRepository).deleteById(1);

        staffService.deleteById(1);

        verify(staffRepository, times(1)).deleteById(1);
    }
}
