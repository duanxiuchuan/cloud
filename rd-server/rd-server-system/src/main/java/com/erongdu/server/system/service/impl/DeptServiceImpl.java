package com.erongdu.server.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erongdu.common.core.entity.DeptTree;
import com.erongdu.common.core.entity.QueryRequest;
import com.erongdu.common.core.entity.Tree;
import com.erongdu.common.core.entity.constant.CommonConstant;
import com.erongdu.common.core.entity.constant.PageConstant;
import com.erongdu.common.core.entity.system.Dept;
import com.erongdu.common.core.utils.SortUtil;
import com.erongdu.common.core.utils.TreeUtil;
import com.erongdu.server.system.mapper.DeptMapper;
import com.erongdu.server.system.service.IDeptService;
import com.erongdu.server.system.service.IUserDataPermissionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author erongdu.com
 */
@Slf4j
@Service("deptService")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

    private final IUserDataPermissionService userDataPermissionService;

    @Override
    public Map<String, Object> findDepts(QueryRequest request, Dept dept) {
        Map<String, Object> result = new HashMap<>(2);
        try {
            List<Dept> depts = findDepts(dept, request);
            List<DeptTree> trees = new ArrayList<>();
            buildTrees(trees, depts);
            List<? extends Tree<?>> deptTree = TreeUtil.build(trees);

            result.put(PageConstant.ROWS, deptTree);
            result.put(PageConstant.TOTAL, depts.size());
        } catch (Exception e) {
            log.error("获取部门列表失败", e);
            result.put(PageConstant.ROWS, null);
            result.put(PageConstant.TOTAL, 0);
        }
        return result;
    }

    @Override
    public List<Dept> findDepts(Dept dept, QueryRequest request) {
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(dept.getDeptName())) {
            queryWrapper.lambda().like(Dept::getDeptName, dept.getDeptName());
        }
        if (StringUtils.isNotBlank(dept.getCreateTimeFrom()) && StringUtils.isNotBlank(dept.getCreateTimeTo())) {
            queryWrapper.lambda()
                    .ge(Dept::getCreateTime, dept.getCreateTimeFrom())
                    .le(Dept::getCreateTime, dept.getCreateTimeTo());
        }
        SortUtil.handleWrapperSort(request, queryWrapper, "orderNum", CommonConstant.ORDER_ASC, true);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDept(Dept dept) {
        if (dept.getParentId() == null) {
            dept.setParentId(Dept.TOP_DEPT_ID);
        }
        dept.setCreateTime(new Date());
        this.save(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDept(Dept dept) {
        if (dept.getParentId() == null) {
            dept.setParentId(Dept.TOP_DEPT_ID);
        }
        dept.setModifyTime(new Date());
        this.baseMapper.updateById(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDepts(String[] deptIds) {
        this.delete(Arrays.asList(deptIds));
    }

    private void buildTrees(List<DeptTree> trees, List<Dept> depts) {
        depts.forEach(dept -> {
            DeptTree tree = new DeptTree();
            tree.setId(dept.getDeptId().toString());
            tree.setParentId(dept.getParentId().toString());
            tree.setLabel(dept.getDeptName());
            tree.setOrderNum(dept.getOrderNum());
            trees.add(tree);
        });
    }

    private void delete(List<String> deptIds) {
        removeByIds(deptIds);
        userDataPermissionService.deleteByDeptIds(deptIds);

        LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dept::getParentId, deptIds);
        List<Dept> depts = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(depts)) {
            List<String> deptIdList = new ArrayList<>();
            depts.forEach(d -> deptIdList.add(String.valueOf(d.getDeptId())));
            this.delete(deptIdList);
        }
    }

}
