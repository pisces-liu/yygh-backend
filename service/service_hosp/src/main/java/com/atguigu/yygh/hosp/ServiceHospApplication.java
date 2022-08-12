package com.atguigu.yygh.hosp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

// 扫描到 mapper 层
@MapperScan("com.atguigu.yygh.hosp.mapper")
@SpringBootApplication
@ComponentScan(basePackages = {"com.atguigu"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.atguigu")
//可以保证当前模块能够扫描当前模块依赖的jar包中的FeignClient接口了
public class ServiceHospApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class, args);
    }
}
