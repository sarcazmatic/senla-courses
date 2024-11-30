package com.senla.courses.dto.module;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ModuleDTO {

    private String name;
    private String description;
    private Integer placeInCourse;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long courseId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String courseName;

}