package com.erongdu.server.activiti.util;

import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;

import cn.hutool.core.io.FileTypeUtil;

public class AttachmentUtil {
	
	/**
	 * 校验文件类型是否合法
	 * @param in
	 * @return
	 */
	public static boolean supportFileType(InputStream in, String supportFileTypes) {
		if(StringUtils.isEmpty(supportFileTypes)) {
			return true;
		}
		String[] sfts = supportFileTypes.split(",");
		String fileType = FileTypeUtil.getType(in);
    	// 上传附件
    	for (String st : sfts) {
    		if(st.trim().equals(fileType)) {
    			return true;
    		}
		}
    	return false;
	}
	
	

}
