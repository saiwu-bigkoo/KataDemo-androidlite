package com.bigkoo.billlite.activity;

import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.billlite.R;
import com.bigkoo.billlite.constants.Constants;
import com.bigkoo.billlite.fragment.CategoryManageFragment;
import com.bigkoo.katafoundation.activity.BaseActivity;

/**
 * Created by sai on 2018/4/30.
 * 分类编辑
 */

public class CategoryManageActivity extends BaseActivity {

    private static final int INCOME = R.id.tvIncome;
    private static final int PAY = R.id.tvPay;

    private TextView tvIncome;
    private TextView tvPay;

    private int currentTab;

    public FragmentManager fragmentManager;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_categorymanage;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        tvIncome = findViewById(R.id.tvIncome);
        tvPay = findViewById(R.id.tvPay);

        fragmentManager = getSupportFragmentManager();
        onChangeFragment(PAY);//默认显示周视图
    }

    public void onChangeFragment(View view) {
        onChangeFragment(view.getId());
    }

    private void onChangeFragment(int id) {
        currentTab = id;//记录当前Tab，用于添加分类时判断
        FragmentTransaction ft = fragmentManager.beginTransaction();
        hideFragment(ft);
        clearSelection();
        Fragment fragment = null;

        switch (id) {
            case INCOME:
                fragment = fragmentManager.findFragmentByTag(String.valueOf(Constants.TYPE_INCOME));
                if (fragment == null) {
                    fragment = new CategoryManageFragment();
                    ft.add(R.id.loContent, fragment, String.valueOf(Constants.TYPE_INCOME));
                }
                break;
            case PAY:
                fragment = fragmentManager.findFragmentByTag(String.valueOf(Constants.TYPE_PAY));
                if (fragment == null) {
                    fragment = new CategoryManageFragment();
                    ft.add(R.id.loContent, fragment, String.valueOf(Constants.TYPE_PAY));
                }
                break;

        }
        if (fragment != null) {
            ((TextView) findViewById(id)).setTextColor(getResources().getColor(R.color.app_text_focus));
            ft.show(fragment);
        }
        ft.commit();
    }

    /**
     * 隐藏所有fragment
     *
     * @param ft
     */
    public void hideFragment(FragmentTransaction ft) {
        for (Fragment fragment : fragmentManager.getFragments()) {
            ft.hide(fragment);
        }
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        tvIncome.setTextColor(getResources().getColor(R.color.app_text_normal));
        tvPay.setTextColor(getResources().getColor(R.color.app_text_normal));
    }

    /**
     * 添加分类
     *
     * @param view 添加分类按钮
     */
    public void onAddCategory(View view) {
        Intent intent = new Intent(CategoryManageActivity.this, CategoryAddActivity.class);
        if (currentTab == INCOME) {
            intent.putExtra("tab", Constants.TYPE_INCOME);
        } else {
            intent.putExtra("tab", Constants.TYPE_PAY);
        }

        startActivity(intent);
    }
}
