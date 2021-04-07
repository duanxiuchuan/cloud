package com.erongdu.server.activiti.model.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class DeploymentRequest {
	
	private String id;
	private String name;// 流程名称
	@NotBlank
	private String category;// 流程分类
	private String key;		// 流程key
	private String sourceName;// 资源文件名称
	private String formKey;	// 表单标识
	

}
