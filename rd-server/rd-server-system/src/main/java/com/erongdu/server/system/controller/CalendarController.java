package com.erongdu.server.system.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erongdu.common.core.base.BaseController;
import com.erongdu.common.tool.domain.Calendar;
import com.erongdu.common.tool.service.CalendarService;

/**
 * 节假日管理
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-8-21
 */
@RestController
@RequestMapping(value = "/actcal")
public class CalendarController  extends BaseController{
    @Resource
    private CalendarService calendarService;

    @PostMapping("/init/{year}")
    public Object add(@PathVariable int year) {
    	calendarService.initWeekend(year);
    	return success();
    }
    
    @PostMapping
    public Object add(@Validated @RequestBody Calendar calendar) {
    	calendarService.save(calendar);
    	return success();
    }

    @DeleteMapping("/{date}")
    public Object delete(@PathVariable(value = "date") String date) {
        try {
        	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			calendarService.deleteByDate(format.parse(date));
		} catch (ParseException e) {
			return fail();
		}
        return success();
    }
	
    @GetMapping
	public Object list(@RequestParam Map<String, Object> params) {
		return success(calendarService.list(params));
	}
}
