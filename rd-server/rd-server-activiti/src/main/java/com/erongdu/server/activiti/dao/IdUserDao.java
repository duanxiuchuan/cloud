package com.erongdu.server.activiti.dao;

import java.util.List;
import java.util.Map;

import com.erongdu.common.core.entity.activiti.IdUser;
import com.erongdu.common.core.entity.system.SystemUser;

/**
 * 
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-7-28
 */
public interface IdUserDao{
	
	/**
     * 根据主键查询数据
     *
     * @param primary 主键值
     *
     * @return 数据结果
     */
    IdUser findById(String id);

    /**
     * 获取一条记录
     *
     * @return 查询结果
     */
    IdUser findSelective(Map<String, Object> params);

    /**
     * 数据查询
     *
     * @param params 查询条件
     *
     * @return 结果集
     */
    List<IdUser> listSelective(Map<String, Object> params);
    
    /**
     * 查询符合条件的记录数
     * @param params
     * @return
     */
    int countSelective(Map<String, Object> params);
    
    /**
     * 查询用户信息
     * @param username
     * @return
     */
    SystemUser findByUsername(String username);

}