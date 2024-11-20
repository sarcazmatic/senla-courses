package com.senla.courses.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.util.Set;

@Entity
@Table(name = "courses")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String field;
    private String complexity;
    private Duration duration;

    @ManyToMany
    @JoinTable(name = "teachers_courses",
            joinColumns = {@JoinColumn(name = "fk_courses")},
            inverseJoinColumns = {@JoinColumn(name = "fk_teacher")})
    private Set<Teacher> teachers;

}