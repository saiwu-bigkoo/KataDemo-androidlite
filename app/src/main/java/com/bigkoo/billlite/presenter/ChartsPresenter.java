package com.bigkoo.billlite.presenter;

import com.bigkoo.billlite.bean.DayGroupBean;
import com.bigkoo.billlite.constants.Constants;
import com.bigkoo.billlite.constants.DBConstant;
import com.bigkoo.billlite.db.DBServiceGenerator;
import com.bigkoo.billlite.utils.DateUtil;
import com.bigkoo.billlite.view.ChartsView;
import com.bigkoo.kataframework.bean.HttpResult;
import com.bigkoo.kataframework.http.observer.HttpResultObserver;
import com.bigkoo.kataframework.mvppresenter.BaseDetailPresenter;
import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import io.reactivex.Observable;

public class ChartsPresenter extends BaseDetailPresenter<ChartsView> {
    String format;

    @Override
    public <Data> Observable<HttpResult<Data>> onLoadDataHttpRequest() {
        return null;
    }


    public ArrayList<Entry> getMockLineChartData(int count, float range) {

        ArrayList<Entry> yAxisValues = new ArrayList<>();
        //收入数据
        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range);
            yAxisValues.add(new Entry(i, val));
        }

        return yAxisValues;
    }

    public HashMap getMockPieChartData(int type) {
        HashMap dataMap = new HashMap();
        if (type == Constants.TYPE_PAY) {
            dataMap.put("餐饮", "900.40");
            dataMap.put("服饰", "800.10");
            dataMap.put("交通", "500.50");
            dataMap.put("水果", "300.35");
            dataMap.put("礼金", "100.85");
        } else {
            dataMap.put("工资", "5000.00");
            dataMap.put("补贴", "1000.00");
            dataMap.put("利息", "400.00");
            dataMap.put("外快", "200.00");
            dataMap.put("红包", "100.00");
        }
        return dataMap;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * 查询查询时间当月的数据
     *
     * @param date
     */
    public void lineChartForMonth(String date) {
        String sql = "SELECT SUM(" + DBConstant.PAY + ") AS " + DBConstant.PAY
                + " , SUM(" + DBConstant.INCOME + ") AS " + DBConstant.INCOME + ", "
                + DBConstant.DATE
                + " FROM " + DBConstant.TABLE_BILLDETAIL
                + " WHERE " + DBConstant.DATE + " LIKE " + "\'" + "%" + date + "%\'"
                + " GROUP BY " + DBConstant.DATE;
        Logger.d("查询：" + date + "月");

        onCallHttpRequest(DBServiceGenerator.getInstance().rawQuery(sql, DayGroupBean.class), new HttpResultObserver() {
            @Override
            public void onHttpSuccess(Object result, String msg) {
                super.onHttpSuccess(result, msg);
                Logger.d(new Gson().toJson(result));
                getView().onLineChartDataSetChange(result);
            }
        });
    }

    /**
     * 查询距查询时间前7天数据
     *
     * @param date 查询时间
     */
    public void lineChartForWeek(String date) {
        String sql = "SELECT SUM(" + DBConstant.PAY + ") AS " + DBConstant.PAY
                + " , SUM(" + DBConstant.INCOME + ") AS " + DBConstant.INCOME + ", "
                + DBConstant.DATE
                + " FROM " + DBConstant.TABLE_BILLDETAIL
                + " WHERE " + DBConstant.DATE + " LIKE " + "\'" + "%" + date + "%\'"
                + " GROUP BY " + DBConstant.DATE + " ORDER BY " + DBConstant.DATE + " DESC LIMIT 7";
        Logger.d("查询：" + date + "前7天");

        onCallHttpRequest(DBServiceGenerator.getInstance().rawQuery(sql, DayGroupBean.class), new HttpResultObserver() {
            @Override
            public void onHttpSuccess(Object result, String msg) {
                super.onHttpSuccess(result, msg);
                Logger.d(new Gson().toJson(result));
                getView().onLineChartDataSetChange(result);
            }
        });
    }


}
