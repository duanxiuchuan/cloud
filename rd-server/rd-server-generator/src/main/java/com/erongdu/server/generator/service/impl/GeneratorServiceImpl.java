package com.erongdu.server.generator.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erongdu.common.core.entity.QueryRequest;
import com.erongdu.common.core.entity.constant.CommonConstant;
import com.erongdu.common.core.entity.system.Column;
import com.erongdu.common.core.entity.system.Table;
import com.erongdu.common.core.utils.SortUtil;
import com.erongdu.server.generator.mapper.GeneratorMapper;
import com.erongdu.server.generator.service.IGeneratorService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author erongdu.com
 */
@Service
@RequiredArgsConstructor
public class GeneratorServiceImpl implements IGeneratorService {

    private final GeneratorMapper generatorMapper;

    @Override
    public List<String> getDatabases(String databaseType) {
        return generatorMapper.getDatabases(databaseType);
    }

    @Override
    public IPage<Table> getTables(String tableName, QueryRequest request, String databaseType, String schemaName) {
        Page<Table> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", CommonConstant.ORDER_ASC, false);
        return generatorMapper.getTables(page, tableName, databaseType, schemaName);
    }

    @Override
    public List<Column> getColumns(String databaseType, String schemaName, String tableName) {
        return generatorMapper.getColumns(databaseType, schemaName, tableName);
    }
}
