package com.min.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.min.reggie.domain.Setmeal;
import com.min.reggie.dto.DishDto;
import com.min.reggie.dto.SetmealDto;

public interface SetmealService extends IService<Setmeal> {
    // 根据id查询菜品和对应的菜品表
    SetmealDto getByIdWithSetmealDish(Long id);
    // 保存套餐
    void saveWithSetmealDish(SetmealDto setmealDto);
    // 更新表单
    void  updateWithSetmealDish(SetmealDto setmealDto);
}
