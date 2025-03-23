package org.example.socialbe.service;

import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.dto.conversation.request.ConversationRequest;
import org.springframework.stereotype.Service;

@Service("conversationService")
public interface IConversationService {
    ErrorResponse createConversation(ConversationRequest request);
}
