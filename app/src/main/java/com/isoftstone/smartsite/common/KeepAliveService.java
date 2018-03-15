package com.isoftstone.smartsite.common;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.uniview.airimos.listener.OnKeepaliveListener;
import com.uniview.airimos.manager.ServiceManager;

import java.util.Timer;
import java.util.TimerTask;

//import android.support.annotation.Nullable;

/**
 * Description:
 * Created by zhangyinfu on 2017.10.21.
 */
public abstract class KeepAliveService extends Service  {



    public abstract void keepAliveSuccess();

    public abstract void keepAliveFailure(String error);

    private Timer mTimer;

    //@Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startKeepAlive();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void startKeepAlive(){
        if (mTimer == null){
            mTimer = new Timer();
        }

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                Log.e("eee", "保活进行中..."  );
                ServiceManager.keepalive(new OnKeepaliveListener() {
                    @Override
                    public void onKeepaliveResult(long errorCode, String errorMsg) {
                        if (errorCode != 0){
                            //保活失败

                            keepAliveFailure(errorMsg);
                        }else {
                            keepAliveSuccess();
                        }
                    }
                });
            }
        },1000,10000);    //每隔10s进行保活
    }
}
