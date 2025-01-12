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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Slf4j
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
                .orElseThrow(() -> new NotFoundException("Не смогли найти такого пользовтеля"));
        Teacher teacher = Teacher.builder()
                .id(userPk)
                .user(userTeacher)
                .courses(new HashSet<>())
                .build();
        Long id = teacherDAO.save(teacher);
        log.info("Учитель с логином '{}' зарегестрирован под id {}", userDTO.getLogin(), id);
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
        log.info("Учитель с id {} обновлен. Стало: {}", id, teacherUpd);
        return userMapper.fromUser(teacherUpd.getUser());
    }

    @Override
    public UserDTO findById(Long id) {
        Teacher teacher = teacherDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Не смогли найти учителя по id " + id));
        log.info("Учитель {} с id {} найден", teacher, id);
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
        log.info("Список учителей с именем '{}' собран. Найдено {} элементов", name, userDTOList.size());
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
        log.info("Учитель {} с id {} удален", teacher, id);
    }

    private boolean teacherValidate(Teacher teacher, org.springframework.security.core.userdetails.User userSec) {
        boolean isValid = !teacher.getUser().getLogin().equals(userSec.getUsername())
                && !userSec.getAuthorities().equals(Role.ADMIN.getAuthorities());
        if (isValid) {
            log.info("Валидация учителя успешна");
        } else {
            log.info("Валидация учителя не была успешна");
        }
        return isValid;
    }

}
