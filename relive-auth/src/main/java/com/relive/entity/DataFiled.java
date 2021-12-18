package com.relive.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.relive.enums.DataFieldType;
import lombok.Data;

import java.util.Date;

/**
 * 列表字段类
 *
 * @author: ReLive
 * @date: 2021/12/7 12:29 下午
 */
@Data
@TableName("data_filed")
public class DataFiled {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long permissionId;

    private String filedName;

    private DataFieldType fieldType;

    private Boolean enable;

    private Date createTime;

    private Date updateTime;
}
