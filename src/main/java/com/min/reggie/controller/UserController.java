package com.min.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.min.reggie.common.Rest;
import com.min.reggie.domain.User;
import com.min.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 用户端控制类
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 前端登录
     */
    @PostMapping("/login")
    public Rest<User> login(@RequestBody Map<String, String> map, HttpSession session) {
        /**
         *   1、将页面提交的验证码和session中的验证码比对（模拟）
         *   2、根据页面提交的电话号码查询数据库
         *   3、如果没有查询到则返回登录失败结果
         *   4、密码比对，如果不一致则返回登录失败结果
         *   5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
         *   6、登录成功，将员工id存入Session并返回登录成功结果
         */

        log.info("aaaaaaaaaaaa");
        String phone = map.get("phone");
        String code = map.get("code");
        log.info("phone={},code={}", phone, code);
        String mockCode = "123456";
        // 1、将页面提交的验证码和session中的验证码比对（模拟）
        if (!mockCode.equals(code)) {
            return Rest.error("验证码错误，请重试");
        }
        // 2、根据页面提交的电话号码查询数据库，有这个号码说明已经注册的用户，可以登录，否则帮他注册
        LambdaQueryWrapper<User> wq = new LambdaQueryWrapper<>();
        wq.eq(User::getPhone, phone);
        User user = userService.getOne(wq);
        if (user == null) {
            // 没有这个用户，需要注册
            user =new User();
            user.setPhone(phone);
            user.setStatus(1);
            userService.save(user);
        }
        session.setAttribute("user", user.getId());
        log.info("启动到这里了");
        return Rest.success(user);
    }
}
