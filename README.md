We need an application for storing Students and Teachers information like name, age, group and courses.

We have two type of courses -Main and Secondary.

The application should be able to add remove or modify students and teachers.

With this application we should be able to create different reports :
• how many students we have

```curl "http://localhost:8080/students/count"```

• how many teachers we have

```curl "http://localhost:8080/teachers/count"```

• how many courses by type we have

```curl "http://localhost:8080/courses/count?courseType=MAIN"```

• which students participate in specific course

```curl "http://localhost:8080/students?courseName=Algebra"```

• which students participate in specific group

```curl "http://localhost:8080/students?groupName=GroupA"```

• find all teachers and students for specific group and course

```curl "http://localhost:8080/reports/people?groupName=GroupA&courseName=Algebra"```

• find all students older than specific age and participate in specific course

```curl "http://localhost:8080/students?ageLowerLimit=19&courseName=Algebra"```

The application should provide public API.

In order to start the MySQL Docker conainer, go to the folder where ```docker-compose.yml``` file is and execute ```docker-compose up```