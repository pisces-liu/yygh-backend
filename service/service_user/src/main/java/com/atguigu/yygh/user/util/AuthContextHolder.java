package com.atguigu.yygh.user.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取当前用户信息工具类
 */
public class AuthContextHolder {

    // 获取当前用户 id
    public static Long getUserId(HttpServletRequest request) {
        // 从 header 中获取 token
        String token = request.getHeader("token");
        // 使用 JWT 根据从 token 中获取 userId
        return JwtHelper.getUserId(token);
    }

    // 获取当前用户名称
    public static String getUserName(HttpServletRequest request) {
        String token = request.getHeader("token");
        return JwtHelper.getUserName(token);
    }
}
