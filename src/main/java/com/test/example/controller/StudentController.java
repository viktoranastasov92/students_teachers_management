package com.test.example.controller;

import com.test.example.model.Person;
import com.test.example.model.Student;
import com.test.example.service.PersonService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final PersonService personService;

    @GetMapping
    public Set<Student> getStudents(@RequestParam(required = false) String courseName,
                                    @RequestParam(required = false) String groupName,
                                    @RequestParam(required = false) Integer ageLowerLimit) {
        return personService.getStudents(courseName, groupName, ageLowerLimit);
    }

    @PostMapping
    public Person createStudent(@RequestBody Student student) {
        return personService.createPerson(student, student.getCourses());
    }

    @PutMapping("/{id}")
    public Person updateStudent(@PathVariable UUID id, @RequestBody Student student) {
        return personService.updatePerson(id, student, student.getCourses());
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable UUID id) {
        personService.deletePerson(id);
    }

    @GetMapping("/count")
    public long getStudentsCount() {
        return personService.countStudents();
    }

}
