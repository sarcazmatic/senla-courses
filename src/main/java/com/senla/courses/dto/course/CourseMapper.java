package com.senla.courses.dto.course;

import com.senla.courses.model.Course;
import com.senla.courses.model.Module;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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
        CourseDTO courseDTO = CourseDTO.builder()
                .complexity(course.getComplexity())
                .description(course.getDescription())
                .duration(course.getDuration())
                .field(course.getField())
                .name(course.getName())
                .build();
        if (course.getTeachers() != null) {
            courseDTO.setTeachersNames((course.getTeachers().stream()
                    .map(t -> t.getUser().getName()).collect(Collectors.toSet())));
        }
        if (course.getModules() != null) {
            courseDTO.setModulesNames(course.getModules().stream().map(Module::getName).collect(Collectors.toSet()));
        }
        return courseDTO;
    }

}
