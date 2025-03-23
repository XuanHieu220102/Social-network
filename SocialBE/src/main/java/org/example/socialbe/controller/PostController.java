package org.example.socialbe.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.dto.BaseFilterRequest;
import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.dto.post.request.ChangeStatusRequest;
import org.example.socialbe.dto.post.request.PostRequest;
import org.example.socialbe.dto.post.request.SearchPostNewFeedRequest;
import org.example.socialbe.dto.post.request.SharePostRequest;
import org.example.socialbe.dto.post.response.PostResponse;
import org.example.socialbe.dto.post.response.SearchPostResponse;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.service.PostService;
import org.example.socialbe.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/post")
public class PostController {
    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    @PostMapping
    public ErrorResponse create(@ModelAttribute PostRequest request) {
        UserEntity user = userService.checkAuthentication();
        log.info("Create new post");
        return postService.create(user, request);
    }

    @PutMapping("/update/{postId}")
    public ErrorResponse update(@PathVariable String postId, @ModelAttribute PostRequest request) {
        UserEntity user = userService.checkAuthentication();
        log.info("Update post");
        return postService.update(user, postId, request);
    }

    @PostMapping("/search")
    public SearchPostResponse search(@RequestBody BaseFilterRequest request) {
        log.info("Search post");
        return postService.searchPost(request);
    }

    @PostMapping("/search-new-feed")
    public SearchPostResponse searchNewFeed(@RequestBody SearchPostNewFeedRequest request) {
        UserEntity user = userService.checkAuthentication();
        log.info("Search post new feed");
        return postService.searchPostNewFeed(user, request);
    }

    @PostMapping("/like-post/{postId}")
    public ErrorResponse likePost(@PathVariable String postId) {
        UserEntity user = userService.checkAuthentication();
        log.info("Like post");
        return postService.likesPost(user, postId);
    }

    @GetMapping("/detail/{postId}")
    public ErrorResponse detail(@PathVariable String postId) {
        log.info("Get detail post id {}", postId);
        return postService.detail(postId);
    }

    @PostMapping("/share")
    public ErrorResponse share(@RequestBody SharePostRequest request) {
        UserEntity user = userService.checkAuthentication();
        log.info("Share post");
        request.setUserId(user.getId());
        return postService.sharePost(request);
    }

    @PostMapping("/change-regime")
    public ErrorResponse changeRegime(@RequestBody ChangeStatusRequest request) {
        UserEntity user = userService.checkAuthentication();
        log.info("Change regime");
        return postService.changeStatus(user, request);
    }

    @DeleteMapping("/delete/{postId}")
    public ErrorResponse delete(@PathVariable String postId) {
        UserEntity user = userService.checkAuthentication();
        log.info("Delete post");
        return postService.delete(user, postId);
    }
}
