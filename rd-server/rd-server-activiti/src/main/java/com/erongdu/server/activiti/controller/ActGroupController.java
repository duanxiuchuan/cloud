package com.erongdu.server.activiti.controller;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erongdu.common.core.base.BaseController;
import com.erongdu.common.core.entity.activiti.IdGroup;
import com.erongdu.server.activiti.util.PageableUtil;

/**
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-7-28
 */
@RestController
@RequestMapping(value = "/actGroup")
public class ActGroupController extends BaseController {

	@Autowired
	private IdentityService identityService;

	@GetMapping("list")
	public Object list(IdGroup query, Pageable pageable) {
		GroupQuery groupQuery = queryGroup(query);
		long count = groupQuery.count();

		int firstResult = PageableUtil.offset(pageable);
		List<Group> groupList = groupQuery.listPage(firstResult, pageable.getPageSize());
		List<IdGroup> list = new ArrayList<IdGroup>();
		for (Group group : groupList) {
			IdGroup ig = new IdGroup(group.getId(), group.getName());
			list.add(ig);
		}		
		return page(list, count);
	}

	@GetMapping("/search")
	public Object list(IdGroup query) {
		GroupQuery groupQuery = queryGroup(query);
		List<Group> groupList = groupQuery.list();
		List<IdGroup> list = new ArrayList<IdGroup>();
		for (Group group : groupList) {
			IdGroup ig = new IdGroup(group.getId(), group.getName());
			list.add(ig);
		}
		return success(list);
	}
	
	private GroupQuery queryGroup(IdGroup query) {
		GroupQuery groupQuery = identityService.createGroupQuery();
		if (StringUtils.isNotBlank(query.getId())) {
			groupQuery.groupId(query.getId());
		}
		if (StringUtils.isNotBlank(query.getName())) {
			groupQuery.groupNameLike("%" + query.getName() + "%");
		}
		return groupQuery;
	}

}
