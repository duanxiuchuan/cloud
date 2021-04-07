package com.erongdu.server.activiti.model.form;

import lombok.Data;

@Data
public class ColumnEle {
	
	private String type;//类型，如input/checkbox/radio/textarea/date/time/datetime
	private String label;//控件名称
	private String value;//控件值
	private String format;//格式化
	private String	cfgName;		 /* 参数名称 */ 
	private String	eleValue;		 /* 控件元素默认值 */ 
	

}
