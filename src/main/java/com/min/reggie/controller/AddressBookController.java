package com.min.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.min.reggie.common.Rest;
import com.min.reggie.common.ThreadLocalUtil;
import com.min.reggie.domain.AddressBook;
import com.min.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Queue;

/**
 * 用户收货地址控制类
 */
@RestController
@Slf4j
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 添加地址
     *
     * @param addressBook  请求体封装为地址对象
     * @return 是否添加成功
     */
    @PostMapping
    public Rest<AddressBook> saveAddressBook(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(ThreadLocalUtil.getThreadLocal());
        addressBookService.save(addressBook);
        return Rest.success(addressBook);
    }

    /**
     * 获取默认地址
     */
    @GetMapping("/default")
    public Rest<AddressBook> getDefault() {  // 此处不能用default作为方法名，default是java关键字
        Long userId = ThreadLocalUtil.getThreadLocal();
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,userId);
        queryWrapper.eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        if (addressBook==null) return Rest.error("还未设置默认地址");
        return Rest.success(addressBook);

    }

    /**
     * 设置默认地址
     */
    @PutMapping("default")
    public Rest<AddressBook> setDefaultAddress(@RequestBody AddressBook addressBook) {  // 此处不能用default作为方法名，default是java关键字
        log.info("设置默认地址");
        log.info("addressBook={}", addressBook);
        /*
          1、将之前和用户匹配的地址选出来，将默认项，全部设置为非默认
          2、将现在用户提交上来的对应的地址id设置为默认值
         */
        // 1、将之前和用户匹配的地址选出来，将默认项，全部设置为非默认
        Long threadLocal = ThreadLocalUtil.getThreadLocal();
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, threadLocal); // 条件：用户userId等于数据库的UserId
        wrapper.set(AddressBook::getIsDefault, 0); // 全部设为非默认
        addressBookService.update(wrapper);

        // 2、将现在用户提交上来的对应的地址id设置为默认值
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getId, addressBook.getId());
        AddressBook one = addressBookService.getOne(queryWrapper);
        one.setIsDefault(1);
        addressBookService.updateById(one);
        return Rest.success(one);

    }


    /**
     * 查询指定用户的全部地址
     */
    @GetMapping("/list")
    public Rest<List<AddressBook>> list(HttpSession session) {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        Long userId = (Long) session.getAttribute("user");
        queryWrapper.eq(null != userId, AddressBook::getUserId, userId);
        List<AddressBook> list = addressBookService.list(queryWrapper);
        if (list.size() == 0) return Rest.error("还没有添加任何地址");
        return Rest.success(list);

    }


    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public Rest<AddressBook> get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return Rest.success(addressBook);
        } else {
            return Rest.error("没有找到该对象");
        }
    }
}
