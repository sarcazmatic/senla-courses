package com.senla.courses.service.teachers;

import com.senla.courses.dao.TeacherDAO;
import com.senla.courses.dao.UserDAO;
import com.senla.courses.dto.user.UserDTO;
import com.senla.courses.dto.user.UserMapper;
import com.senla.courses.model.Teacher;
import com.senla.courses.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final UserMapper userMapper;
    private final TeacherDAO teacherDAO;
    private final UserDAO userDAO;

    @Override
    public Long registerTeacher(UserDTO userDTO) {
        User user = userMapper.fromUserDTO(userDTO);
        user.setDateTimeRegistered(LocalDateTime.now());
        Long userPk = userDAO.save(user);
        User userTeacher = userDAO.find(userPk);
        Teacher teacher = Teacher.builder()
                .id(userPk)
                .user(userTeacher)
                .courses(new HashSet<>())
                .build();
        return teacherDAO.save(teacher);
    }

    public UserDTO updateTeacher(UserDTO userDTO) {
        Teacher teacherIn = new Teacher();
        teacherIn.setUser(userMapper.fromUserDTO(userDTO));
        Teacher teacherOut = teacherDAO.update(teacherIn);
        return userMapper.fromUser(teacherOut.getUser());
    }

    @Override
    public UserDTO findTeacher(Long id) {
        Teacher teacher = teacherDAO.find(id);
        return userMapper.fromUser(teacher.getUser());
    }

    @Override
    public List<UserDTO> findTeachers(String name, int from, int size) {
        return teacherDAO.findAll(name, from, size).stream().map(t -> userMapper.fromUser(t.getUser())).toList();
    }


    @Override
    public void deleteTeacher(Long id) {
        teacherDAO.deleteById(id);
    }

}
