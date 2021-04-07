package com.erongdu.server.activiti.model.form;

import java.io.Serializable;

import lombok.Data;

/**
 * 表单算式-列规则
 * @author lh@erongdu.com
 * @since  2020年8月12日
 *
 */
@Data
public class ColFormula implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String listenTable;
	private String listenField;
	
}
