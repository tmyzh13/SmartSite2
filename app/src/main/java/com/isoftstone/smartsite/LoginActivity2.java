/*
 * Copyright (c) 2013, ZheJiang Uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 *------------------------------------------------------------------------------
 * Product     : IMOS
 * Module Name : 
 * Date Created: 2017-10-19
 * Creator     : zhangyinfu
 * Description : 
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 * 
 *------------------------------------------------------------------------------
 */
package com.isoftstone.smartsite;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.isoftstone.smartsite.common.NewKeepAliveService;
import com.uniview.airimos.listener.OnLoginListener;
import com.uniview.airimos.manager.ServiceManager;
import com.uniview.airimos.parameter.LoginParam;
import com.uniview.airimos.service.KeepaliveService;

/**
 * 登录界面
 * created by zhangyinfu 2017-10-19
 * modified by zhangyinfu 2017-10-21
 */
public class LoginActivity2 extends Activity implements OnLoginListener,KeepaliveService.OnKeepaliveListener
{
    private Button mBtnLoginPass;
    private Button mBtnLogin;
    private EditText mEditServer;
    private EditText mEditPort;
    private EditText mEditUserName;
    private EditText mEditUserPasswd;
    private View mLoginProgress;

    private KeepaliveService mKeepService = null;
    private boolean isBound = false;
    private Intent intentService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);


        mBtnLoginPass = (Button) findViewById(R.id.btn_login_pass);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mEditServer = (EditText) findViewById(R.id.edittext_server);
        mEditPort = (EditText) findViewById(R.id.edittext_port);
        mEditUserName = (EditText) findViewById(R.id.edittext_username);
        mEditUserPasswd = (EditText) findViewById(R.id.edittext_password);
        mBtnLogin.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // 设置登录参数
                    LoginParam params = new LoginParam();
                    params.setServer(mEditServer.getText().toString());
                    params.setPort(Integer.parseInt(mEditPort.getText().toString()));
                    params.setUserName(mEditUserName.getText().toString());
                    params.setPassword(mEditUserPasswd.getText().toString());
                    
                    //调用登录接口
                    ServiceManager.login(params, LoginActivity2.this);
                    //显示登录progress
                    showProgress();
                }
            });

        mBtnLoginPass.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity2.this,MainActivity.class);
                LoginActivity2.this.startActivity(intent);
            }
        });

        mLoginProgress = (View) findViewById(R.id.login_progress);
    }
    
    
    /**
     * 登录结果返回
     */
    @Override
    public void onLoginResult(long errorCode, String errorDesc)
    {
        Log.i("zyf","onLoginResult   errorCode=" + errorCode);
        //隐藏登录progress
        hidProgress();

        //成功为0，其余为失败错误码
        if (errorCode == 0)
        {
            startKeepaliveService();
            Intent intent = new Intent();
            intent.setClass(LoginActivity2.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(LoginActivity2.this, "登录失败：" + errorCode + "," + errorDesc, Toast.LENGTH_LONG).show();
            
        }
    }

    //启动保活服务
    public void startKeepaliveService(){
        Intent toService = new Intent(this, NewKeepAliveService.class);
        startService(toService);
    }

    public ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            KeepaliveService.KeepaliveBinder binder = (KeepaliveService.KeepaliveBinder) service;
            mKeepService =  binder.getService();
            mKeepService.start(LoginActivity2.this);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    public void onKeepaliveFailed() {
        Toast.makeText(LoginActivity2.this, "保活失败，请重新登录", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopKeepaliveService();
    }

    public void stopKeepaliveService(){
        Intent intent = new Intent(this, NewKeepAliveService.class);
        stopService(intent);
        return;
    }

    public void showProgress() {
        mLoginProgress.setVisibility(View.VISIBLE);
        mBtnLogin.setEnabled(false);
        mEditServer.setEnabled(false);
        mEditPort.setEnabled(false);
        mEditUserName.setEnabled(false);
        mEditUserPasswd.setEnabled(false);
    }

    public void hidProgress() {
        mLoginProgress.setVisibility(View.GONE);
        mBtnLogin.setEnabled(true);
        mEditServer.setEnabled(true);
        mEditPort.setEnabled(true);
        mEditUserName.setEnabled(true);
        mEditUserPasswd.setEnabled(true);
    }
}
