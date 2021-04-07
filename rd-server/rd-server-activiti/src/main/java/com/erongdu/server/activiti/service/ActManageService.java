package com.erongdu.server.activiti.service;
/**
 * 工作流管理
 * @author lh@erongdu.com
 * @since  2020年8月1日
 *
 */
public interface ActManageService {
	
	/**
	 * 任务委托
	 * @param taskId	任务id
	 * @param delegateAssignee	委托人
	 * @param comment		是否记录批注
	 * @param processInstanceId	流程实例id
	 */
	void delegateTask(String taskId, String delegateAssignee, boolean comment, String processInstanceId);

}
