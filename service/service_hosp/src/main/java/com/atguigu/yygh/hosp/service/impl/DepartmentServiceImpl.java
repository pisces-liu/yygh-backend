package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.repository.DepartmentRepository;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Resource
    private DepartmentRepository departmentRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        Department department = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Department.class);

        // 一个医院有很多部门。医院的 hoscode 是唯一的，但是 depcode 不是唯一的，所以需要根据 hoscode 和 depcode 一起查询部门信息。
        Department departmentExist = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        //判断
        if (departmentExist != null) {
            department.setCreateTime(departmentExist.getCreateTime());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            department.setId(departmentExist.getId());
            departmentRepository.save(department);
        } else {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }
    }

    @Override
    public Page<Department> selectPage(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo) {

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createTime"));

        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo, department);
        // 将其设为 未锁定
        department.setIsDeleted(0);

        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写

        // 创建实例
        Example<Department> example = Example.of(department, matcher);

        return departmentRepository.findAll(example, pageable);
    }

    @Override
    public void removeDepartment(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (null != department) {
            departmentRepository.delete(department);
        }
    }

    //根据医院编号，查询医院所有科室列表
    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {


        // 1. 根据医院编号，查询医院所有科室信息
        Department departmentQuery = new Department();
        departmentQuery.setHoscode(hoscode);

        Example example = Example.of(departmentQuery);

        // 所有科室列表 departmentList
        List<Department> departmentList = departmentRepository.findAll(example);

        // 2. 组装前端所需的数据，使用 stream()
        //根据大科室编号  bigcode 分组，获取每个大科室里面下级子科室。根据 bigcode 分组，获取数据。
        // key: 大科室编号，value 当前大科室底下的所有子科室列表。
        Map<String, List<Department>> deparmentMap =
                departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));

        // 创建list集合，用于最终数据封装
        List<DepartmentVo> result = new ArrayList<>();

        //遍历map集合 deparmentMap
        for (Map.Entry<String, List<Department>> entry : deparmentMap.entrySet()) {
            //大科室编号
            String bigcode = entry.getKey();
            //大科室编号对应的全局数据
            List<Department> deparment1List = entry.getValue();
            //封装大科室
            DepartmentVo departmentVo1 = new DepartmentVo();
            departmentVo1.setDepcode(bigcode);
            departmentVo1.setDepname(deparment1List.get(0).getBigname());

            //封装小科室
            List<DepartmentVo> children = new ArrayList<>();
            for (Department department : deparment1List) {
                DepartmentVo departmentVo2 = new DepartmentVo();
                departmentVo2.setDepcode(department.getDepcode());
                departmentVo2.setDepname(department.getDepname());
                //封装到list集合
                children.add(departmentVo2);
            }
            //把小科室list集合放到大科室children里面
            departmentVo1.setChildren(children);
            //放到最终result里面
            result.add(departmentVo1);
        }
        //返回
        return result;
    }
}
