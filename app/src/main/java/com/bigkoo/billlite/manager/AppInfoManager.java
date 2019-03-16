package com.bigkoo.billlite.manager;

import android.content.Context;

import com.bigkoo.billlite.BuildConfig;

/**
 * App相关数据管理类
 * Created by Sai on 2018/3/20.
 */

public class AppInfoManager {
    private AppInfoManager() {
    }

    private Context context;

    private static final int VERSION_CODE = BuildConfig.VERSION_CODE;
    private static final String VERSION_NAME = BuildConfig.VERSION_NAME;

    /**
     * 获取版本名称
     *
     * @return 版本名称
     */
    public String getVersionName() {
        return VERSION_NAME;
    }

    /**
     * 获取版本号
     *
     * @return 版本号
     */
    public int getVersionCode() {
        return VERSION_CODE;
    }

    private static class AppInfoManagerInstance {
        private static final AppInfoManager INSTANCE = new AppInfoManager();
    }

    public static AppInfoManager getInstance() {
        return AppInfoManagerInstance.INSTANCE;
    }

    public void init(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

}
