package com.atguigu.yygh.hosp.controller.api;


import com.atguigu.yygh.common.config.result.R;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hosp/hospital")
public class HospitalApiController {

    @Autowired
    private HospitalService hospitalService;

    @ApiOperation("获取待查询条件的首页医院列表")
    @GetMapping("/{page}/{limit}")
    public R index(
            @PathVariable("page") Integer page,
            @PathVariable("limit") Integer limit,
            HospitalQueryVo hospitalQueryVo
    ) {
        Page<Hospital> selectPage = hospitalService.selectPage(page, limit, hospitalQueryVo);
        return R.ok().data("pages", selectPage);
    }

    @ApiOperation("根据医院名称进行模糊查询，查询所有的符合条件的医院信息")
    @GetMapping("/findByNameLike/{hosname}")
    public R findByNameLike(@PathVariable("hosname") String name) {
        List<Hospital> byHosname = hospitalService.findByHosname(name);

        return R.ok().data("list", byHosname);
    }
}
