package com.senla.courses.service.teachers;

import com.senla.courses.dao.TeacherDAO;
import com.senla.courses.dao.UserDAO;
import com.senla.courses.dto.UserDTO;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.exception.ValidationException;
import com.senla.courses.mapper.UserMapper;
import com.senla.courses.exception.EmptyListException;
import com.senla.courses.model.Role;
import com.senla.courses.model.Teacher;
import com.senla.courses.model.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private static final Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);

    private final UserMapper userMapper;
    private final TeacherDAO teacherDAO;
    private final UserDAO userDAO;

    @Override
    public Long registerTeacher(UserDTO userDTO) {
        User user = userMapper.fromUserDTO(userDTO);
        user.setDateTimeRegistered(LocalDateTime.now());
        Long userPk = userDAO.save(user);
        User userTeacher = userDAO.find(userPk)
                .orElseThrow(() -> new NotFoundException("Не смогли найти такого пользовтеля"));
        Teacher teacher = Teacher.builder()
                .id(userPk)
                .user(userTeacher)
                .courses(new HashSet<>())
                .build();
        Long id = teacherDAO.save(teacher);
        logger.info("Учитель с логином {} зарегестрирован", userDTO.getLogin());
        return id;
    }

    @Override
    public UserDTO updateTeacher(org.springframework.security.core.userdetails.User userSec, UserDTO userDTO, Long id) {
        Teacher teacherUpd = teacherDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Не смогли найти учителя по id " + id));
        if (teacherValidate(teacherUpd, userSec)) {
            throw new ValidationException("Обновить можно только инфомрацию о себе");
        }
        User user = userMapper.updateUser(teacherUpd.getUser(), userDTO);
        teacherUpd.setUser(user);
        teacherDAO.update(teacherUpd);
        logger.info("Учитель с id {} обновлен", id);
        return userMapper.fromUser(teacherUpd.getUser());
    }

    @Override
    public UserDTO findById(Long id) {
        Teacher teacher = teacherDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Не смогли найти учителя по id " + id));
        logger.info("Учитель с id {} найден", id);
        return userMapper.fromUser(teacher.getUser());
    }

    @Override
    public List<UserDTO> findTeachersByName(String name, int from, int size) {
        List<UserDTO> userDTOList = teacherDAO.findAllByText(name, from, size)
                .stream()
                .map(t -> userMapper.fromUser(t.getUser()))
                .toList();
        if (userDTOList.isEmpty()) {
            throw new EmptyListException("Список учителей пуст");
        }
        logger.info("Список учителей с именем {} собран", name);
        return userDTOList;
    }


    @Override
    public void deleteTeacher(org.springframework.security.core.userdetails.User userSec, Long id) {
        Teacher teacher = teacherDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Не смогли найти учителя по id " + id));
        if (teacherValidate(teacher, userSec)) {
            throw new ValidationException("Удалить можно только информацию о себе");
        }
        teacherDAO.deleteById(id);
        logger.info("Учитель с id {} удален", id);
    }

    private boolean teacherValidate(Teacher teacher, org.springframework.security.core.userdetails.User userSec) {
        boolean isValid = !teacher.getUser().getLogin().equals(userSec.getUsername())
                && !userSec.getAuthorities().equals(Role.ADMIN.getAuthorities());
        if (isValid) {
            logger.info("Валидация студента успешна");
        } else {
            logger.info("Валидация студента не была успешна");
        }
        return isValid;
    }

}
