package com.atguigu.yygh.user.controller;


import com.atguigu.yygh.common.config.result.R;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.user.util.AuthContextHolder;
import com.atguigu.yygh.vo.user.LoginVo;
import com.atguigu.yygh.vo.user.UserAuthVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/userinfo")
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

    // 用户认证接口
    @PostMapping("/auth/userAuth")
    public R userAuth(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request) {
        // 传递两个参数，第一个参数用户id，第二个参数认证数据 vo 对象
        userInfoService.userAuth(AuthContextHolder.getUserId(request), userAuthVo);
        return R.ok();
    }

    // 获取用户 id 信息接口
    @GetMapping("/auth/getUserInfo")
    public R getUserInfo(HttpServletRequest request) {
        // 通过 AuthContextHolder 根据类获取用户 id
        Long userId = AuthContextHolder.getUserId(request);
        // 通过用户 id 查询用户信息
        UserInfo userInfo = userInfoService.getById(userId);
        // 将用户信息进行返回
        return R.ok().data("userInfo", userInfo);
    }


}

