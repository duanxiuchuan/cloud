package com.erongdu.server.activiti.controller;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erongdu.common.core.base.BaseController;
import com.erongdu.common.core.entity.activiti.IdUser;
import com.erongdu.server.activiti.util.PageableUtil;

/**
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-7-28
 */
@RestController
@RequestMapping(value = "/actUser")
public class ActUserController extends BaseController {
	@Autowired
	private IdentityService identityService;

	@GetMapping("/list")
	public Object list(IdUser query, Pageable pageable) {
		UserQuery userQuery = queryUser(query);
        long count = userQuery.count();
		int firstResult = PageableUtil.offset(pageable);
        List<User> userList = userQuery.listPage(firstResult, pageable.getPageSize());
        List<IdUser> list = new ArrayList<IdUser>();
        for (User user : userList) {
        	IdUser iu = new IdUser(user.getId(), user.getFirstName(), user.getEmail());
        	StringBuilder groupIds = new StringBuilder();
        	List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
        	for (Group g : groups) {
        		groupIds.append(g.getId()).append(", ");
			}
        	iu.setGroupIds(groupIds.length() > 0 ? groupIds.substring(0, groupIds.length() -2) : null);        	
        	list.add(iu);
		}        
		return page(list, count);
	}

	@GetMapping("/search")
	public Object list(IdUser query) {
		UserQuery userQuery = queryUser(query);
		List<User> userList = userQuery.list();
        List<IdUser> list = new ArrayList<IdUser>();
        for (User user : userList) {
        	IdUser iu = new IdUser(user.getId(), user.getFirstName(), user.getEmail());
        	list.add(iu);
		}        
		return success(list);
	}
	
	private UserQuery queryUser(IdUser query) {
		UserQuery userQuery = identityService.createUserQuery();
        if (StringUtils.isNotBlank(query.getId())) {
            userQuery.userId(query.getId());
        }
        if (StringUtils.isNotBlank(query.getFirst())) {
            userQuery.userFirstNameLike("%" + query.getFirst() + "%");
        }
        if (StringUtils.isNotBlank(query.getEmail())) {
            userQuery.userEmailLike("%" + query.getEmail() + "%");
        }
		return userQuery;
	}

}
