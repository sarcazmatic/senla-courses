package com.senla.courses.service.teachers;

import com.senla.courses.dao.TeacherDAO;
import com.senla.courses.dao.UserDAO;
import com.senla.courses.dto.UserDTO;
import com.senla.courses.dto.UserMapper;
import com.senla.courses.model.Teacher;
import com.senla.courses.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
        Optional<User> userTeacher = userDAO.find(userPk);
        if (userTeacher.isEmpty()) {
            throw new RuntimeException("Не смогли найти такого пользовтеля");
        }
        Teacher teacher = Teacher.builder()
                .id(userPk)
                .user(userTeacher.get())
                .courses(new HashSet<>())
                .build();
        return teacherDAO.save(teacher);
    }

    public UserDTO updateTeacher(UserDTO userDTO) {
        Teacher teacherIn = new Teacher();
        teacherIn.setUser(userMapper.fromUserDTO(userDTO));
        Optional<Teacher> teacherOut = teacherDAO.update(teacherIn);
        if (teacherOut.isEmpty()) {
            throw new RuntimeException("Не смогли найти учителя");
        }
        return userMapper.fromUser(teacherOut.get().getUser());
    }

    @Override
    public UserDTO findTeacher(Long id) {
        Optional<Teacher> teacher = teacherDAO.find(id);
        if (teacher.isEmpty()) {
            throw new RuntimeException("Не смогли найти учителя");
        }
        return userMapper.fromUser(teacher.get().getUser());
    }

    @Override
    public List<UserDTO> findTeachersByName(String name, int from, int size) {
        List<UserDTO> userDTOList = teacherDAO.findAll(name, from, size)
                .stream()
                .map(t -> userMapper.fromUser(t.getUser()))
                .toList();
        if (userDTOList.isEmpty()) {
            throw new RuntimeException("Список учителей пуст");
        }
        return userDTOList;
    }


    @Override
    public void deleteTeacher(Long id) {
        teacherDAO.deleteById(id);
    }

}
