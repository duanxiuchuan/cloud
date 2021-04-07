package com.erongdu.server.activiti.listener.assignee;

import org.activiti.engine.IdentityService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.erongdu.common.core.utils.CommonUtil;
import com.erongdu.common.core.utils.SpringContextUtil;

/**
 * 上级领导审批任务监听器
 * @author lh@erongdu.com
 * @since  2020年7月21日
 *
 */
public class SuperiorTaskListener implements TaskListener {

	private static final long serialVersionUID = 1L;
	private static final String KEY_SUPERIOR_ID = "superior_id";
	

	private static final Logger log = LoggerFactory.getLogger(SuperiorTaskListener.class);
	@Override
	public void notify(DelegateTask delegateTask) {
		
		String username = CommonUtil.getCurrentUsername();
		
		IdentityService identityService = SpringContextUtil.getBean(IdentityService.class);
		String superiorId = identityService.getUserInfo(username, KEY_SUPERIOR_ID);
		String superiorName = StringUtils.isNotEmpty(superiorId) ? superiorId: username;
		
		log.info("task username={}, superiorId:{}", username, superiorId);
		delegateTask.setAssignee(superiorName);
		
	}

}
