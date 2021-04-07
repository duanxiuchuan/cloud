package com.erongdu.server.activiti.model.request;

import java.util.Date;

import lombok.Data;

@Data
public class SubTaskRequest {
	
	private String name;	//任务名称
	private String parentTaskId;//父任务id
	private String owner;	//所属人
	private String assignee;// 任务处理人 
	private String description;//描述信息
	private Date dueDate;	//到期日

}
