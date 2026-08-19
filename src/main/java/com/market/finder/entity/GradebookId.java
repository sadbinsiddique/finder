package com.market.finder.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GradebookId implements Serializable {
    @NotNull(message = "Student ID is required")
    @Column(name = "student_id")
    private Integer studentId;

    @NotNull(message = "Course ID is required")
    @Column(name = "course_id")
    private Integer courseId;

    @NotBlank(message = "Assignment name is required")
    @Size(max = 100, message = "Assignment name cannot exceed 100 characters")
    @Column(name = "assignment_name", length = 100)
    private String assignmentName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradebookId that = (GradebookId) o;
        return Objects.equals(studentId, that.studentId) &&
                Objects.equals(courseId, that.courseId) &&
                Objects.equals(assignmentName, that.assignmentName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseId, assignmentName);
    }
}
