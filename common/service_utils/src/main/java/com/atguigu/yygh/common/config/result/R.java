package com.atguigu.yygh.common.config.result;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class R {

    private Integer code;

    private String message;

    private boolean success;

    private Map<String, Object> data = new HashMap<>();

    private R() {
    }

    /**
     * 返回 r 对象，可以直接调用下面的 code 方法，很巧妙！
     */
    public static R ok() {
        R r = new R();
        r.setCode(20000);
        r.setMessage("成功");
        r.setSuccess(true);
        return r;
    }

    public static R error() {
        R r = new R();
        r.setCode(20001);
        r.setMessage("失败");
        r.setSuccess(false);
        return r;
    }

    public R code(Integer code) {
        this.setCode(code);
        return this;
    }

    public R message(String message) {
        this.setMessage(message);
        return this;
    }

    public R success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

    public R data(Map<String, Object> data) {
        this.setData(data);
        return this;
    }

    public R data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

}
