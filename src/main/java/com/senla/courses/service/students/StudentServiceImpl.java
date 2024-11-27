package com.senla.courses.service.students;

import com.senla.courses.dao.StudentDAO;
import com.senla.courses.dao.UserDAO;
import com.senla.courses.dto.UserDTO;
import com.senla.courses.dto.UserMapper;
import com.senla.courses.model.Student;
import com.senla.courses.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final UserMapper userMapper;
    private final StudentDAO studentDAO;
    private final UserDAO userDAO;

    @Override
    public Long registerStudent(UserDTO userDTO) {
        User user = userMapper.fromUserDTO(userDTO);
        user.setDateTimeRegistered(LocalDateTime.now());
        Long userPk = userDAO.save(user);
        User userStudent = userDAO.find(userPk);
        Student student = Student.builder()
                .id(userPk)
                .user(userStudent)
                .rating(0.0)
                .build();
        return studentDAO.save(student);
    }

    public UserDTO updateStudent(UserDTO userDTO) {
        Student studentIn = new Student();
        studentIn.setUser(userMapper.fromUserDTO(userDTO));
        Student studentOut = studentDAO.update(studentIn);
        return userMapper.fromUser(studentOut.getUser());
    }

    @Override
    public UserDTO findStudent(Long id) {
        Student student = studentDAO.find(id);
        return userMapper.fromUser(student.getUser());
    }

    @Override
    public List<UserDTO> findStudents(String name, int from, int size) {
        return studentDAO.findAll(name, from, size).stream().map(s -> userMapper.fromUser(s.getUser())).toList();
    }

    @Override
    public void deleteStudent(Long id) {
        studentDAO.deleteById(id);
    }

}
