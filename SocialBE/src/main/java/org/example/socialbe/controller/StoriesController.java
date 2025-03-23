package org.example.socialbe.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.dto.stories.request.SearchStoriesRequest;
import org.example.socialbe.dto.stories.request.StoriesRequest;
import org.example.socialbe.dto.stories.response.StoriesInterfaceResponse;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.service.StoriesService;
import org.example.socialbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/stories")
public class StoriesController {
    @Resource
    private StoriesService storiesService;

    @Resource
    private UserService userService;

    @PostMapping
    public ErrorResponse createStories(@ModelAttribute StoriesRequest request) {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} is create new stories", user.getUsername());
        return storiesService.create(user, request);
    }

    @PutMapping("/{id}")
    public ErrorResponse updateStories(@PathVariable String id, @RequestBody StoriesRequest request) {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} is update stories", user.getUsername());
        return storiesService.update(id, request);
    }

    @PostMapping("/delete/{id}")
    public ErrorResponse deleteStories(@PathVariable String id) {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} is delete stories", user.getUsername());
        return storiesService.delete(id);
    }

    @PostMapping("/search")
    public List<StoriesInterfaceResponse> getStories(@RequestBody SearchStoriesRequest request) {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} is search stories", user.getUsername());
        return storiesService.getStories(request);
    }
}
