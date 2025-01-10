package com.senla.courses.controller.student;

import com.senla.courses.dto.StudentsCoursesDTO;
import com.senla.courses.service.students.StudentService;
import com.senla.courses.util.UserDetailsExtractor;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/student/course")
@AllArgsConstructor
public class StudentsCoursesController {

    private final StudentService studentService;

    @PostMapping("/request/{studId}/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Long courseRequest(@PathVariable Long studId,
                              @PathVariable Long courseId) {
        User user = UserDetailsExtractor.extractUserDetails();
        return studentService.registerCourseRequest(user, studId, courseId);
    }

    @GetMapping("/{studId}/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentsCoursesDTO findCourseStudents(@PathVariable Long studId,
                                                 @PathVariable Long courseId) {
        User user = UserDetailsExtractor.extractUserDetails();
        return studentService.findStudentsCoursesById(user, studId, courseId);
    }

}
