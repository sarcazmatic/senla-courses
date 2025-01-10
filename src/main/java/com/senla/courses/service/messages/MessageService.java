package com.senla.courses.service.messages;

import com.senla.courses.dto.MessageDTO;
import com.senla.courses.dto.MessageFullDTO;
import org.springframework.security.core.userdetails.User;


import java.util.List;

public interface MessageService {

    Long sendMessage(MessageDTO messageDTO, User user, Long to);

    MessageFullDTO findById(Long id);

    List<MessageFullDTO> getMessagesBetween(User user, List<Long> betweenIds, int from, int size);

    List<MessageFullDTO> findMessagesByText(User user, String text, int from, int size);

    MessageFullDTO updateMessage(User user, MessageDTO messageDTO, Long id);

    void deleteMessage(User user, Long id);

}