package com.atguigu.yygh.hosp.controller;


import com.atguigu.yygh.common.config.result.R;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.StringUtils;
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

    /**
     * @param current 当前页数
     * @param limit   每页展示数量
     * @return 返回分页信息
     */
    @ApiOperation(value = "获取医院分页信息")
    @GetMapping("/{current}/{limit}")
    public R pageList(@PathVariable("current") Integer current, @PathVariable("limit") Integer limit) {
        Page<HospitalSet> page = new Page<>(current, limit);
        hospitalSetService.page(page);
        return R.ok().data("total", page.getTotal()).data("rows", page.getRecords());
    }

    @ApiOperation(value = "根据医院名称获取医院分页信息")
    @PostMapping("/{current}/{limit}")
    public R pageQuery(
            @ApiParam(name = "current", value = "当前页码", required = true)
            @PathVariable("current") Integer current,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable("limit") Integer limit,

            @ApiParam(name = "hospitalSetQueryVo", value = "查询对象")
            @RequestBody HospitalSetQueryVo hospitalSetQueryVo) {

        Page<HospitalSet> page = new Page<>(current, limit);

        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        if (hospitalSetQueryVo != null) {
            String hosname = hospitalSetQueryVo.getHosname();
            String hoscode = hospitalSetQueryVo.getHoscode();
            if (!StringUtils.isEmpty(hosname)) {
                queryWrapper.like("hosname", hosname);
            }
            if (!StringUtils.isEmpty(hoscode)) {
                queryWrapper.eq("hoscode", hoscode);
            }
        }
        hospitalSetService.page(page, queryWrapper);
        return R.ok().data("total", page.getTotal()).data("rows", page.getRecords());
    }

}

