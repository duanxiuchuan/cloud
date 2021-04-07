package com.erongdu.server.system.controller;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.erongdu.common.tool.domain.DictDetail;
import com.erongdu.common.tool.service.DictDetailService;

import io.swagger.annotations.*;


/**
* @author lh@erongdu.com
* @date 2020-09-18
*/
@Api(tags = "字典项管理管理")
@RestController
@RequestMapping("/dictDetail")
public class DictDetailController {

    private final DictDetailService dictDetailService;

    public DictDetailController(DictDetailService dictDetailService) {
        this.dictDetailService = dictDetailService;
    }

    @GetMapping
    @ApiOperation("查询字典项管理")
    @PreAuthorize("hasAuthority('dictDetail:list')")
    public ResponseEntity<Object> getDictDetails(@RequestParam Map<String, Object> params, Pageable pageable){
        return new ResponseEntity<>(dictDetailService.page(params,pageable),HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("新增字典项管理")
    @PreAuthorize("hasAuthority('dictDetail:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody DictDetail resources){
    	dictDetailService.insert(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @ApiOperation("修改字典项管理")
    @PreAuthorize("hasAuthority('dictDetail:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody DictDetail resources){
        dictDetailService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除字典项管理")
    @PreAuthorize("hasAuthority('dictDetail:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        dictDetailService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}