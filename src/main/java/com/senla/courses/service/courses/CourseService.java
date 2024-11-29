package com.senla.courses.service.courses;

import com.senla.courses.dto.course.CourseDTO;

import java.util.List;

public interface CourseService {

    Long addCourse(CourseDTO courseDTO);

    CourseDTO findCourse(Long id);

    CourseDTO editCourse(CourseDTO courseDTO);

    List<CourseDTO> findCourses(String text, int from, int size);

    void deleteCourse(Long id);


}
