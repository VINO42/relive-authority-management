package com.relive.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author: ReLive
 * @date: 2021/12/7 12:48 下午
 */
@Data
@TableName("operation")
public class Operation {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long permissionId;

    private String operationName;

    private String operationCode;

    private String urlPrefix;

    private Boolean enable;

    private Date createTime;

    private Date updateTime;
}
