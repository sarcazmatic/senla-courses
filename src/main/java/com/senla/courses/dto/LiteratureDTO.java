package com.senla.courses.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LiteratureDTO {

    private String name;
    private String author;
    private String url;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ModuleDTO module;

}