package org.example.socialbe.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.dto.comment.request.CommentRequest;
import org.example.socialbe.dto.comment.request.EditCommentRequest;
import org.example.socialbe.dto.comment.request.ReplyCommentRequest;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.service.CommentService;
import org.example.socialbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {
    @Resource
    private CommentService commentService;

    @Resource
    private UserService userService;

    @PostMapping
    public ErrorResponse create(@RequestBody CommentRequest request) {
        UserEntity user = userService.checkAuthentication();
        log.info("Create comment request: {}", request);
        return commentService.createComment(user, request);
    }

    @PostMapping("/reply")
    public ErrorResponse reply(@RequestBody ReplyCommentRequest request) {
        UserEntity user = userService.checkAuthentication();
        log.info("Reply comment request: {}", request);
        return commentService.replyComment(user, request);
    }

    @PostMapping("/update")
    public ErrorResponse update(@RequestBody EditCommentRequest request) {
        log.info("Update comment request: {}", request);
        return commentService.updateComment(request);
    }

    @DeleteMapping("/delete/{commentId}")
    public ErrorResponse delete(@PathVariable String commentId) {
        log.info("Delete comment request: {}", commentId);
        return commentService.deleteComment(commentId);
    }

    @GetMapping("/get-comment-post/{postId}")
    public ErrorResponse getCommentPost(@PathVariable String postId) {
        log.info("Get comment post request: {}", postId);
        return ErrorResponse.success(commentService.getCommentByPostId(postId));
    }
}
