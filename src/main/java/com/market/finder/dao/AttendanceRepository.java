package com.market.finder.dao;

import com.market.finder.entity.Attendance;
import com.market.finder.entity.AttendanceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceId> {
    List<Attendance> findByStudent_Id(Integer studentId);

    List<Attendance> findByCourse_Id(Integer courseId);
}