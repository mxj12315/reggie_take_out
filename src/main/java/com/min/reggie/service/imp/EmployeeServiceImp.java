package com.min.reggie.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.min.reggie.dao.EmployeeDao;
import com.min.reggie.domain.Employee;
import com.min.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * 员工服务类
 */
@Service
public class EmployeeServiceImp extends ServiceImpl<EmployeeDao,Employee> implements EmployeeService {

}
