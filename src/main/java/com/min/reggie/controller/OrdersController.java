package com.min.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.min.reggie.common.Rest;
import com.min.reggie.common.ThreadLocalUtil;
import com.min.reggie.domain.Orders;
import com.min.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单控制类
 */
@RestController
@RequestMapping("order")
@Slf4j
public class OrdersController {
    @Autowired
   private OrdersService ordersService;

    @PostMapping("/submit")
    public Rest<String> submit(@RequestBody Orders orders){
        Long userId = ThreadLocalUtil.getThreadLocal();
        ordersService.submit(orders);
        return Rest.success("下单成功");

    }

    /**
     * 后端查询订单
     * @param name 订单号
     * @param page 起始页码
     * @param pageSize 每一页的大小
     * @return  分页数据
     */
    @GetMapping("/page")
    public Rest<Page<Orders>> page(String name, int page, int pageSize){
        log.info("name={},page={},pageSize={}",name,page,pageSize);
        Page<Orders> ordersPage = new Page<>(page,pageSize);

        LambdaQueryWrapper<Orders>  queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Orders::getNumber,name);
        ordersService.page(ordersPage,queryWrapper);


        return Rest.success(ordersPage);
    }
}
