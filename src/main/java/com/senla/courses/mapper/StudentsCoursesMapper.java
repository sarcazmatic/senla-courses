package com.senla.courses.mapper;

import com.senla.courses.dto.StudentsCoursesDTO;
import com.senla.courses.model.StudentsCourses;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class StudentsCoursesMapper {

    private final CourseMapper courseMapper;
    private final StudentMapper studentMapper;

    public StudentsCoursesDTO fromStudentCourses(StudentsCourses studentsCourses) {
        return StudentsCoursesDTO.builder()
                .course(courseMapper.fromCourse(studentsCourses.getCourse()))
                .student(studentMapper.fromStudent(studentsCourses.getStudent()))
                .courseStatus(studentsCourses.getCourseStatus())
                .currentModule(studentsCourses.getCurrentModule())
                .endDate(studentsCourses.getEndDate())
                .startDate(studentsCourses.getStartDate())
                .rating(studentsCourses.getRating())
                .build();
    }

}
