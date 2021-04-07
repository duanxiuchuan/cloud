package com.erongdu.server.activiti.listener.data;

import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 打印FlowElement信息
 * @author lh@erongdu.com
 * @since 2020年8月20日
 * @version 4.1.0
 */
public class PrintFlowElementListener implements ExecutionListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(PrintFlowElementListener.class);
	
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) {
		
	 	FlowElement flowElement = execution.getCurrentFlowElement(); 
		
		LOGGER.info("processInstanceId:{}, flowElementId:{}, flowElementName:{}, flowElementAttributes:{}", 
				execution.getProcessInstanceId(),
				flowElement.getId(),
				flowElement.getName(),
				flowElement.getAttributes().toString());
		
	}

}
