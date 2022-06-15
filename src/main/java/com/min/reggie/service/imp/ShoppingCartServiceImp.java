package com.min.reggie.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.min.reggie.dao.ShoppingCartDao;
import com.min.reggie.domain.ShoppingCart;
import com.min.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;


@Service
public class ShoppingCartServiceImp extends ServiceImpl<ShoppingCartDao, ShoppingCart> implements ShoppingCartService {
}
