package org.example.socialbe.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.dto.likes.response.GetUserLikePostResponse;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.service.LikesService;
import org.example.socialbe.service.UserService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/like")
public class LikesController {
    @Resource
    private LikesService likesService;

    @Resource
    private UserService userService;

    @PostMapping("/count-like/{postId}")
    public long countLikeByPostId(@PathVariable String postId) {
        log.info("Count like by post id {}", postId);
        return likesService.countLikesByPostId(postId);
    }

    @PostMapping("/get-user-like-post/{postId}")
    public GetUserLikePostResponse getUserLikePostResponse(@PathVariable String postId) {
        log.info("Get user like post id {}", postId);
        return likesService.getUserLikePost(postId);
    }
}
