package com.senla.courses.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "modules")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Column(name = "place_in_course")
    private Integer PlaceInCourse;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

}