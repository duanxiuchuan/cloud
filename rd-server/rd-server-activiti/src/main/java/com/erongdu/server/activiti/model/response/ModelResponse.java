package com.erongdu.server.activiti.model.response;

import org.activiti.engine.impl.persistence.entity.ModelEntityImpl;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ModelResponse extends ModelEntityImpl{
	
	private static final long serialVersionUID = 1L;
	private String formKey;// 表单key
	private String description;//备注

}
