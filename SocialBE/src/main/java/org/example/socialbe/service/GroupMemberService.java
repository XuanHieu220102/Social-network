package org.example.socialbe.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.constant.Constant;
import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.dto.group.request.GroupMemberRequest;
import org.example.socialbe.dto.group.request.UpdateStatusGroupMemberRequest;
import org.example.socialbe.dto.group.response.UserGroupResponse;
import org.example.socialbe.dto.users.response.UserFilterResponse;
import org.example.socialbe.entity.GroupMembersEntity;
import org.example.socialbe.entity.GroupsEntity;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.enums.ErrorCode;
import org.example.socialbe.exception.LeaklessException;
import org.example.socialbe.repository.GroupMembersRepository;
import org.example.socialbe.repository.GroupRepository;
import org.example.socialbe.repository.UsersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GroupMemberService {
    @Resource
    private GroupMembersRepository groupMembersRepository;

    @Resource
    private GroupRepository groupRepository;

    @Resource
    private UsersRepository usersRepository;

    @Resource
    private ModelMapper modelMapper;

    public ErrorResponse create(UserEntity user, GroupMemberRequest request) {
        try {
            GroupMembersEntity groupMembers = groupMembersRepository.findByGroupIdAndMemberId(request.getGroupId(), user.getId());
            if (groupMembers != null) {
                throw new LeaklessException(ErrorCode.VALUE_EXISTS);
            }
            GroupsEntity groups = groupRepository.findById(request.getGroupId()).orElse(null);
            if (groups == null) {
                throw new LeaklessException(ErrorCode.RECORD_NOT_FOUND);
            }
            groupMembers = new GroupMembersEntity();
            groupMembers.setGroupId(request.getGroupId());
            groupMembers.setUserId(user.getId());
            groupMembers.setStatus(groups.getPrivacyStatus() == Constant.GROUP_PRIVACY.PRIVATE ? Constant.GROUP_MEMBER_STATUS.WAITING : Constant.GROUP_MEMBER_STATUS.APPROVED);
            groupMembers.setJoinedAt(LocalDateTime.now());
            groupMembers.setRole(Constant.Role_Group.MEMBER);
            groupMembersRepository.save(groupMembers);
            return ErrorResponse.success(groupMembers.getId());
        }catch (Exception e) {
            log.error(e.getMessage());
            return ErrorResponse.internalServerError(e);
        }
    }

//    public static final Integer APPROVED = 1;
//    public static final Integer INVITED = 2;
//    public static final Integer WAITING = 3;
//    public static final Integer BLOCKED = 4;
//    public static final Integer REJECTED = 5;
    public ErrorResponse updateStatus(UpdateStatusGroupMemberRequest request) {
        try {
            GroupMembersEntity groupMembers = groupMembersRepository.findByGroupIdAndMemberIdV2(request.getGroupId(), request.getMemberId());
            if (groupMembers == null) {
                throw new LeaklessException(ErrorCode.RECORD_NOT_FOUND);
            }
            groupMembers.setStatus(request.getStatus());
            groupMembersRepository.save(groupMembers);
            return ErrorResponse.success(groupMembers.getId());
        }catch (Exception ex) {
            log.error("Has an error: ", ex);
            return ErrorResponse.internalServerError(ex);
        }
    }

    public UserGroupResponse getUserInGroup(String groupId) {
        List<GroupMembersEntity> groupMembers = groupMembersRepository.findByGroupId(groupId);
        UserGroupResponse response = new UserGroupResponse();
        List<UserFilterResponse> userFilterResponses = new ArrayList<>();
        groupMembers.forEach(groupMember -> {
            Optional<UserEntity> user = usersRepository.findById(groupMember.getUserId());
            if (user.isPresent()){
                UserFilterResponse users = modelMapper.map(user.get(), UserFilterResponse.class);
                userFilterResponses.add(users);
            }
        });
        response.setUserGroups(userFilterResponses);
        response.setRecordSize(userFilterResponses.size());
        return response;
    }

}
