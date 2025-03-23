package org.example.socialbe.repository;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.example.socialbe.dto.BaseFilterRequest;
import org.example.socialbe.dto.group.request.SearchGroupRequest;
import org.example.socialbe.dto.group.response.SearchGroupResponse;
import org.example.socialbe.entity.GroupsEntity;
import org.example.socialbe.entity.PageEntity;
import org.example.socialbe.entity.PostEntity;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.util.QueryUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CommomRepositoryCustom {
    @Resource
    private QueryUtil queryUtil;


    public Page<PostEntity> searchPost (BaseFilterRequest request) {
        String table = " posts ";
        String column = " posts.* ";
        String where = " 1 = 1";
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(request.getKeyword())) {
            where += " AND " + request.getKeyword() + " ilike ?";
            params.add("%" + request.getValue() + "%");
        }
        return queryUtil.getResultPage(column, table, where, params, request, PostEntity.class);
    }

    public Page<UserEntity> searchUser (BaseFilterRequest request) {
        String table = " users ";
        String column = " users.* ";
        String where = " 1 = 1";
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(request.getKeyword())) {
            where += " AND " + request.getKeyword() + " ilike ?";
            params.add("%" + request.getValue() + "%");
        }
        return queryUtil.getResultPage(column, table, where, params, request, UserEntity.class);
    }

    public Page<PageEntity> searchPage (BaseFilterRequest request) {
        String table = " pages ";
        String column = " pages.* ";
        String where = " 1 = 1";
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(request.getKeyword())) {
            where += " AND " + request.getKeyword() + " ilike ?";
            params.add("%" + request.getValue() + "%");
        }
        return queryUtil.getResultPage(column, table, where, params, request, PageEntity.class);
    }

    public Page<GroupsEntity> searchGroup(SearchGroupRequest request) {
        String table = " groups ";
        String column = " groups.* ";
        String where = " deleted = false ";
        List<Object> params = new ArrayList<>();
        if (request.getIsOther()) {
            where += " AND created_by <> ?";
            params.add(request.getValue());
            return queryUtil.getResultPage(column, table, where, params, request, GroupsEntity.class);
        }
        if (StringUtils.isNotBlank(request.getKeyword())) {
            where += " AND " + request.getKeyword() + " ilike ?";
            params.add("%" + request.getValue() + "%");
        }
        return queryUtil.getResultPage(column, table, where, params, request, GroupsEntity.class);
    }
}
