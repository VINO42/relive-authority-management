package com.relive.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.relive.enums.PermissionType;
import lombok.Data;

import java.util.Date;

/**
 * @author: ReLive
 * @date: 2021/12/7 12:50 下午
 */
@Data
@TableName("permission")
public class Permission {

    @TableId
    private Long id;

    private PermissionType permissionType;

    private String describe;

    private Boolean enable;

    private Date createTime;

    private Date updateTime;
}
