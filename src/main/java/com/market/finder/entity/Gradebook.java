package com.market.finder.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "gradebook")
public class Gradebook {
    @EmbeddedId
    private GradebookId id = new GradebookId();

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

    @NotNull(message = "Score is required")
    @DecimalMin(value = "0.00", message = "Score cannot be negative")
    @DecimalMax(value = "100.00", message = "Score cannot exceed 100")
    @Column(precision = 5, scale = 2)
    private BigDecimal score;

    public void prepareId() {
        if (id == null) {
            id = new GradebookId();
        }
        if (student != null) {
            id.setStudentId(student.getId());
        }
        if (course != null) {
            id.setCourseId(course.getId());
        }
    }
}