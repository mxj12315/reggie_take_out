package com.min.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.min.reggie.common.Rest;
import com.min.reggie.common.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录url过滤器
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")  // 过滤器起名字，指定拦截的url
public class LoginCheckFilter implements Filter {
    static private  final AntPathMatcher PATHFILTER  =new AntPathMatcher();
    // 不需要过滤的路径
    private String[] notFilterUrls = {
            "/employee/login",  // 登录地址
            "/employee/logout",  // 退出地址
            "/backend/**",  // 静态资源
            "/front/**",
            "/common/**" , // 测试上传文件
            "/user/sendMsg",
            "/user/login"
    };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        /**
         *  1、获取本次请求的URI
         *  2、判断本次请求是否需要处理
         *  3、如果不需要处理，则直接放行
         *  4、判断登录状态，如果已登录，则直接放行
         *  5、如果未登录则返回未登录结果
         */
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 1、获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("过滤器启动，当前的url: {} ",requestURI);
        // 2、判断本次请求是否需要处理
        boolean checkUrlPath = checkUrlPath(notFilterUrls, requestURI);
        // 3、如果不需要处理，则直接放行
        if (checkUrlPath){
            log.info("当前url：{} 是匹配的直接放行",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //4.1、判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("employee") != null) {
            log.info("当前用户已经是登录状态");
            Long empId = (Long) request.getSession().getAttribute("employee");
            ThreadLocalUtil.setThreadLocal(empId);  // 登录成功拿到登陆者的id，用于自动填充
            filterChain.doFilter(request,response);
            return;
        }
        // 4.2、判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            ThreadLocalUtil.setThreadLocal(userId); // 登录成功拿到登陆者的id，用于自动填充
            filterChain.doFilter(request,response);
            return;
        }
        // 5、如果未登录则返回未登录结果
        log.info("用户未登录，放回登录页面");
        response.getWriter().write(JSON.toJSONString(Rest.error("NOTLOGIN")));
        return;
    }

    public boolean checkUrlPath(String[] notFilterUrls,String requestURI ){
        // 遍历不需要过滤的url数组与请求过来的url是否匹配，如果匹配返回true
        for (String url : notFilterUrls) {
            boolean match = PATHFILTER.match(url, requestURI);
            if (match) return true;
        }
        // 遍历完没有匹配的值，返回false
        return false;
    }


}
