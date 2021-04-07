package com.erongdu.server.system.controller;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.erongdu.common.tool.domain.Config;
import com.erongdu.common.tool.service.ConfigService;

import io.swagger.annotations.*;


/**
* @author lh@erongdu.com
* @date 2020-09-18
*/
@Api(tags = "配置管理管理")
@RestController
@RequestMapping("/config")
public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @GetMapping
    @ApiOperation("查询配置管理")
    @PreAuthorize("hasAuthority('config:list')")
    public ResponseEntity<Object> getConfigs(@RequestParam Map<String, Object> params, Pageable pageable){
        return new ResponseEntity<>(configService.page(params,pageable),HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("新增配置管理")
    @PreAuthorize("hasAuthority('config:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Config resources){
    	configService.insert(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @ApiOperation("修改配置管理")
    @PreAuthorize("hasAuthority('config:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Config resources){
        configService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除配置管理")
    @PreAuthorize("hasAuthority('config:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        configService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}