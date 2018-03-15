package com.isoftstone.smartsite.common;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.user.LoginBean;
import com.uniview.airimos.listener.OnLoginListener;
import com.uniview.airimos.manager.ServiceManager;
import com.uniview.airimos.parameter.LoginParam;
import com.uniview.airimos.service.KeepaliveService;

/**
 * Description:
 * Created by L02465 on 2017.01.13.
 */
public class NewKeepAliveService extends KeepAliveService implements OnLoginListener,KeepaliveService.OnKeepaliveListener{
    private static final String TAG = "NewKeepAliveService";

    private HttpPost mHttpPost = new HttpPost();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void keepAliveSuccess() {
        HttpPost.mVideoIsLogin = true;
        Log.e(TAG," keepAlive Success ");
    }

    @Override
    public void keepAliveFailure(String error) {
        HttpPost.mVideoIsLogin = false;
        Log.e(TAG," keepAlive Failure " + error);
        LoginParam params = new LoginParam();
        LoginBean loginBean = mHttpPost.mLoginBean;
        if(loginBean != null){
            params.setServer(loginBean.getmVideoParameter().getIp());
            params.setPort(Integer.parseInt(loginBean.getmVideoParameter().getPort()));
            params.setUserName(loginBean.getmVideoParameter().getLoginName());
            params.setPassword(loginBean.getmVideoParameter().getLoginPass());
            //调用登录接口
            ServiceManager.login(params, this);
        }
    }


    @Override
    public void onLoginResult(long logincode, String s) {
         if(logincode == 0){
             HttpPost.mVideoIsLogin = true;
             Log.e(TAG," onLoginResult -------------------------------ok");
         }else{
             Log.e(TAG," onLoginResult -------------------------------fail");
             HttpPost.mVideoIsLogin = false;
         }
    }

    @Override
    public void onKeepaliveFailed() {

    }
}
