package com.erongdu.server.activiti.util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.erongdu.common.core.enums.DueDateType;
import com.erongdu.common.tool.util.CalendarUtil;

/**
 * 工时计算工具类
 * @author lh@erongdu.com
 * @since 2020年8月21日
 * @version 4.1.0
 */
public class WorkTimeUtil {
	
	public static final int MINUTE_IN_MILLIS = 1000 * 60;
	
	/**
	 * 根据办理时长计算限办期限
	 * @param createTime
	 * @param expression
	 * @return
	 */
	public static Calendar getDueDate(Date createTime, String expression) {
		Calendar dueDate = null;
		if(StringUtils.isNotEmpty(expression)) {
			int splitIndex = expression.length() - 1;
			String numberStr = expression.substring(0, splitIndex);
			String type = expression.substring(splitIndex);
			if(!StringUtils.isNumeric(numberStr)) {
				return dueDate;
			}
			int number = Integer.valueOf(numberStr);
			dueDate = Calendar.getInstance(); 
			dueDate.setTime(createTime);
			switch (DueDateType.of(type.toUpperCase())) {
			case WEEK:
				dueDate = calcEndTime(createTime, Calendar.DATE, number * 7);
				break;
			case DAY:
				dueDate = calcEndTime(createTime, Calendar.DATE, number);
				break;
			case HOUR:
				dueDate = calcEndTime(createTime, Calendar.HOUR, number);
				break;
			case MINUTE:
				dueDate = calcEndTime(createTime, Calendar.MINUTE, number);
				break;
			default:
				return null;
			}
		}
		return dueDate;
	}
	
	/**
	 * 计算限办期限
	 * @param startTime
	 * @param field
	 * @param amount
	 * @return
	 */
	public static Calendar calcEndTime(Date startTime, int field, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startTime);		

		if(Calendar.DATE == field) {
			calcDate(cal, amount);
			return cal;
		}
				
		// 重新设置开始时间
		resetTime(cal);
		
		// 跳过节假日
		skipOutWorkTime(cal);
		
