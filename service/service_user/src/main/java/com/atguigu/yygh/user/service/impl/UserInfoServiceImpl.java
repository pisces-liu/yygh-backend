package com.atguigu.yygh.user.service.impl;

import com.atguigu.yygh.common.config.exception.YyghException;
import com.atguigu.yygh.enums.AuthStatusEnum;
import com.atguigu.yygh.model.acl.User;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.mapper.UserInfoMapper;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.user.util.JwtHelper;
import com.atguigu.yygh.vo.user.LoginVo;
import com.atguigu.yygh.vo.user.UserAuthVo;
import com.atguigu.yygh.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
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
@SuppressWarnings("all")
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Map<String, Object> login(LoginVo loginVo) {
        // 1 获取输入手机号和验证码
        String phone = loginVo.getPhone();
        // 获取用户验证码
        String code = loginVo.getCode();

        // 2 手机号和验证码非空校验
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            throw new YyghException(20001, "数据为空");
        }

        // 验证码校验 输入的验证码 和 存储redis验证码比对
        String redisCode = redisTemplate.opsForValue().get(phone);
        assert redisCode != null;
        if (!redisCode.equals(code)) {
            throw new YyghException(20001, "验证码校验失败");
        }

        // 获取用户 openid
        String openid = loginVo.getOpenid();
        // 创建 map 对象，存储用户信息
        Map<String, Object> map = new HashMap<>();
        // 判断用户是否含有 openid，也就是判断用户是否进行过微信登录
        if (StringUtils.isEmpty(openid)) {
            /*
             * 此时用户第一次进行微信登录，再在数据库中对用户进行手机号查询，判断用户是否进行过手机登录、
             * 将用户信息进行查询，并保存在 map 对象中。
             * 此时，我们已经创建了一个用户对象。接下来，将用户通过手机登录的信息与通过微信登录的信息进行合并
             */
            // 查询数据库，判断用户是否为新用户
            QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phone", phone);

            UserInfo userInfo = baseMapper.selectOne(queryWrapper);

            // 如果返回对象为空，就是第一次登录，存到数据库登录数据 对用户进行判断是否为空
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

            map = get(userInfo);
        } else {
            // 1. 创建最终进行保存用户信息对象。用于将使用手机登录的信息与使用微信登录的信息进行合并
            UserInfo finalUserInfo = new UserInfo();

            // 2. 使用手机进行查询，如果查询到手机号对应的信息，将其封装到 finalUserInfo 中
            QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phone", phone);
            UserInfo userInfoByPhone = baseMapper.selectOne(queryWrapper);
            // 判断通过手机号查询结果不为空的情况，不为空，则进行数据合并
            if (userInfoByPhone != null) {
                // 如果查询手机号对应数据,封装到userInfoFinal
                BeanUtils.copyProperties(userInfoByPhone, finalUserInfo);
                // 删除数据库中通过手机号注册的信息
                baseMapper.delete(queryWrapper);
            }

            // 3. 使用 openid 进行查询信息，如果查询到相应信息，将其也封装到 finalUserInfo 中
            UserInfo userInfoByOpenId = baseMapper.selectOne(new QueryWrapper<UserInfo>().eq("openid", openid));

            // 4. 将通过微信 openid 查询出来的信息中的属性保存到 finalUserInfo 中
            finalUserInfo.setOpenid(userInfoByOpenId.getOpenid());
            finalUserInfo.setNickName(userInfoByOpenId.getNickName());
            finalUserInfo.setId(userInfoByOpenId.getId());

            // 判断通过手机号查询结果为空的情况，如果为空，则为通过微信 openid 查询出来的信息设置手机号
            if (userInfoByPhone == null) {
                finalUserInfo.setPhone(phone);
                finalUserInfo.setStatus(userInfoByOpenId.getStatus());
            }
            // 修改手机号
            baseMapper.updateById(finalUserInfo);

            //5 判断用户是否锁定
            if (finalUserInfo.getStatus() == 0) {
                throw new YyghException(20001, "用户被锁定");
            }

            map = get(finalUserInfo);

        }


        return map;
    }

    /**
     * 将用户信息转换为 map 对象
     */
    private HashMap<String, Object> get(UserInfo userInfo) {
        HashMap<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        map.put("name", name);
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token", token);
        return map;
    }

    @Override
    public UserInfo selectWxInfoOpenId(String openid) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        return baseMapper.selectOne(queryWrapper);
    }

    // 用户认证
    @Override
    public void userAuth(Long userId, UserAuthVo userAuthVo) {
        UserInfo userInfo = baseMapper.selectById(userId);
        // 对用户数据进行更新
        userInfo.setName(userAuthVo.getName());
        userInfo.setCertificatesType(userAuthVo.getCertificatesType());
        userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
        userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
        // 对数据进行更新
        baseMapper.updateById(userInfo);
    }

    @Override
    public IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo) {
        //UserInfoQueryVo获取条件值
        String name = userInfoQueryVo.getKeyword(); //用户名称
        Integer status = userInfoQueryVo.getStatus();//用户状态
        Integer authStatus = userInfoQueryVo.getAuthStatus(); //认证状态
        String createTimeBegin = userInfoQueryVo.getCreateTimeBegin(); //开始时间
        String createTimeEnd = userInfoQueryVo.getCreateTimeEnd(); //结束时间
        //对条件值进行非空判断
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name).or().eq("phone", name);
        }
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("status", status);
        }
        if (!StringUtils.isEmpty(authStatus)) {
            wrapper.eq("auth_status", authStatus);
        }
        if (!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge("create_time", createTimeBegin);
        }
        if (!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le("create_time", createTimeEnd);
        }
        //调用mapper的方法
        IPage<UserInfo> pages = baseMapper.selectPage(pageParam, wrapper);
        //编号变成对应值封装
        pages.getRecords().stream().forEach(item -> {
            this.packageUserInfo(item);
        });
        return pages;
    }

    //编号变成对应值封装
    private UserInfo packageUserInfo(UserInfo userInfo) {
        //处理认证状态编码
        userInfo.getParam().put("authStatusString", AuthStatusEnum.getStatusNameByStatus(userInfo.getAuthStatus()));
        //处理用户状态 0  1
        String statusString = userInfo.getStatus().intValue() == 0 ? "锁定" : "正常";
        userInfo.getParam().put("statusString", statusString);
        return userInfo;
    }
}
