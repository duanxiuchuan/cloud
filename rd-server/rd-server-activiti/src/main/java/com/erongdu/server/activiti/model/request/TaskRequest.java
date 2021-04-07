package com.erongdu.server.activiti.model.request;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TaskRequest {
		
	@NotEmpty
	private String taskId;//任务id
	@NotEmpty
	private String assignee; // 任务办理人
	@NotNull
	private boolean commentFlag;// 是否添加批注
	private String owner;	// 任务拥有人
	private Date dueDate;	// 截止时间
	private Integer priority; // 优先级
	private String processInstanceId;// 流程实例id
	
	private String comment;	//批注信息

}
