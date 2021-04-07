package com.erongdu.server.activiti.listener.data;

import java.util.Calendar;
import java.util.Date;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.erongdu.server.activiti.util.WorkTimeUtil;

/**
 * 任务到期时间设置
 * @author lh@erongdu.com
 * @since 2020年8月20日
 * @version 4.1.0
 */
public class TaskDueDateSettingListener implements TaskListener {

	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateTask delegateTask) {
		Date createTime = delegateTask.getCreateTime();
		String category = delegateTask.getCategory();
		Calendar dueDate = WorkTimeUtil.getDueDate(createTime, category);
		if(dueDate != null) {
			delegateTask.setDueDate(dueDate.getTime());
		}
	}

}
