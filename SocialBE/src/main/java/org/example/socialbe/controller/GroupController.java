package org.example.socialbe.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.dto.BaseFilterRequest;
import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.dto.group.request.GroupRequest;
import org.example.socialbe.dto.group.request.SearchGroupRequest;
import org.example.socialbe.dto.group.response.GroupResponse;
import org.example.socialbe.dto.group.response.SearchGroupResponse;
import org.example.socialbe.dto.users.response.UserFilterResponse;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.service.GroupService;
import org.example.socialbe.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/group")
public class GroupController {
    @Resource
    private GroupService groupService;

    @Resource
    private UserService userService;

    @PostMapping
    public ErrorResponse createGroup(@ModelAttribute GroupRequest request) {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} is created group {}", user.getId(), request.getName());
        return groupService.createGroup(user, request);
    }

    @PutMapping("/{id}")
    public ErrorResponse updateGroup(@ModelAttribute GroupRequest request) {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} is modified group {}", user.getId(), request.getName());
        return groupService.updateGroup(user, request);
    }

    @PostMapping("/search")
    public SearchGroupResponse searchGroup(@RequestBody SearchGroupRequest request) {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} is search group", user.getId());
        return groupService.search(request);
    }

    @GetMapping("/get-group-user-liked")
    public List<GroupResponse> getGroupUserLiked() {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} is liked group", user.getId());
        return groupService.likedGroup(user);
    }

    @GetMapping("/get-group-suggestion")
    public List<GroupResponse> getGroupUserNotJoin() {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} is liked group", user.getId());
        return groupService.suggestionGroup(user);
    }

    @GetMapping("/get-user-joined-group/{groupId}")
    public List<UserFilterResponse> getUserJoinedGroup(@PathVariable String groupId) {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} is joined group {}", user.getId(), groupId);
        return groupService.getUserJoinedGroup(groupId, 1);
    }

    @GetMapping("/get-user-delivery-joined-group/{groupId}")
    public List<UserFilterResponse> getUserDeliveryGroup(@PathVariable String groupId) {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} is delivered group {}", user.getId(), groupId);
        return groupService.getUserJoinedGroup(groupId, 3);
    }
}
