package com.min.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.min.reggie.common.Rest;
import com.min.reggie.common.ThreadLocalUtil;
import com.min.reggie.domain.Category;
import com.min.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品 控制器
 */

@Slf4j
@RestController // RESTful风格
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新建菜品
     * @param category  前端提交的菜品category
     * @return
     */
    @PostMapping
    public Rest<String> save(@RequestBody Category category ){
        log.info("名称: {},类型：{},排序： {}",category.getName(),category.getType(),category.getSort());
        Long threadLocal = ThreadLocalUtil.getThreadLocal();// 得到id
        log.info("用户id:{}",threadLocal);
        categoryService.save(category);
        return  Rest.success("添加成功");
    }

    /**
     * 菜品分页查询
     * @param page  起始页码
     * @param pageSize 分页大小
     * @return
     */
    @GetMapping("/page")
    public Rest<Page> page(int page, int pageSize) {
        log.info("page: {} pageSize: {}", page, pageSize );
        // 构造Page对象
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(Category::getSort);  // 添加排序方式

        // 调用服务层，需要借助消息装换器将Long类型的id字段装换为String类型，防止前端js解析long类型的数字四舍五入精度丢失
        categoryService.page(pageInfo, lambdaQueryWrapper);

        return Rest.success(pageInfo);
    }

    @PutMapping
    public Rest<String> update(@RequestBody Category category){

        categoryService.updateById(category);
        return Rest.success("更新成功");
    }

    @DeleteMapping
    public Rest<String> delete(Long id){
        log.info("删除的id是 {}", id);
//        categoryService.removeById(id);  // 不能直接删除，需要检查
        categoryService.remove(id);  // 自定义的删除方法
        return Rest.success("删除成功");
    }

    @GetMapping("/list")
    public Rest<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();// 条件构造器
        qw.eq(category.getType()!=null,Category::getType,category.getType()); // 查询条件type=?
        qw.orderByDesc(Category::getSort).orderByDesc(Category::getUpdateTime); // 设置排序
        List<Category> list = categoryService.list(qw);  // 返回多个Category存放在list集合中
        return Rest.success(list);
    }

}
