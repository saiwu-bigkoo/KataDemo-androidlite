package com.bigkoo.billlite.presenter;

import com.bigkoo.billlite.bean.BillBean;
import com.bigkoo.billlite.bean.CategoryBean;
import com.bigkoo.billlite.bean.DayGroupBean;
import com.bigkoo.billlite.constants.DBConstant;
import com.bigkoo.billlite.db.DBServiceGenerator;
import com.bigkoo.billlite.utils.DateUtil;
import com.bigkoo.billlite.view.BillListView;
import com.bigkoo.katafoundation.presenter.BaseListRAMPresenter;
import com.bigkoo.kataframework.bean.HttpResult;
import com.bigkoo.kataframework.http.constants.HttpStatusConstants;
import com.bigkoo.kataframework.http.observer.HttpResultObserver;
import com.bigkoo.kataframework.mvppresenter.BaseListPresenter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by sai on 2018/4/30.
 */

public class BillListPresenter extends BaseListRAMPresenter<BillListView> {
    private Date date = new Date();//当前的时间
    private int distance = 0;//与当前时间的距离

    @Override
    public Observable<HttpResult<List<BillBean>>> onLoadDataHttpRequest() {
//        QueryBuilder<BillBean> builder = new QueryBuilder(BillBean.class);
        Date month = DateUtil.getMonth(date, distance);
//        builder.where(DBConstant.DATE + " LIKE ?", DateUtil.formatDateTime(month, DateUtil.DF_YYYY_MM) + "%");

        String sql = "SELECT "+DBConstant.TABLE_BILLDETAIL+"."+"*, "+DBConstant.TABLE_CATEGORY+"."+ DBConstant.ICON + " FROM " + DBConstant.TABLE_BILLDETAIL +" LEFT JOIN "+ DBConstant.TABLE_CATEGORY + " ON "
                + DBConstant.TABLE_BILLDETAIL+"."+DBConstant.CATEGORYID +" = "+ DBConstant.TABLE_CATEGORY+".id"
                + " WHERE " + DBConstant.DATE + " LIKE " + "\'" + DateUtil.formatDateTime(month, DateUtil.DF_YYYY_MM) + "%" + "\'"
                + " ORDER BY " + DBConstant.DATE;

        return DBServiceGenerator.getInstance().rawQuery(sql, BillBean.class);
    }

    /**
     * 初始化分类
     */
    public void initCategory() {
        onCallHttpRequest(DBServiceGenerator.getInstance()
                .queryCount(CategoryBean.class), queryCategoryCountCallBack);
    }

    public void onLoadDayGroups() {
        Date month = DateUtil.getMonth(date, distance);
        //查询并计算出账单的每天收支总额
        String sql = "SELECT " + DBConstant.DATE + ", SUM(" + DBConstant.INCOME + ") AS " + DBConstant.INCOME + ", SUM(" + DBConstant.PAY + ") AS "
                + DBConstant.PAY + "  FROM " + DBConstant.TABLE_BILLDETAIL
                + " WHERE " + DBConstant.DATE + " LIKE " + "\'" + DateUtil.formatDateTime(month, DateUtil.DF_YYYY_MM) + "%" + "\'"
                + " GROUP BY " + DBConstant.DATE + " ORDER BY " + DBConstant.DATE;
        onCallHttpRequest(DBServiceGenerator.getInstance().rawQuery(sql, DayGroupBean.class), loadDayGroupsCallBack);
    }

    public HttpResultObserver queryCategoryCountCallBack =
            new HttpResultObserver<Long>() {
                @Override
                public void onHttpSuccess(Long resultData, String msg) {
                    //如果没有数据，插入默认
                    if (resultData == 0) {
                        Gson gson = new Gson();
                        ArrayList<CategoryBean> categories = gson.fromJson(DBConstant.CATEGORIES_DEFULT,
                                new TypeToken<List<CategoryBean>>() {
                                }.getType());

                        onCallHttpRequest(DBServiceGenerator.getInstance()
                                .insert(categories), new HttpResultObserver());
                    }
                }

                @Override
                public void onHttpFail(int code, String msg) {
                    setStatusError(true, code, msg);
                }

                @Override
                public void onNetWorkError(String msg) {
                    setStatusNetworkError(true, msg);
                }

                @Override
                public void onComplete() {
                }
            };

    public HttpResultObserver loadDayGroupsCallBack = new HttpResultObserver<Object>() {
        @Override
        public void onHttpSuccess(Object resultData, String msg) {
            setStatusError(false, HttpStatusConstants.CODE_SUCCESS, msg);
            setStatusNetworkError(false, msg);

            getView().onDayGroupsChange(resultData);
            onLoadData();

        }

        @Override
        public void onHttpFail(int code, String msg) {
            //这里直接用默认的就行
            setStatusError(true, code, msg);
        }

        @Override
        public void onNetWorkError(String msg) {
            //这里直接用默认的就行
            setStatusNetworkError(true, msg);
        }

        @Override
        public void onComplete() {
        }
    };

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
