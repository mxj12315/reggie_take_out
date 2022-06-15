package com.min.reggie.dto;

import com.min.reggie.domain.Dish;
import com.min.reggie.domain.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

// DTO  Data transfer Object

@Data
public class DishDto extends Dish {

    //菜品对应的口味数据
    private List<DishFlavor> flavors = new ArrayList<>();
    // 菜品分类名称
    private String categoryName;

    private Integer copies;


}
