package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 医院设置表 服务类
 * </p>
 *
 * @author Liu-Liu
 * @since 2022-08-07
 */
public interface HospitalSetService extends IService<HospitalSet> {
    /**
     * 获取签名key
     */
    String getSignKey(String hoscode);
}
