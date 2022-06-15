package com.min.reggie.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.min.reggie.dao.SetmealDishDao;
import com.min.reggie.domain.SetmealDish;
import com.min.reggie.service.SetmealDishService;
import org.springframework.stereotype.Service;


@Service
public class SetmealDishServiceImp extends ServiceImpl<SetmealDishDao, SetmealDish> implements SetmealDishService {
}
