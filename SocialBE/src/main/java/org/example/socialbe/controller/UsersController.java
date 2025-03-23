package org.example.socialbe.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.dto.BaseFilterRequest;
import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.dto.users.request.ChangePasswordRequest;
import org.example.socialbe.dto.users.request.ForgetPasswordForm;
import org.example.socialbe.dto.users.request.UpdateProfilePictureRequest;
import org.example.socialbe.dto.users.request.UserRequest;
import org.example.socialbe.dto.users.response.UserFilterResponse;
import org.example.socialbe.dto.users.response.UserResponse;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UsersController {
    @Resource
    private UserService userService;

    @PostMapping
    public ErrorResponse createUser(@ModelAttribute UserRequest request) {
        log.info("Create user request: {}", request);
        return userService.createUser(request);
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable String id) {
        log.info("Get user by id: {}", id);
        return userService.detail(id);
    }

    @GetMapping("/current-user")
    public UserResponse getCurrentUser() {
        UserEntity user = userService.checkAuthentication();
        log.info("Get current user");
        return userService.currentUserInfo(user);
    }


    @PostMapping("/change-password")
    public ErrorResponse changePassword(@RequestBody ChangePasswordRequest request) {
        UserEntity user = userService.checkAuthentication();
        log.info("Change password request: {}", request);
        userService.changePassword(user, request);
        return ErrorResponse.success("Change password completed");
    }

    @PostMapping("/forget-password")
    public ErrorResponse forgetPassword(@RequestBody ForgetPasswordForm request) {
        log.info("Forget password request: {}", request);
        return userService.forgetPassword(request);
    }

    @PostMapping("/update-picture-profile")
    public ErrorResponse updatePictureProfile(@ModelAttribute UpdateProfilePictureRequest request) {
        UserEntity user = userService.checkAuthentication();
        log.info("Update picture profile request: {}", request);
        return userService.updateProfilePicture(user, request);
    }

    @PostMapping("/search")
    public List<UserFilterResponse> search(@RequestBody BaseFilterRequest request) {
        log.info("Search request: {}", request);
        return userService.searchUser(request);
    }

    @GetMapping("/get-user-suggestion")
    public List<UserFilterResponse> getUserSuggestion() {
        UserEntity user = userService.checkAuthentication();
        log.info("Get user suggestion");
        return userService.getListSuggestionUser(user);
    }
}
