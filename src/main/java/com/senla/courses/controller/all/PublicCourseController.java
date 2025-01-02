package com.senla.courses.controller.all;

import com.senla.courses.dto.CourseDTO;
import com.senla.courses.service.courses.CourseService;
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
@RequestMapping("/all/course")
@AllArgsConstructor
public class PublicCourseController {

    private final CourseService courseService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CourseDTO findById(@PathVariable Long id) {
        return courseService.findById(id);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<CourseDTO> findCourses(@RequestParam (required = false, defaultValue = "1") int from,
                                       @RequestParam (required = false, defaultValue = "10") int size) {
        return courseService.findCourses(from, size);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CourseDTO> findCoursesByText(@RequestParam(required = false, name = "text") String text,
                                             @RequestParam (required = false, defaultValue = "1") int from,
                                             @RequestParam (required = false, defaultValue = "10") int size) {
        return courseService.findCoursesByText(text, from, size);
    }

}
