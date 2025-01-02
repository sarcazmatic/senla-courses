<<<<<<<< HEAD:src/main/java/com/senla/courses/dto/message/MessageMapper.java
package com.senla.courses.dto.message;
========
package com.senla.courses.mapper;
>>>>>>>> main:src/main/java/com/senla/courses/mapper/MessageMapper.java

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
                .teacherName(message.getTeacher().getUser().getName())
                .studentName(message.getStudent().getUser().getName())
                .body(message.getBody())
                .dateTimeSent(message.getDateTimeSent())
                .build();
    }

}
