package com.senla.courses.controller.message;

import com.senla.courses.dto.MessageDTO;
import com.senla.courses.dto.MessageFullDTO;
import com.senla.courses.service.messages.MessageService;
import com.senla.courses.util.UserDetailsExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/message")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send/{to}")
    @ResponseStatus(HttpStatus.CREATED)
    public Long sendMessage(@RequestBody MessageDTO messageDTO,
                            @PathVariable Long to) {
        User user = UserDetailsExtractor.extractUserDetails();
        log.info("Пользователь с логином: {} отправляет сообщение пользователю с id: {}", user.getUsername(), to);
        Long id = messageService.sendMessage(messageDTO, user, to);
        log.info("Сообщение с id: {} успешно отправлено от пользователя с логином: {} к пользователю с id: {}", id, user.getUsername(), to);
        return id;
    }

    @PutMapping("/{id}/edit")
    @ResponseStatus(HttpStatus.OK)
    public MessageFullDTO editMessage(@RequestBody MessageDTO messageDTO,
                                      @PathVariable Long id) {
        User user = UserDetailsExtractor.extractUserDetails();
        log.info("Пользователь с логном: {} редактирует сообщение с id: {}", user.getUsername(), id);
        MessageFullDTO messageFullDTOUpd = messageService.updateMessage(user, messageDTO, id);
        log.info("Сообщение с id: {} успешно отредактировано пользователем с логином {}", id, user.getUsername());
        return messageFullDTOUpd;
    }

    @GetMapping("/between")
    @ResponseStatus(HttpStatus.OK)
    public List<MessageFullDTO> getMessage(@RequestParam(name = "ids") List<Long> betweenIds,
                                           @RequestParam(required = false, defaultValue = "1") int from,
                                           @RequestParam(required = false, defaultValue = "10") int size) {
        User user = UserDetailsExtractor.extractUserDetails();
        log.info("Пользователь с логином: {} запрашивает сообщения между пользователями с id: {}, параметры: from={}, size={}", user.getUsername(), betweenIds, from, size);
        List<MessageFullDTO> messageFullDTOS = messageService.getMessagesBetween(user, betweenIds, from, size);
        log.info("Сообщения между пользователями с id: {} успешно получены пользователем с логином {}, параметры: from={}, size={}", betweenIds, user.getUsername(), from, size);
        return messageFullDTOS;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MessageFullDTO> getMessage(@RequestParam(required = false) String text,
                                           @RequestParam(required = false, defaultValue = "1") int from,
                                           @RequestParam(required = false, defaultValue = "10") int size) {
        User user = UserDetailsExtractor.extractUserDetails();
        log.info("Пользователь с логином: {} запрашивает сообщения с текстом: '{}', параметры: from={}, size={}", user.getUsername(), text, from, size);
        List<MessageFullDTO> messageFullDTOS = messageService.findMessagesByText(user, text, from, size);
        log.info("Сообщения с текстом: '{}' успешно найдены, параметры: from={}, size={}", text, from, size);
        return messageFullDTOS;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteMessage(@PathVariable Long id) {
        User user = UserDetailsExtractor.extractUserDetails();
        log.info("Пользователь с логином: {} удаляет сообщение с id: {}", user.getUsername(), id);
        messageService.deleteMessage(user, id);
        log.info("Сообщение с id: {} успешно удалено", id);
    }

}
