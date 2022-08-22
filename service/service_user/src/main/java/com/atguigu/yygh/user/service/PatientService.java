package com.atguigu.yygh.user.service;

import com.atguigu.yygh.model.user.Patient;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 就诊人表 服务类
 * </p>
 *
 * @author Liu-Liu
 * @since 2022-08-22
 */
public interface PatientService extends IService<Patient> {

    /**
     * 通过认证人 id 获取其名下的所有就诊人
     * @param userId 认证用户 id
     * @return 返回就诊人列表
     */
    List<Patient> findAllPatientByUserId(Long userId);

    Patient getPatientById(Long id);
}
