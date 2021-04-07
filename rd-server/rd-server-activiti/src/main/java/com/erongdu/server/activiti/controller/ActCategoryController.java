package com.erongdu.server.activiti.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erongdu.common.core.base.BaseController;
import com.erongdu.common.core.entity.activiti.Category;
import com.erongdu.server.activiti.service.CategoryService;
/**
 * 流程分类管理
 * @author lh@erongdu.com
 * @since  2020年7月21日
 *
 */
@RestController
@RequestMapping(value = "/actcate")
public class ActCategoryController extends BaseController{
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/list")
	public Object list(@RequestParam Map<String, Object> params, Pageable pageable) {
		params.put("deleteFlag", 0);
		return success(categoryService.page(params, pageable));
	}
	
	/**
	 * 字典列表
	 * @param params
	 * @return
	 */
	@GetMapping("/listDict")
	public Object tplList(@RequestParam Map<String, Object> params) {
		List<Category> list = categoryService.list(params);
		Category category = new Category();
		category.setCategory(Category.NAME_ALL);
		category.setRemark(Category.NAME_ALL);
		category.setId(0L);
		list.add(0, category);
		return success(list);
	}
	

	@PostMapping("/save")
	public Object list(@RequestBody Category entity) {
		categoryService.save(entity);
		return success();
	}
	
	@DeleteMapping("/delete")
	public Object delete(Long id) {
		categoryService.delete(id);			
		return success();
	}

}
