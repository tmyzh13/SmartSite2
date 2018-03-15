package com.isoftstone.smartsite.model.index.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.isoftstone.smartsite.LoginActivity;
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.common.AppManager;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.user.InstallBean;
import com.isoftstone.smartsite.model.map.service.DownloadAPKService;
import com.isoftstone.smartsite.utils.NetworkUtils;

/**
 * Created by zw on 2017/11/9.
 */

public class SplashActivity extends BaseActivity {

    private static final int GO_TO_LOGIN = 0x0001;
    private static final int SHOW_UPDATE_APP_DIALOG = 0x0002;

    private int androidType = 0;
    private String apk_url = "";
    private String apk_version = "";


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GO_TO_LOGIN:
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    AppManager.getAppManager().finishCurrentActivity();
                    break;

                case SHOW_UPDATE_APP_DIALOG:
                    if (Build.VERSION.SDK_INT >= 23) {
                        int checkPermission = ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
                        int writePermission = ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (checkPermission != PackageManager.PERMISSION_GRANTED || writePermission != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, 0);
                            return;
                        }
                    }
                    showToUpdateVersionDialog();
                    break;
            }
        }
    };
    private AlertDialog connectNetworkDialog;
    private AlertDialog updateVersionDialog;
    private Runnable checkRunnable;
    private HttpPost mHttppost;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {

        mHttppost = new HttpPost();

        checkRunnable = new Runnable() {
            @Override
            public void run() {
                InstallBean installBean = mHttppost.getSystemConifg();
                if (installBean != null) {
                    String versionName = installBean.getAndroid_version();
                    apk_url = installBean.getAndroid_url();
                    //apk_url = "http://download.sj.qq.com/upload/connAssitantDownload/upload/MobileAssistant_1.apk";
                    androidType = installBean.getAndroid_type();
                    apk_version = installBean.getAndroid_version();
                    if (getAppVersionName() != null && getAppVersionName().compareTo(versionName) < 0) {
                        mHandler.removeMessages(GO_TO_LOGIN);
                        mHandler.sendEmptyMessage(SHOW_UPDATE_APP_DIALOG);
                    }
                }

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkeNetWork();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            showToUpdateVersionDialog();
            return;
        }
    }

    private void checkeNetWork() {
        if (!NetworkUtils.isConnected()) {
            showToConnectNetworkDialog();
        } else {
            new Thread(checkRunnable).start();
            mHandler.sendEmptyMessageDelayed(GO_TO_LOGIN, 3000);

        }
    }

    private void showToConnectNetworkDialog() {
        if (connectNetworkDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("网络连接检查");
            builder.setMessage("网络没有连接，是否去连接网络？");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    SplashActivity.this.finish();
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    NetworkUtils.openWirelessSettings(SplashActivity.this);
                }
            });
            builder.setCancelable(false);
            connectNetworkDialog = builder.create();
        }

        connectNetworkDialog.show();
    }

    private void showToUpdateVersionDialog() {
        if (updateVersionDialog == null) {
            if(androidType != 1){
            //if (true) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("版本更新检查");
                builder.setMessage("发现新版本，是否更新？");
                builder.setNegativeButton("残忍的拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mHandler.sendEmptyMessage(GO_TO_LOGIN);
                    }
                });
                builder.setPositiveButton("愉快的接受", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SplashActivity.this, DownloadAPKService.class);
                        intent.putExtra("apk_url", apk_url);
                        intent.putExtra("apk_version", apk_version);
                        startService(intent);
                    }
                });
                builder.setCancelable(false);
                updateVersionDialog = builder.create();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("版本更新检查");
                builder.setMessage("发现新版本，该版本为强制更新版本，不更新无法正常使用！");
                builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SplashActivity.this, DownloadAPKService.class);
                        intent.putExtra("apk_url", apk_url);
                        intent.putExtra("apk_version", apk_version);
                        startService(intent);
                    }
                });
                builder.setCancelable(false);
                updateVersionDialog = builder.create();
            }
        }

        updateVersionDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 返回当前程序版本名
     */
    public String getAppVersionName() {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = this.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(this.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        return versionName;
    }


}
