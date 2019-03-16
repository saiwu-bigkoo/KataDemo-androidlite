package com.bigkoo.billlite.bean;

import com.bigkoo.billlite.constants.DBConstant;
import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Default;
import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.annotation.Unique;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by sai on 2018/4/30.
 */

@Table(DBConstant.TABLE_CATEGORY)
public class CategoryBean {
    // 指定自增，每个对象需要有一个主键
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    // 分类名
    @NotNull
    @Column(DBConstant.NAME)
    private String name;

    // 图标
    @Column(DBConstant.ICON)
    private String icon;

    // 排序
    @Column(DBConstant.SORT)
    private int sort;

    //分类类型
    @Column(DBConstant.TYPE)
    private int type;

    @Column(DBConstant.STATUS)
    @Default("0")
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
