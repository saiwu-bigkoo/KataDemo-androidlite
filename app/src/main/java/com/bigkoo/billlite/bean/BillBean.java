package com.bigkoo.billlite.bean;

import com.bigkoo.billlite.constants.DBConstant;
import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Ignore;
import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;


/**
 * Created by sai on 2018/4/30.
 * 账单明细
 */
@Table(DBConstant.TABLE_BILLDETAIL)
public class BillBean implements Serializable{

    // 指定自增，每个对象需要有一个主键
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    //账单类型
    @Column(DBConstant.BILLTYPE)
    private int billType;

    // 备注
    @NotNull
    @Column(DBConstant.REMARK)
    private String remark;

    //账单时间
    @Column(DBConstant.DATE)
    private String date;

    //分类ID
    @Column(DBConstant.CATEGORYID)
    private int categoryId;

    @Ignore
    private String icon;

    //收入金额
    @Column(DBConstant.INCOME)
    private double income;

    //支出金额
    @Column(DBConstant.PAY)
    private double pay;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getPay() {
        return pay;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }

    public int getBillType() {
        return billType;
    }

    public void setBillType(int billType) {
        this.billType = billType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
