package com.atguigu.msmservice.controller;

import com.atguigu.msmservice.service.MsmService;
import com.atguigu.yygh.common.config.result.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/msm")
public class MsmApiController {

    @Resource
    private MsmService msmService;


    @PostMapping("/send/{phone}")
    public R sendMsm(@PathVariable("phone") String phone) {

        boolean flag = msmService.sendCode(phone);
        if (flag) {
            return R.ok();
        } else {
            return R.error().message("验证码发送失败");
        }
    }
}
