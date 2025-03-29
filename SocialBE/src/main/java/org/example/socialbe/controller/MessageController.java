package org.example.socialbe.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.dto.message.response.ChatListDTO;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.service.UserService;
import org.example.socialbe.service.impl.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/message")
public class MessageController {
    @Resource
    private MessageService messageService;

    @Resource
    private UserService userService;

    @GetMapping("/get-chat-list")
    public List<ChatListDTO> getChatList() {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} is get list room chat", user.getId());
        return messageService.getChatList(user.getId());
    }
}
