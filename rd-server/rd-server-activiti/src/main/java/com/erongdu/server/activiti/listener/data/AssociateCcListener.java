package com.erongdu.server.activiti.listener.data;

import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.erongdu.common.core.constant.ActivitiConstant;
import com.erongdu.common.core.utils.SpringContextUtil;
import com.erongdu.server.activiti.model.request.SubTaskRequest;
import com.erongdu.server.activiti.service.ActTaskService;

/**
 * 任务抄送关联人员
 * @author lh@erongdu.com
 * @since 2020年9月8日
 * @version 4.1.0
 */
public class AssociateCcListener  implements ExecutionListener{
	
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) {
		
		String processInstanceId = execution.getProcessInstanceId();
		String processDefinitionId = execution.getProcessDefinitionId();
		FlowElement flowElement = execution.getCurrentFlowElement();
		String flowKey = flowElement.getId();
		String taskId = null;
		String associates = (String)execution.getVariable(ActivitiConstant.VAR_ASSOCIATE);
		String applyUserId = (String)execution.getVariable(ActivitiConstant.VAR_APPLYUSERID);
		String flowName = flowElement.getName();
		TaskService taskService = SpringContextUtil.getBean(TaskService.class);
		if(flowElement instanceof UserTask) {
			Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey(flowKey).singleResult();
			taskId = task.getId();
		}else if(flowElement instanceof StartEvent) {
			flowName = "";
			taskId = execution.getId() + ":" + execution.getCurrentActivityId();
		}
		
		if(StringUtils.isNotEmpty(associates)) {
			ActTaskService actTaskService = SpringContextUtil.getBean(ActTaskService.class);
			String[] users = associates.split(",");
			for (String associate : users) {
				String user = associate.trim();
				if(applyUserId.equalsIgnoreCase(user)) {// 跳过任务发起人
					continue;
				}
				SubTaskRequest sub = new SubTaskRequest();
				sub.setAssignee(user);
				sub.setOwner(user);
				sub.setName("【抄送】" + flowName);
				sub.setParentTaskId(taskId);						
				JSONObject description = new JSONObject();
				description.put(ActivitiConstant.KEY_PROCESS_INSTANCE_ID, processInstanceId);
				description.put(ActivitiConstant.KEY_PROCESS_DEFINITION_ID, processDefinitionId);
				sub.setDescription(description.toJSONString());						
				actTaskService.createSubTask(sub);
			}
		}
	}

}
