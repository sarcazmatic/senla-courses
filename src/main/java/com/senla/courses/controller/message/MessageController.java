package com.senla.courses.controller.message;

import com.senla.courses.dto.MessageDTO;
import com.senla.courses.dto.MessageFullDTO;
import com.senla.courses.service.messages.MessageService;
import com.senla.courses.util.UserDetailsExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send/{to}")
    @ResponseStatus(HttpStatus.CREATED)
    public Long sendMessage(@RequestBody MessageDTO messageDTO,
                            @PathVariable Long to) {
        User user = UserDetailsExtractor.extractUserDetails();
        return messageService.sendMessage(messageDTO, user, to);
    }

    @PutMapping("/{id}/edit")
    @ResponseStatus(HttpStatus.OK)
    public MessageFullDTO editMessage(@RequestBody MessageDTO messageDTO,
                                      @PathVariable Long id) {
        User user = UserDetailsExtractor.extractUserDetails();
        return messageService.updateMessage(user, messageDTO, id);
    }

    @GetMapping("/between")
    @ResponseStatus(HttpStatus.OK)
    public List<MessageFullDTO> getMessage(@RequestParam(name = "ids") List<Long> betweenIds,
                                           @RequestParam(required = false, defaultValue = "1") int from,
                                           @RequestParam(required = false, defaultValue = "10") int size) {
        User user;
        try {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException e) {
            throw new RuntimeException(e.getMessage());
        }
        return messageService.getMessagesBetween(user, betweenIds, from, size);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MessageFullDTO> getMessage(@RequestParam(required = false) String text,
                                           @RequestParam(required = false, defaultValue = "1") int from,
                                           @RequestParam(required = false, defaultValue = "10") int size) {
        User user = UserDetailsExtractor.extractUserDetails();
        return messageService.findMessagesByText(user, text, from, size);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteMessage(@PathVariable Long id) {
        User user = UserDetailsExtractor.extractUserDetails();
        messageService.deleteMessage(user, id);
    }

}
