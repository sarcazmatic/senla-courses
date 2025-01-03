package com.senla.courses.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum Role {

    STUDENT(Set.of(Privilege.STUDENT)),
    TEACHER(Set.of(Privilege.TEACHER)),
    ADMIN(Set.of(Privilege.ADMIN, Privilege.TEACHER, Privilege.STUDENT));

    private final Set<Privilege> privileges;

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return privileges.stream().map(p -> new SimpleGrantedAuthority(p.getPrivilege())).collect(Collectors.toSet());
    }

}
