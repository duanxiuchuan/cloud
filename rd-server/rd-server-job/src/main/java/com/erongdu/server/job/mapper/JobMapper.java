package com.erongdu.server.job.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erongdu.server.job.entity.Job;

import java.util.List;

/**
 * @author erongdu.com
 */
public interface JobMapper extends BaseMapper<Job> {

    /**
     * 获取定时任务列表
     *
     * @return 定时任务列表
     */
    List<Job> queryList();
}