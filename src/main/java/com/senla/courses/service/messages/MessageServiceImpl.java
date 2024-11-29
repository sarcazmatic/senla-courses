package com.senla.courses.service.messages;

import com.senla.courses.dao.MessageDAO;
import com.senla.courses.dao.StudentDAO;
import com.senla.courses.dao.TeacherDAO;
import com.senla.courses.dto.MessageDTO;
import com.senla.courses.dto.MessageFullDTO;
import com.senla.courses.dto.MessageMapper;
import com.senla.courses.model.Message;
import com.senla.courses.model.Student;
import com.senla.courses.model.Teacher;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService{

    private final MessageMapper messageMapper;
    private final StudentDAO studentDAO;
    private final TeacherDAO teacherDAO;
    private final MessageDAO messageDAO;

    @Override
    public Long sendMessage(MessageDTO messageDTO, Long from, Long to) {
        Message message = messageMapper.fromMessageDTO(messageDTO);
        Student student;
        Teacher teacher;
        student = studentDAO.find(from);
        if (student == null) {
            student = studentDAO.find(to);
            teacher = teacherDAO.find(from);
        } else {
            teacher = teacherDAO.find(to);
        }
        message.setStudent(student);
        message.setTeacher(teacher);
        message.setDateTimeSent(LocalDateTime.now());
        return messageDAO.save(message);
    }

    @Override
    public MessageFullDTO getMessage(Long id) {
        Message message = messageDAO.find(id);
        return messageMapper.fromMessage(message);
    }

    @Override
    public List<MessageFullDTO> getMessagesBetween(List<Long> betweenIds, int from, int size) {
        List<Message> messagesList = messageDAO.findMessagesBetween(betweenIds.get(0), betweenIds.get(1), from, size);
        return messagesList.stream().map(messageMapper::fromMessage).toList();
    }

    @Override
    public List<MessageFullDTO> getMessages(String text, int from, int size) {
        List<Message> messagesList = messageDAO.findAll(text, from, size);
        return messagesList.stream().map(messageMapper::fromMessage).toList();
    }

    @Override
    public MessageFullDTO updateMessage(MessageDTO messageDTO, Long id) {
        Message messageIn = messageDAO.find(id);
        messageIn.setBody(messageDTO.getBody());
        Message messageOut = messageDAO.update(messageIn);
        return messageMapper.fromMessage(messageOut);
    }

    @Override
    public void deleteMessage(Long id) {
        messageDAO.deleteById(id);
    }
}
