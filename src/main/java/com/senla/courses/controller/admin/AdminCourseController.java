package com.senla.courses.controller.admin;

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
@RequestMapping("/admin/course")
@AllArgsConstructor
public class AdminCourseController {

    private final CourseService courseService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Long addCourse(@RequestBody @Valid CourseDTO courseDTO) {
        return courseService.addCourse(courseDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CourseDTO editCourse(@RequestBody @Valid CourseDTO courseDTO,
                                @PathVariable Long id) {
        return courseService.editCourse(courseDTO, id);
    }

    @PutMapping("/{id}/add/teachers")
    @ResponseStatus(HttpStatus.OK)
    public CourseDTO addTeachers(@PathVariable Long id, @RequestParam List<Long> ids) {
        return courseService.addTeachers(id, ids);
    }

    @PutMapping("/{id}/rm/teachers")
    @ResponseStatus(HttpStatus.OK)
    public CourseDTO removeTeachers(@PathVariable Long id, @RequestParam List<Long> ids) {
        return courseService.removeTeachers(id, ids);
    }

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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCourse(@PathVariable("id") Long id) {
        courseService.deleteCourse(id);
    }

}
