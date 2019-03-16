package com.bigkoo.billlite.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.billlite.R;
import com.bigkoo.billlite.adapter.BillGroupByDateAdapter;
import com.bigkoo.billlite.bean.DayGroupBean;
import com.bigkoo.billlite.constants.EventConstants;
import com.bigkoo.billlite.presenter.BillListPresenter;
import com.bigkoo.billlite.utils.DateUtil;
import com.bigkoo.billlite.utils.StringUtil;
import com.bigkoo.billlite.view.BillListView;
import com.bigkoo.commonui.topbar.TopBarView;
import com.bigkoo.katafoundation.activity.BaseListActivity;
import com.bigkoo.katafoundation.activity.BaseListRAMActivity;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 主界面
 */
public class MainActivity extends BaseListRAMActivity<BillListPresenter>
        implements BillListView{

    private TopBarView tbTitle;
    private TextView tvMonth;
    private TextView tvPay;
    private TextView tvIncome;
    private TextView tvYear;
    private TimePickerView timePickerView;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        getPresenter().initCategory();
        getPresenter().onLoadDayGroups();
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new BillGroupByDateAdapter(this);
    }

    @Override
    protected void initView() {
        refreshable = false;
        super.initView();
        recyclerView.addItemDecoration(((BillGroupByDateAdapter) adapter).getDecoration());
        tbTitle = findViewById(R.id.tbTitle);
        tbTitle.setRightIcon(R.mipmap.ic_settings);
        tbTitle.setLeftIcon(R.mipmap.ic_chart);
        tvMonth = findViewById(R.id.tvMonth);
        tvPay = findViewById(R.id.tvPay);
        tvIncome = findViewById(R.id.tvIncome);
        tvYear = findViewById(R.id.tvYear);

        setDateText(getPresenter().getDate());
        tvIncome.setText(formatText("0.00", 1.5f, 0, 1));
        tvPay.setText(formatText("0.00", 1.5f, 0, 1));

        timePickerView = new TimePickerBuilder(MainActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                setDateText(date);

                getPresenter().setDate(date);

                getPresenter().onLoadDayGroups();
            }
        }).setType(new boolean[]{false, true, false, false, false, false})
                //.setLineSpacingMultiplier(2.0f)
                .setOutSideCancelable(true)
                .setTitleText(getString(R.string.main_month_pick))
                .setTitleColor(Color.DKGRAY)
                .setTitleSize(20)
                .setCancelColor(Color.DKGRAY)
                .setSubmitColor(Color.DKGRAY)
                .setContentTextSize(22)
                .build();
    }

    @Override
    protected void initListener() {
        super.initListener();
        getPresenter().toObservable(String.class, new Consumer<String>() {

            @Override
            public void accept(String s) throws Exception {
                if(EventConstants.BILLLIST_REFRESH.equals(s)){
                    //刷新列表
                    getPresenter().onLoadDayGroups();
                }
            }
        });
    }

    private void onMonthGroupsChange(List<DayGroupBean> datas) {
        double incomeCount = 0.00;
        double payCount = 0.00;
        for (DayGroupBean item : datas) {
            incomeCount = incomeCount + item.getIncome();
            payCount = payCount + item.getPay();
        }
        String income = StringUtil.doubleTwoDecimal(incomeCount);
        String pay = StringUtil.doubleTwoDecimal(payCount);


        tvIncome.setText(formatText(income, 1.5f, 0, income.length() - 3));
        tvPay.setText(formatText(pay, 1.5f, 0, pay.length() - 3));

    }

    /**
     * 格式化顶部数据显示
     *
     * @param text       格式化的文字
     * @param proportion 放大倍数
     * @param start      开始位置
     * @param end        结束位置
     * @return
     */
    private SpannableString formatText(String text, float proportion, int start, int end) {
        SpannableString msp = new SpannableString(text);
        msp.setSpan(new RelativeSizeSpan(proportion), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return msp;
    }

    /**
     * 新增账单
     *
     * @param view 新增按钮
     */
    public void onAddRecord(View view) {
        startActivity(new Intent(this, BillAddActivity.class));
    }

    @Override
    public void onDayGroupsChange(Object data) {
        ((BillGroupByDateAdapter) adapter).setDayGroups((List<DayGroupBean>) data);
        onMonthGroupsChange((List<DayGroupBean>) data);
    }

    @Override
    public void onTopBarBack(View view) {
        startActivity(new Intent(this, ChartsActivity.class));
    }

    public void onTopBarRightClick(View view) {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(MainActivity.this, BillDetailActivity.class);
        long billId = ((BillGroupByDateAdapter) adapter).getItem(position).getId();
        intent.putExtra("billId", billId);
        startActivity(intent);


    }

    @Override
    public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        return false;
    }


    public void onMonthSelect(View view) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getPresenter().getDate());
        timePickerView.setDate(calendar);
        timePickerView.show();
    }

    @Override
    public void onDataSetChange(Object data) {
        addData((List<Object>) data);
        Logger.d(new Gson().toJson(data));
    }

    @Override
    public void onLoadingMore(boolean isLoadingMore) {

    }

    @Override
    public void onRefreshing(boolean refreshing) {
    }

    @Override
    public void onStatusEmpty(boolean statusEmpty) {

    }

    @Override
    public void onStatusLoading(boolean statusLoading) {

    }

    @Override
    public void onStatusError(boolean statusError, int code, String msg) {

    }

    @Override
    public void onStatusNetworkError(boolean statusNetworkError, String msg) {

    }

    @Override
    public void onLoadComplete() {

    }

    public void setDateText(Date date) {
        tvYear.setText(DateUtil.formatDateTime(date, "yyyy年"));
        tvMonth.setText(formatText(DateUtil.formatDateTime(date, "MM"), 1.5f, 0, 2));
    }
}
