package com.erongdu.server.activiti.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;

import com.erongdu.server.activiti.model.response.SequenceFlowResponse;

public interface ActFlowElementService {
	
	/**
	 * 获取所有SequenceFlow条件表达式中的参数名称
	 * @param processDefinitionId
	 * @return
	 */
	Set<String> getFlowConditionVariables(String processDefinitionId);
	
	/**
	 * 获取表达式中的变量
	 * @param expression
	 * @return
	 */
	Set<String> getExpressionVariables(String expression);
	
	/**
	 * 任务节点出口列表
	 * @param processDefinitionId
	 * @param taskDefinitionKey
	 * @return
	 */
	List<SequenceFlowResponse> getOutgoingFlowsByTaskKey(String processDefinitionId, String taskDefinitionKey);
	
	/**
	 * 获取下个任务候选人
	 * @param processDefinitionId	流程定义id
	 * @param taskDefinitionKey		当前任务定义key
	 * @return
	 */
	Map<String, Object> getNextTaskCandidate(String processDefinitionId, String taskDefinitionKey);
	
	/**
	 * 取得流程定义中的第一个用户任务
	 * @param processDefinitionId
	 * @return
	 */
	UserTask getFirstUserTask(String processDefinitionId);
	
	/**
	 * 取得开始节点
	 * @param processDefinitionId
	 * @return
	 */
	StartEvent getStartEvent(String processDefinitionId);
}
