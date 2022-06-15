package com.min.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.min.reggie.common.Rest;
import com.min.reggie.domain.Category;
import com.min.reggie.domain.Setmeal;
import com.min.reggie.domain.SetmealDish;
import com.min.reggie.dto.SetmealDto;
import com.min.reggie.service.CategoryService;
import com.min.reggie.service.SetmealDishService;
import com.min.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * 套餐控制器
 */
@Slf4j
@RestController
@RequestMapping("setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
   private SetmealDishService setmealDishServicel;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public Rest<Page> page(int page, int pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        // 如果传递了名字，那么进行名称查询，否则不查询
        queryWrapper.like(StringUtils.hasText(name), Setmeal::getName, name);
        // 调用服务层执行page方法
        setmealService.page(setmealPage, queryWrapper);

        // return Rest.success(pageResult); // 这里不能直接返回这个setmealPage 里面没有套餐名
        // 取出买一条记录，通过categoryId字段查询category数据库对应的名称
        Page<SetmealDto> dtoPage = new Page<>(); // 用于存放数据作为新的page对象返回
        BeanUtils.copyProperties(setmealPage, dtoPage, "records"); // 忽略page对象的records属性
        List<Setmeal> records = setmealPage.getRecords(); // 获取之前的setmealPage查询到的记录
        List<SetmealDto> setmealDtos = new ArrayList<>();  // 存放设置了categoryName的SetmealDto对象
        records.forEach(item -> {  // item代表查询到的每一个记录对应Setmeal对象
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category CategoryRecord = categoryService.getById(categoryId);  // 对应id的Category
            if (CategoryRecord != null) {
                // 不为null说明查询到了数据
                String categoryName = CategoryRecord.getName();
                setmealDto.setCategoryName(categoryName);
                setmealDtos.add(setmealDto);
            }

        });
        dtoPage.setRecords(setmealDtos); // 刚刚没有拷贝records字段，所以现在设置上
        return Rest.success(dtoPage);
    }


    /*
    保存套餐
    请求网址: http://127.0.0.1:8080/setmeal  提交套餐
    请求方法: POST
     */
    @PostMapping
    public Rest<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithSetmealDish(setmealDto);
        // 返回字符串
        return Rest.success("保存套餐成功");
    }

    /**
     * 点击修改时候回显数据库中的数据，自动填充
     * @param id
     * @return
     */
    @GetMapping("/{id}")  // http://127.0.0.1:8080/setmeal/1415580119015145474
    public Rest<SetmealDto> get(@PathVariable Long id) {
        //  Dish byId = dishService.getById(id);  // 需要查询两个表要自己写方法

        SetmealDto setmealDto = setmealService.getByIdWithSetmealDish(id);
        return Rest.success(setmealDto);
    }

    /**
     * 更新套餐
     *
     * @param setmealDto
     * @return
     */
    @PutMapping  // http://127.0.0.1:8080/dish
    public Rest<String> update(@RequestBody/*改参数来自请求体中的json数据*/  SetmealDto setmealDto) {
        log.info(setmealDto.toString());

        // 要操作两张表，需要自己实现
        setmealService.updateWithSetmealDish(setmealDto);
        return Rest.success("菜品更新成功");
    }


    /**
     * 套餐起售和停售
     *
     * @param statusCode 标记要起售还是停售
     * @param ids        起售的菜品id
     * @return
     */
    @PostMapping("status/{statusCode}")  //http://127.0.0.1:8080/dish/status/1?ids=1531660115767767041
    public Rest<String> status(@PathVariable int statusCode, String ids) {
        log.info("statusCode：{}，ids：{}", statusCode, ids);
        String[] split = ids.split(",");
        for (String id : split) {
            log.info(id);
            Setmeal setmeal = setmealService.getById(Long.parseLong(id));  // 先查出来
            setmeal.setStatus(statusCode);  // 修改状态
            setmealService.updateById(setmeal);  // 更新回去
        }
        return Rest.success("修改成功，请刷新页面");
    }

    /**
     * 套餐删除
     * @param ids 传入的一个或者多个id
     * @return
     */
    @DeleteMapping
    public Rest<String> delete(String ids) {
        log.info("删除的id={}", ids);
        String[] split = ids.split(",");
        for (String id : split) {
            Setmeal setmeal = setmealService.getById(Long.parseLong(id));
            setmealService.removeById(setmeal);
            // 需要删除对应的id下的菜品
            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getSetmealId,id);
            setmealDishServicel.remove(queryWrapper);
        }

        return Rest.success("删除成功");
    }

    @GetMapping("/list")
    public Rest<List<Setmeal>> getList(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());  // 菜品分类id条件查询
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());  // 条件：判断是否是在售状态
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);  // 排序条件

        List<Setmeal> list = setmealService.list(queryWrapper);
        return Rest.success(list);
    }


}
