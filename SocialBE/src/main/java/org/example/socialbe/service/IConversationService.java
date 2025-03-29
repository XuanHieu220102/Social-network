package org.example.socialbe.service;

import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.dto.conversation.request.ConversationRequest;
import org.example.socialbe.dto.conversation.response.ConversationWithLastMessage;
import org.example.socialbe.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("conversationService")
public interface IConversationService {
    ErrorResponse createConversation(ConversationRequest request);
}
