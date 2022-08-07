package com.atguigu.yygh.hosp.controller;


import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
@RestController
@RequestMapping("/hosp/hospital-set")
public class HospitalSetController {

    @Resource
    private HospitalSetService hospitalSetService;

    @GetMapping("/findAll")
    public List<HospitalSet> findAll() {
        return hospitalSetService.list();
    }
}

