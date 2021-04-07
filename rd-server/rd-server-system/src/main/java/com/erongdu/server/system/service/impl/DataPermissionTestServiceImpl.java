package com.erongdu.server.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erongdu.common.core.entity.QueryRequest;
import com.erongdu.common.core.entity.system.DataPermissionTest;
import com.erongdu.server.system.mapper.DataPermissionTestMapper;
import com.erongdu.server.system.service.IDataPermissionTestService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author erongdu.com
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DataPermissionTestServiceImpl extends ServiceImpl<DataPermissionTestMapper, DataPermissionTest> implements IDataPermissionTestService {

    @Override
    public IPage<DataPermissionTest> findDataPermissionTests(QueryRequest request, DataPermissionTest dataPermissionTest) {
        LambdaQueryWrapper<DataPermissionTest> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(DataPermissionTest::getCreateTime);
        Page<DataPermissionTest> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }
}
