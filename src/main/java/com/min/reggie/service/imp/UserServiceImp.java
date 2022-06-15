package com.min.reggie.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.min.reggie.dao.UserDao;
import com.min.reggie.domain.User;
import com.min.reggie.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImp extends ServiceImpl<UserDao, User>  implements UserService {

}
