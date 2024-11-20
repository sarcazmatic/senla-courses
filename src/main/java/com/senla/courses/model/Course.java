package com.senla.courses.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;

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

}