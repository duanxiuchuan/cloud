package com.erongdu.server.activiti.controller;

import com.erongdu.common.core.base.BaseController;
import com.erongdu.common.core.entity.activiti.Expression;
import com.erongdu.common.core.enums.ExpressionType;
import com.erongdu.server.activiti.service.ExpressionService;

import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.*;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 流程规则管理
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-8-5
 */
@RestController
@RequestMapping(value = "/actexp")
public class ActExpressionController extends BaseController{
    @Resource
    private ExpressionService expressionService;
    
    @GetMapping
    public Object page(@RequestParam Map<String, Object> params, Pageable pageable) {
    	return success(expressionService.page(params, pageable));
    }

    @PostMapping
    public Object save(@Validated @RequestBody Expression expression) {
        expressionService.save(expression);
        return success();
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable(value = "id") Long id) {
        expressionService.delete(id);
        return success();
    }

    @GetMapping("/{id}")
    public Object detail(@PathVariable(value = "id") Long id) {
        return success(expressionService.findById(id));
    }
    
    @GetMapping("/list")
    public Object list(@RequestParam Map<String, Object> params) {
    	if(params == null || !params.containsKey("type")) {
    		return fail("请传入规则类型");
    	}
    	return success(expressionService.list(params));
    }
    
    /**
     * 查询支持的规则类型
     * @return
     */
    @GetMapping("/types")
    public Object types() {
    	return success(ExpressionType.list());
    }
    
}
