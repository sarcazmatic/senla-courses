package com.senla.courses.controller.student;

import com.senla.courses.dto.StudentsCoursesDTO;
import com.senla.courses.service.students.StudentService;
import com.senla.courses.util.UserDetailsExtractor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class StudentsCoursesController {

    private final StudentService studentService;

    @PostMapping("/request/{studId}/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Long courseRequest(@PathVariable Long studId,
                              @PathVariable Long courseId,
                              HttpServletRequest request) {
        log.info("Получен запрос на регистрацию заявки на доступ для студента с id: {} на курс с id: {}. Эндпоинт {}. Метод {}",
                studId, courseId, request.getRequestURL(), request.getMethod());
        User user = UserDetailsExtractor.extractUserDetails();
        return studentService.registerCourseRequest(user, studId, courseId);
    }

    @GetMapping("/{studId}/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public StudentsCoursesDTO findByIds(@PathVariable Long studId,
                                        @PathVariable Long courseId,
                                        HttpServletRequest request) {
        log.info("Получен запрос на получение информации о статусе студенте с id: {} и курса с id: {}. Эндпоинт {}. Метод {}",
                studId, courseId, request.getRequestURL(), request.getMethod());
        User user = UserDetailsExtractor.extractUserDetails();
        return studentService.findStudentsCoursesById(user, studId, courseId);
    }

}
