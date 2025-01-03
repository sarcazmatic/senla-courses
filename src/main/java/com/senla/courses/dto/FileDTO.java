package com.senla.courses.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FileDTO {

    private String name;
    private byte[] content;
    private String url;
    private String contentType;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ModuleDTO module;

}
