package com.senla.courses.service.teachers;

import com.senla.courses.dao.TeacherDAO;
import com.senla.courses.dao.UserDAO;
import com.senla.courses.dto.UserDTO;
import com.senla.courses.dto.UserMapper;
import com.senla.courses.exception.EmptyListException;
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
        User userTeacher = userDAO.find(userPk)
                .orElseThrow(() -> new RuntimeException("Не смогли найти такого пользовтеля"));
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
    public UserDTO findById(Long id) {
        Teacher teacher = teacherDAO.find(id)
                .orElseThrow(() -> new RuntimeException("Не смогли найти учителя"));
        return userMapper.fromUser(teacher.getUser());
    }

    @Override
    public List<UserDTO> findTeachersByName(String name, int from, int size) {
        List<UserDTO> userDTOList = teacherDAO.findAll(name, from, size)
                .stream()
                .map(t -> userMapper.fromUser(t.getUser()))
                .toList();
        if (userDTOList.isEmpty()) {
            throw new EmptyListException("Список учителей пуст");
        }
        return userDTOList;
    }


    @Override
    public void deleteTeacher(Long id) {
        teacherDAO.deleteById(id);
    }

}
