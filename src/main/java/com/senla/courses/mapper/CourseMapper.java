package com.senla.courses.mapper;

import com.senla.courses.dto.CourseDTO;
import com.senla.courses.model.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CourseMapper {

    private final UserMapper userMapper;

    public Course fromCourseDTO(CourseDTO courseDTO) {
        return Course.builder()
                .complexity(courseDTO.getComplexity())
                .description(courseDTO.getDescription())
                .duration(courseDTO.getDuration())
                .field(courseDTO.getField())
                .name(courseDTO.getName())
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
        try {
            courseDTO.setTeachers(course.getTeachers().stream().map(t -> userMapper.fromUser(t.getUser())).collect(Collectors.toSet()));
        } catch (NullPointerException e) {
            course.setTeachers(new HashSet<>());
        }
        return courseDTO;
    }

    public Course updateCourse(Course course, CourseDTO courseDTO) {
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
