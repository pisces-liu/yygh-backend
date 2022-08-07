package com.atguigu.yygh.hosp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

// 扫描到 mapper 层
@MapperScan("com.atguigu.yygh.hosp.mapper")
@SpringBootApplication
@ComponentScan(basePackages = {"com.atguigu"})
public class ServiceHospApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class, args);
    }
}
