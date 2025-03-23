package org.example.socialbe.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.socialbe.dto.BaseFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueryUtil {
    @PersistenceContext
    private EntityManager entityManager;

    public Page getResultPage(String table, String where, List<Object> params, BaseFilterRequest request, Class resultClass) {
        String sqlCount = "select count(*) from " + table + " where %s";
        String sqlSelect = "select * from " + table + " where %s " + request.getOrderByOffsetLimit();

        long count = getCount(String.format(sqlCount, where), params);

        List list = getResultList(String.format(sqlSelect, where), params, resultClass);

        return PageableExecutionUtils.getPage(list, request.getPageable(), () -> count);
    }

    public Page getResultPage(String columns, String table, String where, List<Object> params, BaseFilterRequest request, Class resultClass) {
        String sqlCount = "select count(*) AS distinct_count from ( SELECT DISTINCT " + columns + " from " + table + " where %s ) AS distinct_rows";
        String sqlSelect = "select distinct " + columns + " from " + table + " where %s " + request.getOrderByOffsetLimit();

        long count = getCount(String.format(sqlCount, where), params);
        List list = getResultList(String.format(sqlSelect, where), params, resultClass);

        return PageableExecutionUtils.getPage(list, request.getPageable(), () -> count);
    }

    public Page getResultPage(String nestedSelectQuery, List<Object> params, BaseFilterRequest request, Class resultClass) {
        String sqlCount = "select count(*) from " + nestedSelectQuery;
        String sqlSelect = "select * from " + nestedSelectQuery + " " + request.getOrderByOffsetLimit();

        long count = getCount(sqlCount, params);

        List list = getResultList(sqlSelect, params, resultClass);

        return PageableExecutionUtils.getPage(list, request.getPageable(), () -> count);
    }

    public Object getSingleResult(String sql, List<Object> params) {
        Query query = createNativeQuery(sql, params);
        return query.getSingleResult();
    }

    public Query createNativeQuery(String sql, List<Object> params) {
        Query query = entityManager.createNativeQuery(sql);
        setParameters(query, params);
        return query;
    }

    public void setParameters(Query query, List<Object> params) {
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }
    }

    public Long getCount(String sql, List<Object> params) {
        Object result = getSingleResult(sql, params);
        return ((Number) result).longValue();
    }

    public List getResultList(String sql, List<Object> params, Class resultClass) {
        Query query = entityManager.createNativeQuery(sql, resultClass);
        setParameters(query, params);
        return query.getResultList();
    }
}
