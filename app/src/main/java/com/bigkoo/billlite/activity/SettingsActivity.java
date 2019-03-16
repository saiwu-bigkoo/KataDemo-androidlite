package com.bigkoo.billlite.activity;

import android.content.Intent;
import android.view.View;

import com.bigkoo.billlite.R;
import com.bigkoo.katafoundation.activity.BaseActivity;

/**
 * 设置页面
 *
 * @author wuhao
 */
public class SettingsActivity extends BaseActivity {

    private static final int CATEGORYMANAGE = R.id.loCategotyManage;
    private static final int SHARE = R.id.loShare;
    private static final int FEEDBACK = R.id.loFeedback;
    private static final int PERMISSION = R.id.loPermission;
    private static final int ABOUT = R.id.loAbout;


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }


    public void onItemClick(View view) {
        switch (view.getId()) {
            case CATEGORYMANAGE:
                startActivity(new Intent(SettingsActivity.this, CategoryManageActivity.class));
                break;
            case FEEDBACK:
                startActivity(new Intent(SettingsActivity.this, FeedbackActivity.class));
                break;
            case ABOUT:
                startActivity(new Intent(SettingsActivity.this, AboutActivity.class));
                break;
        }
    }

}
