package com.test.example.controller;

import com.test.example.model.enums.CourseType;
import com.test.example.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/count")
    public int getCoursesCountBy(@RequestParam CourseType courseType) {
        return courseService.getCoursesCountBy(courseType);
    }

}
