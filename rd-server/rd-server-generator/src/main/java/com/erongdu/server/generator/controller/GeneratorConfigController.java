package com.erongdu.server.generator.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erongdu.common.core.entity.CommonResponse;
import com.erongdu.common.core.entity.system.GeneratorConfig;
import com.erongdu.common.core.exception.CommonException;
import com.erongdu.server.generator.service.IGeneratorConfigService;

import javax.validation.Valid;

/**
 * @author erongdu.com
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("config")
public class GeneratorConfigController {

    private final IGeneratorConfigService generatorConfigService;

    @GetMapping
    @PreAuthorize("hasAuthority('gen:config')")
    public CommonResponse getGeneratorConfig() {
        return new CommonResponse().data(generatorConfigService.findGeneratorConfig());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('gen:config:update')")
    public void updateGeneratorConfig(@Valid GeneratorConfig generatorConfig) throws CommonException {
        if (StringUtils.isBlank(generatorConfig.getId())) {
            throw new CommonException("配置id不能为空");
        }
        this.generatorConfigService.updateGeneratorConfig(generatorConfig);
    }
}
