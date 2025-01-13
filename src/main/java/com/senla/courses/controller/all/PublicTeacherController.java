package com.senla.courses.controller.all;

import com.senla.courses.dto.UserDTO;
import com.senla.courses.service.teachers.TeacherService;
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
@RequestMapping("/all/teacher")
@AllArgsConstructor
@Slf4j
public class PublicTeacherController {

    private final TeacherService teacherService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> findTeachersByName(@RequestParam(required = false, name = "name") String name,
                                            @RequestParam(required = false, defaultValue = "1") int from,
                                            @RequestParam(required = false, defaultValue = "10") int size,
                                            HttpServletRequest request) {
        log.info("Получен запрос на поиск преподавателей по имени: '{}', параметры: from={}, size={}. Эндпоинт {}. Метод {}",
                name, from, size, request.getRequestURL(), request.getMethod());
        return teacherService.findTeachersByName(name, from, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO findById(@PathVariable("id") Long id, HttpServletRequest request) {
        log.info("Получен запрос на поиск преподавателя с id: {}. Эндпоинт {}. Метод {}",
                id, request.getRequestURL(), request.getMethod());
        return teacherService.findById(id);
    }

}
