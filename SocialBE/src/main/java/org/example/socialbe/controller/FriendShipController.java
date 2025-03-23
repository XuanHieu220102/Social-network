package org.example.socialbe.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.constant.Constant;
import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.dto.friendship.request.FriendShipRequest;
import org.example.socialbe.dto.friendship.request.UpdateStatusRequest;
import org.example.socialbe.dto.users.response.UserFilterResponse;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.service.FriendShipService;
import org.example.socialbe.service.UserService;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/friend-ship")
public class FriendShipController {
    @Resource
    private FriendShipService friendShipService;
    @Autowired
    private UserService userService;

    @PostMapping("/send-invited")
    public ErrorResponse sendInvitedFriend(@RequestBody FriendShipRequest request) {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} send to invited friend to {}", user.getId(), request.getFriendId());
        return friendShipService.create(user, request);
    }

    @GetMapping("/accept/{friendId}")
    public ErrorResponse acceptFriend(@PathVariable String friendId) {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} accept friend to {}", user.getId(), friendId);
        return friendShipService.accept(friendId, user);
    }

    @GetMapping("/reject/{friendShipId}")
    public ErrorResponse rejectFriend(@PathVariable String friendShipId) {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} reject friend to {}", user.getId(), friendShipId);
        return friendShipService.reject(friendShipId, user);
    }

    @GetMapping("/request-user")
    public List<UserFilterResponse> getRequestUser() {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} request user", user.getId());
        return friendShipService.getFriendRequests(user);
    }

    @GetMapping("/new-request-user")
    public List<UserFilterResponse> getNewRequestUser() {
        UserEntity user = userService.checkAuthentication();
        log.info("User {}get new request user", user.getId());
        return friendShipService.getNewFriendRequest(user);
    }

    @GetMapping("/block-friend/{friendId}")
    public ErrorResponse blockFriend(@PathVariable String friendId) {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} block friend to {}", user.getId(), friendId);
        return friendShipService.blockFriend(user, friendId);
    }

    @GetMapping("/get-friend-user")
    public List<UserFilterResponse> getFriendUser() {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} get friend user", user.getId());
        return friendShipService.getFriendOfUser(user);
    }

    @GetMapping("/get-friend-block")
    public List<UserFilterResponse> getFriendBlock() {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} get friend block", user.getId());
        return friendShipService.getFriendByStatus(user);
    }

    @PostMapping("/update-status")
    public ErrorResponse updateFriendStatus(@RequestBody UpdateStatusRequest request) {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} update friend status", user.getId());
        return friendShipService.updateStatus(user, request);
    }
}
