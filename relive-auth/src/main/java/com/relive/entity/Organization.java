package com.relive.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.relive.enums.OrganizationType;
import lombok.Data;

import java.util.Date;

/**
 * @author: ReLive
 * @date: 2021/12/7 12:54 下午
 */
@Data
@TableName("organization")
public class Organization {

    @TableId
    private Long id;

    private Long parentId;

    private String groupName;

    private OrganizationType groupType;

    private Boolean enable;

    private Date createTime;

    private Date updateTime;
}
