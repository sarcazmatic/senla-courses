package com.senla.courses.service.messages;

import com.senla.courses.dto.message.MessageDTO;
import com.senla.courses.dto.message.MessageFullDTO;

import java.util.List;

public interface MessageService {

    Long sendMessage(MessageDTO messageDTO, Long from, Long to);

    MessageFullDTO findById(Long id);

    List<MessageFullDTO> getMessagesBetween(List<Long> betweenIds, int from, int size);

    List<MessageFullDTO> findMessagesByText(String text, int from, int size);

    MessageFullDTO updateMessage(MessageDTO messageDTO, Long id);

    void deleteMessage(Long id);

}