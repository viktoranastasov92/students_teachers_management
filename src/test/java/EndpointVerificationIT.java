import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.example.SpringTestApplication;
import com.test.example.dto.PeopleDataDto;
import com.test.example.model.Course;
import com.test.example.model.Group;
import com.test.example.model.Student;
import com.test.example.model.Teacher;
import com.test.example.model.enums.CourseType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SpringTestApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EndpointVerificationIT {

    private final Course calculusCourse = createCourse("Calculus", CourseType.MAIN);
    private final Course algebraCourse = createCourse("Algebra", CourseType.MAIN);
    private final Course paintingCourse = createCourse("Painting", CourseType.SECONDARY);
    private final Group groupA = createGroup("Group A");
    private final Group groupB = createGroup("Group B");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void testCreateStudents() throws Exception {
        Student student1 = createStudent("Ivan Ivanov", 20, groupA, Set.of(calculusCourse, algebraCourse));
        Student student2 = createStudent("Petar Petrov", 21, groupA, Set.of(calculusCourse, algebraCourse));
        Student student3 = createStudent("Georgi Georgiev", 22, groupB, Set.of(paintingCourse));
        Student student4 = createStudent("Ivaylo Ivanov", 30, groupB, new HashSet<>());

        executeStudentPostRequestWithResponseCheckByName(student1);
        executeStudentPostRequestWithResponseCheckByName(student2);
        executeStudentPostRequestWithResponseCheckByName(student3);
        executeStudentPostRequestWithResponseCheckByName(student4);
    }

    @Test
    @Order(2)
    void testCreateTeachers() throws Exception {
        Teacher teacher1 = createTeacher("Professor X", 55, groupA, Set.of(calculusCourse, algebraCourse));
        Teacher teacher2 = createTeacher("Doctor Strange", 41, groupA, Set.of(paintingCourse));

        executeTeacherPostRequestWithResponseCheckByName(teacher1);
        executeTeacherPostRequestWithResponseCheckByName(teacher2);
    }

    @Test
    @Order(3)
    void testStudentsCount() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/students/count"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        long studentsCount = Long.parseLong(jsonResponse);

        assertThat(studentsCount).isEqualTo(4);
    }

    @Test
    @Order(4)
    void testTeachersCount() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/teachers/count"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        long teachersCount = Long.parseLong(jsonResponse);

        assertThat(teachersCount).isEqualTo(2);
    }

    @Test
    @Order(5)
    void testCoursesByType() throws Exception {
        assertCourseByType(CourseType.MAIN, 2);
        assertCourseByType(CourseType.SECONDARY, 1);
    }

    @Test
    @Order(6)
    void testStudentsByCourse() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/students")
                .param("courseName", algebraCourse.getName()))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Student[] students = objectMapper.readValue(jsonResponse, Student[].class);

        assertThat(students).hasSize(2);
        assertThat(students)
                .extracting("name")
                .containsExactlyInAnyOrder("Ivan Ivanov", "Petar Petrov");
    }

    @Test
    @Order(7)
    void testStudentsByGroup() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/students")
                .param("groupName", groupB.getName()))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Student[] students = objectMapper.readValue(jsonResponse, Student[].class);

        assertThat(students).hasSize(2);
        assertThat(students)
                .extracting("name")
                .containsExactlyInAnyOrder("Georgi Georgiev", "Ivaylo Ivanov");
    }

    @Test
    @Order(8)
    void testPeopleByGroupAndCourse() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/reports/people")
                .param("groupName", groupA.getName())
                .param("courseName", calculusCourse.getName()))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        PeopleDataDto peopleDataDto = objectMapper.readValue(jsonResponse, PeopleDataDto.class);

        assertThat(peopleDataDto.getStudents()).hasSize(2);
        assertThat(peopleDataDto.getStudents())
                .extracting("name")
                .containsExactlyInAnyOrder("Ivan Ivanov", "Petar Petrov");

        assertThat(peopleDataDto.getTeachers()).hasSize(1);
        assertThat(peopleDataDto.getTeachers().stream().findFirst().get().getName()).isEqualTo("Professor X");
    }

    @Test
    @Order(9)
    void testStudentsOlderThanAgeAndByCourse() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/students")
                .param("courseName", calculusCourse.getName())
                .param("ageLowerLimit", "20"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Student[] students = objectMapper.readValue(jsonResponse, Student[].class);

        assertThat(students).hasSize(1);
        assertThat(students[0].getName()).isEqualTo("Petar Petrov");
    }

    private void assertCourseByType(CourseType courseType, int expectedCourseCount) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/courses/count")
                .param("courseType", courseType.toString()))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        int mainCoursesCount = Integer.parseInt(jsonResponse);

        assertThat(mainCoursesCount).isEqualTo(expectedCourseCount);
    }

    private void executeStudentPostRequestWithResponseCheckByName(Student student) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/students")
                .content(asJsonString(student))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Student resultStudent = objectMapper.readValue(jsonResponse, Student.class);
        assertThat(resultStudent.getName()).isEqualTo(student.getName());
    }

    private void executeTeacherPostRequestWithResponseCheckByName(Teacher teacher) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/teachers")
                .content(asJsonString(teacher))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Teacher resultTeacher = objectMapper.readValue(jsonResponse, Teacher.class);
        assertThat(resultTeacher.getName()).isEqualTo(teacher.getName());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Student createStudent(String name, int age, Group group, Set<Course> courses) {
        Student student = new Student();
        student.setName(name);
        student.setAge(age);
        student.setGroup(group);
        student.setCourses(courses);
        return student;
    }

    private Teacher createTeacher(String name, int age, Group group, Set<Course> courses) {
        Teacher teacher = new Teacher();
        teacher.setName(name);
        teacher.setAge(age);
        teacher.setGroup(group);
        teacher.setCourses(courses);
        return teacher;
    }

    private Group createGroup(String groupName) {
        Group group = new Group();
        group.setName(groupName);
        return group;
    }

    private Course createCourse(String courseName, CourseType courseType) {
        Course course = new Course();
        course.setName(courseName);
        course.setCourseType(courseType);
        return course;
    }

}
