package com.min.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局的异常处理器
 */
@ControllerAdvice(annotations = {RestController.class})
@ResponseBody // 结果要封装成json数据
@Slf4j
public class GloablExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class) // 处理SQL完整性约束冲突异常
    public Rest<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        String message = ex.getMessage(); // 获取异常信息
        log.error(message);
        // 判断异常信息中是否包含Duplicate entry
        if (message.contains("Duplicate entry")) {
            String[] split = message.split(" "); // 切割异常信息，提取用户提交的 用户名
            String msg = split[2] + "用户名已存在";
            return Rest.error(msg);
        }
        return Rest.error("未知错误");

    }

    /**
     * 注册自定义异常类CustomException
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class) // 处理SQL完整性约束冲突异常
    public Rest<String> exceptionHandler(CustomException ex) {
        String message = ex.getMessage(); // 获取异常信息
        return Rest.error(message);

    }
}
