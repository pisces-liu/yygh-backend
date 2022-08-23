package com.atguigu.yygh.hosp.controller.api;


import com.atguigu.yygh.common.config.result.R;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Api("提供给用户界面使用的医院信息接口")
@RestController
@RequestMapping("/api/hosp/hospital")
public class HospitalApiController {

    @Resource
    private HospitalService hospitalService;

    @Resource
    private DepartmentService departmentService;

    @Resource
    private ScheduleService scheduleService;

    @ApiOperation("获取待查询条件的首页医院列表")
    @GetMapping("/{page}/{limit}")
    public R index(@PathVariable("page") Integer page, @PathVariable("limit") Integer limit,
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

    @ApiOperation(value = "获取可预约排班数据")
    @GetMapping("/auth/getBookingScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public R getBookingSchedule(@PathVariable("page") Integer page,
                                @PathVariable("limit") Integer limit,
                                @PathVariable("hoscode") String hoscode,
                                @PathVariable("depcode") String depcode) {

        Map<String, Object> map = scheduleService.getBookingScheduleRule(page, limit, hoscode, depcode);
        return R.ok().data(map);
    }

    // 查询医生排班信息
    @ApiOperation(value = "获取排班数据")
    @GetMapping("/auth/findScheduleList/{hoscode}/{depcode}/{workDate}")
    public R findScheduleList(@PathVariable(value = "hoscode") String hoscode,
                              @PathVariable(value = "depcode") String depcode,
                              @PathVariable(value = "workDate") String workDate) {
        List<Schedule> scheduleList = scheduleService.getDetailSchedule(hoscode, depcode, workDate);
        return R.ok().data("scheduleList", scheduleList);
    }


}
