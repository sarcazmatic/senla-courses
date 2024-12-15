package com.senla.courses.dto;

import com.senla.courses.model.Course;
import com.senla.courses.model.User;
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

    public Course updateCourse (Course course, CourseDTO courseDTO) {
        if (courseDTO.getComplexity() != null) {
            course.setComplexity(courseDTO.getComplexity());
        }
        if (courseDTO.getDuration() != null) {
            course.setDuration(courseDTO.getDuration());
        }
        if (courseDTO.getDescription() != null) {
            course.setDescription(courseDTO.getDescription());
        }
        if (courseDTO.getField() != null) {
            course.setField(courseDTO.getField());
        }
        if (courseDTO.getName() != null) {
            course.setName(courseDTO.getName());
        }
        return course;
    }

}
