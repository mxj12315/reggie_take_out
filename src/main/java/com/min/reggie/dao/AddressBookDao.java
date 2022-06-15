package com.min.reggie.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.min.reggie.domain.AddressBook;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface AddressBookDao extends BaseMapper<AddressBook> {
}
