package com.atguigu.yygh.user.controller;


import com.atguigu.yygh.common.config.result.R;
import com.atguigu.yygh.model.user.Patient;
import com.atguigu.yygh.user.service.PatientService;
import com.atguigu.yygh.user.util.AuthContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 就诊人表 前端控制器
 * </p>
 *
 * @author Liu-Liu
 * @since 2022-08-22
 */
@RestController
@RequestMapping("/api/userinfo/patient")
public class PatientController {

    @Resource
    private PatientService patientService;

    // 获取就诊人列表
    @GetMapping("/auth/findAll")
    public R findAll(HttpServletRequest request) {
        // 获取认证人 id
        Long userId = AuthContextHolder.getUserId(request);
        // 通过认证人 id 获取其名下所有的就诊人
        List<Patient> list = patientService.findAllPatientByUserId(userId);
        return R.ok().data("list", list);
    }

    // 添加就诊人
    @PostMapping("/auth/save")
    public R savePatient(@RequestBody Patient patient, HttpServletRequest request) {
        // 获取认证人 id
        Long userId = AuthContextHolder.getUserId(request);
        // 设置就诊人的认证人 id
        patient.setUserId(userId);
        patientService.save(patient);
        return R.ok();
    }

    // 根据 id 获取就诊人信息
    @GetMapping("/auth/get/{id}")
    public R getPatientById(@PathVariable("id") Long id) {
        Patient patient = patientService.getPatientById(id);
        return R.ok().data("patient", patient);
    }

    // 修改就诊人
    @PostMapping("/auth/update")
    public R update(@RequestBody Patient patient) {
        patientService.updateById(patient);
        return R.ok();
    }

    // 删除就诊人
    @DeleteMapping("/auth/remove/{id}")
    public R delete(@PathVariable("id") Long id) {
        patientService.removeById(id);
        return R.ok();
    }

}

