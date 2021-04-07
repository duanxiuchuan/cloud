package com.erongdu.server.activiti.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.RepositoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erongdu.common.core.entity.activiti.Category;
import com.erongdu.server.activiti.dao.CategoryDao;
import com.erongdu.server.activiti.service.CategoryService;
import com.erongdu.server.activiti.util.PageableUtil;

/**
 * 流程分类管理
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-7-21
 */
 
@Service("categoryService") 
public class CategoryServiceImpl implements CategoryService{
	
    @Resource
    private CategoryDao categoryDao;
	@Autowired
	private RepositoryService repositoryService;

	@Override
	public int save(Category entity) {		
		if(StringUtils.isEmpty(entity.getCategory())) {
			throw new RuntimeException("流程分类名称不能为空");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deleteFlag", 0);
		params.put("category", entity.getCategory());
		if(entity.getId() != null) {
			params.put("nid", entity.getId());
		}
		int count = categoryDao.countSelective(params);
		if(count > 0) {
			throw new RuntimeException("流程分类名称重复");
		}
		
		if(entity.getId() == null) {
			entity.setDeleteFlag(false);
			return categoryDao.insert(entity);
		}else {
			Category dbCate = findById(entity.getId());
			if(!dbCate.getCategory().equals(entity.getCategory())) {
				long pdcount = repositoryService.createProcessDefinitionQuery().processDefinitionCategory(dbCate.getCategory()).count();
				if(pdcount > 0) {
					throw new RuntimeException("该流程分类已绑定流程定义，不能修改名称");
				}
			}
			return categoryDao.update(entity);
		}
	}

	@Override
	public int delete(Long id) {
		if(id == null) {
			return 0;
		}
		Category dbCate = findById(id);
		if(dbCate != null && dbCate.getId() != null) {
			long pdcount = repositoryService.createProcessDefinitionQuery().processDefinitionCategory(dbCate.getCategory()).count();
			if(pdcount > 0) {
				throw new RuntimeException("该流程分类已绑定流程定义，不能删除");
			}
		}
		return categoryDao.deleteById(id);
	}

	@Override
	public Map<String,Object> page(Map<String, Object> params, Pageable pageable) {
		IPage<Category> viewPage = new Page<>(pageable.getPageNumber(), pageable.getPageSize());	
    	IPage<Category> result = categoryDao.pageSelective(viewPage, params);
        return PageableUtil.toPage(result.getRecords(), result.getTotal());
	}
	
	@Override
	public List<Category> list(Map<String, Object> params) {
		return categoryDao.listSelective(params);
	}

	@Override
	public Category findById(Long id) {
		return categoryDao.findById(id);
	}
}