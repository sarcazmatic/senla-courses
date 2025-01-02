package com.senla.courses.controller.student;

import com.senla.courses.dto.UserDTO;
import com.senla.courses.model.StudentsCourses;
import com.senla.courses.service.students.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/student/course")
@AllArgsConstructor
public class StudentCourseController {

    private final StudentService studentService;

    @PostMapping("/register/{studId}/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Long courseRequest(@PathVariable Long studId,
                              @PathVariable Long courseId) {
        return studentService.registerCourseRequest(studId, courseId);
    }

    @GetMapping("/{studId}/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentsCourses findCourseStudents(@PathVariable Long studId,
                                              @PathVariable Long courseId) {
        return studentService.findStudentsCoursesById(studId, courseId);
    }

}
