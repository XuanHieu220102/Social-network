package org.example.socialbe.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.constant.Constant;
import org.example.socialbe.dto.BaseFilterRequest;
import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.dto.page.request.FollowPageRequest;
import org.example.socialbe.dto.page.request.PageRequest;
import org.example.socialbe.dto.page.response.PagesResponse;
import org.example.socialbe.dto.page.response.SeachPageResponse;
import org.example.socialbe.entity.GroupMembersEntity;
import org.example.socialbe.entity.PageEntity;
import org.example.socialbe.entity.PageFollowersEntity;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.enums.ErrorCode;
import org.example.socialbe.exception.LeaklessException;
import org.example.socialbe.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PagesService {
    @Resource
    private PageRepository pageRepository;

    @Resource
    private ModelMapper modelMapper;

    @Resource
    private PageFollowersRepository pageFollowersRepository;

    @Resource
    private CloudinaryService cloudinaryService;

    @Resource
    private CommomRepositoryCustom commomRepositoryCustom;

    @Resource
    private UserService userService;

    @Resource
    private UsersRepository usersRepository;

    public ErrorResponse create(UserEntity user, PageRequest request, MultipartFile coverImageRequest, MultipartFile profileImageRequest) {
        try {
            PageEntity page = pageRepository.findByNameAndDeletedFalse(request.getName());
            if (page != null) {
                throw new LeaklessException(ErrorCode.CREATE_FAILED);
            }
            page = modelMapper.map(request, PageEntity.class);
            if (coverImageRequest != null) {
                String coverImage = cloudinaryService.uploadImage(coverImageRequest);
                page.setCoverImage(coverImage);
            }else {
                page.setCoverImage("https://i.pinimg.com/736x/7d/d7/49/7dd749ba968cd0f2716d988a592f461e.jpg");
            }
            if (profileImageRequest != null) {
                String profileImage = cloudinaryService.uploadImage(profileImageRequest);
                page.setProfileImage(profileImage);
            }else {
                page.setProfileImage("https://i.pinimg.com/736x/97/c5/4a/97c54ace3dbd45c7d9dd53f4696d7b3f.jpg");
            }
            page.setCreatedBy(user.getId());
            page.setDeleted(false);
            pageRepository.save(page);
            PageFollowersEntity pageFollowersEntity = PageFollowersEntity.builder()
                    .pageId(page.getId())
                    .userId(user.getId())
                    .role(Constant.PAGE_ROLE.ADMIN)
                    .createdAt(LocalDateTime.now())
                    .status(Constant.PAGE_FOLLOW_STATUS.APPROVED)
                    .build();
            pageFollowersRepository.save(pageFollowersEntity);
            return ErrorResponse.success(page.getId());
        }catch (Exception e) {
            log.error("Has an error :" + e.getMessage());
            return ErrorResponse.internalServerError(e);
        }
    }

    public ErrorResponse update(UserEntity user, String pageId, PageRequest request) {
        try {
            Optional<PageEntity> page = pageRepository.findById(pageId);
            if (page.isEmpty()) {
                throw new LeaklessException(ErrorCode.RECORD_NOT_FOUND);
            }
            PageEntity pageEntity = page.get();
            boolean isAdmin = pageFollowersRepository.isAdmin(user.getId(), pageId);
            if (!isAdmin) {
                throw new LeaklessException(ErrorCode.ACCESS_DENIED);
            }
            modelMapper.map(request, pageEntity);
            pageEntity.setUpdatedAt(LocalDateTime.now());
            pageRepository.save(pageEntity);
            return ErrorResponse.success(pageEntity.getId());
        }catch (Exception e) {
            log.error("Has an error :" + e.getMessage());
            return ErrorResponse.internalServerError(e);
        }
    }

    public ErrorResponse deletePage(UserEntity user, String pageId) {
        try {
            Optional<PageEntity> page = pageRepository.findById(pageId);
            if (page.isEmpty()) {
                throw new LeaklessException(ErrorCode.RECORD_NOT_FOUND);
            }
            PageEntity pageEntity = page.get();
            boolean isAdmin = pageFollowersRepository.isAdmin(user.getId(), pageId);
            if (!isAdmin) {
                throw new LeaklessException(ErrorCode.ACCESS_DENIED);
            }
            page.get().setDeleted(true);
            pageRepository.save(page.get());
            return ErrorResponse.success("Deleted page succesfully: " + pageEntity.getId());
        }catch (Exception e) {
            log.error("Has an error :" + e.getMessage());
            return ErrorResponse.internalServerError(e);
        }
    }

    public SeachPageResponse searchPage(BaseFilterRequest request) {
        Page<PageEntity> pageEntities = commomRepositoryCustom.searchPage(request);
        SeachPageResponse response = new SeachPageResponse();
        List<PagesResponse> pagesResponses = new ArrayList<>();
        pageEntities.forEach(pageEntity -> {
            PagesResponse pagesResponse = modelMapper.map(pageEntity, PagesResponse.class);
            Optional<PageFollowersEntity> pageFollowers = pageFollowersRepository.getAdminPageFollow(pageEntity.getId(),"ADMIN");
            if (pageFollowers.isPresent()) {
                Optional<UserEntity> author = usersRepository.findById(pageFollowers.get().getUserId());
                if (author.isPresent()) {
                    PagesResponse.Author authorRes = modelMapper.map(author.get(), PagesResponse.Author.class);
                    pagesResponse.setAuthor(authorRes);
                }
            }
            pagesResponses.add(pagesResponse);
        });
        response.setRecordSize((int) pageEntities.getTotalElements());
        response.setPages(pagesResponses);
        return response;
    }

    public List<PagesResponse> getMyPage(UserEntity user) {
        List<PageFollowersEntity> pageFollowers = pageFollowersRepository.getMyPage(user.getId(), "ADMIN");
        List<PagesResponse> pagesResponses = new ArrayList<>();
        pageFollowers.forEach(pageFollowersEntity -> {
            Optional<PageEntity> pageEntity = pageRepository.findById(pageFollowersEntity.getPageId());
            if (pageEntity.isPresent()) {
                PagesResponse pagesResponse = modelMapper.map(pageEntity, PagesResponse.class);
                PagesResponse.Author author = modelMapper.map(user, PagesResponse.Author.class);
                pagesResponse.setAuthor(author);
                pagesResponses.add(pagesResponse);
            }
        });
        return pagesResponses;
    }

    public List<PagesResponse> getSuggestionPage(UserEntity user, String role) {
        List<PageEntity> pageEntities = pageRepository.getPageByUserIdAndRole(user.getId(), role);
        List<PagesResponse> pagesResponses = new ArrayList<>();
        pageEntities.forEach(pageEntity -> {
            Optional<PageEntity> pageEntity2 = pageRepository.findById(pageEntity.getId());
            if (pageEntity2.isPresent()) {
                PagesResponse pagesResponse = modelMapper.map(pageEntity2.get(), PagesResponse.class);
                PagesResponse.Author author = modelMapper.map(user, PagesResponse.Author.class);
                pagesResponse.setAuthor(author);
                pagesResponses.add(pagesResponse);
            }
        });
        return pagesResponses;
    }

    public List<PagesResponse> getPageUserFollowing(UserEntity user, String role) {
        List<PageEntity> pageEntities = pageRepository.getPageUserFollowing(user.getId(), role);
        List<PagesResponse> pagesResponses = new ArrayList<>();
        pageEntities.forEach(pageEntity -> {
            Optional<PageEntity> pageEntity2 = pageRepository.findById(pageEntity.getId());
            if (pageEntity2.isPresent()) {
                PagesResponse pagesResponse = modelMapper.map(pageEntity2.get(), PagesResponse.class);
                PagesResponse.Author author = modelMapper.map(user, PagesResponse.Author.class);
                pagesResponse.setAuthor(author);
                pagesResponses.add(pagesResponse);
            }
        });
        return pagesResponses;
    }

    public ErrorResponse followPage (UserEntity user, String pageId) {
        Optional<PageEntity> page = pageRepository.findById(pageId);
        if (page.isEmpty()) {
            throw new LeaklessException(ErrorCode.RECORD_NOT_FOUND);
        }
        PageFollowersEntity pageFollowers = PageFollowersEntity.builder()
                .pageId(pageId)
                .userId(user.getId())
                .role(Constant.PAGE_ROLE.MEMBER)
                .status(Constant.PAGE_FOLLOW_STATUS.APPROVED)
                .createdAt(LocalDateTime.now())
                .build();
        pageFollowersRepository.save(pageFollowers);
        return ErrorResponse.success(pageFollowers.getId());
    }

    public List<PagesResponse> getPopularPage(UserEntity user) {
        List<Object[]> results = pageRepository.getPopularPage(user.getId());
        System.out.println(results.size());
        List<PagesResponse> pagesResponses = new ArrayList<>();
        for (Object[] row : results) {
            PageEntity page = (PageEntity) row[0];
            PagesResponse pagesResponse = modelMapper.map(page, PagesResponse.class);
            PagesResponse.Author author = modelMapper.map(user, PagesResponse.Author.class);
            pagesResponse.setAuthor(author);
            pagesResponse.setTotalFollower((Long) row[1]);
            pagesResponses.add(pagesResponse);
        }
        return pagesResponses;
    }

}
