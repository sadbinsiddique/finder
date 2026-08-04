package com.market.finder.dto;

import com.market.finder.entity.Attendance;
import com.market.finder.entity.AttendanceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceId> {
}
