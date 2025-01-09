package com.senla.courses.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReturnFileDTO {

    private String endpoint;
    private String name;

}
