package com.test.example.service.impl;

import com.test.example.model.Course;
import com.test.example.model.enums.CourseType;
import com.test.example.repository.CourseRepository;
import com.test.example.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    @Transactional(readOnly = true)
    public int getCoursesCountBy(CourseType courseType) {
        return courseRepository.countByCourseType(courseType);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> findCourseBy(String courseName) {
        return courseRepository.findByName(courseName);
    }

    @Override
    @Transactional
    public Set<Course> saveCourses(Set<Course> courses) {
        if (CollectionUtils.isEmpty(courses)) {
            return new HashSet<>();
        }

        Set<Course> savedCourses = new HashSet<>();
        for (Course course : courses) {
            Optional<Course> existingCourseOpt = courseRepository.findByName(course.getName());
            if (existingCourseOpt.isPresent()) {
                var existingCourse = existingCourseOpt.get();
                existingCourse.setCourseType(course.getCourseType());
                savedCourses.add(courseRepository.save(existingCourse));
            } else {
                savedCourses.add(courseRepository.save(course));
            }
        }
        return savedCourses;
    }

}
