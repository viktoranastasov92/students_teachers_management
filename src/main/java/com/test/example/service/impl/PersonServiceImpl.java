package com.test.example.service.impl;

import com.test.example.exception.InputValidationException;
import com.test.example.exception.NotFoundException;
import com.test.example.model.*;
import com.test.example.repository.PersonRepository;
import com.test.example.service.CourseService;
import com.test.example.service.GroupService;
import com.test.example.service.PersonService;
import com.test.example.validation.InputValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@FunctionalInterface
interface FindByCourse<T> {
    Set<T> findByCourse(Course course);
}

@FunctionalInterface
interface FindByGroup<T> {
    Set<T> findByGroup(Group group);
}

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final CourseService courseService;
    private final GroupService groupService;

    @Override
    @Transactional
    public Person createPerson(Person person, Set<Course> courses) {
        var savedGroup = groupService.saveGroup(person.getGroup());
        person.setGroup(savedGroup);

        Set<Course> savedCourses = courseService.saveCourses(courses);
        assignCoursesToPersonSubclass(person, savedCourses);


        return personRepository.save(person);
    }

    @Override
    @Transactional
    public Person updatePerson(UUID id, Person person, Set<Course> courses) {
        var existingPerson = personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Person not found with id " + id));

        var savedGroup = groupService.saveGroup(person.getGroup());
        Set<Course> savedCourses = courseService.saveCourses(courses);

        existingPerson.setAge(person.getAge());
        existingPerson.setName(person.getName());
        existingPerson.setGroup(savedGroup);
        assignCoursesToPersonSubclass(person, savedCourses);
        return personRepository.save(existingPerson);
    }

    @Override
    @Transactional
    public void deletePerson(UUID id) {
        personRepository.deleteById(id);
    }

    @Override
    public long countStudents() {
        return personRepository.countStudents();
    }

    @Override
    public long countTeachers() {
        return personRepository.countTeachers();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Student> getStudentsBy(String courseName, String groupName) {
        if (!StringUtils.hasText(courseName) && !StringUtils.hasText(groupName)) {
            return personRepository.findAllStudents();
        }
        return getPeopleBy(courseName, groupName, personRepository::findStudentsByCourses, personRepository::findStudentsByGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Student> getStudentsBy(Integer ageLowerLimit, String courseName) {
        Optional<Course> courseOpt = courseService.findCourseBy(courseName);
        if (courseOpt.isEmpty()) {
            return new HashSet<>();
        }

        return personRepository.findStudentsByAgeGreaterThanAndCourses(ageLowerLimit, courseOpt.get());
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Student> getStudents(String courseName, String groupName, Integer ageLowerLimit) {
        InputValidator.validateStudentInput(groupName, ageLowerLimit);

        if (ageLowerLimit != null) {
            return getStudentsBy(ageLowerLimit, courseName);
        }

        return getStudentsBy(courseName, groupName);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Teacher> getTeachersBy(String courseName, String groupName) {
        if (!StringUtils.hasText(courseName) && !StringUtils.hasText(groupName)) {
            return personRepository.findAllTeachers();
        }
        return getPeopleBy(courseName, groupName, personRepository::findTeachersByCourses, personRepository::findTeachersByGroup);
    }

    private <T> Set<T> getPeopleBy(String courseName, String groupName, FindByCourse<T> findByCourse, FindByGroup<T> findByGroup) {
        Set<T> resultPeople = new HashSet<>();

        if (!StringUtils.hasText(courseName) && !StringUtils.hasText(groupName)) {
            throw new InputValidationException("Cannot search by empty course name and empty group name");
        }

        if (StringUtils.hasText(courseName)) {
            resultPeople.addAll(getPeopleByCourse(courseName, findByCourse));
        }

        if (StringUtils.hasText(groupName)) {
            Set<T> peopleByGroup = getPeopleByGroup(groupName, findByGroup);

            if (StringUtils.hasText(courseName)) {
                resultPeople.retainAll(peopleByGroup);
            } else {
                resultPeople.addAll(peopleByGroup);
            }
        }

        return resultPeople;
    }

    private <T> Set<T> getPeopleByGroup(String groupName, FindByGroup<T> findByGroup) {
        Optional<Group> groupOpt = groupService.findGroupBy(groupName);
        if (groupOpt.isEmpty()) {
            return new HashSet<>();
        }
        return findByGroup.findByGroup(groupOpt.get());
    }

    private <T> Set<T> getPeopleByCourse(String courseName, FindByCourse<T> findByCourse) {
        Optional<Course> courseOpt = courseService.findCourseBy(courseName);
        if (courseOpt.isEmpty()) {
            return new HashSet<>();
        }
        return findByCourse.findByCourse(courseOpt.get());
    }

    private void assignCoursesToPersonSubclass(Person person, Set<Course> courses) {
        if (person instanceof Student student) {
            student.setCourses(courses);
        } else if (person instanceof Teacher teacher) {
            teacher.setCourses(courses);
        }
    }

}
