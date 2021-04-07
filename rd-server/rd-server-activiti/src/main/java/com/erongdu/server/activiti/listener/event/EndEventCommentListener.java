package com.erongdu.server.activiti.listener.event;

import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.erongdu.common.core.utils.SpringContextUtil;
/**
 * 归档时添加批注
 * @author lh@erongdu.com
 * @since  2020年8月4日
 *
 */
public class EndEventCommentListener implements ExecutionListener {


	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) {
		
		FlowElement currentFlowElement = execution.getCurrentFlowElement();
		
		
		if(currentFlowElement instanceof EndEvent) {
			TaskService taskService = SpringContextUtil.getBean(TaskService.class);
			taskService.addComment(null, execution.getProcessInstanceId(), "已归档");
		}
		
	}

}
