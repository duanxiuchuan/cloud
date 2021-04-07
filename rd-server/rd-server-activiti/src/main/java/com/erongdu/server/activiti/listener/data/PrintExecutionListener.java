package com.erongdu.server.activiti.listener.data;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 打印Execution信息
 * @author lh@erongdu.com
 * @since 2020年8月20日
 * @version 4.1.0
 */
public class PrintExecutionListener implements ExecutionListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(PrintExecutionListener.class);
	
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) {
		
		LOGGER.info("processInstanceId:{}, businessKey:{}, executionId:{}, activityId:{}, eventName:{}", 
				execution.getProcessInstanceId(),
				execution.getProcessInstanceBusinessKey(),
				execution.getId(),
				execution.getCurrentActivityId(), 
				execution.getEventName());
		
	}

}
