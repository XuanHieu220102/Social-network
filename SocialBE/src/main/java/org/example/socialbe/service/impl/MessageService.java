package org.example.socialbe.service.impl;

import jakarta.annotation.Resource;
import org.example.socialbe.repository.ConversationParticipantRepository;
import org.example.socialbe.repository.MessageRepository;
import org.example.socialbe.service.IMessageService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageService implements IMessageService {
    @Resource
    private MessageRepository messageRepository;

    @Resource
    private ConversationParticipantRepository conversationParticipantRepository;

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;
}
