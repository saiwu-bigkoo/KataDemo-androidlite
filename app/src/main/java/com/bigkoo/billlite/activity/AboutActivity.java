package com.bigkoo.billlite.activity;

import android.view.View;
import android.widget.TextView;

import com.bigkoo.billlite.R;
import com.bigkoo.billlite.manager.AppInfoManager;
import com.bigkoo.katafoundation.activity.BaseActivity;

/**
 * 关于App页面
 *
 * @author wuhao
 */
public class AboutActivity extends BaseActivity {

    private TextView tvVersion;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        tvVersion = findViewById(R.id.tvVersion);
    }

    @Override
    protected void initData() {
        tvVersion.setText(String.format(getString(R.string.about_version),
                AppInfoManager.getInstance().getVersionName()));
    }
}
