package com.atguigu.yygh.user.service;

import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.vo.user.LoginVo;
import com.atguigu.yygh.vo.user.UserAuthVo;
import com.atguigu.yygh.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    /**
     * 带条件分页信息查询
     * @param pageParam 分页信息，含当前页，每页展示数量
     * @param userInfoQueryVo 查询参数携带
     * @return 返回分页对象
     */
    IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo);

    /**
     * 锁定用户功能
     * @param userId 用户id
     * @param status 用户状态
     */
    void lock(Long userId, Integer status);
}
