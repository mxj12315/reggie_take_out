package com.min.reggie.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.min.reggie.domain.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailDao extends BaseMapper<OrderDetail> {
}
