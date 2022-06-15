package com.min.reggie.dto;


import com.min.reggie.domain.SetmealDish;
import com.min.reggie.domain.Setmeal;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {
    // 存放套餐
    private List<SetmealDish> setmealDishes;
    // 分类名称
    private String categoryName;
}
