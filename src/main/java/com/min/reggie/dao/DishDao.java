package com.min.reggie.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.min.reggie.domain.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishDao extends BaseMapper<Dish> {
}
