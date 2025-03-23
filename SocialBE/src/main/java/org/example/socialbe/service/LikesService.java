package org.example.socialbe.service;

import jakarta.annotation.Resource;
import org.example.socialbe.constant.Constant;
import org.example.socialbe.dto.likes.response.GetUserLikePostResponse;
import org.example.socialbe.entity.LikesEntity;
import org.example.socialbe.entity.PostEntity;
import org.example.socialbe.enums.ErrorCode;
import org.example.socialbe.exception.LeaklessException;
import org.example.socialbe.repository.LikesRepository;
import org.example.socialbe.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikesService {
    @Resource
    private LikesRepository likesRepository;

    @Resource
    private PostRepository postRepository;

    public void createLikes(String userId, String postId) {
        LikesEntity likes = likesRepository.findLikesByUserIdAndPostId(userId, postId);
        PostEntity postEntity = postRepository.findByIdAndDeletedFalse(postId);
        if (postEntity == null) {
            throw new LeaklessException(ErrorCode.RECORD_NOT_FOUND);
        }
        if (likes == null) {
            LikesEntity likesEntity = new LikesEntity();
            likesEntity.setUserId(userId);
            likesEntity.setPostId(postId);
            likesEntity.setStatus(Constant.LikeStatus.LIKE);
            likesRepository.save(likesEntity);
            postEntity.setLikes(postEntity.getLikes() + 1);
            postRepository.save(postEntity);
            return;
        }
        if (likes.getStatus() == Constant.LikeStatus.LIKE) {
            likes.setStatus(Constant.LikeStatus.UNLIKE);
            postEntity.setLikes(postEntity.getLikes() - 1);
        } else if (likes.getStatus() == Constant.LikeStatus.UNLIKE) {
            likes.setStatus(Constant.LikeStatus.LIKE);
            postEntity.setLikes(postEntity.getLikes() + 1);
        }
        postRepository.save(postEntity);
        likesRepository.save(likes);
    }


    public long countLikesByPostId(String postId) {
        return likesRepository.countLikesByPostId(postId);
    }

    public GetUserLikePostResponse getUserLikePost(String postId) {
        GetUserLikePostResponse response = new GetUserLikePostResponse();
        List<GetUserLikePostResponse.UserLikePost> userLikePosts = likesRepository.findUsersLikesByPostId(postId);
        response.setUserLikePosts(userLikePosts);
        response.setRecordSize(userLikePosts.size());
        return response;
    }
}
