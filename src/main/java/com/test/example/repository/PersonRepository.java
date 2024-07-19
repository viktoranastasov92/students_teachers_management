package com.test.example.repository;

import com.test.example.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {

    @Query("SELECT COUNT(s) FROM Student s")
    long countStudents();

    @Query("SELECT COUNT(t) FROM Teacher t")
    long countTeachers();

    @Query("SELECT s FROM Student s")
    Set<Student> findAllStudents();

    @Query("SELECT t FROM Teacher t")
    Set<Teacher> findAllTeachers();

    Set<Student> findStudentsByCourses(Course course);
    Set<Student> findStudentsByGroup(Group group);
    Set<Student> findStudentsByAgeGreaterThanAndCourses(int age, Course course);

    Set<Teacher> findTeachersByCourses(Course course);
    Set<Teacher> findTeachersByGroup(Group group);
}
