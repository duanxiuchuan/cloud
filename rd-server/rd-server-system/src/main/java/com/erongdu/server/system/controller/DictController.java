package com.erongdu.server.system.controller;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.erongdu.common.tool.domain.Dict;
import com.erongdu.common.tool.service.DictService;

import io.swagger.annotations.*;


/**
* @author lh@erongdu.com
* @date 2020-09-18
*/
@Api(tags = "字典管理管理")
@RestController
@RequestMapping("/dict")
public class DictController {

    private final DictService dictService;

    public DictController(DictService dictService) {
        this.dictService = dictService;
    }

    @GetMapping
    @ApiOperation("查询字典管理")
    @PreAuthorize("hasAuthority('dict:list')")
    public ResponseEntity<Object> getDicts(@RequestParam Map<String, Object> params, Pageable pageable){
        return new ResponseEntity<>(dictService.page(params,pageable),HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("新增字典管理")
    @PreAuthorize("hasAuthority('dict:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Dict resources){
    	dictService.insert(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @ApiOperation("修改字典管理")
    @PreAuthorize("hasAuthority('dict:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Dict resources){
        dictService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除字典管理")
    @PreAuthorize("hasAuthority('dict:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        dictService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}