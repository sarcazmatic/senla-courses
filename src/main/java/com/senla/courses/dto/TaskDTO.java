package com.senla.courses.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.courses.model.Module;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TaskDTO {

    private String name;
    private String body;
    private String url;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ModuleDTO module;

}
