package com.min.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.min.reggie.common.Rest;
import com.min.reggie.domain.Category;
import com.min.reggie.domain.Dish;
import com.min.reggie.domain.DishFlavor;
import com.min.reggie.domain.SetmealDish;
import com.min.reggie.dto.DishDto;
import com.min.reggie.service.CategoryService;
import com.min.reggie.service.DishFlavorService;
import com.min.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * 菜品管理 控制器
 */
@RequestMapping("/dish")
@RestController
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;  // 菜品管理

    @Autowired
    private DishFlavorService dishFlavorService;  // 爱好 口味

    @Autowired
    private CategoryService categoryService;  // 分类

    /**
     * 添加菜品
     *
     * @param dishDto 数据转储对象
     * @return
     */
    @PostMapping  // http://127.0.0.1:8080/dish
    public Rest<String> save(@RequestBody/*改参数来自请求体中的json数据*/  DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        // 1、要操作两张表
        return Rest.success("菜品保存成功");
    }

    @GetMapping("/page")
    public Rest<Page> page(int page, int pageSize, String name) {
        log.info("page:{},pageSize:{}", page, pageSize);
        // 分页查询
        Page<Dish> dishPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();// 创建条件查询器
        queryWrapper.like(StringUtils.hasText(name)/*判断是否传name字段*/, Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime); // 根据更新时间排序
        dishService.page(dishPage, queryWrapper);// 此时的dishPage中已经有菜品信息，但是没有分类信息
        Page<DishDto> dtoPage = new Page<>();
        BeanUtils.copyProperties(dishPage, dtoPage, "records");  // 第三个参数指定忽略哪个参数 这里忽略records

        List<Dish> records = dishPage.getRecords();
        log.info("dishPage:{}", dishPage.toString());

        List<DishDto> dtoList = new ArrayList<>();
        records.forEach(item -> {  // 得到每一条记录
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);  // 拷贝对象属性
            Long categoryId = item.getCategoryId();//分类id
            Category category = categoryService.getById(categoryId);  // 更具id查询分类
            if (category != null) {  // 不为null说明查询到了
                String categoryName = category.getName();  // 查询到分类名称
                dishDto.setCategoryName(categoryName);   // 把分类名称设给dishDto
                dtoList.add(dishDto);  // 将整理后的对象存放在list集合中
            }
        });

        dtoPage.setRecords(dtoList);


        return Rest.success(dtoPage);
    }


    /**
     * 根据id查询菜品信息和对应的口味信息 点击修改时候回显数据库中的数据，自动填充
     *
     * @param id 要查询的菜品id
     * @return
     */
    @GetMapping("/{id}")  // http://127.0.0.1:8080/dish/1531660115767767041
    public Rest<DishDto> get(@PathVariable Long id) {
        //  Dish byId = dishService.getById(id);  // 需要查询两个表要自己写方法

        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return Rest.success(dishDto);
    }

    /**
     * 更新菜品
     *
     * @param dishDto
     * @return
     */
    @PutMapping  // http://127.0.0.1:8080/dish
    public Rest<String> update(@RequestBody/*改参数来自请求体中的json数据*/  DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        // 1、要操作两张表
        return Rest.success("菜品更新成功");
    }

    /**
     * 起售和停售
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
            Dish byId = dishService.getById(Long.parseLong(id));
            byId.setStatus(statusCode);
            dishService.updateById(byId);
        }
        return Rest.success("修改成功，请刷新页面");
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public Rest<String> delete(String ids) {
        log.info("删除的id={}", ids);
        String[] split = ids.split(",");
        for (String id : split) {
            Dish dish = dishService.getById(Long.parseLong(id));
            dishService.removeById(dish);
            // 需要删除对应的id下的口味
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId,id);
            dishFlavorService.remove(queryWrapper);
        }
        return Rest.success("删除成功");
    }

    /**
     * 分类对象的菜品
     * @param categoryId 分类id
     * @return
     */
    // http://127.0.0.1:8080/dish/list?categoryId=1413341197421846529 GET
    @GetMapping("/list")
    public Rest<List<DishDto>> list(Long categoryId){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,categoryId);// 条件1 查询id相同的菜品
        queryWrapper.eq(Dish::getIsDeleted,0);  // 条件2 只查询起售的
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dtoList = new ArrayList<>();
        list.forEach(item->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long itemCategoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(itemCategoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);

            dtoList.add(dishDto);
        });

        return Rest.success(dtoList);
    }

}
