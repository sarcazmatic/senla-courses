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
import java.util.Optional;

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
        Student student = studentDAO.find(from)
                .orElse(studentDAO.find(to)
                        .orElseThrow(() -> new RuntimeException("Не смогли найти студента ни по from, не по to")));
        Teacher teacher = teacherDAO.find(from)
                .orElse(teacherDAO.find(to)
                        .orElseThrow(() -> new RuntimeException("Не смогли найти преподавателя ни по from, не по to")));
        message.setStudent(student);
        message.setTeacher(teacher);
        message.setDateTimeSent(LocalDateTime.now());
        return messageDAO.save(message);
    }

    @Override
    public MessageFullDTO getMessage(Long id) {
        Message message = messageDAO.find(id)
                .orElseThrow(() -> new RuntimeException("Не смогли найти сообщение"));
        return messageMapper.fromMessage(message);
    }

    @Override
    public List<MessageFullDTO> getMessagesBetween(List<Long> betweenIds, int from, int size) {
        List<Message> messagesList = messageDAO.findMessagesBetween(betweenIds.get(0), betweenIds.get(1), from, size);
        if (messagesList.isEmpty()) {
            throw new RuntimeException("Список сообщений между указанными id пусть");
        }
        return messagesList.stream().map(messageMapper::fromMessage).toList();
    }

    @Override
    public List<MessageFullDTO> findMessagesByText(String text, int from, int size) {
        List<Message> messagesList = messageDAO.findAll(text, from, size);
        if (messagesList.isEmpty()) {
            throw new RuntimeException("Список сообщений между указанными id пусть");
        }
        return messagesList.stream().map(messageMapper::fromMessage).toList();
    }

    @Override
    public MessageFullDTO updateMessage(MessageDTO messageDTO, Long id) {
        Message messageIn = messageDAO.find(id)
                .orElseThrow(() -> new RuntimeException("Не смогли найти сообщение для обновления"));
        messageIn.setBody(messageDTO.getBody());
        Optional<Message> messageOutOpt = messageDAO.update(messageIn);
        if (messageOutOpt.isEmpty()) {
            throw new RuntimeException("Не смогли найти сообщение для обновления");
        }
        return messageMapper.fromMessage(messageOutOpt.get());
    }

    @Override
    public void deleteMessage(Long id) {
        messageDAO.deleteById(id);
    }
}
