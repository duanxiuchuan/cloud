package com.erongdu.server.activiti.model.response;

import java.util.Date;

import lombok.Data;

@Data
public class DeploymentResponse {

	private String name;
	private String category;
	private String key;
	private String tenantId;
	private Date deploymentTime;

}
