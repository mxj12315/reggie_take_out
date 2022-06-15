package com.min.reggie.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.min.reggie.dao.OrderDetailDao;
import com.min.reggie.domain.OrderDetail;
import com.min.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImp extends ServiceImpl<OrderDetailDao, OrderDetail> implements OrderDetailService {
}
