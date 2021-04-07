package com.erongdu.server.activiti.model.request;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class DelegateTaskRequest {

	@NotEmpty
	private String taskId;	//任务id
	@NotEmpty
	private String delegateAssignee;//被委派人
	private boolean commentFlag;// 是否添加批注
	private String processInstanceId;// 流程实例id
	
}
