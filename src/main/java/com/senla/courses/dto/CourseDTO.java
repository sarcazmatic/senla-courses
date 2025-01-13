package com.senla.courses.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Builder
@Getter
@Setter
@AllArgsConstructor
public class CourseDTO {

    private String name;
    private String description;
    private String field;
    private Integer complexity;
    private Integer duration;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<UserDTO> teachers;
    private Set<String> modulesNames;
}