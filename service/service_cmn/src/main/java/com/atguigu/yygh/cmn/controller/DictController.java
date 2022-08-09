package com.atguigu.yygh.cmn.controller;


import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.common.config.result.R;
import com.atguigu.yygh.model.cmn.Dict;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

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
@CrossOrigin
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
}

