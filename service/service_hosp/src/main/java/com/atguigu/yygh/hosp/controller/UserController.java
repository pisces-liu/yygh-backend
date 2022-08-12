package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.config.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @PostMapping("/login")
    public R login() {
        return R.ok().data("token", "admin-token");
    }

    @GetMapping("/info")
    public R info(String token) {
        log.info("token: {}", token);
        return R.ok()
                .data("roles", "[admin]")
                .data("introduction", "admin")
                .data("avatar", "https://pic4.zhimg.com/80/v2-9715174c95f9665b71cc8f3cceea910f_720w.jpg")
                .data("name", "Super admin");
    }
}
