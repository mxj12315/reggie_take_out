package com.min.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 处理自动填充的类
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 执行插入操作时候自动更新
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());  // 自动填充创建时间
        metaObject.setValue("updateTime", LocalDateTime.now()); // 自动填充更新时间
        metaObject.setValue("createUser", ThreadLocalUtil.getThreadLocal()); // 自动填充创建者
        metaObject.setValue("updateUser", ThreadLocalUtil.getThreadLocal()); // 自动填充更新者

    }

    /**
     * 执行更新操作时候自动更新字段
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now()); // 自动填充更新时间
        metaObject.setValue("updateUser", ThreadLocalUtil.getThreadLocal());  // 自动填充更新者

    }
}
