package com.test.example.repository;

import com.test.example.model.Course;
import com.test.example.model.enums.CourseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    int countByCourseType(CourseType courseType);

    Optional<Course> findByName(String courseName);
}
