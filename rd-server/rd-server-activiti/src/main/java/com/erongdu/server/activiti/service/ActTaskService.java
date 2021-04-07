package com.erongdu.server.activiti.service;

import java.util.List;

import org.activiti.engine.task.Task;

import com.erongdu.server.activiti.model.request.SubTaskRequest;
import com.erongdu.server.activiti.model.request.TaskRequest;
import com.erongdu.server.activiti.model.response.CommentResponse;

public interface ActTaskService {
    
    /**
     * 是否为任务候选人之一
     * @param task
     * @param username
     * @return
     */
    boolean candidate(Task task, String username);
    
    /**
     * 是否为任务设置候选人
     * @param task
     * @return
     */
	boolean candidate(Task task) ;
	
	/**
	 * 任务委托
	 * @param taskId	任务id
	 * @param taskAssignee	委托人
	 */
	void delegate(String taskId, String taskAssignee);
	
	/**
	 * 更新任务信息
	 * @param taskId
	 * @param params	
	 * 	owner-所有者、assignee-办理人、dueDate-到期日、priority-优先级
	 */
	Task saveTask(TaskRequest model);
	
	/**
	 * 创建子任务
	 * notice：新建的task不影响主流程的推进，就是父任务结束了流程也会向后面流转，子任务也不会结束
	 * @param subTask
	 */
	Task createSubTask(SubTaskRequest subTask);
	
	/**
	 * 查询实例的批注列表
	 * @param processInstanceId
	 * @return
	 */
	List<CommentResponse> getCommentsByProcessInstanceId(String processInstanceId);
	
	
}
