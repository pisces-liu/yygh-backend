package com.atguigu.yygh.common.config.handler;

import com.atguigu.yygh.common.config.exception.YyghException;
import com.atguigu.yygh.common.config.result.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 全局统一异常处理
    @ExceptionHandler(Exception.class)
    public R error(Exception exception) {
        exception.printStackTrace();
        return R.error().message("发生了全局异常");
    }

    // 处理特定异常
    @ExceptionHandler(ArithmeticException.class)
    public R error(ArithmeticException arithmeticException) {
        arithmeticException.printStackTrace();
        return R.error().message("数学逻辑异常");
    }

    // 处理自定义异常
    @ExceptionHandler(YyghException.class)
    public R error(YyghException ex) {
        ex.printStackTrace();
        return R.error().code(ex.getCode()).message(ex.getMessage());
    }
}
