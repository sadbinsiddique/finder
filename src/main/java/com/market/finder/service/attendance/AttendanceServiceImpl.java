package com.market.finder.service.attendance;

import com.market.finder.entity.Attendance;
import com.market.finder.entity.AttendanceId;
import com.market.finder.repository.AttendanceRepository;
import com.market.finder.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AttendanceServiceImpl extends BaseServiceImpl<Attendance, AttendanceId, AttendanceRepository> implements AttendanceService {

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository) {
        super(attendanceRepository);
    }

    @Override
    public boolean existsById(AttendanceId id) {
        return repository.existsById(id);
    }
}
