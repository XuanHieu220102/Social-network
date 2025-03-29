package org.example.socialbe.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.dto.conversation.request.ConversationRequest;
import org.example.socialbe.dto.conversation.response.ConversationResponse;
import org.example.socialbe.dto.conversation.response.ConversationWithLastMessage;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.repository.ConversationParticipantRepository;
import org.example.socialbe.repository.ConversationRepository;
import org.example.socialbe.service.IConversationService;
import org.example.socialbe.util.IdGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements IConversationService {
    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository conversationParticipantRepository;

    @Override
    public ErrorResponse createConversation(ConversationRequest request) {
        try {
            if (Boolean.TRUE.equals(!conversationRepository.isConversationExist(request.getSenderId(), request.getReceiverId()))
            || Boolean.TRUE.equals(!conversationRepository.isConversationExist(request.getReceiverId(), request.getSenderId()))) {
                String conversationId = IdGenerator.generateId();
                conversationRepository.createConversation(conversationId, request.getType(), request.getName());
                conversationParticipantRepository.insertConversationParticipants(IdGenerator.generateId(), conversationId, request.getSenderId());
                conversationParticipantRepository.insertConversationParticipants(IdGenerator.generateId(), conversationId, request.getReceiverId());
                return ErrorResponse.success(conversationId);
            }
            return new ErrorResponse(String.valueOf(HttpStatus.BAD_REQUEST), "Cuộc trò chuyện đã tồn tại");
        }catch (Exception ex){
            return new ErrorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getMessage());
        }
    }

}
