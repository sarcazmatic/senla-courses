package com.senla.courses.service.messages;

import com.senla.courses.dao.MessageDAO;
import com.senla.courses.dao.StudentDAO;
import com.senla.courses.dao.TeacherDAO;
import com.senla.courses.dao.UserDAO;
import com.senla.courses.dto.MessageDTO;
import com.senla.courses.dto.MessageFullDTO;
import com.senla.courses.mapper.MessageMapper;
import com.senla.courses.exception.EmptyListException;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.model.Message;
import com.senla.courses.model.Student;
import com.senla.courses.model.Teacher;
import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService{

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
        return messageDAO.save(message);
    }

    @Override
    public MessageFullDTO updateMessage(MessageDTO messageDTO, Long id) {
        Message messageIn = messageDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Не смогли найти сообщение для обновления"));
        messageIn.setBody(messageDTO.getBody());
        Message messageOut = messageDAO.update(messageIn);
        return messageMapper.fromMessage(messageOut);
    }

    @Override
    public MessageFullDTO findById(Long id) {
        Message message = messageDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Не смогли найти сообщение"));
        return messageMapper.fromMessage(message);
    }

    @Override
    public List<MessageFullDTO> getMessagesBetween(List<Long> betweenIds, int from, int size) {
        List<Message> messagesList = messageDAO.findMessagesBetween(betweenIds.get(0), betweenIds.get(1), from, size);
        if (messagesList.isEmpty()) {
            throw new EmptyListException("Список сообщений между указанными id пусть");
        }
        return messagesList.stream().map(messageMapper::fromMessage).toList();
    }

    @Override
    public List<MessageFullDTO> findMessagesByText(String text, int from, int size) {
        List<Message> messagesList = messageDAO.findAllByText(text, from, size);
        if (messagesList.isEmpty()) {
            throw new EmptyListException("Список сообщений между указанными id пусть");
        }
        return messagesList.stream().map(messageMapper::fromMessage).toList();
    }

    @Override
    public void deleteMessage(Long id) {
        messageDAO.deleteById(id);
    }
}
