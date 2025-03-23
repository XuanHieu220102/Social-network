package org.example.socialbe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/send")
    public String sendMessage() {
        messagingTemplate.convertAndSend("/topic/test", "Hello from WebSocket!");
        return "Message sent!";
    }
}