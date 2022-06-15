package com.min.reggie.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.min.reggie.dao.SetmealDao;

import com.min.reggie.domain.SetmealDish;
import com.min.reggie.domain.Setmeal;
import com.min.reggie.dto.DishDto;
import com.min.reggie.dto.SetmealDto;
import com.min.reggie.service.SetmealDishService;
import com.min.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SetmealServiceImp extends ServiceImpl<SetmealDao, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 根据id查询菜品
     *
     * @param id 要查询的套餐id
     * @return
     */
    @Override
    public SetmealDto getByIdWithSetmealDish(Long id) {
        Setmeal setmeal = this.getById(id);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmeal.getId()); // 查询条件SetmealDish表中的DSetmealId和传入的setmeal的id是否相等
        List<SetmealDish> setmealList = setmealDishService.list(queryWrapper);  // 根据条件查询结果
        SetmealDto setmealDto = new SetmealDto(); // 创建一个SetmealDto用于存放Setmeal和SetmealDish数据
        BeanUtils.copyProperties(setmeal, setmealDto); // 对象属性拷贝
        setmealDto.setSetmealDishes(setmealList);
        return setmealDto;
    }

    @Override
    public void saveWithSetmealDish(SetmealDto setmealDto) {
        // 先存 setmeal表
        this.save(setmealDto);

        // 再存setmeal_Dishes 表
        // setmealDishes中每一项都没有id,遍历每一项,添加当前套餐的id
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        Long id = setmealDto.getId();  // 当前套餐的id
        setmealDishes.forEach(item -> {
            item.setSetmealId(id);
        });
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 更新套餐
     *
     * @param setmealDto 用户提交过来的信息
     */
    @Override
    public void updateWithSetmealDish(SetmealDto setmealDto) {
        // 1、先更新setmeal这张表
        this.updateById(setmealDto);
        /*
            2、更新setmeal_id这张表
            2.1 删除原有的
            2.1 插入新提交过来的
         */
        //  2.1 删除原有的
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();  // 查询条件
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        // 2.1 插入新提交过来的
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();// 拿到所有的套餐对应的菜品
        setmealDishes.forEach(item->{  // item中没有setmealId的值，要手动添加上
            item.setSetmealId(setmealDto.getId());
        });
        setmealDishService.saveBatch(setmealDishes); // 保存

    }
}
