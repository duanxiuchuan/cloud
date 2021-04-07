package com.erongdu.server.activiti.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.InclusiveGateway;
import org.activiti.bpmn.model.ParallelGateway;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erongdu.common.core.utils.UELUtil;
import com.erongdu.server.activiti.model.response.SequenceFlowResponse;
import com.erongdu.server.activiti.service.ActFlowElementService;
/**
 * flow元素解析处理
 * @author lh@erongdu.com
 * @since  2020年7月29日
 *
 */
@Service
public class ActFlowElementServiceImpl implements ActFlowElementService{

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private IdentityService identityService;
	
	/**
	 * 获取所有SequenceFlow的条件表达式
	 * @param processDefinitionId
	 * @return
	 */
	private Set<String> getFlowConditionExps(String processDefinitionId){
		Set<String> conditions = new HashSet<String>();
		//获取当前模型
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
    	Collection<FlowElement> flowElements = bpmnModel.getMainProcess().getFlowElements();
    	for (FlowElement flowElement : flowElements) {
			if(flowElement.getClass().equals(SequenceFlow.class)) {
				SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
				String condition = sequenceFlow.getConditionExpression();
				if(StringUtils.isNotEmpty(condition)) {
					conditions.add(condition);
				}
			}
		}
    	return conditions;
	}
	
	/**
	 * 获取所有SequenceFlow条件表达式中的参数名称
	 * @param processDefinitionId
	 * @return
	 */
	@Override
	public Set<String> getFlowConditionVariables(String processDefinitionId){
		Set<String> variables = new HashSet<String>();
		Set<String> expressions = getFlowConditionExps(processDefinitionId);
		for (String expression : expressions) {
			 variables.addAll(UELUtil.getVariableNames(expression));			
		}
		return variables;
	}
	
	/**
	 * 获取条件表达式中的参数名称
	 * @param model
	 * @return
	 */
	public Set<String> getExpressionVariables(String expression){
		return UELUtil.getVariableNames(expression);
	}
	
	/**
     * 任务节点出口列表
     * @param task
     * @return
     */
	@Override
	public List<SequenceFlowResponse> getOutgoingFlowsByTaskKey(String processDefinitionId, String taskDefinitionKey) {
		//获取当前模型
    	BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

    	// 获取当前节点
    	FlowElement flowElement = bpmnModel.getFlowElement(taskDefinitionKey);
    	List<SequenceFlowResponse> flows = new ArrayList<SequenceFlowResponse>();
    	if(flowElement instanceof UserTask) {
    		UserTask userTask = (UserTask)flowElement;    		
    		
    		List<SequenceFlow> outgoingFlows = userTask.getOutgoingFlows();
    		
    		if(outgoingFlows != null && outgoingFlows.size() == 1) {
    			FlowElement fe = outgoingFlows.get(0).getTargetFlowElement();
    			if(fe instanceof ExclusiveGateway) {
    				ExclusiveGateway xor = (ExclusiveGateway)fe;
    				outgoingFlows = xor.getOutgoingFlows();    				
    			}else if(fe instanceof InclusiveGateway) {
    				((InclusiveGateway) fe).getOutgoingFlows();
    				InclusiveGateway inc = (InclusiveGateway)fe;
    				outgoingFlows = inc.getOutgoingFlows();    				
    			}else if(fe instanceof ParallelGateway) {
    				outgoingFlows = null;
    			}
    		}
    		if(outgoingFlows != null && outgoingFlows.size() > 0) {
    			for (SequenceFlow sf : outgoingFlows) {
    				SequenceFlowResponse model = new SequenceFlowResponse();
    				BeanUtils.copyProperties(sf, model);
    				if(StringUtils.isNotEmpty(model.getName())) {
    					flows.add(model);
    				}
    			}
    		}
    	}
		return flows;
	}
	
	@Override
	public Map<String, Object> getNextTaskCandidate(String processDefinitionId, String taskDefinitionKey){
		
		Map<String, Object> users = new HashMap<String, Object>();		
		List<SequenceFlowResponse> sfs = getOutgoingFlowsByTaskKey(processDefinitionId, taskDefinitionKey);
		//获取当前模型
    	BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
		for (SequenceFlowResponse sf : sfs) {
			String targetKey = sf.getTargetRef();
			// 获取当前节点
	    	FlowElement flowElement = bpmnModel.getFlowElement(targetKey);
	    	if(flowElement instanceof UserTask) {
	    		UserTask userTask = (UserTask)flowElement;    	
	    		if(StringUtils.isNotEmpty(userTask.getAssignee())) {
	    			continue;
	    		}else if(userTask.getCandidateUsers() != null && !userTask.getCandidateUsers().isEmpty()) {
	    			List<String> uids = userTask.getCandidateUsers();
	    			List<User> cus = new ArrayList<>();
	    			if(uids.size() > 1) {
	    				for (String userId : uids) {
	    					cus.add(identityService.createUserQuery().userId(userId).singleResult());
						}
	    				users.put(targetKey, cus);
	    			}
	    		}else if(userTask.getCandidateGroups() != null && !userTask.getCandidateGroups().isEmpty()) {
	    			List<User> cus = new ArrayList<>();
	    			List<String> groups = userTask.getCandidateGroups();
	    			for (String groupId : groups) {
	    				cus.addAll(identityService.createUserQuery().memberOfGroup(groupId).list());
					}
	    			if(cus.size() > 1) {
	    				users.put(targetKey, cus);
	    			}
	    		}	    		
	    	}
		}		
		return users;
	}
	
	/**
	 * 获取流程模型 中的第一个用户任务
	 * @param processDefinitionId
	 * @return
	 */
	@Override
	public UserTask getFirstUserTask(String processDefinitionId) {
		//获取当前模型
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
		Collection<FlowElement> flowElements = bpmnModel.getMainProcess().getFlowElements();
		// 找到开始节点
		StartEvent startEvent = null;
    	for (FlowElement flowElement : flowElements) {
			if(flowElement.getClass().equals(StartEvent.class)) {
				startEvent = (StartEvent) flowElement;
				break;
			}
		}
    	// 找到第一个任务节点
    	FlowElement firstFlow = null;
    	for (FlowElement flowElement : flowElements) {
			if(flowElement.getClass().equals(SequenceFlow.class)) {
				SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
				if(sequenceFlow.getSourceRef().equals(startEvent.getId())) {
					firstFlow = sequenceFlow.getTargetFlowElement();
					break;
				}
			}
		}
    	if(firstFlow != null && firstFlow instanceof UserTask) {
    		return (UserTask) firstFlow;
    	}
    	return null;    	
	}
	
	@Override
	public StartEvent getStartEvent(String processDefinitionId) {
		//获取当前模型
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
		Collection<FlowElement> flowElements = bpmnModel.getMainProcess().getFlowElements();
		// 找到开始节点
		StartEvent startEvent = null;
    	for (FlowElement flowElement : flowElements) {
			if(flowElement.getClass().equals(StartEvent.class)) {
				startEvent = (StartEvent) flowElement;
				break;
			}
		}
		return startEvent;
	}
	
}
