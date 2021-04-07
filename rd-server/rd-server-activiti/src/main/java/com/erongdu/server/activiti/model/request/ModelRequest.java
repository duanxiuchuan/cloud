package com.erongdu.server.activiti.model.request;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class ModelRequest {
	@NotEmpty
	private String key;
	@NotEmpty
	private String name;
	@NotEmpty
	private String category; 
	@NotEmpty
	private String description;
	//表單key
	private String formKey;
	
	
}
