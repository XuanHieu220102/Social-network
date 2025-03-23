package org.example.socialbe.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.constant.Constant;
import org.example.socialbe.dto.BaseFilterRequest;
import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.dto.group.request.GroupRequest;
import org.example.socialbe.dto.group.request.SearchGroupRequest;
import org.example.socialbe.dto.group.response.GroupResponse;
import org.example.socialbe.dto.group.response.SearchGroupResponse;
import org.example.socialbe.dto.users.response.UserFilterResponse;
import org.example.socialbe.entity.GroupMembersEntity;
import org.example.socialbe.entity.GroupsEntity;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.enums.ErrorCode;
import org.example.socialbe.exception.LeaklessException;
import org.example.socialbe.repository.CommomRepositoryCustom;
import org.example.socialbe.repository.GroupMembersRepository;
import org.example.socialbe.repository.GroupRepository;
import org.example.socialbe.repository.UsersRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class GroupService {
    @Resource
    private GroupRepository groupRepository;

    @Resource
    private ModelMapper modelMapper;

    @Resource
    private GroupMembersRepository groupMembersRepository;

    @Resource
    private CloudinaryService cloudinaryService;
    @Autowired
    private CommomRepositoryCustom commomRepositoryCustom;
    @Autowired
    private UsersRepository usersRepository;

    public ErrorResponse createGroup(UserEntity createdBy, GroupRequest request) {
        try {
            GroupsEntity groups = groupRepository.findByNameAndDeletedFalse(request.getName());
            if (groups != null) {
                throw new LeaklessException(ErrorCode.VALUE_EXISTS);
            }
            groups = new GroupsEntity();
            modelMapper.map(request, groups);
            if (request.getCoverImage() != null) {
                String coverImage = cloudinaryService.uploadImage(request.getCoverImage());
                groups.setCoverImage(coverImage);
            }else {
                groups.setCoverImage("https://i.pinimg.com/736x/7d/d7/49/7dd749ba968cd0f2716d988a592f461e.jpg");
            }
            if (request.getProfileImage() != null) {
                String profileImage = cloudinaryService.uploadImage(request.getProfileImage());
                groups.setProfileImage(profileImage);
            }else {
                groups.setProfileImage("https://i.pinimg.com/736x/97/c5/4a/97c54ace3dbd45c7d9dd53f4696d7b3f.jpg");
            }
            groups.setCreatedBy(createdBy.getId());
            groups.setDeleted(false);
            groupRepository.saveAndFlush(groups);
            GroupMembersEntity groupMembers = GroupMembersEntity.builder()
                    .groupId(groups.getId())
                    .userId(createdBy.getId())
                    .role(Constant.Role_Group.ADMIN)
                    .joinedAt(LocalDateTime.now())
                    .status(Constant.GROUP_MEMBER_STATUS.APPROVED)
                    .build();
            groupMembersRepository.saveAndFlush(groupMembers);
            return ErrorResponse.success(groups.getId());
        }catch (Exception e) {
            log.error(e.getMessage());
            return ErrorResponse.internalServerError(e);
        }
    }

    public ErrorResponse updateGroup(UserEntity updatedBy, GroupRequest request) {
        try {
            GroupsEntity groups = groupRepository.findByNameAndDeletedFalse(request.getName());
            if (groups == null) {
                throw new LeaklessException(ErrorCode.RECORD_NOT_FOUND);
            }
            if (!groups.getCreatedBy().equals(updatedBy.getId())) {
                throw new LeaklessException(ErrorCode.ACCESS_DENIED);
            }
            modelMapper.map(request, groups);
            if (request.getCoverImage() != null) {
                String coverImage = cloudinaryService.uploadImage(request.getCoverImage());
                groups.setCoverImage(coverImage);
            }else {
                groups.setCoverImage("https://i.pinimg.com/736x/7d/d7/49/7dd749ba968cd0f2716d988a592f461e.jpg");
            }
            if (request.getProfileImage() != null) {
                String profileImage = cloudinaryService.uploadImage(request.getProfileImage());
                groups.setProfileImage(profileImage);
            }else {
                groups.setProfileImage("https://i.pinimg.com/736x/97/c5/4a/97c54ace3dbd45c7d9dd53f4696d7b3f.jpg");
            }
            groups.setUpdatedAt(LocalDateTime.now());
            groupRepository.saveAndFlush(groups);
            return ErrorResponse.success(groups.getId());
        }catch (Exception e) {
            log.error(e.getMessage());
            return ErrorResponse.internalServerError(e);
        }
    }

    public ErrorResponse deleteGroup(UserEntity deletedBy, String groupId) {
        try {
            Optional<GroupsEntity> groups = groupRepository.findById(groupId);
            if(groups.isPresent()) {
                if (!groups.get().getCreatedBy().equals(deletedBy.getId())) {
                    throw new LeaklessException(ErrorCode.ACCESS_DENIED);
                }
                groups.get().setDeleted(true);
                groupRepository.save(groups.get());
                return ErrorResponse.success(groups.get().getId());
            }
            return ErrorResponse.recordNotFound(groupId);
        }catch (Exception e) {
            log.error(e.getMessage());
            return ErrorResponse.internalServerError(e);
        }
    }

    public SearchGroupResponse search(SearchGroupRequest request) {
        Page<GroupsEntity> groupsEntities = commomRepositoryCustom.searchGroup(request);
        SearchGroupResponse response = new SearchGroupResponse();
        List<GroupResponse> groupResponses = new ArrayList<>();
        groupsEntities.forEach(group -> {
            GroupResponse groupResponse = modelMapper.map(group, GroupResponse.class);
            long members = groupMembersRepository.countByGroupId(group.getId());
            groupResponse.setMembers(members);
            groupResponses.add(groupResponse);
        });
        response.setGroups(groupResponses);
        response.setRecordSize(groupsEntities.getTotalElements());
        return response;
    }

    public List<GroupResponse> likedGroup(UserEntity user) {
        List<GroupsEntity> groupsEntities = groupRepository.findGroupsUserJoinedAsMemberExcludingAdmins(user.getId());
        return modelMapper.map(groupsEntities, new TypeToken<List<GroupResponse>>() {}.getType());
    }

    public List<GroupResponse> suggestionGroup(UserEntity user) {
        List<GroupsEntity> groupsEntities = groupRepository.findGroupsUserNotJoined(user.getId());
        return modelMapper.map(groupsEntities, new TypeToken<List<GroupResponse>>() {}.getType());
    }

    public List<UserFilterResponse> getUserJoinedGroup(String groupId, int status) {
        List<GroupMembersEntity> groupMembersEntities = groupMembersRepository.findAll();
        List<GroupMembersEntity> groupMembersFilter = groupMembersEntities
                .stream()
                .filter(member -> member.getGroupId().equals(groupId) && member.getStatus() == status)
                .toList();
        List<UserFilterResponse> userFilterResponses = new ArrayList<>();
        groupMembersFilter.forEach(groupMember -> {
            UserEntity user = usersRepository.findById(groupMember.getUserId()).get();
            UserFilterResponse userFilterResponse = modelMapper.map(user, UserFilterResponse.class);
            userFilterResponses.add(userFilterResponse);
        });
        return userFilterResponses;

    }
}
