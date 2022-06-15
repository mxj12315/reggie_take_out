package com.min.reggie.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.min.reggie.dao.AddressBookDao;
import com.min.reggie.domain.AddressBook;
import com.min.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImp extends ServiceImpl<AddressBookDao, AddressBook>  implements AddressBookService {
}
