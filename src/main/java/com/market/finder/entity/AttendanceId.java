package com.market.finder.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceId implements Serializable {
    @NotNull(message = "Student ID is required")
    @Column(name = "student_id")
    private Integer studentId;

    @NotNull(message = "Course ID is required")
    @Column(name = "course_id")
    private Integer courseId;

    @NotNull(message = "Attendance date is required")
    @Column(name = "attendance_date")
    private LocalDate attendanceDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendanceId that = (AttendanceId) o;
        return Objects.equals(studentId, that.studentId) &&
                Objects.equals(courseId, that.courseId) &&
                Objects.equals(attendanceDate, that.attendanceDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseId, attendanceDate);
    }
}
