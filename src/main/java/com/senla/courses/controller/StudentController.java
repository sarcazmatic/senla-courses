package com.senla.courses.controller;

import com.senla.courses.dto.UserDTO;
import com.senla.courses.service.students.StudentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/student")
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Long registerUser(@RequestBody @Valid UserDTO userDTO) {
        return studentService.registerStudent(userDTO);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO updateStudent(@RequestBody UserDTO userDTO) {
        return studentService.updateStudent(userDTO);
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserDTO findStudent(@PathVariable("id") Long id) {
        return studentService.findStudent(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<UserDTO> findStudents(@RequestParam(required = false, name = "text") String text,
                                   @RequestParam (required = false, defaultValue = "1") int from,
                                   @RequestParam (required = false, defaultValue = "10") int size) {
        return studentService.findStudents(text, from, size);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudent(id);
    }

}
