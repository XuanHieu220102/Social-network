package org.example.socialbe.service;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.socialbe.constant.Constant;
import org.example.socialbe.dto.BaseFilterRequest;
import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.dto.comment.response.CommentResponse;
import org.example.socialbe.dto.notification.response.NotificationResponse;
import org.example.socialbe.dto.post.request.ChangeStatusRequest;
import org.example.socialbe.dto.post.request.PostRequest;
import org.example.socialbe.dto.post.request.SearchPostNewFeedRequest;
import org.example.socialbe.dto.post.request.SharePostRequest;
import org.example.socialbe.dto.post.response.PostDetailResponse;
import org.example.socialbe.dto.post.response.PostResponse;
import org.example.socialbe.dto.post.response.SearchPostResponse;
import org.example.socialbe.dto.post.response.SharePostResponse;
import org.example.socialbe.entity.NotificationEntity;
import org.example.socialbe.entity.PostEntity;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.enums.ErrorCode;
import org.example.socialbe.exception.LeaklessException;
import org.example.socialbe.repository.*;
import org.example.socialbe.util.JsonUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostService {
    @Resource
    private PostRepository postRepository;

    @Resource
    private ModelMapper modelMapper;

    @Resource
    private CloudinaryService cloudinaryService;

    @Resource
    private CommomRepositoryCustom commomRepositoryCustom;

    @Resource
    private LikesService likesService;

    @Resource
    private UserService userService;

    @Resource
    private UsersRepository usersRepository;

    @Resource
    private CommentService commentService;

    @Resource
    private JsonUtil jsonUtil;

    @Resource
    private NotificationRepository notificationRepository;

    @Resource
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private FriendShipService friendShipService;
    @Autowired
    private FriendShipsRepository friendShipsRepository;

    public void createNotification(String senderId, List<UserEntity> receives, String type,String postId ,String message) {
        UserEntity sender = usersRepository.findById(senderId).orElse(null); // Lấy thông tin người gửi
        receives.forEach(receive -> {
            NotificationEntity notification = new NotificationEntity();
            notification.setSenderId(senderId);
            notification.setReceiveId(receive.getId());
            notification.setPostId(postId);
            notification.setType(type);
            notification.setMessage(message);
            NotificationResponse response = modelMapper.map(notification, NotificationResponse.class);
            if (sender != null) {
                response.setSenderId(sender.getId());
                response.setSenderUsername(sender.getUsername());
                response.setSenderAvatarUrl(sender.getAvatarUrl());
            }
            response.setReceiverId(receive.getId());
            response.setReceiverUsername(receive.getUsername());
            response.setReceiverAvatarUrl(receive.getAvatarUrl());
            response.setCreatedAt(LocalDateTime.now());
            notificationRepository.save(notification);
            messagingTemplate.convertAndSend("/topic/notifications/" + receive.getId(), response);
        });
    }

    @CacheEvict(value = "curentUser", key = "#user.username")
    public ErrorResponse create(UserEntity user, PostRequest request) {
        try {
            PostEntity post = modelMapper.map(request, PostEntity.class);
            List<String> images;
            if (request.getImageUrls() != null) {
                if (request.getTypeFile().equals("IMAGE")){
                    images = cloudinaryService.uploadMultipleImages(request.getImageUrls()).join();
                } else {
                    images = new ArrayList<>();
                    if (request.getTypeFile().equals("VIDEO")) {
                        request.getImageUrls().forEach(video -> {
                            try {
                                String imageUrl = cloudinaryService.uploadMedia(video);
                                images.add(imageUrl);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                }
            } else {
                images = new ArrayList<>();
            }
            if (user.getListImages() == null) {
                user.setListImages(new ArrayList<>());
            }
            List<String> userImages = user.getListImages();
            userImages.addAll(images);
            user.setListImages(userImages);
            usersRepository.save(user);
            post.setUserId(user.getId());
            post.setImageUrls(images);
            post.setLikes(0);
            post.setComments(0);
            post.setDeleted(false);
            postRepository.save(post);
            List<UserEntity> userEntities = friendShipsRepository.findFriends(user.getId());
            createNotification(user.getId(), userEntities, "NEW_POST", post.getId(),user.getFullName() + " vừa đăng bài viết mới");
            return ErrorResponse.success(post.getId());
        }catch (Exception e) {
            log.error(e.getMessage());
            return ErrorResponse.internalServerError(e);
        }
    }

    @CacheEvict(value = "curentUser", key = "#user.username")
    public ErrorResponse update(UserEntity user, String postId, PostRequest request) {
        try {
            Optional<PostEntity> post = postRepository.findById(postId);
            if (post.isEmpty()) {
                return ErrorResponse.recordNotFound(postId);
            }
            if (!post.get().getUserId().equals(user.getId())) {
                return new ErrorResponse(String.valueOf(HttpStatus.UNAUTHORIZED.value()), "Your dont permission");
            }
            PostEntity postEntity = post.get();
            modelMapper.map(request, postEntity);
            List<String> images;
            if (request.getImageUrls() != null) {
                if (request.getTypeFile().equals("IMAGE")){
                    images = cloudinaryService.uploadMultipleImages(request.getImageUrls()).join();
                } else {
                    images = new ArrayList<>();
                    if (request.getTypeFile().equals("VIDEO")) {
                        request.getImageUrls().forEach(video -> {
                            try {
                                String imageUrl = cloudinaryService.uploadMedia(video);
                                images.add(imageUrl);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                }
            } else {
                images = new ArrayList<>();
            }
            postEntity.setImageUrls(images);
            postEntity.setUpdatedAt(LocalDateTime.now());
            postRepository.save(postEntity);
            return ErrorResponse.success(postId);
        }catch (Exception e) {
            log.error(e.getMessage());
            return ErrorResponse.internalServerError(e);
        }
    }

    public SearchPostResponse searchPost (BaseFilterRequest request) {
        Page<PostEntity> postEntities = commomRepositoryCustom.searchPost(request);
        SearchPostResponse response = new SearchPostResponse();
        List<PostDetailResponse> postResponseList = new ArrayList<>();
        postEntities.getContent().forEach(postEntity -> {
            PostDetailResponse postResponse = modelMapper.map(postEntity, PostDetailResponse.class);
            postResponse.setAuthor(mapToUser(postEntity.getUserId()));
            List<PostDetailResponse.User> tags = new ArrayList<>();
                postEntity.getTag().forEach(tag -> {
                    tag = tag.replaceAll("[\\[\\]\"]", "");
                    if (StringUtils.isNotBlank(tag)) {
                        Optional<UserEntity> userEntity = usersRepository.findById(tag);
                        if (userEntity.isEmpty()) {
                            throw new LeaklessException(ErrorCode.RECORD_NOT_FOUND);
                        }
                        PostDetailResponse.User user = modelMapper.map(userEntity.get(), PostDetailResponse.User.class);
                        tags.add(user);
                    }
                });
            if (postResponse.getHashtag() != null) {
                postResponse.setHashtag(postEntity.getHashtag().stream()
                        .map(tag -> tag.replaceAll("[\\[\\]\"]", "")) // Loại bỏ dấu [] và "
                        .toList());
            }
            postResponse.setTags(tags);
            if (postEntity.getType().equals(Constant.TYPE_POST.SHARED)) {
                PostDetailResponse.ShareInfo shareInfo = jsonUtil.fromJson(jsonUtil.toJson(postEntity.getShareInfo()), PostDetailResponse.ShareInfo.class);
                if (shareInfo != null) {
                    PostDetailResponse.User usershare = mapToUser(shareInfo.getUserId());
                    shareInfo.setUsername(usershare.getUsername());
                    shareInfo.setAvatarUrl(usershare.getAvatarUrl());
                    postResponse.setShareInfo(shareInfo);
                }
            }
            long countLike = likesService.countLikesByPostId(postEntity.getId());
            long countComment = commentService.countCommentByPostId(postEntity.getId());
            long countShare = postRepository.countSharedPost(postEntity.getId());
            postResponse.setCountShare(countShare);
            postResponse.setCountLike(countLike);
            postResponse.setCountComment(countComment);
            postResponseList.add(postResponse);
        });
        response.setPosts(postResponseList);
        return response;
    }
    public SearchPostResponse searchPostNewFeed(UserEntity userCurrent, SearchPostNewFeedRequest request) {
        List<PostEntity> postEntities = postRepository.findPostsByUserAndFriends(userCurrent.getId(), request.getSize(), request.getPage() * request.getSize());
        SearchPostResponse response = new SearchPostResponse();
        List<PostDetailResponse> postResponseList = new ArrayList<>();
        postEntities.forEach(postEntity -> {
            PostDetailResponse postResponse = modelMapper.map(postEntity, PostDetailResponse.class);
            postResponse.setAuthor(mapToUser(postEntity.getUserId()));
            List<PostDetailResponse.User> tags = new ArrayList<>();
            postEntity.getTag().forEach(tag -> {
                tag = tag.replaceAll("[\\[\\]\"]", "");
                if (StringUtils.isNotBlank(tag)) {
                    Optional<UserEntity> userEntity = usersRepository.findById(tag);
                    if (userEntity.isEmpty()) {
                        throw new LeaklessException(ErrorCode.RECORD_NOT_FOUND);
                    }
                    PostDetailResponse.User user = modelMapper.map(userEntity.get(), PostDetailResponse.User.class);
                    tags.add(user);
                }
            });
            if (postResponse.getHashtag() != null) {
                postResponse.setHashtag(postEntity.getHashtag().stream()
                        .map(tag -> tag.replaceAll("[\\[\\]\"]", "")) // Loại bỏ dấu [] và "
                        .toList());
            }
            postResponse.setTags(tags);
            if (postEntity.getType().equals(Constant.TYPE_POST.SHARED)) {
                PostDetailResponse.ShareInfo shareInfo = jsonUtil.fromJson(jsonUtil.toJson(postEntity.getShareInfo()), PostDetailResponse.ShareInfo.class);
                if (shareInfo != null) {
                    PostDetailResponse.User usershare = mapToUser(shareInfo.getUserId());
                    shareInfo.setUsername(usershare.getUsername());
                    shareInfo.setAvatarUrl(usershare.getAvatarUrl());
                    postResponse.setShareInfo(shareInfo);
                }
            }
            long countLike = likesService.countLikesByPostId(postEntity.getId());
            long countComment = commentService.countCommentByPostId(postEntity.getId());
            long countShare = postRepository.countSharedPost(postEntity.getId());
            postResponse.setCountShare(countShare);
            postResponse.setCountLike(countLike);
            postResponse.setCountComment(countComment);
            postResponseList.add(postResponse);
        });
        response.setPosts(postResponseList);
        return response;
    }
    public ErrorResponse delete(UserEntity user, String postId) {
        try {
            Optional<PostEntity> post = postRepository.findById(postId);
            if (post.isEmpty()) {
                return ErrorResponse.recordNotFound(postId);
            }
            if (!post.get().getUserId().equals(user.getId())) {
                return new ErrorResponse(String.valueOf(HttpStatus.UNAUTHORIZED.value()), "Your dont permission");
            }
            PostEntity postEntity = post.get();
            postEntity.setDeleted(true);
            postRepository.save(postEntity);
            return new ErrorResponse(String.valueOf(HttpStatus.OK.value()), "Your post has been deleted");
        }catch (Exception e) {
            log.error(e.getMessage());
            return ErrorResponse.internalServerError(e);
        }
    }

    public ErrorResponse changeStatus(UserEntity user, ChangeStatusRequest request) {
        try {
            Optional<PostEntity> post = postRepository.findById(request.getPostId());
            if (post.isEmpty()) {
                return ErrorResponse.recordNotFound(request.getPostId());
            }
            if (!post.get().getUserId().equals(user.getId())) {
                return new ErrorResponse(String.valueOf(HttpStatus.UNAUTHORIZED.value()), "Your dont permission");
            }
            PostEntity postEntity = post.get();
            if (request.getStatus() == Constant.PostStatus.PRIVATE) {
                postEntity.setStatus(Constant.PostStatus.PUBLIC);
            }else if (request.getStatus() == Constant.PostStatus.PUBLIC) {
                postEntity.setStatus(Constant.PostStatus.PUBLIC);
            }else if (request.getStatus() == Constant.PostStatus.FRIEND) {
                postEntity.setStatus(Constant.PostStatus.FRIEND);
            }else if (request.getStatus() == Constant.PostStatus.CUSTOM) {
                postEntity.setStatus(Constant.PostStatus.CUSTOM);
            }
            postRepository.save(postEntity);
            return ErrorResponse.success(request.getPostId());
        }catch (Exception e) {
            log.error(e.getMessage());
            return ErrorResponse.internalServerError(e);
        }
    }


    public ErrorResponse likesPost(UserEntity user, String postId) {
        try {
            likesService.createLikes(user.getId(), postId);
            return ErrorResponse.success(" Like successfully !");
        }catch (Exception ex) {
            log.error(ex.getMessage());
            return ErrorResponse.internalServerError(ex);
        }
    }


    public ErrorResponse detail(String postId) {
        Optional<PostEntity> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new LeaklessException(ErrorCode.RECORD_NOT_FOUND);
        }
        PostEntity postEntity = post.get();
        PostDetailResponse response = modelMapper.map(postEntity, PostDetailResponse.class);
        response.setAuthor(mapToUser(postEntity.getUserId()));
        List<PostDetailResponse.User> tags = new ArrayList<>();
        postEntity.getTag().forEach(tag -> {
            tag = tag.replaceAll("[\\[\\]\"]", "");
            if (StringUtils.isNotBlank(tag)) {
                Optional<UserEntity> userEntity = usersRepository.findById(tag);
                if (userEntity.isEmpty()) {
                    throw new LeaklessException(ErrorCode.RECORD_NOT_FOUND);
                }
                PostDetailResponse.User user = modelMapper.map(userEntity.get(), PostDetailResponse.User.class);
                tags.add(user);
            }
        });
        if (response.getHashtag() != null) {
            response.setHashtag(postEntity.getHashtag().stream()
                    .map(tag -> tag.replaceAll("[\\[\\]\"]", "")) // Loại bỏ dấu [] và "
                    .toList());
        }
        response.setTags(tags);
        if (postEntity.getType().equals(Constant.TYPE_POST.SHARED)) {
            PostDetailResponse.ShareInfo shareInfo = jsonUtil.fromJson(jsonUtil.toJson(postEntity.getShareInfo()), PostDetailResponse.ShareInfo.class);
            if (shareInfo != null) {
                PostDetailResponse.User usershare = mapToUser(shareInfo.getUserId());
                shareInfo.setUsername(usershare.getUsername());
                shareInfo.setAvatarUrl(usershare.getAvatarUrl());
                response.setShareInfo(shareInfo);
            }
        }
        List<CommentResponse> comments = commentService.getCommentByPostId(postId);
        List<PostDetailResponse.Comment> commentsByPost = modelMapper
                .map(comments, new TypeToken<List<PostDetailResponse.Comment>>() {}.getType());
        response.setComments(commentsByPost);
        long countLike = likesService.countLikesByPostId(postEntity.getId());
        long countComment = commentService.countCommentByPostId(postEntity.getId());
        response.setCountLike(countLike);
        response.setCountComment(countComment);
        return ErrorResponse.success(response);
    }

    public PostDetailResponse.User mapToUser(String userId) {
        Optional<UserEntity> user = usersRepository.findById(userId);
        if (user.isEmpty()) {
            throw new LeaklessException(ErrorCode.RECORD_NOT_FOUND);
        }
        UserEntity userEntity = user.get();
        return  modelMapper.map(userEntity, PostDetailResponse.User.class);
    }

    public ErrorResponse sharePost(SharePostRequest request) {
        try {
            PostEntity postEntity = postRepository.findById(request.getPostId()).orElse(null);
            if (postEntity == null) {
                return ErrorResponse.recordNotFound("Post not found :" + request.getPostId());
            }
            PostEntity newPost = new PostEntity();
            newPost.setUserId(postEntity.getUserId());
            newPost.setContent(postEntity.getContent());
            newPost.setImageUrls(postEntity.getImageUrls());
            newPost.setStatus(postEntity.getStatus());
            newPost.setDeleted(postEntity.isDeleted());
            newPost.setTag(postEntity.getTag());
            newPost.setHashtag(postEntity.getHashtag());
            newPost.setLikes(postEntity.getLikes());
            newPost.setShareInfo(SharePostRequest.builder()
                    .userId(request.getUserId())
                    .postId(request.getPostId())
                    .description(request.getDescription())
                    .regime(request.getRegime()).build());
            newPost.setCreatedAt(LocalDateTime.now());
            newPost.setUpdatedAt(LocalDateTime.now());
            newPost.setType(Constant.TYPE_POST.SHARED);
            postRepository.save(newPost);
            return ErrorResponse.success(newPost.getId());
        }catch (Exception ex){
            log.error("Has an error ", ex);
            return ErrorResponse.internalServerError(ex);
        }
    }

}
