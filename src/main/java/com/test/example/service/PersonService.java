package com.test.example.service;

import com.test.example.model.Course;
import com.test.example.model.Person;
import com.test.example.model.Student;
import com.test.example.model.Teacher;

import java.util.Set;
import java.util.UUID;

public interface PersonService {

    Person createPerson(Person person, Set<Course> courses);

    Person updatePerson(UUID id, Person person, Set<Course> courses);

    void deletePerson(UUID id);

    long countStudents();

    long countTeachers();

    Set<Student> getStudentsBy(String courseName, String groupName);
    Set<Student> getStudentsBy(Integer ageLowerLimit, String courseName);
    Set<Student> getStudents(String courseName, String groupName, Integer ageLowerLimit);

    Set<Teacher> getTeachersBy(String courseName, String groupName);
}
