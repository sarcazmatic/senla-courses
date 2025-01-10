package com.senla.courses.mapper;

import com.senla.courses.dto.MessageDTO;
import com.senla.courses.dto.MessageFullDTO;
import com.senla.courses.model.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public Message fromMessageDTO(MessageDTO messageDTO) {
        return Message.builder()
                .body(messageDTO.getBody())
                .build();
    }

    public MessageFullDTO fromMessage(Message message) {
        return MessageFullDTO.builder()
                .from(message.getFrom().getName())
                .to(message.getTo().getName())
                .body(message.getBody())
                .dateTimeSent(message.getDateTimeSent())
                .build();
    }

}
