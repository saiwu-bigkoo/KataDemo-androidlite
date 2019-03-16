package com.bigkoo.billlite.presenter;

import com.bigkoo.billlite.bean.BillBean;
import com.bigkoo.billlite.bean.CategoryBean;
import com.bigkoo.billlite.constants.DBConstant;
import com.bigkoo.billlite.db.DBServiceGenerator;
import com.bigkoo.billlite.utils.DateUtil;
import com.bigkoo.billlite.view.BillAddView;
import com.bigkoo.kataframework.bean.HttpResult;
import com.bigkoo.kataframework.http.observer.HttpResultObserver;
import com.bigkoo.kataframework.mvppresenter.BaseDetailPresenter;
import com.bigkoo.kataframework.mvppresenter.BaseListPresenter;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Date;

import io.reactivex.Observable;

/**
 * Created by sai on 2018/5/1.
 */

public class BillAddPresenter extends BaseListPresenter<BillAddView> {

    private int type;//收入还是支出

    @Override
    public Observable<HttpResult<CategoryBean>> onLoadDataHttpRequest() {
        QueryBuilder<CategoryBean> builder = new QueryBuilder<>(CategoryBean.class)
                .whereEquals(DBConstant.TYPE, type)
                .whereAppendAnd()
                .whereEquals(DBConstant.STATUS, 0)
                .appendOrderAscBy(DBConstant.SORT);
        return DBServiceGenerator.getInstance().query(builder);
    }

    /**
     * 设置收入还是支出
     *
     * @param type 收入还是支出
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * 保存账单
     *
     * @param bill 要保存的账单
     */
    public void sumbit(BillBean bill) {
        onCallHttpRequest(DBServiceGenerator.getInstance().save(bill), new HttpResultObserver() {
            @Override
            public void onHttpSuccess(Object result, String msg) {
                getView().onSubmit();
            }
        });
    }
}
