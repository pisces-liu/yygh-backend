package com.atguigu.yygh.hosp.service.impl;

import com.atguigu.yygh.common.config.exception.YyghException;
import com.atguigu.yygh.hosp.mapper.HospitalSetMapper;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <h1>
 * 医院设置表 服务实现类
 * </h1>
 *
 * @author Liu-Liu
 * @since 2022-08-07
 */
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {


    @Override
    public String getSignKey(String hoscode) {
        // 通过 hoscode 获取 医院预约设置对象
        HospitalSet hospitalSet = this.getByHoscode(hoscode);
        // 对 对象进行判空操作, 避免获取 signKey 时，出现空指针异常问题
        if (null == hospitalSet) {
            throw new YyghException(20001, "失败");
        }
        return hospitalSet.getSignKey();
    }

    private HospitalSet getByHoscode(String hoscode) {
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hoscode", hoscode);
        return baseMapper.selectOne(queryWrapper);
    }
}
