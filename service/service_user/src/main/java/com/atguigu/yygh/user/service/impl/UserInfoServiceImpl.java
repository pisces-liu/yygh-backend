package com.atguigu.yygh.user.service.impl;

import com.atguigu.yygh.common.config.exception.YyghException;
import com.atguigu.yygh.model.acl.User;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.mapper.UserInfoMapper;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.user.util.JwtHelper;
import com.atguigu.yygh.vo.user.LoginVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Liu-Liu
 * @since 2022-08-15
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Map<String, Object> login(LoginVo loginVo) {
        // 获取用户手机号
        String phone = loginVo.getPhone();
        // 获取用户验证码
        String code = loginVo.getCode();

        // 校验参数
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            throw new YyghException(20001, "数据为空");
        }

        // TODO 使用 redis 校验验证码
        String redisCode = redisTemplate.opsForValue().get(phone);
        if (code.equals(redisCode)) {
            throw new YyghException(20001, "验证码不正确");
        }

        // 查询数据库，判断用户是否为新用户
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);

        UserInfo userInfo = baseMapper.selectOne(queryWrapper);

        // 对用户进行判断是否为空
        if (null == userInfo) {
            // 如果用户为空，则进行增加操作
            userInfo = new UserInfo();
            userInfo.setName("");
            userInfo.setPhone(phone);
            userInfo.setCreateTime(new Date());
            userInfo.setStatus(1);
            this.save(userInfo);
        }

        // 当用户已存在时，进行用户状态校验
        if (userInfo.getStatus() == 0) {
            throw new YyghException(20001, "用户已被禁用");
        }
        // 返回用户信息
        HashMap<String, Object> info = new HashMap<>();

        // 如果 name 为空的话，就将 nickName 作为 name 进行返回
        String name = userInfo.getName();
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }

        // 如果 name 为空的话，就将 phone 作为 name 进行返回
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }

        info.put("name", name);
        // JWT 生成 token 字符串。数据库中 id 字段类型为 bigint 自增
        String token = JwtHelper.createToken(userInfo.getId(), name);
        info.put("token", token);

        return info;
    }
}
