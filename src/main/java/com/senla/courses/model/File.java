package com.senla.courses.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "files")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String content;
    private String url;
    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

}