package com.erongdu.server.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.erongdu.common.core.entity.QueryRequest;
import com.erongdu.common.core.entity.system.Dept;

import java.util.List;
import java.util.Map;

/**
 * @author erongdu.com
 */
public interface IDeptService extends IService<Dept> {

    /**
     * 获取部门信息
     *
     * @param request request
     * @param dept    dept
     * @return 部门信息
     */
    Map<String, Object> findDepts(QueryRequest request, Dept dept);

    /**
     * 获取部门列表
     *
     * @param dept    dept
     * @param request request
     * @return 部门列表
     */
    List<Dept> findDepts(Dept dept, QueryRequest request);

    /**
     * 创建部门
     *
     * @param dept dept
     */
    void createDept(Dept dept);

    /**
     * 更新部门
     *
     * @param dept dept
     */
    void updateDept(Dept dept);

    /**
     * 删除部门
     *
     * @param deptIds 部门id数组
     */
    void deleteDepts(String[] deptIds);
}
