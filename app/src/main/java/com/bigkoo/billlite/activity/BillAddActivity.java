package com.bigkoo.billlite.activity;

import android.content.Intent;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.billlite.R;
import com.bigkoo.billlite.bean.BillBean;
import com.bigkoo.billlite.bean.CategoryBean;
import com.bigkoo.billlite.constants.Constants;
import com.bigkoo.billlite.constants.EventConstants;
import com.bigkoo.billlite.presenter.BillAddPresenter;
import com.bigkoo.billlite.utils.DateUtil;
import com.bigkoo.billlite.utils.ImageUtil;
import com.bigkoo.billlite.view.BillAddView;
import com.bigkoo.billlite.view.KeyboardView;
import com.bigkoo.commonui.topbar.TopBarView;
import com.bigkoo.horizontalgridpage.HorizontalGridPage;
import com.bigkoo.katafoundation.activity.BaseListActivity;
import com.bigkoo.horizontalgridpage.PageCallBack;
import com.bigkoo.horizontalgridpage.PageBuilder;
import com.bigkoo.horizontalgridpage.PageGridAdapter;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Date;

/**
 * 添加账单
 *
 * @author wuhao
 */

public class BillAddActivity extends BaseListActivity<BillAddPresenter>
        implements BillAddView, View.OnClickListener, PageCallBack {


    private KeyboardView keyboard;//键盘
    private HorizontalGridPage gridPage;//分类网格
    private TimePickerView timePickerView;//时间选择器
    private PageGridAdapter<CategoryBean> adapter;
    private PageBuilder pageBuilder;//网格构建器

    private int selectedPos = -1;//分类选中的位置
    private int oldPos = -1;//分类上一次选中位置
    private int type;//收入还是支出
    private Date billDate;//账单日期
    private BillBean bill;//详情页编辑时传递过来的订单

    private TextView tvIncome;//收入Tab
    private TextView tvPay;//支出Tab
    private boolean editStatus = false;//是否编辑状态

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_billadd;
    }

    @Override
    protected void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        pageBuilder = new PageBuilder.Builder()
                .setGrid(3, 4)//设置网格
                .setPageMargin(5)//页面边距
                .setIndicatorMargins(5, 5, 5, 5)//设置指示器间隔
                .setIndicatorSize(10)//设置指示器大小
                .setIndicatorRes(android.R.drawable.presence_invisible,
                        android.R.drawable.presence_online)//设置指示器图片资源
                .setIndicatorGravity(Gravity.CENTER)
                .setSwipePercent(50)
                .setShowIndicator(true)
                .build();

        tvIncome = findViewById(R.id.tvIncome);
        tvPay = findViewById(R.id.tvPay);
        keyboard = findViewById(R.id.loKeyboard);
        gridPage = findViewById(R.id.gridPage);
        gridPage.init(pageBuilder);

        adapter = new PageGridAdapter<>(this);
        adapter.init(pageBuilder);
        gridPage.setAdapter(adapter);

        timePickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                billDate = date;
                keyboard.setKeyFnText(DateUtil.formatDateTime(date, DateUtil.DF_MMDD));

            }
        }).setType(new boolean[]{false, true, true, false, false, false})
                .setOutSideCancelable(true)
                .setTitleText(getString(R.string.billadd_day_pick))
                .setTitleColor(Color.DKGRAY)
                .setTitleSize(20)
                .setCancelColor(Color.DKGRAY)
                .setSubmitColor(Color.DKGRAY)
                .setContentTextSize(22)
                .build();
    }

    @Override
    protected void initListener() {
        tvIncome.setOnClickListener(this);
        tvPay.setOnClickListener(this);
        keyboard.setOnKeyClickListener(new KeyboardView.OnKeyClickListener() {
            @Override
            public void onKeyEnterClick() {
                //没有输入金额不提交
                if (keyboard.getRightNumber() == 0) {
                    showToast(R.string.billadd_money_empty);
                    return;
                }
                //没有选中分类不提交
                if (selectedPos == -1) {
                    showToast(R.string.billadd_category_empty);
                    return;
                }
                //保存数据
                bill.setBillType(type);
                bill.setCategoryId((adapter.getData().get(selectedPos)).getId());
                //如果没有写备注，就将分类名称填充备注
                bill.setRemark(TextUtils.isEmpty(keyboard.getLeftContent()) ?
                        adapter.getData().get(selectedPos).getName() : keyboard.getLeftContent());
                if (type == Constants.TYPE_INCOME) {
                    bill.setIncome(keyboard.getRightNumber());
                } else {
                    bill.setPay(keyboard.getRightNumber());
                }
                bill.setDate(DateUtil.formatDateTime(billDate, DateUtil.DF_YYYY_MM_DD));
                getPresenter().sumbit(bill);
            }

            @Override
            public void onKeyFnClick(View key) {
                timePickerView.show();
            }
        });
    }

    @Override
    protected void initData() {
        bill = (BillBean) getIntent().getSerializableExtra(EventConstants.DATA);//获取详情传递过来的订单
        if (bill != null) {//详情页跳转的订单编辑页面
            ((TopBarView) findViewById(R.id.topBar)).setTitleText(getString(R.string.billadd_title_edit));
            billDate = DateUtil.parseDate(bill.getDate(), DateUtil.DF_YYYY_MM_DD);

            editStatus = true;
            switch (bill.getBillType()) {
                case Constants.TYPE_INCOME:
                    onTabChange(Constants.TYPE_INCOME);
                    keyboard.setRightNumber(bill.getIncome());
                    break;
                case Constants.TYPE_PAY:
                    onTabChange(Constants.TYPE_PAY);
                    keyboard.setRightNumber(bill.getPay());
                    break;
            }
            keyboard.setLeftContent(bill.getRemark());
        } else {//新增按钮跳转的订单添加页面
            editStatus = false;
            bill = new BillBean();
            billDate = new Date();
            onTabChange(Constants.TYPE_PAY);
        }
        keyboard.setKeyFnText(DateUtil.formatDateTime(billDate, DateUtil.DF_MMDD));

    }

    /**
     * 更新订单分类为选中状态
     *
     * @param billId
     */
    private void updateSelectedCategory(int billId) {
        for (int i = 0; i < adapter.getData().size(); i++) {
            if (adapter.getData().get(i) != null && billId == adapter.getData().get(i).getId()) {
                selectedPos = i;
                updatePosition(selectedPos);
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPay:
                onTabChange(Constants.TYPE_PAY);
                break;
            case R.id.tvIncome:
                onTabChange(Constants.TYPE_INCOME);
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_billadd_category, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CategoryHolder) holder).tvCategory.setText(adapter.getData().get(position).getName());
        ((CategoryHolder) holder).ivCategory.setImageResource(ImageUtil.getResId(adapter.getData().get(position).getIcon()));
        ((CategoryHolder) holder).update(position);
    }

    @Override
    public void onItemClickListener(View view, int position) {
        updatePosition(position);
    }

    @Override
    public void onItemLongClickListener(View view, int position) {

    }

    /**
     * 自定义分类容器
     */
    public class CategoryHolder extends RecyclerView.ViewHolder {

        TextView tvCategory;
        ImageView ivCategory;

        private CategoryHolder(View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            ivCategory = itemView.findViewById(R.id.ivCategory);
        }

        /**
         * 更新显示
         *
         * @param position 要更新的位置
         */
        public void update(int position) {
            if (position == selectedPos) {
                ivCategory.setBackgroundResource(R.mipmap.bg_category_focus);
            } else {
                ivCategory.setBackgroundResource(R.mipmap.bg_category_normal);
            }
        }
    }

    /**
     * 更新分类选中位置
     *
     * @param position 选中位置
     */
    public void updatePosition(int position) {
        if (selectedPos != -1) {
            oldPos = selectedPos;
        }
        selectedPos = position;
        if (oldPos != -1) {
            adapter.notifyItemChanged(oldPos);
        }
        adapter.notifyItemChanged(selectedPos);
    }

    @Override
    public void onSubmit() {
        getPresenter().post(EventConstants.BILLLIST_REFRESH);
        Intent intent = new Intent();
        intent.putExtra(EventConstants.DATA, bill);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 收入支出Tab切换
     *
     * @param tab 收入还是支出
     */
    public void onTabChange(int tab) {
        type = tab;
        selectedPos = -1;//将选择的分类清除
        oldPos = -1;//将之前的分类清除
        switch (tab) {
            case Constants.TYPE_INCOME:
                tvIncome.setTextColor(getResources().getColor(R.color.app_text_focus));
                tvPay.setTextColor(getResources().getColor(R.color.app_text_normal));
                break;
            case Constants.TYPE_PAY:
                tvIncome.setTextColor(getResources().getColor(R.color.app_text_normal));
                tvPay.setTextColor(getResources().getColor(R.color.app_text_focus));
                break;
        }

        getPresenter().setType(type);
        getPresenter().onLoadData();

    }

    @Override
    public void onDataSetChange(Object data) {
        Logger.d("获取分类数据：\n" + new Gson().toJson(data));
        adapter.setData((ArrayList<CategoryBean>) data);
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return null;
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
        if (bill != null && editStatus) {
            updateSelectedCategory(bill.getCategoryId());
            editStatus = false;
        }
    }
}
