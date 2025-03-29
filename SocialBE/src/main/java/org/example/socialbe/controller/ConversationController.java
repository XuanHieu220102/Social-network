package org.example.socialbe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.dto.conversation.request.ConversationRequest;
import org.example.socialbe.dto.conversation.response.ConversationWithLastMessage;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.service.IConversationService;
import org.example.socialbe.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conversation")
@Slf4j
@RequiredArgsConstructor
public class ConversationController {
    private final IConversationService conversationService;
    private final UserService userService;

    @PostMapping
    public ErrorResponse createConversation(@RequestBody ConversationRequest conversationRequest) {
        UserEntity user = userService.checkAuthentication();
        conversationRequest.setSenderId(user.getId());
        log.info("User {} is created conversation", user.getId());
        return conversationService.createConversation(conversationRequest);
    }

//    @GetMapping("/get-conversation-by-user")
//    public List<ConversationWithLastMessage> getConversationsByUser() {
//        UserEntity user = userService.checkAuthentication();
//        log.info("User {} is getting conversations by user", user.getId());
//        return conversationService.getAllConversationsByUser(user);
//    }
}
