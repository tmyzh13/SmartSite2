package com.isoftstone.smartsite.common;

import android.app.Application;
import android.content.Context;

import com.isoftstone.smartsite.utils.NetworkUtils;
import com.tencent.bugly.crashreport.CrashReport;


/**
 * Created by zw on 2017/10/11.
 */

public class App extends Application {

    private static App mInstance;
    public static int mNetWorkState;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initNetWorkData();
        CrashReport.initCrashReport(getAppContext(),"5ba8f42531",false);
    }

    private void initNetWorkData() {
        mNetWorkState = NetworkUtils.getNetworkType(this);
    }

    public static Context getAppContext(){
        return mInstance.getApplicationContext();
    }
}
