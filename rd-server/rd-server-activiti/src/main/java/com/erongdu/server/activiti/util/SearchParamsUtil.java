package com.erongdu.server.activiti.util;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.erongdu.common.core.entity.activiti.Category;
import com.erongdu.common.core.utils.DateUtil;

/**
 * 查询条件处理工具类
 * @author lh@erongdu.com
 * @since  2020年7月30日
 *
 */
public class SearchParamsUtil {
	
	public static final String KEY_END_TIME = "endTime";	
	public static final String KEY_CATEGORY = "category";	
	
	public static Map<String, Object> wrapper(Map<String, Object> params){
		String endTime = MapUtils.getString(params, KEY_END_TIME);
		if(StringUtils.isNotEmpty(endTime)) {
			params.put(KEY_END_TIME,  DateUtil.toDateEnd(endTime));
		}
		String category = MapUtils.getString(params, KEY_CATEGORY);
		if(Category.NAME_ALL.equals(category)) {
			params.put(KEY_CATEGORY, null);
		}
		return params;
	}

}
