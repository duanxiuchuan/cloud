package com.erongdu.server.activiti.controller;

import java.util.HashMap;
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
import com.erongdu.common.core.entity.activiti.Forminit;
import com.erongdu.common.core.enums.ExpressionType;
import com.erongdu.server.activiti.service.ExpressionService;
import com.erongdu.server.activiti.service.ForminitService;

/**
 * 表单预设
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-8-7
 */
@RestController
@RequestMapping(value = "/actfi")
public class ActForminitController extends BaseController{
    @Resource
    private ForminitService forminitService;
	@Resource
	private ExpressionService expressionService;

	/**
	 * 
	 * @param forminit
	 * @return
	 */
    @PostMapping
    public Object add(@Validated @RequestBody Forminit forminit) {
        forminitService.insert(forminit);
        return success();
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable(value = "id") Long id) {
        forminitService.delete(id);
        return success();
    }

    @PutMapping
    public Object update(@Validated @RequestBody Forminit forminit) {
        forminitService.update(forminit);
        return success();
    }

    @GetMapping("/{id}")
    public Object detail(@PathVariable(value = "id") Long id) {
        return success(forminitService.findById(id));
    }

	
    @GetMapping
	public Object list(@RequestParam Map<String, Object> params, Pageable pageable) {
		return success(forminitService.page(params, pageable));
	}
    
	
	/**
	 * 获取系统预设表单变量
	 * @return
	 */
	@GetMapping("/paramCfg")
	public Object paramCfg() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", ExpressionType.FORMVAR.getCode());
		return success(expressionService.list(params));
	}
	
	
}
