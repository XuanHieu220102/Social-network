package org.example.socialbe.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.constant.Constant;
import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.dto.friendship.request.FriendShipRequest;
import org.example.socialbe.dto.friendship.request.UpdateStatusRequest;
import org.example.socialbe.dto.notification.response.NotificationResponse;
import org.example.socialbe.dto.notification.response.UserInfo;
import org.example.socialbe.dto.users.response.UserFilterResponse;
import org.example.socialbe.entity.FriendShipEntity;
import org.example.socialbe.entity.NotificationEntity;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.repository.FriendShipsRepository;
import org.example.socialbe.repository.NotificationRepository;
import org.example.socialbe.repository.UsersRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FriendShipService {
    @Resource
    private FriendShipsRepository friendShipsRepository;

    @Resource
    private UsersRepository usersRepository;

    @Resource
    private ModelMapper modelMapper;

    @Resource
    private NotificationRepository notificationRepository;

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    private void createNotification(String senderId, String receiveId, String type, String message) {
        UserEntity sender = usersRepository.findById(senderId).orElse(null); // Lấy thông tin người gửi
        NotificationEntity notification = new NotificationEntity();
        notification.setSenderId(senderId);
        notification.setReceiveId(receiveId);
        notification.setType(type);
        notification.setMessage(message);
        NotificationResponse response = modelMapper.map(notification, NotificationResponse.class);
        if (sender != null) {
            response.setSenderId(sender.getId());
            response.setSenderUsername(sender.getUsername());
            response.setSenderAvatarUrl(sender.getAvatarUrl());
        }
        usersRepository.findById(receiveId).ifPresent(receiver -> {
            response.setReceiverId(receiver.getId());
            response.setReceiverUsername(receiver.getUsername());
            response.setReceiverAvatarUrl(receiver.getAvatarUrl());
        });
        response.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/notifications/" + receiveId, response);        // Đẩy thông báo qua WebSocket
    }


    public ErrorResponse create(UserEntity user, FriendShipRequest request) {
        try {
            Optional<FriendShipEntity> friendShipExist = friendShipsRepository.findByUserIdAndFriendId(user.getId(), request.getFriendId());

            if (friendShipExist.isPresent()) {
                FriendShipEntity friendShipEntity = friendShipExist.stream()
                        .filter(f -> f.getUserId().equals(user.getId()))
                        .findFirst()
                        .orElse(friendShipExist.get());

                if (friendShipEntity.getStatus() == Constant.FriendStatus.BLOCK) {
                    return ErrorResponse.recordNotFound("Bạn đã bị chặn hoặc đã chặn người này.");
                }

                if (friendShipEntity.getStatus() != Constant.FriendStatus.ACCEPT) {
                    friendShipEntity.setStatus(Constant.FriendStatus.PENDING);
                    friendShipsRepository.save(friendShipEntity);
                    createNotification(user.getId(), request.getFriendId(), "FRIEND_REQUEST", "Bạn nhận được lời mời kết bạn.");
                    return ErrorResponse.success(friendShipEntity.getId());
                }else {
                    friendShipEntity.setStatus(Constant.FriendStatus.PENDING);
                }

                return ErrorResponse.recordNotFound("Hai người đã là bạn bè!");
            }

            FriendShipEntity newFriendShip = new FriendShipEntity();
            newFriendShip.setUserId(user.getId());
            newFriendShip.setFriendId(request.getFriendId());
            newFriendShip.setStatus(Constant.FriendStatus.PENDING);
            friendShipsRepository.save(newFriendShip);

            createNotification(user.getId(), request.getFriendId(), "FRIEND_REQUEST", "Bạn nhận được lời mời kết bạn.");

            return ErrorResponse.success(newFriendShip.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ErrorResponse.internalServerError(e);
        }
    }


    public ErrorResponse accept(String friendShipId, UserEntity user) {
        try {
            List<FriendShipEntity> friendships = friendShipsRepository.findFriendshipBetweenUsers(user.getId(), friendShipId);

            if (friendships.isEmpty()) {
                return ErrorResponse.recordNotFound(friendShipId);
            }
            FriendShipEntity friendShipEntity = friendships.stream()
                    .filter(f -> f.getFriendId().equals(user.getId())) // Chỉ lấy bản ghi do friend gửi
                    .findFirst()
                    .orElse(null);

            if (friendShipEntity == null) {
                return ErrorResponse.recordNotFound("Không tìm thấy yêu cầu kết bạn.");
            }

            if (friendShipEntity.getStatus() == Constant.FriendStatus.ACCEPT) {
                return new ErrorResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()),"Hai người đã là bạn bè.");
            }
            if (friendShipEntity.getStatus() == Constant.FriendStatus.BLOCK) {
                return new ErrorResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()),"Bạn không thể chấp nhận vì đã bị chặn.");
            }

            // Cập nhật trạng thái thành ACCEPT
            friendShipEntity.setStatus(Constant.FriendStatus.ACCEPT);
            friendShipsRepository.save(friendShipEntity);

            return ErrorResponse.success(friendShipEntity.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ErrorResponse.internalServerError(e);
        }
    }


    public ErrorResponse reject(String friendShipId, UserEntity user) {
        try {
            List<FriendShipEntity> friendships = friendShipsRepository.findFriendshipBetweenUsers(user.getId(), friendShipId);

            if (friendships.isEmpty()) {
                return ErrorResponse.recordNotFound(friendShipId);
            }


            FriendShipEntity friendShipEntity = friendships.stream()
                    .filter(f -> f.getFriendId().equals(user.getId()))
                    .findFirst()
                    .orElse(null);

            if (friendShipEntity == null) {
                return ErrorResponse.recordNotFound("Không tìm thấy yêu cầu kết bạn để từ chối.");
            }

            // Kiểm tra trạng thái hiện tại
            if (friendShipEntity.getStatus() == Constant.FriendStatus.ACCEPT) {
                return new ErrorResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()),"Hai người đã là bạn bè, không thể từ chối.");
            }
            if (friendShipEntity.getStatus() == Constant.FriendStatus.BLOCK) {
                return new ErrorResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()),"Bạn không thể từ chối vì đã bị chặn.");
            }

            friendships.forEach(f -> {
                f.setStatus(Constant.FriendStatus.REJECT);
                friendShipsRepository.save(friendShipEntity);
            });
            return ErrorResponse.success(friendShipEntity.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ErrorResponse.internalServerError(e);
        }
    }


    public List<UserFilterResponse> getFriendRequests(UserEntity user) {
        List<UserEntity> friendShipEntities = friendShipsRepository.findUsersSentFriendRequests(user.getId(), Constant.FriendStatus.PENDING);
        return modelMapper.map(friendShipEntities, new TypeToken<List<UserFilterResponse>>(){}.getType());
    }

    public List<UserFilterResponse> getNewFriendRequest(UserEntity user) {
        List<UserEntity> userEntities = friendShipsRepository.findNewUsersSentFriendRequests(user.getId(), Constant.FriendStatus.PENDING);
        return modelMapper.map(userEntities, new TypeToken<List<UserFilterResponse>>(){}.getType());
    }
    public ErrorResponse blockFriend(UserEntity user, String friendId) {
        try {
            List<FriendShipEntity> friendships = friendShipsRepository.findFriendshipBetweenUsers(user.getId(), friendId);

            if (friendships.isEmpty()) {
                return ErrorResponse.recordNotFound(friendId);
            }
            for (FriendShipEntity friendShipEntity : friendships) {
                // Kiểm tra xem mối quan hệ đã bị chặn chưa
                if (friendShipEntity.getStatus() == Constant.FriendStatus.BLOCK) {
                    return ErrorResponse.recordNotFound("Bạn đã chặn người này rồi.");
                }

                // Cập nhật trạng thái thành BLOCK
                friendShipEntity.setStatus(Constant.FriendStatus.BLOCK);
                friendShipsRepository.save(friendShipEntity);
            }
            return ErrorResponse.success("Block người dùng này thành công");

        } catch (Exception e) {
            log.error(e.getMessage());
            return ErrorResponse.internalServerError(e);
        }
    }


    public List<UserFilterResponse> getFriendOfUser(UserEntity user) {
        List<UserEntity> friendShipEntities = friendShipsRepository.findFriends(user.getId());
        return modelMapper
                .map(friendShipEntities, new TypeToken<List<UserFilterResponse>>(){}.getType());
    }

    public List<UserFilterResponse> getFriendByStatus(UserEntity user) {
        List<UserEntity> friendShipEntities = friendShipsRepository.findBlockedUsers(user.getId());
        return modelMapper.map(friendShipEntities, new TypeToken<List<UserFilterResponse>>(){}.getType());
    }

    public ErrorResponse updateStatus(UserEntity user, UpdateStatusRequest request) {
        try {
            List<FriendShipEntity> friendships = friendShipsRepository.findFriendshipBetweenUsers(user.getId(), request.getFriendId());

            if (friendships.isEmpty()) {
                return ErrorResponse.recordNotFound(request.getFriendId());
            }
            FriendShipEntity friendShipEntity = friendships.stream()
                    .filter(f -> f.getUserId().equals(user.getId())) // Chọn quan hệ user → friend
                    .findFirst()
                    .orElse(null);

            if (friendShipEntity == null) {
                return ErrorResponse.recordNotFound("Không tìm thấy quan hệ hợp lệ để cập nhật.");
            }

            if (friendShipEntity.getStatus() == request.getStatus()) {
                return new ErrorResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()),"Trạng thái đã được cập nhật trước đó.");
            }
            friendShipEntity.setStatus(request.getStatus());
            friendShipsRepository.save(friendShipEntity);

            return ErrorResponse.success(friendShipEntity.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ErrorResponse.internalServerError(e);
        }
    }

}
