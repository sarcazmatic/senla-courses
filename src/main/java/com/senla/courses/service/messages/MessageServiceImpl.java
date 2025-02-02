package com.senla.courses.service.messages;

import com.senla.courses.dao.MessageDAO;
import com.senla.courses.dao.StudentDAO;
import com.senla.courses.dao.TeacherDAO;
import com.senla.courses.dao.UserDAO;
import com.senla.courses.dto.MessageDTO;
import com.senla.courses.dto.MessageFullDTO;
import com.senla.courses.exception.ValidationException;
import com.senla.courses.mapper.MessageMapper;
import com.senla.courses.exception.EmptyListException;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.model.Message;
import com.senla.courses.model.Student;
import com.senla.courses.model.Teacher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageMapper messageMapper;
    private final StudentDAO studentDAO;
    private final TeacherDAO teacherDAO;
    private final MessageDAO messageDAO;
    private final UserDAO userDAO;

    @Override
    public Long sendMessage(MessageDTO messageDTO, User user, Long to) {
        Message message = messageMapper.fromMessageDTO(messageDTO);
        Long from = userDAO.findByLogin(user.getUsername()).orElseThrow(()
                -> new AuthenticationException("Не пройдена аутентификация") {
        }).getId();
        Optional<Student> student;
        Optional<Teacher> teacher;
        student = studentDAO.find(from);
        if (student.isEmpty()) {
            student = studentDAO.find(to);
            if (student.isEmpty()) {
                throw new NotFoundException("Не смогли найти студента ни по from, не по to");
            }
            teacher = teacherDAO.find(from);
            if (teacher.isEmpty()) {
                throw new NotFoundException("Не смогли найти преподавателя по from");
            }
            message.setTo(student.get().getUser());
            message.setFrom(teacher.get().getUser());
        } else {
            teacher = teacherDAO.find(to);
            if (teacher.isEmpty()) {
                throw new NotFoundException("Не смогли найти преподавателя по to");
            }
            message.setFrom(student.get().getUser());
            message.setTo(teacher.get().getUser());
        }
        message.setDateTimeSent(LocalDateTime.now());
        Long id = messageDAO.save(message);
        log.info("Сообщение с id {} создано", id);
        return id;
    }

    @Override
    public MessageFullDTO updateMessage(User userSec, MessageDTO messageDTO, Long id) {
        Message messageIn = messageDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Не смогли найти сообщение для обновления"));
        Long from = userDAO.findByLogin(userSec.getUsername()).orElseThrow(()
                -> new ValidationException("Не пройдена аутентификация") {
        }).getId();
        if (!messageIn.getFrom().getId().equals(from)) {
            throw new ValidationException("Не пройдена аутентификация");
        }
        messageIn.setBody(messageDTO.getBody());
        Message messageOut = messageDAO.update(messageIn);
        log.info("Сообщение с id {} отредактировано. Было: {}. Стало {}", id, messageIn, messageOut);
        return messageMapper.fromMessage(messageOut);
    }

    @Override
    public MessageFullDTO findById(Long id) {
        Message message = messageDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Не смогли найти сообщение"));
        log.info("Сообщение с id {} найдено", id);
        return messageMapper.fromMessage(message);
    }

    @Override
    public List<MessageFullDTO> getMessagesBetween(User userSec, List<Long> betweenIds, int from, int size) {
        Long fromValid = userDAO.findByLogin(userSec.getUsername()).orElseThrow(()
                -> new ValidationException("Не пройдена аутентификация") {
        }).getId();
        if (!betweenIds.contains(fromValid)) {
            throw new ValidationException("Можно просматривать только сообщения, в которых вы один из адресатов");
        }
        List<Message> messagesList = messageDAO.findMessagesBetween(betweenIds.get(0), betweenIds.get(1), from, size);
        if (messagesList.isEmpty()) {
            throw new EmptyListException("Список сообщений между указанными id пуст");
        }
        List<MessageFullDTO> messageFullDTOList = messagesList.stream().map(messageMapper::fromMessage).toList();
        log.info("Список сообщений между {} собран. Найдено {} элементов", betweenIds, messageFullDTOList.size());
        return messageFullDTOList;
    }

    @Override
    public List<MessageFullDTO> findMessagesByText(User userSec, String text, int from, int size) {
        Long fromOrTo = userDAO.findByLogin(userSec.getUsername())
                .orElseThrow(() -> new ValidationException("Не пройдена аутентификация") {
                }).getId();
        List<Message> messagesList = messageDAO.findAllByText(fromOrTo, text, from, size);
        if (messagesList.isEmpty()) {
            throw new EmptyListException("Список сообщений между указанными id пусть");
        }
        List<MessageFullDTO> messageFullDTOList = messagesList.stream().map(messageMapper::fromMessage).toList();
        log.info("Список сообщений, содержащих текст '{}', найден. Найдено {} элементов", text, messageFullDTOList.size());
        return messageFullDTOList;
    }

    @Override
    public void deleteMessage(User userSec, Long id) {
        Message message = messageDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Не смогли найти сообщение"));
        Long from = userDAO.findByLogin(userSec.getUsername())
                .orElseThrow(()
                        -> new ValidationException("Не пройдена аутентификация") {
                }).getId();
        if (!message.getFrom().getId().equals(from)) {
            throw new ValidationException("Можно удалять только сообщения, в которых вы -- отправитель");
        }
        messageDAO.deleteById(id);
        log.info("Сообщение с id {} удалено", id);
    }
}
