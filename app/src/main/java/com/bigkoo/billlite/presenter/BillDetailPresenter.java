package com.bigkoo.billlite.presenter;

import android.util.Log;

import com.bigkoo.billlite.bean.BillBean;
import com.bigkoo.billlite.bean.CategoryBean;
import com.bigkoo.billlite.db.DBServiceGenerator;
import com.bigkoo.billlite.view.BillDetailView;
import com.bigkoo.kataframework.bean.HttpResult;
import com.bigkoo.kataframework.http.observer.HttpResultObserver;
import com.bigkoo.kataframework.mvppresenter.BaseDetailPresenter;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;


import io.reactivex.Observable;

public class BillDetailPresenter extends BaseDetailPresenter<BillDetailView> {

    private long billId;

    @Override
    public Observable<HttpResult<BillBean>> onLoadDataHttpRequest() {
        //按ID 进行查询
        return DBServiceGenerator.getInstance().queryById(billId, BillBean.class);
    }

    public void setBillId(long billId) {
        this.billId = billId;
    }


    /**
     * 根据账单删除数据
     *
     * @param bill
     * @return
     */
    public void delete(BillBean bill) {
        onCallHttpRequest(DBServiceGenerator.getInstance().delete(bill), new HttpResultObserver() {
            @Override
            public void onHttpSuccess(Object result, String msg) {
                super.onHttpSuccess(result, msg);
                getView().onDeleteComplete();
            }
        });
    }

    public void queryCategory(int categoryId) {
        onCallHttpRequest(DBServiceGenerator.getInstance().queryById(categoryId, CategoryBean.class),
                new HttpResultObserver() {
                    @Override
                    public void onHttpSuccess(Object result, String msg) {
                        getView().onQueryCategroy((CategoryBean) result);
                    }
                });
    }

}
