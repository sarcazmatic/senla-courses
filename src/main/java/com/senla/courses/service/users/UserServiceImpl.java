package com.senla.courses.service.users;

import com.senla.courses.dao.UserDAO;
import com.senla.courses.dto.UserDTO;
import com.senla.courses.mapper.UserMapper;
import com.senla.courses.exception.EmptyListException;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserDAO userDao;

    public Long registerUser(UserDTO userDTO) {
        User user = userMapper.fromUserDTO(userDTO);
        user.setDateTimeRegistered(LocalDateTime.now());
        return userDao.save(user);
    }

    @Override
    public UserDTO findById(Long id) {
        User user = userDao.find(id)
                .orElseThrow(() -> new NotFoundException("Не нашли пользователя по id " + id));
        return userMapper.fromUser(user);
    }

}
