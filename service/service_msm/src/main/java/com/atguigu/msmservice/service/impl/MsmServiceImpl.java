package com.atguigu.msmservice.service.impl;

import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.util.HttpUtils;
import com.atguigu.msmservice.util.RandomUtil;
import org.apache.http.HttpResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class MsmServiceImpl implements MsmService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean sendCode(String phone) {

        String host = "http://dingxintz.market.alicloudapi.com";
        String path = "/dx/notifySms";
        String method = "POST";
        String appcode = "a742f1fb3a9b47c99bfabbddad3a745e";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);

        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phone);
        // 生成验证码
        String code = RandomUtil.getFourBitRandom();

        querys.put("param", "code:" + code);
        querys.put("tpl_id", "TP18040315");

        Map<String, String> bodys = new HashMap<String, String>();


        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));

            // 保存验证码到 redis 中。后两个参数是设置验证码保存的时间。
            redisTemplate.opsForValue().set(phone, code, 24, TimeUnit.HOURS);
            // 当发送验证码成功时，返回结果 true
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
