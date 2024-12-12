package com.senla.courses.service.users;

import com.senla.courses.dao.UserDAO;
import com.senla.courses.dto.UserDTO;
import com.senla.courses.dto.UserMapper;
import com.senla.courses.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        User user = userDao.update(userMapper.fromUserDTO(userDTO))
                .orElseThrow(() -> new RuntimeException("Не нашли пользователя"));
        return userMapper.fromUser(user);
    }

    @Override
    public UserDTO findUser(Long id) {
        User user = userDao.find(id)
                .orElseThrow(() -> new RuntimeException("Не нашли пользователя"));
        return userMapper.fromUser(user);
    }

    @Override
    public List<UserDTO> findUsersByName(String name, int from, int size) {
        List<UserDTO> userDTOList = userDao.findAll(name, from, size).stream().map(userMapper::fromUser).toList();
        if (userDTOList.isEmpty()) {
            throw new RuntimeException("Список пуст");
        }
        return userDTOList;
    }

    @Override
    public void deleteUser(Long id) {
        userDao.deleteById(id);
    }

}
