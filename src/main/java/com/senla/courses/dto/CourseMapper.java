package com.senla.courses.dto;

import com.senla.courses.model.Course;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public Course fromCourseDTO(CourseDTO courseDTO) {
        return Course.builder()
                .complexity(courseDTO.getComplexity())
                .description(courseDTO.getDescription())
                .duration(courseDTO.getDuration())
                .field(courseDTO.getField())
                .name(courseDTO.getName())
                .teachers(courseDTO.getTeachers())
                .build();
    }

    public CourseDTO fromCourse(Course course) {
        return CourseDTO.builder()
                .complexity(course.getComplexity())
                .description(course.getDescription())
                .duration(course.getDuration())
                .field(course.getField())
                .name(course.getName())
                .teachers(course.getTeachers())
                .build();
    }

}
