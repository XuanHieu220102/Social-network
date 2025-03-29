package org.example.socialbe.service.impl;

import jakarta.annotation.Resource;
import org.example.socialbe.dto.message.response.ChatListDTO;
import org.example.socialbe.entity.ConversationEntity;
import org.example.socialbe.entity.FriendShipEntity;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.repository.*;
import org.example.socialbe.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService implements IMessageService {
    @Resource
    private MessageRepository messageRepository;

    @Resource
    private ConversationParticipantRepository conversationParticipantRepository;

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @Resource
    private ConversationRepository conversationRepository;

    @Resource
    private FriendShipsRepository friendShipRepository;

    @Resource
    private UsersRepository userRepository;

    public List<ChatListDTO> getChatList(String userId) {
        List<ChatListDTO> chatList = new ArrayList<>();

        // 1. Lấy danh sách cuộc trò chuyện
        List<ConversationEntity> conversations = conversationRepository.findConversationsByUserId(userId);
        for (ConversationEntity conv : conversations) {
            ChatListDTO dto = new ChatListDTO();
            dto.setConversationId(conv.getId());
            dto.setType(conv.getType());

            if ("GROUP".equals(conv.getType())) {
                dto.setDisplayName(conv.getName());
            } else { // PRIVATE
                // Lấy friendId từ participant khác
                String friendId = getFriendIdFromConversation(conv.getId(), userId);
                if (friendId != null) {
                    UserEntity friend = userRepository.findById(friendId).orElse(null);
                    dto.setDisplayName(friend != null ? friend.getFullName() : "Unknown");
                    dto.setFriendId(friendId);
                }
            }

            // Lấy thời gian tin nhắn cuối (giả sử bạn có cách lấy từ MessageEntity)
            dto.setLastMessageTime(messageRepository.findLastMessageTimeByConversationId(conv.getId()));
            chatList.add(dto);
        }

        // 2. Lấy danh sách bạn bè chưa có cuộc trò chuyện
        List<FriendShipEntity> friendsWithoutChat = friendShipRepository.findFriendsWithoutConversation(userId);
        for (FriendShipEntity friend : friendsWithoutChat) {
            UserEntity friendUser = userRepository.findById(friend.getFriendId()).orElse(null);
            if (friendUser != null) {
                ChatListDTO dto = new ChatListDTO();
                dto.setDisplayName(friendUser.getFullName());
                dto.setFriendId(friend.getFriendId());
                dto.setAvatarUrl(friendUser.getAvatarUrl());
                dto.setLastMessageTime(null); // Không có tin nhắn
                chatList.add(dto);
            }
        }

        // 3. Sắp xếp theo lastMessageTime, null xuống cuối
        return chatList.stream()
                .sorted((a, b) -> {
                    if (a.getLastMessageTime() == null && b.getLastMessageTime() == null) return 0;
                    if (a.getLastMessageTime() == null) return 1;
                    if (b.getLastMessageTime() == null) return -1;
                    return b.getLastMessageTime().compareTo(a.getLastMessageTime());
                })
                .collect(Collectors.toList());
    }

    private String getFriendIdFromConversation(String conversationId, String userId) {
        List<String> friendIds = conversationParticipantRepository.findFriendIdByConversationId(conversationId, userId);
        return friendIds.isEmpty() ? null : friendIds.get(0);
    }
}
