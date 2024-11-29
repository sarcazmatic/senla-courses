package com.senla.courses.dto;

import com.senla.courses.model.Teacher;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Builder
@Getter
@Setter
public class CourseDTO {

    private String name;
    private String description;
    private String field;
    private Integer complexity;
    private Integer duration;
    private Set<Teacher> teachers;

}