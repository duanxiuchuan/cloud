package com.erongdu.server.activiti.listener.event;

import java.util.Calendar;
import java.util.Date;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.erongdu.common.core.constant.ActivitiConstant;
import com.erongdu.common.core.utils.SpringContextUtil;
import com.erongdu.server.activiti.util.WorkTimeUtil;

/**
 * 任务创建事件处理器
 * @author lh@erongdu.com
 * @since 2020年8月21日
 * @version 4.1.0
 */
@Component
public class TaskCreatedEventHandler implements EventHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskCreatedEventHandler.class);

	
	@Override
	public void handle(ActivitiEvent event) {
		ActivitiEntityEventImpl eventImpl = (ActivitiEntityEventImpl) event;
        TaskEntity taskEntity = (TaskEntity) eventImpl.getEntity();
        logger.info("流程实例id:{}, 任务名称：{}， task key:{} ", taskEntity.getProcessInstanceId(), taskEntity.getName(), taskEntity.getTaskDefinitionKey());
        
		Date createTime = taskEntity.getCreateTime();
		String category = taskEntity.getCategory();
		
		// 任务到期时间设置
		if(StringUtils.isNotEmpty(category)) {
			Calendar dueDate = WorkTimeUtil.getDueDate(createTime, category);
			if(dueDate != null) {
				logger.info("taskId:{}, dueDate:{}", taskEntity.getId(), dueDate.toString());
				taskEntity.setDueDate(dueDate.getTime());
			}
		}
		
		// 设置任务处理人
		String taskDefinitionKey = taskEntity.getTaskDefinitionKey();
		String taskAssignee = taskEntity.getAssignee();
		Object varTaskAssignee = taskEntity.getVariable(taskDefinitionKey+ActivitiConstant.VAR_TAG_ASSIGNEE);
		if(StringUtils.isEmpty(taskAssignee) && varTaskAssignee != null) {
			taskEntity.setAssignee(varTaskAssignee.toString());
			taskEntity.removeVariable(taskDefinitionKey+ActivitiConstant.VAR_TAG_ASSIGNEE);
		}
		
		// 设置任务的category
		RepositoryService repositoryService = SpringContextUtil.getBean(RepositoryService.class);
		String processDefinitionId = taskEntity.getProcessDefinitionId();
		if(StringUtils.isEmpty(processDefinitionId)) {
    		JSONObject desc = JSONObject.parseObject(taskEntity.getDescription());
    		processDefinitionId = desc.getString(ActivitiConstant.KEY_PROCESS_DEFINITION_ID);
    	}
		logger.info("processDefinitionId:{}", processDefinitionId);
		
		ProcessDefinition definition = null;
		if(StringUtils.isNotEmpty(processDefinitionId)) {
			definition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).latestVersion().singleResult();
		}
		if(definition != null) {
			category = definition.getCategory();
			taskEntity.setCategory(category);
			
			// 设置历史任务的category
			ProcessEngineConfigurationImpl processEngineConfiguration =  SpringContextUtil.getBean(ProcessEngineConfigurationImpl.class);
			HistoricTaskInstanceEntity historicTaskInstance = processEngineConfiguration.getHistoricTaskInstanceEntityManager().findById(taskEntity.getId());
			if (historicTaskInstance != null) {
				historicTaskInstance.setCategory(category);
			}		
		}		
	}

}
