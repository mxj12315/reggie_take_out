package com.min.reggie.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.min.reggie.common.CustomException;
import com.min.reggie.dao.CategoryDao;
import com.min.reggie.domain.Category;
import com.min.reggie.domain.Dish;
import com.min.reggie.domain.Setmeal;
import com.min.reggie.service.CategoryService;
import com.min.reggie.service.DishService;
import com.min.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CategoryServiceImp extends ServiceImpl<CategoryDao, Category> implements CategoryService {
    @Autowired  // 注入DishService
    private DishService dishService;

    @Autowired  // 注入SetmealService
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        // 1、检查菜品
        LambdaQueryWrapper<Dish> dish = new LambdaQueryWrapper<>();
        // 根据id查询菜品数量
        LambdaQueryWrapper<Dish> eqDish = dish.eq(Dish::getCategoryId, id);  // 查询时候有这个id
        long countDish = dishService.count(eqDish);
        // 数量大于0 说明不能删除，抛出业务异常
        if (countDish > 0) {
            throw  new CustomException(String.format("菜品分类下已经有%d个菜品与该分类关联,不能删除",countDish));
        }

        // 2、检查套餐
        LambdaQueryWrapper<Setmeal> setmeal = new LambdaQueryWrapper<>();
        // 根据id查询套餐数量
        LambdaQueryWrapper<Setmeal> eqSetmeal = setmeal.eq(Setmeal::getCategoryId, id);  // 查询时候有这个id
        long counteqSetmeal = setmealService.count(eqSetmeal);
        // 数量大于0 说明不能删除，抛出业务异常
        if (counteqSetmeal > 0) {
           throw  new CustomException(String.format("套餐分类下已经有%d个套餐与该分类关联,不能删除",counteqSetmeal));
        }

        // 3、正常则删除
        super.removeById(id);
    }
}
