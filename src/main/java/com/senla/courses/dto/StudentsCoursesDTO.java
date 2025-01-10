package com.senla.courses.dto;


import com.senla.courses.util.enums.StudentsCoursesRequestEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Builder
@Getter
@Setter
public class StudentsCoursesDTO {

    private StudentDTO student;
    private CourseDTO course;
    private Double rating;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer currentModule;
    private StudentsCoursesRequestEnum courseStatus;
}