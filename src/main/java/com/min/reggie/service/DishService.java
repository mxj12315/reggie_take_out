package com.min.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.min.reggie.domain.Dish;
import com.min.reggie.dto.DishDto;

public interface DishService  extends IService<Dish> {
    // 新增菜品数据，新增爱好 口味数据
    void saveWithFlavor(DishDto dishDto);

    // 根据id查询菜品和对应的口味
    DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);
}
