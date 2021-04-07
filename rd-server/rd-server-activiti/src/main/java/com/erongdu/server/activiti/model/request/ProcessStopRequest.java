package com.erongdu.server.activiti.model.request;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class ProcessStopRequest {
	
	@NotEmpty
	private String processInstanceId;//流程实例id
	@NotEmpty
	private String stopReason;//停止原因
	
}
