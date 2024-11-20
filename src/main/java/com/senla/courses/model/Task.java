package com.senla.courses.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String body;
    private String url;
    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

}