package com.erongdu.server.activiti.support.cmd;

import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.task.Task;  
/** 
 * 回退到第一个节点
 * 
 * @author lh@erongdu.com
 * @since  2020年7月17日
 *
 */
public class FlowToFirstCmd implements Command<String> { 
	
    private Task task;  
    public FlowToFirstCmd(Task task) {  
        this.task = task;  
    }  
    @Override  
    public String execute(CommandContext context) {  
        FlowElement startNode = this.getFirstNode(this.task, context);  
        ExecutionEntity executionEntity = context.getExecutionEntityManager().findById(task.getExecutionId());  
        executionEntity.setCurrentFlowElement(startNode);  
        context.getAgenda().planTakeOutgoingSequenceFlowsOperation(executionEntity, true);  
        return executionEntity.getId();  
    }  
    private FlowElement getFirstNode(Task task, CommandContext context) {  
        HistoryService historyService = context.getProcessEngineConfiguration().getHistoryService();  
        HistoricActivityInstance startNode = historyService.createHistoricActivityInstanceQuery()  
                .processInstanceId(task.getProcessInstanceId())  
                .activityType("startEvent")  
                .singleResult();  
        if (startNode == null) {  
            throw new RuntimeException("未找到开始节点");  
        }  
        RepositoryService repositoryService = context.getProcessEngineConfiguration().getRepositoryService();  
        org.activiti.bpmn.model.Process process = repositoryService.getBpmnModel(task.getProcessDefinitionId()).getMainProcess();  
        FlowElement node = process.getFlowElement(startNode.getActivityId());  
        return node;  
    }  
}  