package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.config.result.R;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api("医院接口")
@RestController
@RequestMapping("/admin/hosp/hospital")
@CrossOrigin
public class HospitalController {

    @Resource
    private HospitalService hospitalService;

    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public R index(@PathVariable("page") Integer page, @PathVariable("limit") Integer limit, HospitalQueryVo hospitalQueryVo){
        Page<Hospital> hospitalPage = hospitalService.selectPage(page, limit, hospitalQueryVo);
        return R.ok().data("pages", hospitalPage);
    }
}
