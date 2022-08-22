package com.atguigu.yygh.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.config.result.R;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.user.util.ConstantPropertiesUtil;
import com.atguigu.yygh.user.util.HttpClientUtils;
import com.atguigu.yygh.user.util.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/ucenter/wx")
public class WeixinApiController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private RedisTemplate redisTemplate;


    @GetMapping("/callback")
    public String callBack(String code, String state) {
        // System.out.println("state = " + state);

        // 1. 获取临时票据 code
        // System.out.println("code:" + code);

        // 2. 拿着 code 和微信 id 和秘钥，请求微信固定地址，得到两个值。使用 code 和 appid 以及 app_secret 获取 access_token
        // %s 占位符
        StringBuilder baseAccessTokenUrl = new StringBuilder().append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&code=%s")
                .append("&grant_type=authorization_code");

        String accessTokenUrl = String.format(
                baseAccessTokenUrl.toString(),
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code);

        try {
            // 使用httpclient请求这个地址
            String jsonStr = HttpClientUtils.get(accessTokenUrl);
            // 从返回字符串获取两个值 openid  和  access_token
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            // 获取 access_token
            String accessToken = jsonObject.getString("access_token");
            // 获取 openid
            String openid = jsonObject.getString("openid");

            // 判断数据库是否存在微信的扫描人信息
            UserInfo userInfo = userInfoService.selectWxInfoOpenId(openid);
            // 根据openid判断
            if (userInfo == null) {
                // 如果数据库中不存在该扫描人，通过请求微信地址，得到扫描人信息
                StringBuilder baseUserInfoUrl = new StringBuilder();
                baseUserInfoUrl.append("https://api.weixin.qq.com/sns/userinfo")
                        .append("?access_token=%s")
                        .append("&openid=%s");
                String userInfoUrl = String.format(String.valueOf(baseUserInfoUrl), accessToken, openid);
                // 通过 httpclient 发送请求获取扫描人信息
                String userInfoStr = HttpClientUtils.get(userInfoUrl);
                // 将获取的 string 转换为 object
                JSONObject userInfoObj = JSONObject.parseObject(userInfoStr);

                // 解析用户信息，获取用户昵称
                String userNickname = userInfoObj.getString("nickname");
                // 解析用户信息，获取用户头像
                // String userHeadimgurl = userInfoObj.getString("headimgurl");

                // 将信息添加至数据库中
                userInfo = new UserInfo();
                userInfo.setNickName(userNickname);
                userInfo.setOpenid(openid);
                userInfo.setStatus(1);
                userInfoService.save(userInfo);

            }

            // --- 当数据库中含有用户信息时，则获取用户信息
            //返回name和token字符串
            HashMap<String, String> map = new HashMap<>();
            // 获取用户姓名
            String name = userInfo.getName();
            // 如果用户名称为空，则将昵称保存为姓名
            if (StringUtils.isEmpty(name)) {
                name = userInfo.getNickName();
            }
            // 如果用户名称为空，则将手机号保存为姓名
            if (StringUtils.isEmpty(name)) {
                name = userInfo.getPhone();
            }
            // 将姓名保存进 map 中
            map.put("name", name);

            /*
             * 判断 userInfo 时候含有手机号，如果手机号为空，返回 openid,
             * 如果手机号不为空，返回 openid 值是空字符串
             *
             * 前端在显示用户名称时，会进行以下判断：
             * 如果 openid 不为空，则显示绑定的手机号，
             * 如果 openid 为空，显示绑定的手机号
             */
            if (StringUtils.isEmpty(userInfo.getPhone())) {
                map.put("openid", userInfo.getOpenid());
            } else {
                map.put("openid", "");
            }

            // 使用 jwt 生成 token
            String token = JwtHelper.createToken(userInfo.getId(), name);
            map.put("token", token);
            // 发送请求
            return "redirect:http://localhost:3000/weixin/callback?token=" + map.get("token") + "&openid=" + map.get("openid") + "&name=" + URLEncoder.encode(map.get("name"),
                    "utf-8");


        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     * 获取微信登录参数
     */
    @GetMapping("/getLoginParam")
    @ResponseBody
    public R genQrConnect(HttpSession session) throws UnsupportedEncodingException {
        String redirectUri = URLEncoder.encode(ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL, "UTF-8");
        Map<String, Object> map = new HashMap<>();
        map.put("appid", ConstantPropertiesUtil.WX_OPEN_APP_ID);
        map.put("redirectUri", redirectUri);
        map.put("scope", "snsapi_login");
        map.put("state", System.currentTimeMillis() + "");//System.currentTimeMillis()+""
        return R.ok().data(map);
    }
}