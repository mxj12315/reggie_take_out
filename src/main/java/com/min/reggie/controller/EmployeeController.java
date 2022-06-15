package com.min.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.min.reggie.common.Rest;

import com.min.reggie.common.ThreadLocalUtil;
import com.min.reggie.domain.Employee;
import com.min.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * 员工控制器
 */
@Slf4j
@RestController  // 相当于@Controller和@ResponseBody
@RequestMapping("/employee")  //
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 用户登录请求
     *
     * @param httpServletRequest 用来获取session
     * @param employee           用户提交过来的用户密码封装到Employee对象中
     * @return
     */
    @PostMapping("/login")
    public Rest<Employee> login(HttpServletRequest httpServletRequest,
                                @RequestBody/*指定提交过来的参数在body里面*/ Employee employee) {
        /**
         *   1、将页面提交的密码password进行md5加密处理
         *   2、根据页面提交的用户名username查询数据库
         *   3、如果没有查询到则返回登录失败结果
         *   4、密码比对，如果不一致则返回登录失败结果
         *   5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
         *   6、登录成功，将员工id存入Session并返回登录成功结果
         */
        log.info("访问登录页面");
        // 1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();  // 获取用户的密码
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));  // 对密码进行md5加密 md5摘要为十六进制
        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();  // 创建查询包装器
        lambdaQueryWrapper.eq(Employee::getUsername, employee.getUsername()); // 查看数据库的用户名和用户输入的用户名是否相等
        Employee emp = employeeService.getOne(lambdaQueryWrapper);  // 调用业务层方法查询一条
        // 3、如果没有查询到则返回登录失败结果
        if (emp == null) {
            return Rest.error("用户名或者密码错误，请重试！");
        }
        // 4、密码比对，如果不一致则返回登录失败结果
        if (!emp.getPassword().equals(password)) {
            return Rest.error("用户名或者密码错误，请重试！");
        }
        //  5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0) {
            return Rest.error("账号已经禁用！");
        }
        // 6、登录成功，将查询到的员工id存入Session并返回登录成功结果
        httpServletRequest.getSession().setAttribute("employee", emp.getId());// 设置session和属性
        ThreadLocalUtil.threadLocal.set( emp.getId());
        log.info("session： {}", emp.getId());
        return Rest.success(emp);  // 将查询到的数据发送给前端页面
    }

    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Rest<String> logout(HttpServletRequest request) {
        /**
         * 1、清湖session
         * 2、跳转页面到登录界面
         */
        request.getSession().removeAttribute("employee");

        return Rest.success("退出成功");
    }

    /**
     * 添加员工的方法
     *
     * @param request  用户的请求
     * @param employee 提交的json对象封装成Employee对象
     * @return
     */
    @PostMapping
    public Rest<String> addEmployee(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(String.valueOf(employee));
        /**
         * 1、设置初始密码
         * 2、设置更新时间和创建时间 更新着者
         * 3、将信息写入数据库
         */
        // 1、设置初始密码
        String passwd = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(passwd);
        //2、设置更新时间和创建时间 更新着者
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        long id = (long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(id);
//        employee.setUpdateUser(id);


        employeeService.save(employee);
        return Rest.success("添加成功");
    }

    /**
     * 分页查询
     *
     * @param page     起始野页码
     * @param pageSize 分页大小
     * @param name     查询的名字
     * @return
     */
    @GetMapping("/page")
    public Rest<Page> page(int page, int pageSize, String name) {
        log.info("page: {} pageSize: {} name:　{}", page, pageSize, name);
        // 构造Page对象
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();// 创建条件查询器
        lambdaQueryWrapper.like(StringUtils.hasText(name), Employee::getName, name);  // 模糊查询
        lambdaQueryWrapper.orderByDesc(Employee::getCreateTime);  // 添加排序方式

        // 调用服务层，需要借助消息装换器将Long类型的id字段装换为String类型，防止前端js解析long类型的数字四舍五入精度丢失
        employeeService.page(pageInfo, lambdaQueryWrapper);

        return Rest.success(pageInfo);
    }

    /**
     * 更改员工账号状态
     *
     * @param request  用户发送的请求
     * @param employee 请求的数据封装到Employee对象中
     * @return
     */
    @PutMapping
    public Rest<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateUser(empId); // 设置更新人
//        employee.setUpdateTime(LocalDateTime.now()); // 设置更新时间
        employeeService.updateById(employee);  // 调用业务层更新数据， 需要借助消息装换器将Long类型的id字段装换为String类型，防止前端js解析long类型的数字四舍五入精度丢失


        return Rest.success("更新成功");

    }

    /**
     * 根据id查询用户
     *
     * @param id 前端传递回来的员工id
     * @return
     */
    @GetMapping("/{id}")
    public Rest<Employee> getByID(@PathVariable/*路径参数*/ Long id) {
        Employee employee = employeeService.getById(id);
        return Rest.success(employee);
    }
}
