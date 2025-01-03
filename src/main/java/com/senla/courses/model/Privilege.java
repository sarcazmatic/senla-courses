package com.senla.courses.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Privilege {

    STUDENT("student:write"),
    TEACHER("teacher:write"),
    ADMIN("admin:write");

    private final String privilege;

}
