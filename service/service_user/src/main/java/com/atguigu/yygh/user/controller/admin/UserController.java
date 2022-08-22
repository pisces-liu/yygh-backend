package com.atguigu.yygh.user.controller.admin;

import com.atguigu.yygh.common.config.result.R;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/admin/userinfo")
public class UserController {

    @Resource
    private UserInfoService userInfoService;

    // 用户列表（带条件分页查询）
    @GetMapping("{page}/{limit}")
    public R list(@PathVariable("page") Integer page,
                  @PathVariable("limit") Integer limit,
                  UserInfoQueryVo userInfoQueryVo) {

        Page<UserInfo> pageParam = new Page<>(page, limit);
        IPage<UserInfo> pageModel = userInfoService.selectPage(pageParam, userInfoQueryVo);
        return R.ok().data("pageModel", pageModel);
    }

    // 锁定用户功能
    @GetMapping("/lock/{userId}/{status}")
    public R lock(@PathVariable("userId") Long userId, @PathVariable("status") Integer status) {
        userInfoService.lock(userId, status);
        return R.ok();
    }

    // 显示用户详情功能
    @GetMapping("/show/{userId}")
    public R show(@PathVariable("userId") Long userId) {
        Map<String, Object> map = userInfoService.show(userId);
        return R.ok().data(map);
    }
}
