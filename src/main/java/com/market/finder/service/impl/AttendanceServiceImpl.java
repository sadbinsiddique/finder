package com.market.finder.service.impl;

import com.market.finder.dao.AttendanceRepository;
import com.market.finder.entity.Attendance;
import com.market.finder.entity.AttendanceId;
import com.market.finder.service.AttendanceService;
import com.market.finder.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AttendanceServiceImpl extends BaseServiceImpl<Attendance, AttendanceId, AttendanceRepository> implements AttendanceService {

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository) {
        super(attendanceRepository);
    }

    @Override
    public List<Attendance> findAll() {
        return super.findAll();
    }

    @Override
    public Optional<Attendance> findById(AttendanceId id) {
        return super.findById(id);
    }

    @Override
    @Transactional
    public Attendance save(Attendance attendance) {
        return repository.saveAndFlush(attendance);
    }

    @Override
    @Transactional
    public void deleteById(AttendanceId id) {
        super.deleteById(id);
        repository.flush();
    }

    @Override
    public boolean existsById(AttendanceId id) {
        return repository.existsById(id);
    }
}
