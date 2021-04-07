package com.erongdu.server.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erongdu.common.core.entity.system.GeneratorConfig;

/**
 * @author erongdu.com
 */
public interface IGeneratorConfigService extends IService<GeneratorConfig> {

    /**
     * 查询
     *
     * @return GeneratorConfig
     */
    GeneratorConfig findGeneratorConfig();

    /**
     * 修改
     *
     * @param generatorConfig generatorConfig
     */
    void updateGeneratorConfig(GeneratorConfig generatorConfig);

}
