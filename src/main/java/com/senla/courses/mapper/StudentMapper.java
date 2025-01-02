package com.senla.courses.mapper;

import com.senla.courses.dto.StudentDTO;
import com.senla.courses.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentMapper {

    private final UserMapper userMapper;

    public Student updateStudent (Student student, StudentDTO studentDTO) {
        if (studentDTO.getRating() != null) {
            student.setRating(studentDTO.getRating());
        }
        return student;
    }

    public StudentDTO fromStudent (Student student) {
        return StudentDTO.builder()
                .rating(student.getRating())
                .userDTO(userMapper.fromUser(student.getUser()))
                .build();
    }

}
