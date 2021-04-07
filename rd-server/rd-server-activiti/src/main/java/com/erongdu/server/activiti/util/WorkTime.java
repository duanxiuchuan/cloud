package com.erongdu.server.activiti.util;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
/**
 * 工作时间
 * @author lh@erongdu.com
 * @since 2020年8月21日
 * @version 4.1.0
 */
public class WorkTime {
	
	// 上午时间
	public static int AM_START_HOUR = 9; 
	public static int AM_START_MINUTE = 0; 
	public static int AM_END_HOUR = 12; 
	public static int AM_END_MINUTE = 0; 
	
	// 下午时间
	public static int PM_START_HOUR = 13; 
	public static int PM_START_MINUTE = 0; 
	public static int PM_END_HOUR = 18; 
	public static int PM_END_MINUTE = 0; 
	
	/**
	 * 工作时间初始化
	 * @param json
	 */
	public static void init(String json) {
		if(StringUtils.isEmpty(json)) {
			return;
		}
		// {"am":"09:00-12:00", "pm":"13:00-18:00"}
		JSONObject timeObject = JSONObject.parseObject(json);
		String am = timeObject.getString("am");
		if(StringUtils.isNotEmpty(am) && am.contains("-")) {
			String[] ams = am.split("-");
			String[] am1 = ams[0].split(":");
			AM_START_HOUR = Integer.valueOf(am1[0].trim());
			if(am1.length > 1) {
				AM_START_MINUTE = Integer.valueOf(am1[1].trim());
			}
			String[] am2 = ams[1].split(":");
			AM_END_HOUR = Integer.valueOf(am2[0].trim());
			if(am2.length > 1) {
				AM_END_MINUTE = Integer.valueOf(am2[1].trim());
			}
		}
		
		String pm = timeObject.getString("pm");
		if(StringUtils.isNotEmpty(pm) && pm.contains("-")) {
			String[] pms = pm.split("-");
			String[] pm1 = pms[0].split(":");
			PM_START_HOUR = Integer.valueOf(pm1[0].trim());
			if(pm1.length > 1) {
				PM_START_MINUTE = Integer.valueOf(pm1[1].trim());
			}
			String[] pm2 = pms[1].split(":");
			PM_END_HOUR = Integer.valueOf(pm2[0].trim());
			if(pm2.length > 1) {
				PM_END_MINUTE = Integer.valueOf(pm2[1].trim());
			}
		}
	}
	
}
