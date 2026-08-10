package com.market.finder.entity;

import jakarta.persistence.*;
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

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

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