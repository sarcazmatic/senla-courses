package com.senla.courses.controller.student;

import com.senla.courses.dto.StudentDTO;
import com.senla.courses.dto.UserDTO;
import com.senla.courses.service.students.StudentService;
import com.senla.courses.util.UserDetailsExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
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
@RequestMapping("/student")
@AllArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Long registerUser(@RequestBody @Valid UserDTO userDTO,
                             HttpServletRequest request) {
        log.info("Получен запрос на регистрацию студента: {}. Эндпоинт {}. Метод {}",
                userDTO, request.getRequestURL(), request.getMethod());
        return studentService.registerStudent(userDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentDTO updateStudent(@RequestBody StudentDTO studentDTO,
                                    @PathVariable Long id,
                                    HttpServletRequest request) {
        log.info("Получен запрос на обновление данных студента с id: {}. Эндпоинт {}. Метод {}",
                id, request.getRequestURL(), request.getMethod());
        User user = UserDetailsExtractor.extractUserDetails();
        return studentService.updateStudent(user, studentDTO, id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentDTO findById(@PathVariable("id") Long id,
                               HttpServletRequest request) {
        log.info("Получен запрос на поиск студента с id: {}. Эндпоинт {}. Метод {}",
                id, request.getRequestURL(), request.getMethod());
        return studentService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> findStudents(@RequestParam(required = false, name = "text") String name,
                                      @RequestParam(required = false, defaultValue = "1") int from,
                                      @RequestParam(required = false, defaultValue = "10") int size,
                                      HttpServletRequest request) {
        log.info("Получен запрос на поиск студентов по имени: '{}', страница: {}, размер: {}. Эндпоинт {}. Метод {}",
                name, from, size, request.getRequestURL(), request.getMethod());
        return studentService.findStudentsByName(name, from, size);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteStudent(@PathVariable("id") Long id,
                              HttpServletRequest request) {
        log.info("Получен запрос на удаление студента с id: {}. Эндпоинт {}. Метод {}",
                id, request.getRequestURL(), request.getMethod());
        User user = UserDetailsExtractor.extractUserDetails();
        studentService.deleteStudent(user, id);
    }

}
