package com.senla.courses.controller.admin;

import com.senla.courses.dto.CourseDTO;
import com.senla.courses.dto.StudentsCoursesDTO;
import com.senla.courses.service.courses.CourseService;
import com.senla.courses.service.students.StudentService;
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
    public Long addCourse(@RequestBody @Valid CourseDTO courseDTO) {
        log.info("Получен запрос на добавление нового курса: {}", courseDTO);
        Long id = courseService.addCourse(courseDTO);
        log.info("Курс успешно добавлен с id: {}", id);
        return id;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CourseDTO editCourse(@RequestBody @Valid CourseDTO courseDTO,
                                @PathVariable Long id) {
        log.info("Получен запрос на редактирование курса с id: {}", id);
        CourseDTO courseUpd = courseService.editCourse(courseDTO, id);
        log.info("Курс успешно обновлен с id: {}", id);
        return courseUpd;
    }

    @PutMapping("/{id}/teachers/add")
    @ResponseStatus(HttpStatus.OK)
    public CourseDTO addTeachers(@PathVariable Long id, @RequestParam List<Long> ids) {
        log.info("Получен запрос на добавление преподавателей в курс с id: {}. id преподавателей: {}", id, ids);
        CourseDTO courseUpd = courseService.addTeachers(id, ids);
        log.info("Преподаватели успешно добавлены в курс с id: {}", id);
        return courseUpd;
    }

    @PutMapping("/{id}/teachers/rm")
    @ResponseStatus(HttpStatus.OK)
    public CourseDTO removeTeachers(@PathVariable Long id, @RequestParam List<Long> ids) {
        log.info("Получен запрос на удаление преподавателей из курса с id: {}. id преподавателей: {}", id, ids);
        CourseDTO courseUpd = courseService.removeTeachers(id, ids);
        log.info("Преподаватели успешно удалены из курса с id: {}", id);
        return courseUpd;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCourse(@PathVariable("id") Long id) {
        log.info("Получен запрос на удаление курса с id: {}", id);
        courseService.deleteCourse(id);
        log.info("Курс успешно удален с id: {}", id);
    }

    @GetMapping("/student/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentsCoursesDTO> findStudentsCourses(@PathVariable Long courseId) {
        log.info("Получен запрос на получение студентов курса с id: {}", courseId);
        List<StudentsCoursesDTO> studentsCoursesDTOS = studentService.findStudentsCoursesByCourseId(courseId);
        log.info("Список студентов успешно получен для курса с id: {}", courseId);
        return studentsCoursesDTOS;
    }

    @PutMapping("/student/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public Integer updateRequest(@PathVariable Long courseId,
                                 @RequestParam(name = "ids") List<Long> ids,
                                 @RequestParam(name = "response", defaultValue = "APPROVED") String response) {
        log.info("Получен запрос на обновление статуса запросов для курса с id: {}. id студентов: {}, Ответ: {}", courseId, ids, response);
        Integer rowsAffected = studentService.updateRequest(courseId, ids, response);
        log.info("Статусы запросов успешно обновлены для курса с id: {}. Количество обновленных запросов: {}", courseId, rowsAffected);
        return rowsAffected;
    }

}
