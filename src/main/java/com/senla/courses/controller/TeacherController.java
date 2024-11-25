package com.senla.courses.controller;

import com.senla.courses.dto.UserDTO;
import com.senla.courses.service.teachers.TeacherService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
@RequestMapping("/teacher")
@AllArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Long registerTeacher(@RequestBody @Valid UserDTO userDTO) {
        return teacherService.registerTeacher(userDTO);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {
        return teacherService.updateTeacher(userDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<UserDTO> findUsers(@RequestParam(required = false, name = "name") String name,
                                   @RequestParam (required = false, defaultValue = "1") int from,
                                   @RequestParam (required = false, defaultValue = "10") int size) {
        return teacherService.findTeachers(name, from, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserDTO findUser(@PathVariable("id") Long id) {
        return teacherService.findTeacher(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteTeacher(@PathVariable("id") Long id) {
        teacherService.deleteTeacher(id);
    }

}
