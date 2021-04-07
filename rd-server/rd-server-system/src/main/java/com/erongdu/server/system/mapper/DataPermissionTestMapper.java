package com.erongdu.server.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erongdu.common.core.entity.system.DataPermissionTest;
import com.erongdu.common.datasource.starter.annotation.DataPermission;

/**
 * @author erongdu.com
 */
@DataPermission(methods = {"selectPage"})
public interface DataPermissionTestMapper extends BaseMapper<DataPermissionTest> {

}
