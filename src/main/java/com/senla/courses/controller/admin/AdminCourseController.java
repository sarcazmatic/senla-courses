package com.senla.courses.controller.admin;

import com.senla.courses.dto.CourseDTO;
import com.senla.courses.dto.StudentsCoursesDTO;
import com.senla.courses.service.courses.CourseService;
import com.senla.courses.service.students.StudentService;
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
    private final StudentService studentService;


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

    @PutMapping("/{id}/teachers/add")
    @ResponseStatus(HttpStatus.OK)
    public CourseDTO addTeachers(@PathVariable Long id, @RequestParam List<Long> ids) {
        return courseService.addTeachers(id, ids);
    }

    @PutMapping("/{id}/teachers/rm")
    @ResponseStatus(HttpStatus.OK)
    public CourseDTO removeTeachers(@PathVariable Long id, @RequestParam List<Long> ids) {
        return courseService.removeTeachers(id, ids);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCourse(@PathVariable("id") Long id) {
        courseService.deleteCourse(id);
    }

    @GetMapping("/student/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentsCoursesDTO> findStudentsCourses(@PathVariable Long courseId) {
        return studentService.findStudentsCoursesByCourseId(courseId);
    }

    @PutMapping("/student/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public Integer updateRequest(@PathVariable Long courseId,
                                 @RequestParam(name = "ids") List<Long> ids,
                                 @RequestParam(name = "response", defaultValue = "APPROVED") String response) {
        return studentService.updateRequest(courseId, ids, response);
    }

}
