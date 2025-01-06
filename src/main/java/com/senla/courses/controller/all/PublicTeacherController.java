package com.senla.courses.controller.all;

import com.senla.courses.dto.UserDTO;
import com.senla.courses.service.teachers.TeacherService;
import lombok.AllArgsConstructor;
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
public class PublicTeacherController {

    private final TeacherService teacherService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> findUsers(@RequestParam(required = false, name = "name") String name,
                                   @RequestParam (required = false, defaultValue = "1") int from,
                                   @RequestParam (required = false, defaultValue = "10") int size) {
        return teacherService.findTeachersByName(name, from, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO findUser(@PathVariable("id") Long id) {
        return teacherService.findById(id);
    }

}
