package com.erongdu.server.activiti.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erongdu.common.core.utils.CommonUtil;
import com.erongdu.server.activiti.model.request.SubTaskRequest;
import com.erongdu.server.activiti.model.request.TaskRequest;
import com.erongdu.server.activiti.model.response.CommentResponse;
import com.erongdu.server.activiti.service.ActTaskService;

@Service
public class ActTaskServiceImpl implements ActTaskService{
	
	@Autowired 
	private TaskService taskService;
	@Autowired
	private IdentityService identityService;
	
	private static String SQL_CANDIDATE_COUNT = "SELECT COUNT(ID_) FROM ACT_RU_IDENTITYLINK WHERE TASK_ID_ = #{taskId} ";
    
    /**
     * 是否为任务候选人之一
     * @param task
     * @param username
     * @return
     */
    @Override
    public boolean candidate(Task task, String username) {
    	if(username.equalsIgnoreCase(task.getAssignee())) {
    		return true;
    	}
    	long count = taskService.createTaskQuery().taskId(task.getId()).taskCandidateOrAssigned(username).count();
    	return count > 0;
    }
    
    /**
     * 是否为任务设置候选人
     * @param task
     * @return
     */
    @Override
	public boolean candidate(Task task) {
		if(StringUtils.isNotEmpty(task.getAssignee())) {
    		return true;
    	}
		long count = taskService.createNativeTaskQuery().sql(SQL_CANDIDATE_COUNT)
    			.parameter("taskId", task.getId())
    			.count();
		return count > 0;
	}
	
	/**
	 * 任务委托
	 * @param taskId	任务id
	 * @param taskAssignee	委托人
	 */
	@Override
	public void delegate(String taskId, String taskAssignee) {
		taskService.delegateTask(taskId, taskAssignee);
	}
	
	/**
	 * 更新任务信息
	 * @param taskId
	 * @param params	
	 * 	owner-所有者、assignee-办理人、dueDate-到期日、priority-优先级
	 */
	@Override
	public Task saveTask(TaskRequest model) {
		Task task = taskService.createTaskQuery().taskId(model.getTaskId()).singleResult();
		if(task != null) {
			int upCounter = 0;
			String owner = model.getOwner();
			if(StringUtils.isNotEmpty(owner)) {// 所有者
				task.setOwner(owner);
				upCounter++;
			}
			
			String assignee = model.getAssignee();
			if(StringUtils.isNotEmpty(assignee)) {//办理人
				task.setAssignee(assignee);
				upCounter++;
			}
			Date dueDate = model.getDueDate();
			if(dueDate != null) {//到期日
				task.setDueDate(dueDate);
				upCounter++;
			}
			
			Integer priority = model.getPriority();
			if(priority != null) {// 优先级
				task.setPriority(priority);
				upCounter++;
			}
			
			if(upCounter > 0) {
				taskService.saveTask(task);
			}			
		}
		return task;
	}
	
	/**
	 * 创建子任务
	 * notice：新建的task不影响主流程的推进，就是父任务结束了流程也会向后面流转，子任务也不会结束
	 * @param subTask
	 */
	@Override
	public Task createSubTask(SubTaskRequest subTask) {
		Task sub = taskService.newTask();
		sub.setDescription(subTask.getDescription());
		sub.setOwner(subTask.getOwner() == null ? CommonUtil.getCurrentUsername(): subTask.getOwner());
		sub.setAssignee(subTask.getAssignee());
		sub.setName(subTask.getName());
		sub.setParentTaskId(subTask.getParentTaskId());
		sub.setDescription(subTask.getDescription());
		sub.setDueDate(subTask.getDueDate());		
		taskService.saveTask(sub);		
		return sub;
	}
    
	/**
	 * 查询实例的批注列表
	 * @param processInstanceId
	 * @return
	 */
	@Override
	public List<CommentResponse> getCommentsByProcessInstanceId(String processInstanceId) {
		// 获取批注信息
		List<CommentResponse> comments = new ArrayList<CommentResponse>();
		List<Comment> dbcomments = taskService.getProcessInstanceComments(processInstanceId);
		for (Comment cm : dbcomments) {
			CommentResponse model = new CommentResponse();
			BeanUtils.copyProperties(cm, model);
			if(StringUtils.isNotEmpty(cm.getUserId())) {
				User user = identityService.createUserQuery().userId(cm.getUserId()).singleResult();
				model.setAssigneeName(user != null ? user.getFirstName(): cm.getUserId());
			}else {
				model.setAssigneeName(cm.getUserId());
			}
			comments.add(model);
		}
		return comments;
	}
	
}
