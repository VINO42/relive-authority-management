package com.relive.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.relive.enums.UserType;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

import java.util.Date;

/**
 * @author: ReLive
 * @date: 2021/9/29 1:24 下午
 */
@Data
@TableName("user")
public class User {

    private Long id;

    private String username;

    private String password;

    private String phone;

    private String email;

    private UserType userType;

    private Boolean nonLocked;

    private Boolean status;

    private Boolean enable;

    private Date createTime;

    private Date updateTime;

}
