package com.bigkoo.billlite;

import android.app.Application;

import com.bigkoo.billlite.manager.AppInfoManager;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

public class BillApplicantion extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppInfoManager.getInstance().init(this);
        onApplicationSettings();
    }

    /**
     * App设置
     */
    private void onApplicationSettings() {
        LoggerSettings();
    }

    /**
     * 配置打印信息
     */
    private void LoggerSettings() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  //不显示线程信息
                .methodCount(2) //方法打印2行信息
                .methodOffset(0)    //打印偏移量
                .tag(getString(R.string.app_name))  //打印标签
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;   //Debug模式才会打印
            }
        });
    }

}
