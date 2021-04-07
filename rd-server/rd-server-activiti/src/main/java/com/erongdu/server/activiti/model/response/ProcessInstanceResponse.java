package com.erongdu.server.activiti.model.response;

import java.util.Date;

import com.erongdu.common.core.enums.SuspensionState;

import lombok.Data;

/**
 * 流程实例信息
 * @author lh@erongdu.com
 * @since  2020年7月20日
 *
 */
@Data
public class ProcessInstanceResponse {
	private String deleteReason;
	private int suspensionState;
	private String startUserId;
	private Date startTime;
	private String processDefinitionId;
	private String processDefinitionKey;
	private String processDefinitionName;
	private Integer processDefinitionVersion;
	private String deploymentId;
	private String activityId;
	private String activityName;
	private String processInstanceId;
	private String businessKey;
	private String parentId;
	
	/**
	 * 状态
	 * @return
	 */
	public String getSuspensionStateStr(){
		return SuspensionState.ofDesc(suspensionState);
	}

}
