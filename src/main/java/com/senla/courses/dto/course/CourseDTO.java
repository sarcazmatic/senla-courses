package com.senla.courses.dto.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.courses.model.Module;
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<Teacher> teachers;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<String> teachersNames;
    private Set<String> modulesNames;

}