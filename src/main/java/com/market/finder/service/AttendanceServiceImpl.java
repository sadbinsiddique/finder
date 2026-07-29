package com.market.finder.service;

import com.market.finder.dao.AttendanceRepository;
import com.market.finder.entity.Attendance;
import com.market.finder.entity.AttendanceId;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    @Cacheable(value = "attendance", key = "'all'")
    public List<Attendance> findAll() {
        return attendanceRepository.findAll();
    }

    @Override
    @Cacheable(value = "attendance", key = "#id")
    public Optional<Attendance> findById(AttendanceId id) {
        return attendanceRepository.findById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "attendance", allEntries = true)
    public Attendance save(Attendance attendance) {
        return attendanceRepository.saveAndFlush(attendance);
    }

    @Override
    @Transactional
    @CacheEvict(value = "attendance", allEntries = true)
    public void deleteById(AttendanceId id) {
        attendanceRepository.deleteById(id);
        attendanceRepository.flush();
    }

    @Override
    public boolean existsById(AttendanceId id) {
        return attendanceRepository.existsById(id);
    }
}
