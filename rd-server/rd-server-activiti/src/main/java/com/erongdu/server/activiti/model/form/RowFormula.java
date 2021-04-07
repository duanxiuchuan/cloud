package com.erongdu.server.activiti.model.form;

import java.io.Serializable;

import lombok.Data;

/**
 * 表单算式-行规则
 * @author lh@erongdu.com
 * @since  2020年8月12日
 *
 */
@Data
public class RowFormula  implements Serializable{


	private static final long serialVersionUID = 1L;
	private Long id;
	private String listenTable;
	private String operateField1;
	private String operateField2;
	private String operator;
	private String resultField;
	
}
