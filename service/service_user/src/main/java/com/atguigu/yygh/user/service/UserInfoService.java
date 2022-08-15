package com.atguigu.yygh.user.service;

import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.vo.user.LoginVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Liu-Liu
 * @since 2022-08-15
 */
public interface UserInfoService extends IService<UserInfo> {

    Map<String, Object> login(LoginVo loginVo);
}
