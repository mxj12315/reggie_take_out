package com.min.reggie.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.min.reggie.domain.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersDao extends BaseMapper<Orders> {
}
