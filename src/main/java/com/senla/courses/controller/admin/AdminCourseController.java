package com.senla.courses.controller.admin;

import com.senla.courses.dto.CourseDTO;
import com.senla.courses.dto.StudentsCoursesDTO;
import com.senla.courses.service.courses.CourseService;
import com.senla.courses.service.students.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/admin/course")
@AllArgsConstructor
@Slf4j
public class AdminCourseController {

    private final CourseService courseService;
    private final StudentService studentService;


    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Long addCourse(@RequestBody @Valid CourseDTO courseDTO, HttpServletRequest request) {
        log.info("Получен запрос на добавление нового курса: {}. Эндпоинт {}. Метод {}",
                courseDTO, request.getRequestURL(), request.getMethod());
        return courseService.addCourse(courseDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CourseDTO editCourse(@RequestBody @Valid CourseDTO courseDTO,
                                @PathVariable Long id,
                                HttpServletRequest request) {
        log.info("Получен запрос на редактирование курса с id: {}. Эндпоинт {}. Метод {}",
                id, request.getRequestURL(), request.getMethod());
        return courseService.editCourse(courseDTO, id);
    }

    @PutMapping("/{id}/teachers/add")
    @ResponseStatus(HttpStatus.OK)
    public CourseDTO addTeachers(@PathVariable Long id, @RequestParam List<Long> ids, HttpServletRequest request) {
        log.info("Получен запрос на добавление преподавателей в курс с id: {}. id преподавателей: {}. Эндпоинт {}. Метод {}",
                id, ids, request.getRequestURL(), request.getMethod());
        return courseService.addTeachers(id, ids);
    }

    @PutMapping("/{id}/teachers/rm")
    @ResponseStatus(HttpStatus.OK)
    public CourseDTO removeTeachers(@PathVariable Long id, @RequestParam List<Long> ids, HttpServletRequest request) {
        log.info("Получен запрос на удаление преподавателей из курса с id: {}. id преподавателей: {}. Эндпоинт {}. Метод {}",
                id, ids, request.getRequestURL(), request.getMethod());
        return courseService.removeTeachers(id, ids);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCourse(@PathVariable("id") Long id, HttpServletRequest request) {
        log.info("Получен запрос на удаление курса с id: {}. Эндпоинт {}. Метод {}",
                id, request.getRequestURL(), request.getMethod());
        courseService.deleteCourse(id);
    }

    @GetMapping("/student/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentsCoursesDTO> findStudentsCourses(@PathVariable Long courseId, HttpServletRequest request) {
        log.info("Получен запрос на получение студентов курса с id: {}. Эндпоинт {}. Метод {}",
                courseId, request.getRequestURL(), request.getMethod());
        return studentService.findStudentsCoursesByCourseId(courseId);
    }

    @PutMapping("/student/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public Integer updateRequest(@PathVariable Long courseId,
                                 @RequestParam(name = "ids") List<Long> ids,
                                 @RequestParam(name = "response", defaultValue = "APPROVED") String response,
                                 HttpServletRequest request) {
        log.info("Получен запрос на обновление статуса запросов для курса с id: {}. id студентов: {}, Ответ: {}. Эндпоинт {}. Метод {}",
                courseId, ids, response, request.getRequestURL(), request.getMethod());
        return studentService.updateRequest(courseId, ids, response);
    }

}
