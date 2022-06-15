package com.min.reggie.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.min.reggie.dao.DishDao;
import com.min.reggie.domain.Dish;
import com.min.reggie.domain.DishFlavor;
import com.min.reggie.dto.DishDto;
import com.min.reggie.service.DishFlavorService;
import com.min.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional //表示所有该类的public方法都配置相同的事务属性信息
public class DishServiceImp extends ServiceImpl<DishDao, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 保存口味和菜品
     * @param dishDto
     */
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        // 1、新增菜品数据 操作dish表
        this.save(dishDto);  // dishDto继承自dish里面有提交过来的数据
        // 2、新增爱好 口味数据 操作dish_flavors表
        List<DishFlavor> flavors = dishDto.getFlavors();  // 取出封装的菜品口味
        flavors.forEach(item/*每一个DishFlavor*/ -> {
            log.info(item.toString());
            item.setDishId(dishDto.getId());
        });
        // 调用接口保存口味数据
        dishFlavorService.saveBatch(flavors);

    }

    /**
     *  根据id查询菜品和对应的口味
     * @param id 菜品的id
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);  // 查询到菜品信息

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());  // 查询条件DishFlavor表中的DishId和传入的dishd的id是否相等
        List<DishFlavor> flavorsList = dishFlavorService.list(queryWrapper);  // 根据条件查询结果
        DishDto dishDto = new DishDto(); // 创建一个DishDto用于存放Dish和DishFlavor数据
        BeanUtils.copyProperties(dish,dishDto); // 对象属性拷贝
        dishDto.setFlavors(flavorsList);
        return dishDto;
    }

    /**
     * 更新菜品信息
     * @param dishDto
     */
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        // 1、先更新dish表信息
        this.updateById(dishDto);
        // 2、清空原来的dish_flavor表中对应dish id的信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();// 条件查询器
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);  // 删除指定条件的数据
        // 3、重新保存用户提交过来的信息
        List<DishFlavor> flavors = dishDto.getFlavors();  // 取出封装的菜品口味
        flavors.forEach(item/*每一个DishFlavor*/ -> {
            log.info(item.toString());
            item.setDishId(dishDto.getId());
        });
        // 调用接口保存口味数据
        dishFlavorService.saveBatch(flavors);
    }
}