		// 计算截止时间
		return calcTime(cal, field, amount);		
		
	}

	/**
	 * 计算截止日期
	 * @param cal
	 * @param days
	 */
	private static void calcDate(Calendar cal, int days) {
		Date startDate = cal.getTime();
		Date endDate = DateUtils.addDays(startDate, 60);
		List<Date> holidays = CalendarUtil.holidays(startDate, endDate);
		while(days > 0) {
			cal.add(Calendar.DATE, 1);
			days--;			
			// 判断是否节假日，是则跳过
			skipHolidays(cal, holidays);
		}
	}


	/**
	 * 计算截止时间
	 * @param field
	 * @param amount
	 * @param cal
	 * @return
	 */
	private static Calendar calcTime(Calendar cal, int field, int amount) {
		
		// 计算剩余多少分钟
		int leftMin = amount;
		if(Calendar.HOUR == field) {
			leftMin = amount * 60;
		}
		while(leftMin > 0) {
			int currentLeft = 0;
			Calendar amEnd = getAmEnd(cal);
			boolean amflag = cal.compareTo(amEnd) < 0;
			if(amflag) {
				currentLeft = (int)(amEnd.getTimeInMillis() - cal.getTimeInMillis()) / MINUTE_IN_MILLIS;
			} else {
				Calendar pmEnd = getPmEnd(cal);
				currentLeft = (int)(pmEnd.getTimeInMillis() - cal.getTimeInMillis()) / MINUTE_IN_MILLIS;
			}			
			if(leftMin <= currentLeft) {
				cal.add(Calendar.MINUTE, leftMin);
				break;
			}
			
			if(amflag) {
				cal = getPmStart(cal);
			} else {
				cal = getAmStart(cal);
				cal.add(Calendar.DATE, 1);
				skipOutWorkTime(cal);
			}				
			leftMin -= currentLeft;
		}
		return cal;
	}

	/**
	 * 重新设置时间（防止任务创建时间不在上班时间范围内）
	 * @param cal
	 * @return
	 */
	private static void resetTime(Calendar cal) {
		// 上午上班时间
		Calendar amStart = getAmStart(cal);
		
		// 上午下班时间
		Calendar amEnd = getAmEnd(cal);
		
		// 下午上班时间
		Calendar pmStart = getPmStart(cal);
		
		// 下午下班时间
		Calendar pmEnd = getPmEnd(cal);
		
		// 任务在上午上班前创建，开始时间更为上午上班时间
		if(cal.compareTo(amStart) <= 0) {
			cal.setTime(amStart.getTime());
		}// 任务在上午下班后和下午上班前创建，开始时间更为下午上班时间 
		else if(cal.compareTo(amEnd) >= 0 && cal.compareTo(pmStart) <= 0) {
			cal.setTime(pmStart.getTime());
		}// 任务在下午下班后创建，开始时间更为次日上午上班时间
		else if(cal.compareTo(pmEnd) >= 0) {
			cal.setTime(amStart.getTime());
			cal.add(Calendar.DATE, 1);
		}
	}

	/**
	 * 取下午下班时间
	 * @param cal
	 * @return
	 */
	private static Calendar getPmEnd(Calendar cal) {
		Calendar pmEnd = Calendar.getInstance();
		pmEnd.setTime(cal.getTime());
		pmEnd.set(Calendar.HOUR_OF_DAY, WorkTime.PM_END_HOUR);
		pmEnd.set(Calendar.MINUTE, WorkTime.PM_END_MINUTE);
		return pmEnd;
	}

	/**
	 * 取下午上班时间
	 * @param cal
	 * @return
	 */
	private static Calendar getPmStart(Calendar cal) {
		Calendar pmStart = Calendar.getInstance();
		pmStart.setTime(cal.getTime());
		pmStart.set(Calendar.HOUR_OF_DAY, WorkTime.PM_START_HOUR);
		pmStart.set(Calendar.MINUTE, WorkTime.PM_START_MINUTE);
		return pmStart;
	}

	/**
	 * 取上午下班时间
	 * @param cal
	 * @return
	 */
	private static Calendar getAmEnd(Calendar cal) {
		Calendar amEnd = Calendar.getInstance();
		amEnd.setTime(cal.getTime());
		amEnd.set(Calendar.HOUR_OF_DAY, WorkTime.AM_END_HOUR);
		amEnd.set(Calendar.MINUTE, WorkTime.AM_END_MINUTE);
		return amEnd;
	}

	/**
	 * 取上午上班时间
	 * @param cal
	 * @return
	 */
	private static Calendar getAmStart(Calendar cal) {
		Calendar amStart = Calendar.getInstance();
		amStart.setTime(cal.getTime());
		amStart.set(Calendar.HOUR_OF_DAY, WorkTime.AM_START_HOUR);
		amStart.set(Calendar.MINUTE, WorkTime.AM_START_MINUTE);
		return amStart;
	}
	

	/**
	 * 跳过节假日：目前只默认跳过周六、周日。如需对接国家法定节假日，请自行对接
	 * @param cal
	 */
	private static void skipOutWorkTime(Calendar cal) {
		Date startDate = cal.getTime();
		Date endDate = DateUtils.addDays(startDate, 30);
		List<Date> holidays = CalendarUtil.holidays(startDate, endDate);
		// 跳过节假日
		skipHolidays(cal, holidays);
	}

	/**
	 * 跳过节假日
	 * @param cal
	 * @param holidays
	 */
	private static void skipHolidays(Calendar cal, List<Date> holidays) {
		if(holidays.isEmpty()) {
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			if(dayOfWeek == Calendar.SUNDAY) {
				cal.add(Calendar.DATE, 1);
			}else if(dayOfWeek == Calendar.SATURDAY) {
				cal.add(Calendar.DATE, 2);
			}					
		}else {
			while(holidays.contains(DateUtils.truncate(cal.getTime(), Calendar.DATE))) {
				cal.add(Calendar.DATE, 1);
			}
		}
	}
	

}
