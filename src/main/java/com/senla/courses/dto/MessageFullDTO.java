package com.senla.courses.dto;

import com.senla.courses.model.Student;
import com.senla.courses.model.Teacher;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MessageFullDTO {

    private String body;
    private LocalDateTime dateTimeSent;
    private String teacherName;
    private String studentName;

}
