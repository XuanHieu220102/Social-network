package org.example.socialbe.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.dto.BaseFilterRequest;
import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.dto.page.request.PageRequest;
import org.example.socialbe.dto.page.response.PagesResponse;
import org.example.socialbe.dto.page.response.SeachPageResponse;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.service.PagesService;
import org.example.socialbe.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/page")
public class PageController {
    @Resource
    private PagesService pagesService;

    @Resource
    private UserService userService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ErrorResponse createPage(
            @RequestPart("pageRequest") PageRequest pageRequest,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} is created page {}", user.getId(), pageRequest.getName());
        return pagesService.create(user, pageRequest, coverImage, profileImage);
    }

    @PutMapping("/update/{id}")
    public ErrorResponse update(@PathVariable String id, @RequestBody PageRequest request) {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} is updated page {}", user.getId(), request.getName());
        return pagesService.update(user, id, request);
    }

    @DeleteMapping("/delete/{id}")
    public ErrorResponse delete(@PathVariable String id) {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} is deleted page {}", user.getId(), id);
        return pagesService.deletePage(user, id);
    }


    @PostMapping("/search")
    public SeachPageResponse searchPage(@RequestBody BaseFilterRequest request) {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} is search page", user.getId());
        return pagesService.searchPage(request);
    }

    @GetMapping("/get-my-page")
    public List<PagesResponse> getMyPage() {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} is get my page", user.getId());
        return pagesService.getMyPage(user);
    }

    @GetMapping("/get-suggestion-page")
    public List<PagesResponse> getSuggestionPage() {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} is get page suggestion", user.getId());
        return pagesService.getSuggestionPage(user,"ADMIN");
    }

    @GetMapping("/get-follow-page")
    public List<PagesResponse> getFollowedPage() {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} is get page suggestion", user.getId());
        return pagesService.getPageUserFollowing(user,"MEMBER");
    }

    @GetMapping("/follow-page/{pageId}")
    public ErrorResponse followPage(@PathVariable String pageId) {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} is follow page {}", user.getId(), pageId);
        return pagesService.followPage(user, pageId);
    }

    @GetMapping("/get-popular-page")
    public List<PagesResponse> getPopularPage(){
        UserEntity user = userService.checkAuthentication();
        log.info("User {} get popular page", user.getId());
        return pagesService.getPopularPage(user);
    }
}
