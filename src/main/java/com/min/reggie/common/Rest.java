package com.min.reggie.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Rest<T> {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map<String,Object> map = new HashMap<>(); //动态数据

    /**
     * 成功的泛型方法
     */
    public static <T> Rest<T> success(T object) {
        Rest<T> r = new Rest<T>();
        r.data = object;
        r.code = 1;
        return r;
    }
    /**
     * 失败的泛型方法
     */
    public static <T> Rest<T> error(String msg) {
        Rest<T> r = new Rest<>();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public Rest<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
