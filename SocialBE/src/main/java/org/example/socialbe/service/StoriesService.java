package org.example.socialbe.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.dto.stories.request.SearchStoriesRequest;
import org.example.socialbe.dto.stories.request.StoriesRequest;
import org.example.socialbe.dto.stories.response.StoriesInterfaceResponse;
import org.example.socialbe.entity.StoriesEntity;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.repository.FriendShipsRepository;
import org.example.socialbe.repository.StoriesRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class StoriesService {
    @Resource
    private StoriesRepository storiesRepository;

    @Resource
    private ModelMapper modelMapper;

    @Resource
    private CloudinaryService cloudinaryService;

    @Resource
    private FriendShipService friendShipService;

    @Resource
    private FriendShipsRepository friendShipsRepository;

    @Resource
    private PostService postService;

    public ErrorResponse create(UserEntity user,StoriesRequest request) {
        try {
            StoriesEntity stories = modelMapper.map(request, StoriesEntity.class);
            if (request.getVideoUrl() != null) {
                String medialUrl = cloudinaryService.uploadImage(request.getVideoUrl());
                stories.setVideoUrl(medialUrl);
            }
            stories.setUserId(user.getId());
            storiesRepository.save(stories);
            List<UserEntity> userEntities = friendShipsRepository.findFriends(user.getId());
            postService.createNotification(user.getId(), userEntities, "NEW_STORIES", stories.getId(),user.getFullName() + " vừa đăng tin mới");
            return new ErrorResponse(String.valueOf(HttpStatus.OK.value()), "Create story succesfully", stories.getId());
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ErrorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage());
        }
    }

    public ErrorResponse update(String storiesId, StoriesRequest request) {
        try {
            Optional<StoriesEntity> stories = storiesRepository.findById(storiesId);
            if (stories.isPresent()) {
                StoriesEntity storiesEntity = stories.get();
                modelMapper.map(request, storiesEntity);
                if (request.getVideoUrl() != null) {
                    String medialUrl = cloudinaryService.uploadMedia(request.getVideoUrl());
                    storiesEntity.setVideoUrl(medialUrl);
                }
                storiesRepository.save(storiesEntity);
                return new ErrorResponse(String.valueOf(HttpStatus.OK.value()), "Update stories", storiesEntity.getId());
            }
            return new ErrorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), "Update stories failed", storiesId);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ErrorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage());
        }
    }

    public ErrorResponse delete(String storiesId) {
        try {
            Optional<StoriesEntity> stories = storiesRepository.findById(storiesId);
            if (stories.isPresent()) {
                stories.get().setActive(false);
                storiesRepository.save(stories.get());
                return new ErrorResponse(String.valueOf(HttpStatus.OK.value()), "Delete stories", storiesId);
            }
            return new ErrorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), "Delete stories failed", storiesId);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ErrorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage());
        }
    }

    public List<StoriesInterfaceResponse> getStories(SearchStoriesRequest request) {
        return storiesRepository.findAllActiveStories(request.getSize(), request.getPage());
    }
}
