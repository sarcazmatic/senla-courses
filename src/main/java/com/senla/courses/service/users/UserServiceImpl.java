package com.senla.courses.service.users;

import com.senla.courses.dao.UserDAO;
import com.senla.courses.dto.UserDTO;
import com.senla.courses.mapper.UserMapper;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserDAO userDao;

    @Override
    public UserDTO findById(Long id) {
        User user = userDao.find(id)
                .orElseThrow(() -> new NotFoundException("Не нашли пользователя по id " + id));
        return userMapper.fromUser(user);
    }

}
