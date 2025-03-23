package org.example.socialbe.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.dto.page.request.FollowPageRequest;
import org.example.socialbe.dto.page.request.PageFollowRequest;
import org.example.socialbe.entity.PageEntity;
import org.example.socialbe.entity.PageFollowersEntity;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.enums.ErrorCode;
import org.example.socialbe.exception.LeaklessException;
import org.example.socialbe.repository.PageFollowersRepository;
import org.example.socialbe.repository.PageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class PageFollowersService {
    @Resource
    private PageFollowersRepository pageFollowersRepository;

    @Resource
    private PageRepository pageRepository;

    @Resource
    private ModelMapper modelMapper;

    public ErrorResponse create(PageFollowRequest request) {
        try {
            PageFollowersEntity followPage = pageFollowersRepository.getUserFollowPage(request.getUserId(),request.getPageId());
            if (followPage != null) {
                throw new LeaklessException(ErrorCode.VALUE_EXISTS);
            }
            followPage = new PageFollowersEntity();
            modelMapper.map(request, PageFollowersEntity.class);
            pageFollowersRepository.save(followPage);
            return ErrorResponse.success(followPage.getId());
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            return ErrorResponse.internalServerError(e);
        }
    }

    public ErrorResponse changeStatus(UserEntity user, FollowPageRequest request) {
        try {
            Optional<PageEntity> page = pageRepository.findById(request.getPageId());
            if (page.isEmpty()) {
                throw new LeaklessException(ErrorCode.RECORD_NOT_FOUND);
            }
            boolean isAdmin = pageFollowersRepository.isAdmin(user.getId(), request.getPageId());
            if (!isAdmin) {
                throw new LeaklessException(ErrorCode.ACCESS_DENIED);
            }
            PageFollowersEntity pageFollowers = pageFollowersRepository.getUserFollowPage(user.getId(), request.getPageId());
            if (pageFollowers == null) {
                throw new LeaklessException(ErrorCode.RECORD_NOT_FOUND);
            }
            pageFollowers.setStatus(request.getStatus());
            pageFollowersRepository.save(pageFollowers);
            return ErrorResponse.success(pageFollowers.getId());
        }catch (Exception ex) {
            log.error("Has an error");
            return ErrorResponse.internalServerError(ex);
        }
    }
}
