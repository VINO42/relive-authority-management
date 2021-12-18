package com.relive.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.relive.enums.ElementType;
import lombok.Data;

import java.util.Date;

/**
 * 页面元素类
 *
 * @author: ReLive
 * @date: 2021/12/7 12:35 下午
 */
@Data
@TableName("element")
public class Element {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long permissionId;

    private String elementName;

    private ElementType elementType;

    private Boolean enable;

    private Date createTime;

    private Date updateTime;
}
