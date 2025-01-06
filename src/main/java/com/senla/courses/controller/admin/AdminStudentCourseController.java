package com.senla.courses.controller.admin;

import com.senla.courses.dto.StudentsCoursesDTO;
import com.senla.courses.service.courses.CourseService;
import com.senla.courses.service.students.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/admin/course/student")
@AllArgsConstructor
public class AdminStudentCourseController {

    private final StudentService studentService;

    @GetMapping("/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentsCoursesDTO> findStudent(@PathVariable Long courseId) {
        return studentService.findStudentsCoursesByCourseId(courseId);
    }

    @PutMapping("/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public Integer updateRequest(@PathVariable Long courseId,
                                                  @RequestParam(name = "ids") List<Long> ids,
                                                  @RequestParam(name = "response", defaultValue = "APPROVED") String response) {
        return studentService.updateRequest(courseId, ids, response);
    }

}
