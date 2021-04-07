package com.erongdu.server.activiti.service.impl;

import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erongdu.server.activiti.service.ActManageService;

@Service
public class ActManageServiceImpl implements ActManageService {
	@Autowired
	private TaskService taskService;
	@Autowired
	private IdentityService identityService;
	
	
	@Override
	public void delegateTask(String taskId, String delegateAssignee, boolean comment, String processInstanceId) {
		taskService.delegateTask(taskId, delegateAssignee);
		if(comment && StringUtils.isNotEmpty(processInstanceId)) {
			User delegateUser = identityService.createUserQuery().userId(delegateAssignee).singleResult();
			String message = String.format("任务已委托[%s]进行办理", 
					delegateUser.getFirstName() == null ? delegateAssignee : delegateUser.getFirstName());
			taskService.addComment(taskId, processInstanceId, message);
		}
	}

}
