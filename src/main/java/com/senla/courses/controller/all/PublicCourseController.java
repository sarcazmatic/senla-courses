package com.senla.courses.controller.all;

import com.senla.courses.dto.CourseDTO;
import com.senla.courses.service.courses.CourseService;
import jakarta.servlet.http.HttpServletRequest;
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
    public CourseDTO findById(@PathVariable Long id, HttpServletRequest request) {
        log.info("Получен запрос на поиск курса с id: {}. Эндпоинт {}. Метод {}",
                id, request.getRequestURL(), request.getMethod());
        return courseService.findById(id);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<CourseDTO> findCourses(@RequestParam(required = false, defaultValue = "1") int from,
                                       @RequestParam(required = false, defaultValue = "10") int size,
                                       HttpServletRequest request) {
        log.info("Получен запрос на получение всех курсов с параметрами: from={}, size={}. Эндпоинт {}. Метод {}",
                from, size, request.getRequestURL(), request.getMethod());
        return courseService.findCourses(from, size);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CourseDTO> findCoursesByText(@RequestParam(required = false, name = "text", defaultValue = "") String text,
                                             @RequestParam(required = false, defaultValue = "1") int from,
                                             @RequestParam(required = false, defaultValue = "10") int size,
                                             HttpServletRequest request) {
        log.info("Получен запрос на поиск курсов по тексту: '{}', параметры: from={}, size={}. Эндпоинт {}. Метод {}",
                text, from, size, request.getRequestURL(), request.getMethod());
        return courseService.findCoursesByText(text, from, size);
    }

}
