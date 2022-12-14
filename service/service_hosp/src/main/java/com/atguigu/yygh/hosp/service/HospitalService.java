package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface HospitalService {

    /**
     * 上传医院信息
     */
    void save(Map<String, Object> paramMap);

    Hospital getHospitalByHoscode(String hoscode);

    Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    void updateStatus(String id, Integer status);

    Object show(String id);

    //定义方法 HospService添加医院编号获取医院名称方法
    String getHospName(String hoscode);

    /**
     * 根据医院名称获取医院列表
     */
    List<Hospital> findByHosname(String hosname);

    /**
     * 根据医院编号，获取医院详细信息
     */
    Map<String, Object> item(String hoscode);
}
