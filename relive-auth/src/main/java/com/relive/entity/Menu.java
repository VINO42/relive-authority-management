package com.relive.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.relive.enums.MenuType;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author: ReLive
 * @date: 2021/11/14 6:19 下午
 */
@Data
@TableName("menu")
public class Menu {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long permissionId;

    private Long parentId;

    private String title;

    private String path;

    private String icon;

    private Integer sort;

    private String code;

    private MenuType type;

    private Boolean enable;

    private Date createTime;

    private Date updateTime;

    public String name;

    @TableField(exist = false)
    public List<Menu> childList;

    public Menu(Long id, Long parentId, String title) {
        this.id = id;
        this.parentId = parentId;
        this.title = title;
    }


}
