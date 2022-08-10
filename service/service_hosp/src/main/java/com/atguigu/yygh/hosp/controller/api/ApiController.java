package com.atguigu.yygh.hosp.controller.api;

import com.atguigu.yygh.common.config.exception.YyghException;
import com.atguigu.yygh.hosp.result.Result;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.hosp.util.HttpRequestHelper;
import com.atguigu.yygh.hosp.util.MD5;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    @Resource
    private HospitalService hospitalService;

    @Resource
    private HospitalSetService hospitalSetService;

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


}
