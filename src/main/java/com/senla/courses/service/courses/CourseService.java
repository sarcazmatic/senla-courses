package com.senla.courses.service.courses;

import com.senla.courses.dto.CourseDTO;

import java.util.List;

public interface CourseService {

    Long addCourse(CourseDTO courseDTO);

    CourseDTO findById(Long id);

    CourseDTO editCourse(CourseDTO courseDTO, Long id);

    List<CourseDTO> findCourses(int from, int size);

    List<CourseDTO> findCoursesByText(String text, int from, int size);

    void deleteCourse(Long id);


}
