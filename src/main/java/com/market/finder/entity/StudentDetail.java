package com.market.finder.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "student_detail")
public class StudentDetail {
    @Id
    @Column(name = "student_id")
    private Integer studentId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "student_id")
    private Student student;

    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Blood group must be valid (e.g. A+, O-, AB+)")
    @Size(max = 5, message = "Blood group cannot exceed 5 characters")
    @Column(name = "blood_group", length = 5)
    private String bloodGroup;

    @Size(max = 255, message = "Address cannot exceed 255 characters")
    @Column(length = 255)
    private String address;
}