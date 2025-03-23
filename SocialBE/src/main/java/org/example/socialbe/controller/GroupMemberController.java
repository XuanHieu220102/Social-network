package org.example.socialbe.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.dto.group.request.GroupMemberRequest;
import org.example.socialbe.dto.group.request.UpdateStatusGroupMemberRequest;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.service.GroupMemberService;
import org.example.socialbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/group-member")
@Slf4j
public class GroupMemberController {
    @Resource
    private GroupMemberService groupMemberService;

    @Resource
    private UserService userService;

    @PostMapping
    public ErrorResponse joinGroup(@RequestBody GroupMemberRequest request) {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} is joined group {}", user.getId(), request.getGroupId());
        return groupMemberService.create(user, request);
    }

    @PostMapping("/update-status")
    public ErrorResponse updateStatus(@RequestBody UpdateStatusGroupMemberRequest request) {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} is update status group {}", user.getId(), request.getGroupId());
        return groupMemberService.updateStatus(request);
    }
}
