package com.erongdu.server.activiti.listener.data;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 打印Task信息
 * @author lh@erongdu.com
 * @since 2020年8月20日
 * @version 4.1.0
 */
public class PrintTaskListener implements TaskListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(PrintTaskListener.class);
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateTask delegateTask) {

		LOGGER.info("processInstanceId:{}, taskId:{}, taskName:{}, assignee:{}, createTime:{}, dueDate:{}, formKey:{}, taskDefinitionKey:{}", 
				delegateTask.getProcessInstanceId(),
				delegateTask.getId(),
				delegateTask.getName(),
				delegateTask.getAssignee(),
				delegateTask.getCreateTime() == null ? "": delegateTask.getCreateTime().toString(), 
				delegateTask.getDueDate()== null ? "": delegateTask.getDueDate().toString(),
				delegateTask.getFormKey(),
				delegateTask.getTaskDefinitionKey()
				);
		
	}

}
