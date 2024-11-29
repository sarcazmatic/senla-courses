package com.senla.courses.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Builder
@Getter
@Setter
public class CourseDTO {

    private String name;
    private String description;
    private String field;
    private Integer complexity;
    private Duration duration;

}