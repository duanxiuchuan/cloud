package com.erongdu.server.activiti.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.MapUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erongdu.common.core.entity.activiti.Formula;
import com.erongdu.common.core.enums.FormulaType;
import com.erongdu.server.activiti.dao.FormulaDao;
import com.erongdu.server.activiti.model.form.ColFormula;
import com.erongdu.server.activiti.model.form.RowFormula;
import com.erongdu.server.activiti.service.FormulaService;
import com.erongdu.server.activiti.util.PageableUtil;

/**
 * 表单算式
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-8-12
 */
 
@Service("formulaService") 
public class FormulaServiceImpl implements FormulaService{
	
    @Resource
    private FormulaDao formulaDao;

	@Override
	public int insert(Formula entity) {
		entity.setCreateTime(new Date());
		return formulaDao.insert(entity);
	}
	
	@Override
	public void insertBatch(List<Formula> list) {
		formulaDao.insertBatch(list);
	}

	@Override
	public int delete(Long id) {
		return formulaDao.deleteById(id);
	}

	@Override
	public int update(Formula entity) {
		return formulaDao.update(entity);
	}

	@Override
	public Map<String,Object> page(Map<String, Object> params, Pageable pageable) {
		IPage<Formula> viewPage = new Page<>(pageable.getPageNumber(), pageable.getPageSize());	
		IPage<Formula> result = (IPage<Formula>)formulaDao.pageSelective(viewPage, params);
        return PageableUtil.toPage(result.getRecords(), result.getTotal());
	}
	
	@Override
	public List<Formula> list(Map<String, Object> params) {
		return formulaDao.listSelective(params);
	}

	@Override
	public Formula findById(Long id) {
		return formulaDao.findById(id);
	}
	
	@Override
	public List<Object> formula(Map<String, Object> params) {
		List<Object> result = new ArrayList<Object>();
		List<Formula> all = formulaDao.listSelective(params);
		switch (FormulaType.of(MapUtils.getString(params, "type"))) {
			case COL:
				List<ColFormula> colf = new ArrayList<ColFormula>();
				for (Formula formula : all) {
					ColFormula col = JSONObject.parseObject(formula.getExpression(), ColFormula.class);
					col.setId(formula.getId());
					colf.add(col);
				}
				result.addAll(colf);
				break;
			case ROW:
				List<RowFormula> rowf = new ArrayList<RowFormula>();
				for (Formula formula : all) {
					RowFormula rf = JSONObject.parseObject(formula.getExpression(), RowFormula.class);
					rf.setId(formula.getId());
					rowf.add(rf);
				}
				// 按步骤排序（冒泡排序）算式
				int len = rowf.size();
				for (int i = 0; i < len; i++) {
					for (int j = 0; j < len-i-1; j++) {
						final RowFormula rowj = rowf.get(j);
						final RowFormula rowjp = rowf.get(j+1);
						if(rowjp.getResultField().equals(rowj.getOperateField1()) || 
						   rowjp.getResultField().equals(rowj.getOperateField2())) {
							rowf.set(j, rowjp);
							rowf.set(j+1, rowj);
						}
					}
				}
				result.addAll(rowf);
				break;
		}
		return result;		
	}
	
	@Override
	public int deleteByFormtplId(Long formtplId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("formtplId", formtplId);
		return formulaDao.deleteByParams(params);
	}
}