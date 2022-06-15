package com.min.reggie.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.min.reggie.domain.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据层的接口
 */
@Mapper
public interface EmployeeDao extends BaseMapper<Employee> {

}
