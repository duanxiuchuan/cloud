package com.erongdu.server.activiti.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erongdu.common.core.base.BaseController;
import com.erongdu.common.core.entity.activiti.Formula;
import com.erongdu.server.activiti.service.FormulaService;

/**
 * 表单算式管理
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-8-12
 */
@RestController
@RequestMapping(value = "/actfm")
public class ActFormulaController extends BaseController{
	
    @Resource
    private FormulaService formulaService;

    @PostMapping
    public Object add(@Validated @RequestBody Formula formula) {
        formulaService.insert(formula);
        return success();
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable(value = "id") Long id) {
        formulaService.delete(id);
        return success();
    }

    @PutMapping
    public Object update(@Validated @RequestBody Formula formula) {
        formulaService.update(formula);
        return success();
    }
	
    @GetMapping
	public Object list(@RequestParam Map<String, Object> params, Pageable pageable) {
		return success(formulaService.page(params, pageable));
	}
    
    
    @GetMapping("/formula")
	public Object formula(@RequestParam Map<String, Object> params) {
		return success(formulaService.formula(params));
	}
    
    
}
