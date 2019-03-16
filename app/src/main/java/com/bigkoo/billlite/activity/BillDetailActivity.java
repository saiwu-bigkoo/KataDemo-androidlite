package com.bigkoo.billlite.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.billlite.R;
import com.bigkoo.billlite.bean.BillBean;
import com.bigkoo.billlite.bean.CategoryBean;
import com.bigkoo.billlite.constants.Constants;
import com.bigkoo.billlite.constants.EventConstants;
import com.bigkoo.billlite.presenter.BillDetailPresenter;
import com.bigkoo.billlite.utils.ImageUtil;
import com.bigkoo.billlite.view.BillDetailView;
import com.bigkoo.katafoundation.activity.BaseDetailActivity;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;


/**
 * 账单详情页面
 *
 * @author wuhao
 */
public class BillDetailActivity extends BaseDetailActivity<BillDetailPresenter> implements BillDetailView {

    private TextView tvBillType;   //账单类型
    private TextView tvMoney;   //金额
    private TextView tvDate;    //日期
    private TextView tvRemark;  //备注
    private TextView tvCategory;//分类
    private ImageView ivCategory;//分类图标
    private final int REQUESTCODE_EDIT = 1;

    private static final int EDIT = R.id.tvEdit;
    private static final int DELETE = R.id.tvDelete;

    private long billId;    //订单ID
    private BillBean bill;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_billdetail;
    }

    @Override
    protected void initView() {
        tvBillType = findViewById(R.id.tvBillType);
        tvMoney = findViewById(R.id.tvMoney);
        tvDate = findViewById(R.id.tvDate);
        tvRemark = findViewById(R.id.tvRemark);
        tvCategory = findViewById(R.id.tvCategory);
        ivCategory = findViewById(R.id.ivCategory);
    }

    @Override
    protected void initData() {

        //获取上一个页面传递过来的账单ID
        billId = getIntent().getLongExtra("billId", 1);
        Logger.d("订单ID："+billId);
        getPresenter().setBillId(billId);
        getPresenter().onLoadData();
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
    public void onDataSetChange(Object data) {
        bill = (BillBean) data;
        Logger.d("订单：\n"+ new Gson().toJson(data));
        //显示到页面上
        int type = bill.getBillType();
        String billType = type == Constants.TYPE_INCOME ?
                getString(R.string.billtype_income) : getString(R.string.billtype_pay);
        tvBillType.setText(billType);

        String money = type == Constants.TYPE_INCOME ?
                String.valueOf(bill.getIncome()) : String.valueOf(bill.getPay());
        tvMoney.setText(money);

        tvDate.setText(bill.getDate());

        tvRemark.setText(bill.getRemark());
        getPresenter().queryCategory(bill.getCategoryId());
    }

    @Override
    public void onLoadComplete() {

    }

    /**
     * 查询分类成功后调用并显示数据
     *
     * @param categoryBean
     */
    public void onQueryCategroy(CategoryBean categoryBean) {
        if (categoryBean != null) {
            tvCategory.setText(categoryBean.getName());
            ivCategory.setImageResource(ImageUtil.getResId(categoryBean.getIcon()));
            ivCategory.setBackgroundResource(R.mipmap.bg_category_normal);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case EDIT:
                Intent intent = new Intent(this, BillAddActivity.class);
                intent.putExtra(EventConstants.DATA, bill);
                startActivityForResult(intent, REQUESTCODE_EDIT);
                break;
            case DELETE:
                new AlertView.Builder().setContext(this).setStyle(AlertView.Style.Alert)
                        .setTitle(getString(R.string.app_option_delete))
                        .setMessage(getString(R.string.billdetail_delete_message))
                        .setDestructive(new String[]{getString(R.string.app_option_delete)})
                        .setOthers(new String[]{getString(R.string.app_option_cancel)})
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(Object o, int position) {
                                if (position == 0)
                                    getPresenter().delete(bill);
                            }
                        })
                        .build().show();
                break;
        }
    }

    @Override
    public void onDeleteComplete() {
        getPresenter().post(EventConstants.BILLLIST_REFRESH);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK)return;

        bill = (BillBean) data.getSerializableExtra(EventConstants.DATA);
        onDataSetChange(bill);
    }
}
