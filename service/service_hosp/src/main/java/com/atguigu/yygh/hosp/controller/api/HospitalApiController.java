package com.atguigu.yygh.hosp.controller.api;


import com.atguigu.yygh.common.config.result.R;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp/hospital")
public class HospitalApiController {

    @Resource
    private HospitalService hospitalService;

    @Resource
    private DepartmentService departmentService;

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


    @ApiOperation("获取科室列表")
    @GetMapping("/department/{hoscode}")
    public R index(@PathVariable("hoscode") String hoscode) {
        List<DepartmentVo> deptTree = departmentService.findDeptTree(hoscode);
        return R.ok().data("list", deptTree);
    }

    @ApiOperation("医院预约挂号详情")
    @GetMapping("/{hoscode}")
    public R item(@PathVariable("hoscode") String hoscode) {
        Map<String, Object> item = hospitalService.item(hoscode);
        return R.ok().data(item);
    }

}
