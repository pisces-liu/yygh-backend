package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.config.result.R;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api("医院接口")
@RestController
@RequestMapping("/admin/hosp/hospital")
public class HospitalController {

    @Resource
    private HospitalService hospitalService;

    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public R index(@PathVariable("page") Integer page, @PathVariable("limit") Integer limit, HospitalQueryVo hospitalQueryVo){
        Page<Hospital> hospitalPage = hospitalService.selectPage(page, limit, hospitalQueryVo);
        return R.ok().data("pages", hospitalPage);
    }

    @ApiOperation(value = "更新上线状态")
    @GetMapping("updateStatus/{id}/{status}")
    public R lock(
            @ApiParam(name = "id", value = "医院id", required = true)
            @PathVariable("id") String id,
            @ApiParam(name = "status", value = "状态（0：未上线 1：已上线）", required = true)
            @PathVariable("status") Integer status){
        hospitalService.updateStatus(id, status);
        return R.ok();
    }

    @ApiOperation(value = "获取医院详情")
    @GetMapping("show/{id}")
    public R show(
            @ApiParam(name = "id", value = "医院id", required = true)
            @PathVariable String id) {
        return R.ok().data("hospital",hospitalService.show(id));
    }
}
