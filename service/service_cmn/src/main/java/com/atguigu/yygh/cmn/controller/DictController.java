package com.atguigu.yygh.cmn.controller;


import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.common.config.result.R;
import com.atguigu.yygh.model.cmn.Dict;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 组织架构表 前端控制器
 * </p>
 *
 * @author Liu-Liu
 * @since 2022-08-08
 */
@RestController
@RequestMapping("/admin/cmn/dict")
public class DictController {

    @Resource
    private DictService dictService;

    @ApiOperation("根据数据 id 查询子数据列表")
    @GetMapping("/findChildData/{id}")
    public R findChildData(@PathVariable Long id) {
        List<Dict> list = dictService.findChildData(id);
        return R.ok().data("list", list);
    }

    @ApiOperation(value = "导出数据")
    @GetMapping("/exportData")
    public void export(HttpServletResponse response) {
        dictService.exportData(response);
    }

    @ApiOperation("导入数据")
    @PostMapping("/importData")
    public R importDictData(MultipartFile multipartFile) {
        dictService.importDictData(multipartFile);
        return R.ok();
    }

    @ApiOperation(value = "获取数据字典名称")
    @GetMapping(value = "/getName/{parentDictCode}/{value}")
    public String getName(
            @ApiParam(name = "parentDictCode", value = "上级编码", required = true)
            @PathVariable("parentDictCode") String parentDictCode,
            @ApiParam(name = "value", value = "值", required = true)
            @PathVariable("value") String value) {
        return dictService.getNameByParentDictCodeAndValue(parentDictCode, value);
    }

    @ApiOperation(value = "获取数据字典名称")
    @GetMapping(value = "/getName/{value}")
    public String getName(
            @ApiParam(name = "value", value = "值", required = true)
            @PathVariable("value") String value) {
        return dictService.getNameByParentDictCodeAndValue("", value);
    }

    @ApiOperation(value = "根据dictCode获取下级节点")
    @GetMapping(value = "/findByDictCode/{dictCode}")
    public R findByDictCode(
            @ApiParam(name = "dictCode", value = "节点编码", required = true)
            @PathVariable String dictCode) {
        List<Dict> list = dictService.findByDictCode(dictCode);
        return R.ok().data("list", list);
    }
}

