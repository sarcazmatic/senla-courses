package com.senla.courses.controller.all;

import com.senla.courses.dto.CourseDTO;
import com.senla.courses.service.courses.CourseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/all/course")
@AllArgsConstructor
@Slf4j
public class PublicCourseController {

    private final CourseService courseService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CourseDTO findById(@PathVariable Long id) {
        log.info("Получен запрос на поиск курса с id: {}", id);
        CourseDTO courseDTO = courseService.findById(id);
        log.info("Курс с id: {} успешно найден", id);
        return courseDTO;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<CourseDTO> findCourses(@RequestParam(required = false, defaultValue = "1") int from,
                                       @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Получен запрос на получение всех курсов с параметрами: from={}, size={}", from, size);
        List<CourseDTO> courseDTOS = courseService.findCourses(from, size);
        log.info("Успешно получены все курсы с параметрами: from={}, size={}", from, size);
        return courseDTOS;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CourseDTO> findCoursesByText(@RequestParam(required = false, name = "text") String text,
                                             @RequestParam(required = false, defaultValue = "1") int from,
                                             @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Получен запрос на поиск курсов по тексту: '{}', параметры: from={}, size={}", text, from, size);
        List<CourseDTO> coursesDTOS = courseService.findCoursesByText(text, from, size);
        log.info("Успешно найдены курсы по тексту: '{}', параметры: from={}, size={}", text, from, size);
        return coursesDTOS;
    }

}
