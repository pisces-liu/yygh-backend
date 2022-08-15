package com.atguigu.yygh.user.controller;


import com.atguigu.yygh.common.config.result.R;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.LoginVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author Liu-Liu
 * @since 2022-08-15
 */
@RestController
@RequestMapping("/user/userinfo")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @ApiOperation("会员登录")
    @PostMapping("/login") // 用户登录的时候，有可能也是新用户，那就是注册操作，所以这里使用 post 操作。
    // 使用 pojo 对象接受用户手机号和接收到的验证码
    public R login(@RequestBody LoginVo loginVo) {
        // 登录成功之后，返回一些用户信息，例如用户名，头像等。讲这些信息封装到 map 中。
        Map<String, Object> info = userInfoService.login(loginVo);
        return R.ok().data(info);
    }


}

