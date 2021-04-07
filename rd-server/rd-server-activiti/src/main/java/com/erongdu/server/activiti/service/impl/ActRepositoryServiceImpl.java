package com.erongdu.server.activiti.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.erongdu.common.core.entity.activiti.Category;
import com.erongdu.common.core.utils.CommonUtil;
import com.erongdu.server.activiti.model.response.ProcessDefinitionResponse;
import com.erongdu.server.activiti.service.ActRepositoryService;
import com.erongdu.server.activiti.util.PageableUtil;

@Service
public class ActRepositoryServiceImpl implements ActRepositoryService {
	
	@Autowired
	private IdentityService identityService;
	@Autowired
	private RepositoryService repositoryService;
	
	private static String SQL_COUNT = "select count(RES.ID_) ";
	private static String SQL_START = "select RES.* ";
	private static String SQL_END = " order by RES.ID_ asc LIMIT #{limit} OFFSET #{offset} ";
	private static StringBuilder SQL_BODY = new StringBuilder();
	static {
		SQL_BODY.append(" FROM ACT_RE_PROCDEF RES");
		SQL_BODY.append(" WHERE	RES.VERSION_ = (SELECT max(VERSION_) FROM ACT_RE_PROCDEF WHERE KEY_ = RES.KEY_ AND RES.SUSPENSION_STATE_ = 1) ");
		SQL_BODY.append(" AND RES.SUSPENSION_STATE_ = 1 ");
		SQL_BODY.append(" AND ( ");
		SQL_BODY.append("    NOT EXISTS (SELECT IDN.ID_ FROM ACT_RU_IDENTITYLINK IDN WHERE IDN.PROC_DEF_ID_ = RES.ID_  ) ");		
		SQL_BODY.append("    OR EXISTS ( SELECT IDN.ID_ FROM ACT_RU_IDENTITYLINK IDN WHERE IDN.PROC_DEF_ID_ = RES.ID_ AND IDN.USER_ID_ = #{userId}) ");
		SQL_BODY.append("    OR EXISTS ( SELECT IDN.ID_ FROM ACT_RU_IDENTITYLINK IDN WHERE IDN.PROC_DEF_ID_ = RES.ID_ AND IDN.GROUP_ID_ IN ({groupIds})	) ");
		SQL_BODY.append(" )");		
	}
	

	@Override
	public Map<String, Object> queryProcessList(Map<String, Object> params, Pageable pageable) {
		
		String username = CommonUtil.getCurrentUsername();
    	// 查询用户所在组
    	List<Group> glist = identityService.createGroupQuery().groupMember(username).list();
    	StringBuilder groupIds = new StringBuilder();
    	for (Group group : glist) {
    		groupIds.append("'").append(group.getId()).append("'").append(",");
		}
    	//
    	StringBuilder body = new StringBuilder(SQL_BODY.toString()
    			.replaceAll("\\{groupIds\\}", groupIds.substring(0, groupIds.length() - 1)));
		
    	// 动态条件
		String key = MapUtils.getString(params, "key");
        if(StringUtils.isNotEmpty(key)) {
        	body.append(" AND RES.KEY_ like concat('%', '");
        	body.append(key);
        	body.append("', '%')");
        }
        // name like
        String name = MapUtils.getString(params, "name");
        if(StringUtils.isNotEmpty(name)) {
        	body.append(" AND RES.NAME_ like concat('%', '");
        	body.append(name);
        	body.append("', '%')");
        }
        // category like
        String category = MapUtils.getString(params, "category");
        if(StringUtils.isNotEmpty(category) && !Category.NAME_ALL.equals(category)) {
        	body.append(" AND RES.CATEGORY_ like concat('%', '");
        	body.append(category);
        	body.append("', '%')");
        }
        
        // 查询符合条件的记录数
    	StringBuilder countSql = new StringBuilder();
    	countSql.append(SQL_COUNT).append(body);
    	long total = repositoryService.createNativeProcessDefinitionQuery()
    			.sql(countSql.toString())
    			.parameter("userId", username)
        		.count();
    	
    	// 查询符合条件的记录列表
    	StringBuilder listSql = new StringBuilder();
    	listSql.append(SQL_START).append(body).append(SQL_END);
        List<ProcessDefinition> page = repositoryService.createNativeProcessDefinitionQuery()
        		.sql(listSql.toString())
        		.parameter("userId", username)
        		.parameter("limit", pageable.getPageSize())
        		.parameter("offset", PageableUtil.offset(pageable))
        		.list();
        
        // 转为model
        List<ProcessDefinitionResponse> data = new ArrayList<>(page.size());
        if(page.size() > 0) {
        	for (ProcessDefinition pd : page) {
        		ProcessDefinitionResponse model = new ProcessDefinitionResponse();
        		BeanUtils.copyProperties(pd, model);
        		data.add(model);
        	}
        }      	
        return PageableUtil.toPage(data, total);
	}

	/**
	 * 加载候选用户、用户组
	 * @param id	流程定义id
	 * @param model
	 */
	public void queryProcessCandicate(String id, ProcessDefinitionResponse model) {
		// 设置候选用户、用户组
		StringBuilder users = new StringBuilder();
		StringBuilder userNames = new StringBuilder();
		StringBuilder groups = new StringBuilder();
		StringBuilder groupNames = new StringBuilder();
		List<IdentityLink> links = repositoryService.getIdentityLinksForProcessDefinition(id);
		for (IdentityLink link : links) {
			String userId = link.getUserId();
			if(StringUtils.isNotEmpty(userId)) {
				users.append(userId).append(",");
				User user = identityService.createUserQuery().userId(userId).singleResult();
				if(user != null) {
					userNames.append(user.getFirstName()).append(",");
				}
			}
			
			String groupId = link.getGroupId();
			if(StringUtils.isNotEmpty(groupId)) {
				groups.append(groupId).append(",");
				Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
				if(group != null) {
					groupNames.append(group.getName()).append(",");
				}
			}
		}        		
		if(users.length() > 0) {
			model.setUsers(users.substring(0, users.length() -1));
		}
		if(userNames.length() > 0) {
			model.setUserNames(userNames.substring(0, userNames.length() -1));
		}        		
		if(groups.length() > 0) {
			model.setGroups(groups.substring(0, groups.length() -1));
		}
		if(groupNames.length() > 0) {
			model.setGroupNames(groupNames.substring(0, groupNames.length() -1));
		}
	}

}
