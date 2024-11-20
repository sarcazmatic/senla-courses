package com.senla.courses.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "teachers")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(name = "teachers_courses",
            joinColumns = {@JoinColumn(name = "fk_teacher")},
            inverseJoinColumns = {@JoinColumn(name = "fk_courses")})
    private Set<Course> courses;

}
