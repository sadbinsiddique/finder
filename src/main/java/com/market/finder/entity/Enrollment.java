package com.market.finder.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "enrollment")
public class Enrollment {
    @EmbeddedId
    private EnrollmentId id = new EnrollmentId();

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

    @NotNull(message = "Enrollment date is required")
    @PastOrPresent(message = "Enrollment date cannot be in the future")
    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    public void prepareId() {
        if (id == null) {
            id = new EnrollmentId();
        }
        if (student != null) {
            id.setStudentId(student.getId());
        }
        if (course != null) {
            id.setCourseId(course.getId());
        }
    }
}