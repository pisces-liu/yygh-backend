package com.atguigu.yygh.hosp.controller.api;

import com.atguigu.yygh.common.config.exception.YyghException;
import com.atguigu.yygh.hosp.result.Result;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.hosp.util.HttpRequestHelper;
import com.atguigu.yygh.hosp.util.MD5;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp")
@SuppressWarnings("all")
public class ApiController {

    @Resource
    private HospitalService hospitalService;

    @Resource
    private HospitalSetService hospitalSetService;

    @Resource
    private DepartmentService departmentService;


    @ApiOperation(value = "删除科室")
    @PostMapping("/department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());

        String hoscode = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");

        departmentService.removeDepartment(hoscode, depcode);

        return Result.ok();
    }

    @ApiOperation(value = "科室分页信息")
    @PostMapping("/department/list")
    public Result department(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());

        String hoscode = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");

        // 获取分页信息。page 和 limit 信息，均是 hospital_manage 传递过来的，源 json 数据没有 page 和 limit
        int page = Integer.parseInt((String) paramMap.get("page"));
        int limit = Integer.parseInt((String) paramMap.get("limit"));

        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setDepcode(depcode);
        departmentQueryVo.setHoscode(hoscode);

        Page<Department> pageModel = departmentService.selectPage(page, limit, departmentQueryVo);

        return Result.ok(pageModel);

    }

    @ApiOperation(value = "上传科室")
    @PostMapping("/saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        // 参数校验，如果传递的参数没有 hoscode 的话，直接抛出异常
        String hoscode = (String) paramMap.get("hoscode");
        if (ObjectUtils.isEmpty(hoscode)) {
            throw new YyghException(20001, "失败");
        }
        departmentService.save(paramMap);
        return Result.ok();
    }


    @ApiOperation(value = "上传医院")
    @PostMapping("/saveHospital")
    public Result saveHospital(HttpServletRequest request) {
        // getParameterMap 待掌握
        // switchMap： 将 Map<String, String[]> 转为 Map<String, Object>
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());

        // 参数校验，如果传递的参数没有 hoscode 的话，直接抛出异常
        String hoscode = (String) paramMap.get("hoscode");
        if (ObjectUtils.isEmpty(hoscode)) {
            throw new YyghException(20001, "失败");
        }

        // 签名校验
        // 1. 获取医院系统传递过来的签名
        String hospSign = (String) paramMap.get("sign");
        // 2. 获取医院设置的签名，进行校验
        String signKey = hospitalSetService.getSignKey(hoscode);
        // 3. 对医院设置的签名进行加密
        String signKeyMD5 = MD5.encrypt(signKey);
        // 4. 判断签名是否一致，
        if (!(hospSign).equals(signKeyMD5)) {
            throw new YyghException(20001, "校验失败");
        }
        //传输过程中“+”转换为了“ ”，因此我们要转换回来
        String logoData = (String) paramMap.get("logoData");
        logoData = logoData.replaceAll(" ", "+");
        paramMap.put("logoData", logoData);

        hospitalService.save(paramMap);
        return Result.ok();
    }

    @ApiOperation("查询医院")
    @PostMapping("/hospital/show")
    public Result hospital(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        // 参数校验
        String hoscode = (String) paramMap.get("hoscode");
        if (ObjectUtils.isEmpty(hoscode)) {
            throw new YyghException(20001, "失败");
        }
        Hospital hospitalByHoscode = hospitalService.getHospitalByHoscode(hoscode);
        return Result.ok(hospitalByHoscode);
    }

}
