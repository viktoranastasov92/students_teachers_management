package com.test.example.service;

import com.test.example.model.Course;
import com.test.example.model.enums.CourseType;

import java.util.Optional;
import java.util.Set;

public interface CourseService {
    int getCoursesCountBy(CourseType courseType);

    Optional<Course> findCourseBy(String courseName);

    Set<Course> saveCourses(Set<Course> courses);
}
