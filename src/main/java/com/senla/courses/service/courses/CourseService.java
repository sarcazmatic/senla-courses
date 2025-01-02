package com.senla.courses.service.courses;

import com.senla.courses.dto.CourseDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CourseService {

    Long addCourse(CourseDTO courseDTO);

    CourseDTO findById(Long id);

    CourseDTO editCourse(CourseDTO courseDTO, Long id);

    CourseDTO addTeachers(Long id, List<Long> ids);

    CourseDTO removeTeachers(Long id, List<Long> ids);

    List<CourseDTO> findCourses(int from, int size);

    List<CourseDTO> findCoursesByText(String text, int from, int size);

    void deleteCourse(Long id);


}
