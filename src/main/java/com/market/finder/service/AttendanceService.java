package com.market.finder.service;

import com.market.finder.entity.Attendance;
import com.market.finder.entity.AttendanceId;
import com.market.finder.service.base.BaseService;

public interface AttendanceService extends BaseService<Attendance, AttendanceId> {
    boolean existsById(AttendanceId id);
}
