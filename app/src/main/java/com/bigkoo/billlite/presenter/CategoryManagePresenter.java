package com.bigkoo.billlite.presenter;

import com.bigkoo.billlite.bean.CategoryBean;
import com.bigkoo.billlite.constants.DBConstant;
import com.bigkoo.billlite.db.DBServiceGenerator;
import com.bigkoo.billlite.view.CategoryManageView;
import com.bigkoo.kataframework.bean.HttpResult;
import com.bigkoo.kataframework.http.observer.HttpResultObserver;
import com.bigkoo.kataframework.mvppresenter.BaseListPresenter;
import com.google.gson.Gson;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.orhanobut.logger.Logger;

import java.util.List;

import io.reactivex.Observable;

/**
 * 依据收支类型操作数据库分类表
 *
 * @author wuhao
 */

public class CategoryManagePresenter extends BaseListPresenter<CategoryManageView> {

    private int type;//收入还是支出

    @Override
    public Observable<HttpResult<CategoryBean>> onLoadDataHttpRequest() {
        Logger.d(type);
        //按sort 进行排序查询
        QueryBuilder<CategoryBean> builder = new QueryBuilder<>(CategoryBean.class)
                .whereEquals("type", type)
                .whereAppendAnd()
                .whereEquals("status", 0)
                .appendOrderAscBy(DBConstant.SORT);
        return DBServiceGenerator.getInstance().query(builder);
    }

    public void onUpdate(List<CategoryBean> datas) {
        onCallHttpRequest(DBServiceGenerator.getInstance().update(datas), new HttpResultObserver());
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
     * 删除数据（软删除，更新status字段为1）
     *
     * @param category 要更新的记录
     */
    public void onDelete(CategoryBean category) {
        category.setStatus(1);
        Logger.d(new Gson().toJson(category));
        onCallHttpRequest(DBServiceGenerator.getInstance().update(category), new HttpResultObserver());
    }
}
