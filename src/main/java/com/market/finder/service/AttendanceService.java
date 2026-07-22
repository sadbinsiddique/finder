package com.market.finder.service;

import com.market.finder.entity.Attendance;
import com.market.finder.entity.AttendanceId;

import java.util.List;
import java.util.Optional;

public interface AttendanceService {

    List<Attendance> findAll();

    Optional<Attendance> findById(AttendanceId id);

    Attendance save(Attendance attendance);

    void deleteById(AttendanceId id);

    boolean existsById(AttendanceId id);
}
