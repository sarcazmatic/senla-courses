package com.senla.courses.controller.teacher;

import com.senla.courses.dto.message.MessageDTO;
import com.senla.courses.dto.message.MessageFullDTO;
import com.senla.courses.service.messages.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/admin/message")
@RequiredArgsConstructor
public class TeacherMessageController {

    private final MessageService messageService;

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.CREATED)
    public Long sendMessage(@RequestBody MessageDTO messageDTO,
                            @RequestParam Long from,
                            @RequestParam Long to) {
        return messageService.sendMessage(messageDTO, from, to);
    }

    @PutMapping("/{id}/update")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageFullDTO editMessage(@RequestBody MessageDTO messageDTO,
                            @PathVariable Long id) {
        return messageService.updateMessage(messageDTO, id);
    }

    @GetMapping("/between")
    @ResponseStatus(HttpStatus.OK)
    public List<MessageFullDTO> getMessage(@RequestParam(name = "ids") List<Long> betweenIds,
                                           @RequestParam (required = false, defaultValue = "1") int from,
                                           @RequestParam (required = false, defaultValue = "10") int size) {
        return messageService.getMessagesBetween(betweenIds, from, size);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MessageFullDTO> getMessage(@RequestParam (required = false) String text,
                                           @RequestParam (required = false, defaultValue = "1") int from,
                                           @RequestParam (required = false, defaultValue = "10") int size) {
        return messageService.getMessages(text, from, size);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
    }

}
