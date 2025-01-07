package com.senla.courses.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TaskDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String name;
    private String body;
    private String url;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ModuleDTO module;

}
