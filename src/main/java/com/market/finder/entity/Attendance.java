package com.market.finder.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "attendance")
public class Attendance {
    @EmbeddedId
    private AttendanceId id = new AttendanceId();

    @NotNull(message = "Student is required")
    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;

    @NotNull(message = "Course is required")
    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    @NotNull(message = "Attendance status is required")
    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    public void prepareId() {
        if (id == null) {
            id = new AttendanceId();
        }
        if (student != null) {
            id.setStudentId(student.getId());
        }
        if (course != null) {
            id.setCourseId(course.getId());
        }
    }
}