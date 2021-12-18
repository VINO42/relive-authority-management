package com.relive.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author: ReLive
 * @date: 2021/12/7 12:53 下午
 */
@Data
@TableName("role")
public class Role {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long parentId;

    private String roleName;

    private String roleCode;

    private Boolean enable;

    private Date createTime;

    private Date updateTime;
}
