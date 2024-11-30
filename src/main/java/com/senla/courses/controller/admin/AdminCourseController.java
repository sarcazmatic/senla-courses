package com.senla.courses.controller.admin;

import com.senla.courses.dto.course.CourseDTO;
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
@RequestMapping("/admin/course")
@AllArgsConstructor
public class AdminCourseController {

    private final CourseService courseService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Long addCourse(@RequestBody @Valid CourseDTO courseDTO) {
        return courseService.addCourse(courseDTO);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDTO findCourse(@PathVariable Long id) {
        return courseService.findCourse(id);
    }

    @PutMapping("/edit")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDTO editCourse(@RequestBody @Valid CourseDTO courseDTO) {
        return courseService.editCourse(courseDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<CourseDTO> findCourses(@RequestParam(required = false, name = "text") String text,
                                   @RequestParam (required = false, defaultValue = "1") int from,
                                   @RequestParam (required = false, defaultValue = "10") int size) {
        return courseService.findCourses(text, from, size);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteCourse(@PathVariable("id") Long id) {
        courseService.deleteCourse(id);
    }

}
