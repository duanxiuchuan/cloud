package com.erongdu.server.generator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.erongdu.common.core.entity.QueryRequest;
import com.erongdu.common.core.entity.system.Column;
import com.erongdu.common.core.entity.system.Table;

import java.util.List;

/**
 * @author erongdu.com
 */
public interface IGeneratorService {

    /**
     * 获取数据库列表
     *
     * @param databaseType databaseType
     * @return 数据库列表
     */
    List<String> getDatabases(String databaseType);

    /**
     * 获取数据表
     *
     * @param tableName    tableName
     * @param request      request
     * @param databaseType databaseType
     * @param schemaName   schemaName
     * @return 数据表分页数据
     */
    IPage<Table> getTables(String tableName, QueryRequest request, String databaseType, String schemaName);

    /**
     * 获取数据表列信息
     *
     * @param databaseType databaseType
     * @param schemaName   schemaName
     * @param tableName    tableName
     * @return 数据表列信息
     */
    List<Column> getColumns(String databaseType, String schemaName, String tableName);

}
