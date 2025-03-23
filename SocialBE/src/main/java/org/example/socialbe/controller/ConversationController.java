package org.example.socialbe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.dto.conversation.request.ConversationRequest;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.service.IConversationService;
import org.example.socialbe.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
