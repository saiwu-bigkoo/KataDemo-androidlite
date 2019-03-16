package com.bigkoo.billlite.presenter;


import com.bigkoo.billlite.R;
import com.bigkoo.billlite.bean.CategoryBean;
import com.bigkoo.billlite.bean.CategoryEvent;
import com.bigkoo.billlite.constants.DBConstant;
import com.bigkoo.billlite.db.DBServiceGenerator;
import com.bigkoo.billlite.view.CategoryAddView;
import com.bigkoo.kataframework.bean.HttpResult;
import com.bigkoo.kataframework.http.observer.HttpResultObserver;
import com.bigkoo.kataframework.mvppresenter.BaseListPresenter;
import com.google.gson.Gson;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

/**
 * Created by sai on 2018/4/30.
 */

public class CategoryAddPresenter extends BaseListPresenter<CategoryAddView> {

    private CategoryBean category;

    @Override
    public Observable<HttpResult<CategoryBean>> onLoadDataHttpRequest() {
        return null;
    }

    /**
     * 查询已删除的分类数据
     */
    public void queryDeleteCategories() {
        QueryBuilder<CategoryBean> builder = new QueryBuilder<>(CategoryBean.class)
                .whereEquals(DBConstant.STATUS, 1)
                .whereAppendAnd()
                .whereEquals(DBConstant.TYPE, category.getType());
        onCallHttpRequest(DBServiceGenerator.getInstance().query(builder), queryDeleteCategories);
    }

    /**
     * 保存分类
     *
     * @param category 要添加的分类
     */
    public void save(CategoryBean category) {
        this.category = category;
        //先查询数据库有没相同的名称存在
        QueryBuilder<CategoryBean> builder = new QueryBuilder<>(CategoryBean.class)
                .whereEquals(DBConstant.NAME, category.getName())
                .whereAppendAnd()
                .whereEquals(DBConstant.TYPE, category.getType());
        onCallHttpRequest(DBServiceGenerator.getInstance().query(builder), getTotalCount);
    }

    /**
     * 更新分类
     *
     * @param category
     */
    public void update(CategoryBean category) {
        Logger.d(new Gson().toJson(category));
        onCallHttpRequest(DBServiceGenerator.getInstance().update(category), new HttpResultObserver());
        CategoryEvent event = new CategoryEvent(CategoryEvent.ADD, category);
        post(event);
    }

    //查询已删除的数据结果
    public HttpResultObserver<ArrayList<CategoryBean>> queryDeleteCategories =
            new HttpResultObserver<ArrayList<CategoryBean>>() {
                @Override
                public void onHttpSuccess(ArrayList<CategoryBean> categories, String msg) {
                    getView().onQueryDeleteCategories(categories);
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


    // 插入数据
    public HttpResultObserver<Long> save =
            new HttpResultObserver<Long>() {
                @Override
                public void onHttpSuccess(Long count, String msg) {
                    category.setSort(count.intValue() + 1);//序号为总数+1
                    Logger.d("插入数据：\n" + new Gson().toJson(category));
                    onCallHttpRequest(DBServiceGenerator.getInstance().insert(category), new HttpResultObserver());
                    //发送广播通知页面刷新
                    CategoryEvent event = new CategoryEvent(CategoryEvent.ADD, category);
                    post(event);
                    getView().onSave();

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

    // 数据库没有同名分类后查询数据总数
    public HttpResultObserver<ArrayList<CategoryBean>> getTotalCount =
            new HttpResultObserver<ArrayList<CategoryBean>>() {
                @Override
                public void onHttpSuccess(ArrayList<CategoryBean> categories, String msg) {
                    if (categories.size() == 0) {
                        //查询当前数据的数量
                        onCallHttpRequest(DBServiceGenerator.getInstance().queryCount(CategoryBean.class), save);
                    } else {
                        getView().onStatusError(true, 1, getContext().getString(R.string.categoryadd_exist));
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

    public void setCategory(CategoryBean category) {
        this.category = category;
    }
}
