package com.erongdu.server.activiti.model.response;

import java.util.Date;

import lombok.Data;

@Data
public class TaskResponse {
	private String id; // 任务id
	private String name; // 任务名称
	private Date createTime;// 任务创建时间
	private String owner;
	private String assignee; // 任务处理人
	private String parentTaskId;// 上级任务
	private String description;
	private Date dueDate;
	private String category;
	private String	processInstanceId;		 /* 流程实例id */ 
	private String	processDefinitionId;		 /* 流程定义id */ 
	private String formKey;

	// 表单实例信息
	private String formName;	//表单实例名称
	private Date formCreateTime;//流程发起时间
	private String nodeName;	// 当前节点

	// his task
	private Date startTime;//任务开始时间
	private Date endTime;//任务完成时间
	private Long durationInMillis = 0l;//任务耗时
	
	public String getDuration() {
		if(durationInMillis == null || durationInMillis == 0l) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		Long seconds = durationInMillis/1000;
		Long minutes = 0l, hours = 0l, days = 0l;
		if(seconds > 60) {
			minutes = seconds / 60;
			seconds = seconds % 60;
		}		
		if(minutes > 60) {
			hours = minutes / 60;
			minutes = minutes % 60;
		}		
		if(hours > 60) {
			days = hours / 24;
			hours = hours % 24;
		}
		if(days > 0) {
			builder.append(days).append("天 ");
		}
		if(hours > 0) {
			builder.append(hours).append("时 ");
		}
		if(minutes > 0) {
			builder.append(minutes).append("分 ");
		}
		if(builder.length() == 0) {
			builder.append("1分钟内");
		}
		return builder.toString();
	}
	

}
