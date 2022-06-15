package com.min.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.min.reggie.domain.Orders;

public interface OrdersService extends IService<Orders> {

    void submit(Orders orders);
}
