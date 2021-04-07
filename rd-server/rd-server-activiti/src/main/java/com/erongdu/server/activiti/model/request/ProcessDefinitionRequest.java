package com.erongdu.server.activiti.model.request;

import lombok.Data;

@Data
public class ProcessDefinitionRequest {

	private String deploymentId;//部署id
	private String processDefinitionId;//流程定义id
	
}
