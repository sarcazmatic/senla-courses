package com.senla.courses.service;

import com.senla.courses.dao.UserDAO;
import com.senla.courses.dto.UserDTO;
import com.senla.courses.dto.UserMapper;
import com.senla.courses.model.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    public UserDTO updateUser(UserDTO userDTO) {
        User user = userDao.update(userMapper.fromUserDTO(userDTO));
        return userMapper.fromUser(user);
    }

    @Override
    public UserDTO findUser(Long id) {
        User user = userDao.find(id);
        return userMapper.fromUser(user);
    }

    @Override
    public List<UserDTO> findUsers(String name, int from, int size) {
        return userDao.findAll(name, from, size).stream().map(userMapper::fromUser).toList();
    }

    @Override
    public void deleteUser(Long id) {
        userDao.deleteById(id);
    }

}
