package com.test.example.model;

import com.test.example.model.enums.CourseType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "course", indexes = {
        @jakarta.persistence.Index(name = "idx_course_type", columnList = "course_type")
})
public class Course {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "course_type")
    @NotNull
    private CourseType courseType;

}
