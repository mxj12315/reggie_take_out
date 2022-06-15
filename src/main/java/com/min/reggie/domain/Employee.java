package com.min.reggie.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工实体类
 */
@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;
    //idCard
    private String idNumber;
    // 用户的登录状态
    private Integer status;
    // 创建时间
    @TableField(fill = FieldFill.INSERT)  // 自动填充
    private LocalDateTime createTime;
    // 更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)    // 自动填充
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)  // 自动填充
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)  // 自动填充
    private Long updateUser;

}
