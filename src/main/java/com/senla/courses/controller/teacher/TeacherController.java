package com.senla.courses.controller.teacher;

import com.senla.courses.dto.UserDTO;
import com.senla.courses.service.teachers.TeacherService;
import com.senla.courses.util.UserDetailsExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/teacher")
@AllArgsConstructor
@Slf4j
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Long registerTeacher(@RequestBody @Valid UserDTO userDTO,
                                HttpServletRequest request) {
        log.info("Получен запрос на регистрацию нового учителя с данными: {}. Эндпоинт {}. Метод {}",
                userDTO, request.getRequestURL(), request.getMethod());
        return teacherService.registerTeacher(userDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO updateTeacher(@RequestBody UserDTO userDTO,
                                 @PathVariable Long id,
                                 HttpServletRequest request) {
        log.info("Получен запрос на обновление данных учителя с id: {}. Новые данные: {}. Эндпоинт {}. Метод {}",
                id, userDTO, request.getRequestURL(), request.getMethod());
        User user = UserDetailsExtractor.extractUserDetails();
        return teacherService.updateTeacher(user, userDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTeacher(@PathVariable("id") Long id,
                              HttpServletRequest request) {
        log.info("Получен запрос на удаление учителя с id: {}. Эндпоинт {}. Метод {}",
                id, request.getRequestURL(), request.getMethod());
        User user = UserDetailsExtractor.extractUserDetails();
        teacherService.deleteTeacher(user, id);
    }

}
