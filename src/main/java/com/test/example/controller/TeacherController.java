package com.test.example.controller;


import com.test.example.model.Person;
import com.test.example.model.Teacher;
import com.test.example.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final PersonService personService;

    @GetMapping
    public Set<Teacher> getTeachers(@RequestParam(required = false) String courseName,
                                    @RequestParam(required = false) String groupName) {
        return personService.getTeachersBy(courseName, groupName);
    }

    @PostMapping
    public Person createTeacher(@RequestBody Teacher teacher) {
        return personService.createPerson(teacher, teacher.getCourses());
    }

    @PutMapping("/{id}")
    public Person updateTeacher(@PathVariable UUID id, @RequestBody Teacher teacher) {
        return personService.updatePerson(id, teacher, teacher.getCourses());
    }

    @DeleteMapping("/{id}")
    public void deleteTeacher(@PathVariable UUID id) {
        personService.deletePerson(id);
    }

    @GetMapping("/count")
    public long getTeachersCount() {
        return personService.countTeachers();
    }

}
