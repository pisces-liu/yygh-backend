package com.atguigu.yygh.hosp.controller;


import com.atguigu.yygh.common.config.result.R;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 医院设置表 前端控制器
 * </p>
 *
 * @author Liu-Liu
 * @since 2022-08-07
 */
@Api(tags = "预约设置接口")
@RestController
@RequestMapping("/hosp/hospital-set")
public class HospitalSetController {

    @Resource
    private HospitalSetService hospitalSetService;

    @ApiOperation(value = "查询所有医院信息")
    @GetMapping("/findAll")
    public R findAll() {
        List<HospitalSet> list = hospitalSetService.list();
        return R.ok().data("list", list);
    }

    @ApiOperation(value = "根据医院 id 删除医院信息")
    @DeleteMapping("/remove/{id}")
    public R removeById(@ApiParam(name = "id", value = "医院设置ID", required = true) @PathVariable("id") String id) {
        boolean b = hospitalSetService.removeById(id);
        if (b) {
            return R.ok();
        } else {
            return R.error();
        }
    }

}

