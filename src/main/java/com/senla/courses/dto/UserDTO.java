package com.senla.courses.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String login;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Integer age;
    private String description;
    private LocalDateTime dateTimeRegistered;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String role;
}
