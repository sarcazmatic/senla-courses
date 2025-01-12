package com.senla.courses.controller.all;

import com.senla.courses.dto.UserDTO;
import com.senla.courses.service.teachers.TeacherService;
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
                                            @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Получен запрос на поиск преподавателей по имени: '{}', параметры: from={}, size={}", name, from, size);
        List<UserDTO> teachersDTOS = teacherService.findTeachersByName(name, from, size);
        log.info("Преподаватели успешно найдены по имени: '{}', параметры: from={}, size={}", name, from, size);
        return teachersDTOS;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO findById(@PathVariable("id") Long id) {
        log.info("Получен запрос на поиск преподавателя с id: {}", id);
        UserDTO teacherDTO = teacherService.findById(id);
        log.info("Преподаватель с id: {} успешно найден", id);
        return teacherDTO;
    }

}
