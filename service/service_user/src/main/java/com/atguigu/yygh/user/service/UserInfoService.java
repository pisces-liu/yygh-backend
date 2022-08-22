package com.atguigu.yygh.user.service;

import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.vo.user.LoginVo;
import com.atguigu.yygh.vo.user.UserAuthVo;
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

    UserInfo selectWxInfoOpenId(String openid);

    /**
     * 用户认证
     * 方法作用：通过传递的 userId, 查询出用户信息之后，
     * 将 userAuthVo 对象赋给 通过 userId 查询出的 userInfo 对象
     * @param userId 用户 id
     * @param userAuthVo 用户认证信息
     */
    void userAuth(Long userId, UserAuthVo userAuthVo);
}
