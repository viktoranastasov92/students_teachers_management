package com.test.example.dto;

import com.test.example.model.Student;
import com.test.example.model.Teacher;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PeopleDataDto {
    private Set<Student> students;
    private Set<Teacher> teachers;
}
