package com.min.reggie.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.min.reggie.dao.DishFlavorDao;
import com.min.reggie.domain.DishFlavor;
import com.min.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;


@Service
public class DishFlavorServiceImp extends ServiceImpl<DishFlavorDao, DishFlavor> implements DishFlavorService {
}
