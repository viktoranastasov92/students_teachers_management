package com.test.example.controller;

import com.test.example.dto.PeopleDataDto;
import com.test.example.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final PersonService personService;

    @GetMapping("/people")
    public PeopleDataDto getPeople(@RequestParam(required = false) String courseName, @RequestParam(required = false) String groupName) {
        var peopleDataDto = new PeopleDataDto();
        peopleDataDto.setStudents(personService.getStudentsBy(courseName, groupName));
        peopleDataDto.setTeachers(personService.getTeachersBy(courseName, groupName));
        return peopleDataDto;
    }

}
