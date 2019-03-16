package com.bigkoo.billlite.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.billlite.R;
import com.bigkoo.billlite.constants.Constants;
import com.bigkoo.billlite.fragment.ChartsFragment;
import com.bigkoo.billlite.utils.DateUtil;
import com.bigkoo.katafoundation.activity.BaseActivity;

/**
 * 数据分析页面，采用MPAndroidChart
 *
 * @author wuhao
 */
public class ChartsActivity extends BaseActivity{

    private static final int WEEK = R.id.tvWeek;
    private static final int MONTY = R.id.tvMonth;
    private static final int YEAR = R.id.tvYear;

    private TextView tvWeek, tvMonth, tvYear;

    public FragmentManager fragmentManager;


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_charts;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        tvWeek = findViewById(R.id.tvWeek);
        tvMonth = findViewById(R.id.tvMonth);
        tvYear = findViewById(R.id.tvYear);

        fragmentManager = getSupportFragmentManager();
        onChangeFragment(WEEK);//默认显示周视图
    }

    public void onChangeFragment(View view) {
        onChangeFragment(view.getId());
    }

    private void onChangeFragment(int id) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        hideFragment(ft);
        clearSelection();
        Fragment fragment = null;

        switch (id) {
            case WEEK:
                fragment = fragmentManager.findFragmentByTag(String.valueOf(Constants.WEEK));
                if (fragment == null) {
                    fragment = ChartsFragment.newInstance(DateUtil.DF_YYYY_MM_DD);
                    ft.add(R.id.loContent, fragment, String.valueOf(Constants.WEEK));
                }
                break;
            case MONTY:
                fragment = fragmentManager.findFragmentByTag(String.valueOf(Constants.MONTH));
                if (fragment == null) {
                    fragment = ChartsFragment.newInstance(DateUtil.DF_YYYY_MM);
                    ft.add(R.id.loContent, fragment, String.valueOf(Constants.MONTH));
                }
                break;
            case YEAR:
                fragment = fragmentManager.findFragmentByTag(String.valueOf(Constants.YEAR));
                if (fragment == null) {
                    fragment = ChartsFragment.newInstance(DateUtil.DF_YYYY);
                    ft.add(R.id.loContent, fragment, String.valueOf(Constants.YEAR));
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
        tvWeek.setTextColor(getResources().getColor(R.color.app_text_normal));
        tvMonth.setTextColor(getResources().getColor(R.color.app_text_normal));
        tvYear.setTextColor(getResources().getColor(R.color.app_text_normal));
    }

}
