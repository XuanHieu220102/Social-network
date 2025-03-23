package org.example.socialbe.service;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.constant.Constant;
import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.dto.comment.request.CommentRequest;
import org.example.socialbe.dto.comment.request.EditCommentRequest;
import org.example.socialbe.dto.comment.request.ReplyCommentRequest;
import org.example.socialbe.dto.comment.response.CommentResponse;
import org.example.socialbe.dto.post.response.PostDetailResponse;
import org.example.socialbe.entity.CommentsEntity;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.enums.ErrorCode;
import org.example.socialbe.exception.LeaklessException;
import org.example.socialbe.repository.CommentsRepository;
import org.example.socialbe.repository.UsersRepository;
import org.example.socialbe.util.JsonUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CommentService {
    @Resource
    private CommentsRepository commentsRepository;

    @Resource
    private ModelMapper modelMapper;

    @Resource
    private UserService userService;

    @Resource
    private UsersRepository usersRepository;

    @Resource
    private JsonUtil jsonUtil;

    public ErrorResponse createComment(UserEntity user, CommentRequest request) {
        try {
            CommentsEntity comments = modelMapper.map(request, CommentsEntity.class);
            comments.setUserId(user.getId());
            comments.setStatus(Constant.CommentStatus.PRESENTLY);
            comments.setDeleted(false);
            commentsRepository.save(comments);
            return ErrorResponse.success(comments.getId());
        }catch (Exception e) {
            log.error(e.getMessage());
            return ErrorResponse.internalServerError(e);
        }
    }

    public ErrorResponse replyComment(UserEntity user, ReplyCommentRequest request) {
        try {
            Optional<CommentsEntity> comments = commentsRepository.findById(request.getCommentId());
            if (comments.isEmpty()) {
                throw new LeaklessException(ErrorCode.RECORD_NOT_FOUND);
            }
            Optional<UserEntity> receiver = usersRepository.findById(request.getReceiverId());
            if (receiver.isEmpty()) {
                throw new LeaklessException(ErrorCode.RECORD_NOT_FOUND);
            }
            if (comments.get().getReply() != null) {
                List<CommentResponse.Reply> replies
                        = jsonUtil.fromJson(jsonUtil.toJson(comments.get().getReply()), new TypeReference<>() {});
                CommentResponse.Reply reply = CommentResponse.Reply
                        .builder()
                        .id(UUID.randomUUID().toString())
                        .content(request.getContent())
                        .sender(new PostDetailResponse.User(user.getId(), user.getUsername(), user.getAvatarUrl()))
                        .receiver(new PostDetailResponse.User(receiver.get().getId(), receiver.get().getUsername(), receiver.get().getAvatarUrl()))
                        .createdAt(LocalDateTime.now())
                        .build();
                replies.add(reply);
                comments.get().setReply(jsonUtil.toJson(replies));
                commentsRepository.save(comments.get());
                return ErrorResponse.success(comments.get().getId());
            }else {
                List<CommentResponse.Reply> replies = new ArrayList<>();
                CommentResponse.Reply reply = CommentResponse.Reply
                        .builder()
                        .id(UUID.randomUUID().toString())
                        .content(request.getContent())
                        .sender(new PostDetailResponse.User(user.getId(), user.getUsername(), user.getAvatarUrl()))
                        .receiver(new PostDetailResponse.User(receiver.get().getId(), receiver.get().getUsername(), receiver.get().getAvatarUrl()))
                        .createdAt(LocalDateTime.now())
                        .build();
                replies.add(reply);
                comments.get().setReply(replies);
                commentsRepository.save(comments.get());
                return ErrorResponse.success(comments.get().getId());
            }
        }catch (Exception e) {
            log.error(e.getMessage());
            return ErrorResponse.internalServerError(e);
        }
    }
    public ErrorResponse updateComment(EditCommentRequest request) {
        try {
            Optional<CommentsEntity> comments = commentsRepository.findById(request.getCommentId());
            if (comments.isEmpty()) {
                throw new LeaklessException(ErrorCode.RECORD_NOT_FOUND);
            }
            CommentsEntity commentsEntity = comments.get();
            commentsEntity.setContent(request.getContent());
            commentsEntity.setUpdatedAt(LocalDateTime.now());
            commentsRepository.save(commentsEntity);
            return ErrorResponse.success(commentsEntity.getId());
        }catch (Exception e) {
            log.error(e.getMessage());
            return ErrorResponse.internalServerError(e);
        }
    }

    public ErrorResponse deleteComment(String commentId) {
        try {
            Optional<CommentsEntity> comments = commentsRepository.findById(commentId);
            if (comments.isEmpty()) {
                throw new LeaklessException(ErrorCode.RECORD_NOT_FOUND);
            }
            CommentsEntity commentsEntity = comments.get();
            commentsEntity.setDeleted(true);
            commentsEntity.setUpdatedAt(LocalDateTime.now());
            commentsRepository.save(commentsEntity);
            return ErrorResponse.success(commentsEntity.getId());
        }catch (Exception e) {
            log.error(e.getMessage());
            return ErrorResponse.internalServerError(e);
        }
    }

    public List<CommentResponse> getCommentByPostId(String postId) {
        List<CommentResponse> responses = new ArrayList<>();
        List<CommentsEntity> comments = commentsRepository.findByPostId(postId);
        comments.forEach(comment -> {
            CommentResponse commentResponse = modelMapper.map(comment, CommentResponse.class);
            Optional<UserEntity> user = usersRepository.findById(comment.getUserId());
            if (user.isEmpty()) {
                throw new LeaklessException(ErrorCode.RECORD_NOT_FOUND);
            }
            PostDetailResponse.User userRes = modelMapper.map(user.get(), PostDetailResponse.User.class);
            List<CommentResponse.Reply> replies = jsonUtil.fromJson(jsonUtil.toJson(comment.getReply()), new TypeReference<>() {});
            commentResponse.setReplies(replies);
            commentResponse.setUser(userRes);
            responses.add(commentResponse);
        });
        return responses;
    }

    public long countCommentByPostId(String postId) {
        return commentsRepository.countCommentsByPostId(postId);
    }

}
