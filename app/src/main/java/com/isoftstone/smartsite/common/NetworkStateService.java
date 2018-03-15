package com.isoftstone.smartsite.common;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.model.inspectplan.activity.ApprovalPendingInspectPlansActivity;
import com.isoftstone.smartsite.utils.NetworkUtils;
import com.isoftstone.smartsite.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by zhang on 2017/11/29.
 */

public class NetworkStateService extends Service{

    private static final String TAG = "NetworkStateService";
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo mNetworkInfo;
    private NetworkConnectChangedReceiver mReceiver;

    /**private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Log.d(tag, "网络状态已经改变");
                connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();
                if(info != null && info.isAvailable()) {
                    String name = info.getTypeName();
                    Log.d(tag, "当前网络名称：" + name);
                    //doSomething()
                } else {
                    Log.d(tag, "没有可用网络");
                    //doSomething()
                }
            }
        }
    };*/

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mReceiver = new NetworkConnectChangedReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public interface NetEventHandler {
        public void onNetChange(boolean isConnected);
    }

    public static class NetworkConnectChangedReceiver extends BroadcastReceiver {

        public static ArrayList<NetEventHandler> mListeners = new ArrayList<NetEventHandler>();
        //private static String NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                App.mNetWorkState = NetworkUtils.getNetworkType(context);
                Log.i("zzz", "onReceive......"  + mListeners.size());
                if (mListeners.size() > 0) {// 通知接口完成加载
                    for (NetEventHandler handler : mListeners) {
                        handler.onNetChange(NetworkUtils.isConnected());
                    }
                }

                if (NetworkUtils.isConnected()) {
                    //ToastUtils.showShort("网络已连接");
                } else {
                    //ToastUtils.showShort("无网络");
                }

            }
        }
    }
}