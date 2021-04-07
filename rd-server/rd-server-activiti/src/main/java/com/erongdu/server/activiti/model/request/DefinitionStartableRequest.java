package com.erongdu.server.activiti.model.request;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class DefinitionStartableRequest {
	
	@NotEmpty
	private String id;
	private String[] users;
	private String[] groups;
	

}
