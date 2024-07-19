package com.test.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public abstract class Person {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private Integer age;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
}
