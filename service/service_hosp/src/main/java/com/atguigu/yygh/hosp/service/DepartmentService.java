package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DepartmentService {
    void save(Map<String, Object> paramMap);

    Page<Department> selectPage(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo);

    // 删除医院部门
    void removeDepartment(String hoscode, String depcode);

    //根据医院编号，查询医院所有科室列表
    List<DepartmentVo> findDeptTree(String hoscode);

    //根据科室编号，和医院编号，查询科室名称
    String getDepName(String hoscode, String depcode);

    /**
     * 根据医院编号 和 科室编号获取科室数据
     */
    Department getDepartment(String hoscode, String depcode);
}
