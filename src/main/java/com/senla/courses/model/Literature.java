package com.senla.courses.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "literature")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Literature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String author;
    private String url;
    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

}