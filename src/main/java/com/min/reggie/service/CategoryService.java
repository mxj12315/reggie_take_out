package com.min.reggie.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.min.reggie.domain.Category;

public interface CategoryService extends IService<Category> {

     void remove(Long id) ;
}
