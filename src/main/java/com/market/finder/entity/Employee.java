package com.market.finder.entity;

import com.market.finder.entity.base.BasePersonEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "employee")
@AttributeOverride(name = "firstName", column = @Column(name = "fast_name", length = 45, nullable = false))
public class Employee extends BasePersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Employee(String firstName, String lastName, String email) {
        super(firstName, lastName, email);
    }
}
