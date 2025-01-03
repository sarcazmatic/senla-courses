package com.senla.courses.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "students")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Student {

    @Id
    private Long id;
    private Double rating;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
