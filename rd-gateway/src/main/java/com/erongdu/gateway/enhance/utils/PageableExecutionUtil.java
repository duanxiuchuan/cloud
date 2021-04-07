package com.erongdu.gateway.enhance.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import com.erongdu.common.core.entity.QueryRequest;
import com.erongdu.common.core.entity.constant.CommonConstant;

import reactor.core.publisher.Flux;

/**
 * @author erongdu.com
 */
public class PageableExecutionUtil {

    public static <FEBS> Flux<FEBS> getPages(Query query, QueryRequest request, Class<FEBS> clazz,
                                             ReactiveMongoTemplate template) {
        Sort sort = Sort.by("id").descending();
        if (StringUtils.isNotBlank(request.getField()) && StringUtils.isNotBlank(request.getOrder())) {
            sort = CommonConstant.ORDER_ASC.equals(request.getOrder()) ?
                    Sort.by(request.getField()).ascending() :
                    Sort.by(request.getField()).descending();
        }
        Pageable pageable = PageRequest.of(request.getPageNum(), request.getPageSize(), sort);
        return template.find(query.with(pageable), clazz);
    }
}
