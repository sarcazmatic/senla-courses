package com.senla.courses.controller;

import com.senla.courses.dto.CourseDTO;
import com.senla.courses.service.courses.CourseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/course")
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Long addCourse(@RequestBody @Valid CourseDTO courseDTO) {
        return courseService.addCourse(courseDTO);
    }

}
