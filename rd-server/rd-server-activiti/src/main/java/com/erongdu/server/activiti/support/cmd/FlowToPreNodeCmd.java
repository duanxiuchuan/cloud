package com.erongdu.server.activiti.support.cmd;
import org.activiti.bpmn.model.FlowElement;  
import org.activiti.bpmn.model.FlowNode;  
import org.activiti.bpmn.model.SequenceFlow;  
import org.activiti.engine.HistoryService;  
import org.activiti.engine.RepositoryService;  
import org.activiti.engine.history.HistoricActivityInstance;  
import org.activiti.engine.impl.interceptor.Command;  
import org.activiti.engine.impl.interceptor.CommandContext;  
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;  
import org.activiti.engine.task.Task;  
import java.util.List;  
/** 
 * 回退到上个审批节点
 * @author lh@erongdu.com
 * @since  2020年7月17日
 *
 */
public class FlowToPreNodeCmd implements Command<String> { 
	
    private Task task;  
    public FlowToPreNodeCmd(Task task) {  
        this.task = task;  
    }  
    
    @Override  
    public String execute(CommandContext context) {  
        FlowElement element = this.getPreNode(this.task, context);  
        if (element == null) {  
            throw new RuntimeException("该节点不能进行退回");  
        }  
        SequenceFlow flow = this.findAcessSequenceFlow((FlowNode) element);  
        ExecutionEntity executionEntity = context.getExecutionEntityManager().findById(task.getExecutionId());  
        executionEntity.setCurrentFlowElement(flow);  
        context.getAgenda().planTakeOutgoingSequenceFlowsOperation(executionEntity, true);  
        return executionEntity.getId();  
    }  
    
    private FlowElement getPreNode(Task task, CommandContext context) {  
        HistoryService historyService = context.getProcessEngineConfiguration().getHistoryService();  
        List<HistoricActivityInstance> items = historyService.createHistoricActivityInstanceQuery()  
                .executionId(task.getExecutionId())  
                .activityType("userTask")  
                .orderByHistoricActivityInstanceStartTime()  
                .desc()  
                .list();  
        if (items == null || items.size() == 0) {  
            throw new RuntimeException("未找到上一节点");  
        }  
        String currentAct = task.getTaskDefinitionKey();  
        String preAct = null;  
        for (int i = 0; i < items.size(); ++i) {  
            HistoricActivityInstance item = items.get(i);  
            if (currentAct.equals(item.getActivityId())) {  
                continue;  
            }  
            preAct = item.getActivityId();  
            break;  
        }  
        if (preAct == null) {  
            return null;  
        }  
        RepositoryService repositoryService = context.getProcessEngineConfiguration().getRepositoryService();  
        org.activiti.bpmn.model.Process process = repositoryService.getBpmnModel(task.getProcessDefinitionId()).getMainProcess();  
        FlowElement node = process.getFlowElement(preAct);  
        return node;  
    }  
    
    private SequenceFlow findAcessSequenceFlow(FlowNode node) {  
        List<SequenceFlow> flows = node.getIncomingFlows();  
        if (flows == null || flows.size() == 0) {  
            throw new RuntimeException("上一节点找不到入口");  
        }  
        //找没有加条件的连线  
        for (SequenceFlow flow : flows) {  
            if (flow.getConditionExpression() == null) {  
                return flow;  
            }  
        }  
        //如果都没有选择第一条  
        return flows.get(0);  
    }  
}  