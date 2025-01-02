package com.senla.courses.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "students_courses")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class StudentsCourses {

    @EmbeddedId
    private StudentsCoursesPK id;

    @OneToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private Student student;

    @OneToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    private Double rating;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @Column(name = "current_module")
    private Integer currentModule;
    @Column(name = "course_started")
    private Boolean courseStarted;
}
