package com.isoftstone.smartsite.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.common.NewKeepAliveService;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.model.main.ui.MainFragment;
import com.isoftstone.smartsite.model.main.ui.VideoMonitoringActivity;
import com.isoftstone.smartsite.model.video.VideoPlayActivity;
import com.isoftstone.smartsite.model.video.VideoRePlayActivity;
import com.uniview.airimos.listener.OnLoginListener;
import com.uniview.airimos.manager.ServiceManager;
import com.uniview.airimos.parameter.LoginParam;

/**
 * Created by zhang on 2017/11/17.
 */

public class Utils {

    /* 查询请求识别码 登陆成功*/
    private static final int LOGIN_RESULTS_SUCCESSFUL_CODE = 1;
    /* 查询请求识别码 登陆失败*/
    private static final int LOGIN_RESULTS_FAILED_CODE = 2;
    /* 查询请求识别码 登陆异常*/
    private static final int LOGIN_RESULTS_EXCEPTION_CODE = 3;
    private static int mLoginResultCode = 0;

    public static final int ENTER_VIDEO_DEVICE_LIST = 0;//视频监控
    public static final int ENTER_REAL_TIME_VIDEO = 1;//实时视频
    public static final int ENTER_HISTORICAL_VIDEO = 2;//历史监控

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(PackageManager pm, String packageName) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return null;
            }
        } catch (Exception e) {
            return "";
        }
        return versionName;
    }

    public static boolean isEmptyStr(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        }
        return false;
    }

    public static void showInitVideoServerDialog(final BaseActivity context, final int enterType, final Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getText(R.string.init_video_service_dialog_title).toString());
        builder.setMessage(context.getText(R.string.init_video_service_dialog_msg).toString());
        builder.setPositiveButton(context.getText(R.string.yes).toString(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //mHandler.sendEmptyMessage(GO_TO_LOGIN);
                LogginVideoTask logginVideoTask = new LogginVideoTask(context, enterType, bundle);
                logginVideoTask.execute();
            }
        });

        builder.create().show();
    }

    public static class LogginVideoTask extends AsyncTask<Void,Void,Integer> {
        private BaseActivity mAcitity;
        private int mEnterType;
        private Bundle mBundle;

        private HttpPost mHttpPost = new HttpPost();

        public LogginVideoTask(BaseActivity activity, int enterType, Bundle bundle) {
            this.mAcitity = activity;
            this.mEnterType = enterType;
            this.mBundle = bundle;
        }

        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            //Toast.makeText(context,"开始执行",Toast.LENGTH_SHORT).show();
            mAcitity.showDlg(mAcitity.getText(R.string.init_video_service_task_msg).toString());
        }
        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Integer doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {

                if(mHttpPost.getVideoConfig()){
                    LoginParam param = new LoginParam();
                    param.setServer(mHttpPost.mLoginBean.getmVideoParameter().getIp());
                    param.setPort(Integer.parseInt(mHttpPost.mLoginBean.getmVideoParameter().getPort()));
                    param.setUserName(mHttpPost.mLoginBean.getmVideoParameter().getLoginName());
                    param.setPassword(mHttpPost.mLoginBean.getmVideoParameter().getLoginPass());
                    //调用登录接口
                    ServiceManager.login(param, new OnLoginListener(){

                        @Override
                        public void onLoginResult(long errorCode, String errorDesc) {
                            if (errorCode == 0) {
                                mLoginResultCode = LOGIN_RESULTS_SUCCESSFUL_CODE;
                                startKeepaliveService(mAcitity);
                                mAcitity.closeDlg();

                                Intent intent = new Intent();
                                if (mBundle != null) {
                                    intent.putExtras(mBundle);
                                }
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                if (mEnterType == ENTER_VIDEO_DEVICE_LIST) {
                                    intent.setClass(mAcitity, VideoMonitoringActivity.class);
                                } else if (mEnterType == ENTER_REAL_TIME_VIDEO) {
                                    intent.setClass(mAcitity, VideoPlayActivity.class);
                                } else if (mEnterType == ENTER_HISTORICAL_VIDEO){
                                    intent.setClass(mAcitity, VideoRePlayActivity.class);
                                }
                                mAcitity.startActivity(intent);

                            } else {
                                mLoginResultCode = LOGIN_RESULTS_FAILED_CODE;
                                mAcitity.closeDlg();
                                ToastUtils.showShort(mAcitity.getText(R.string.init_video_service_task_faided_msg).toString() +  " " + errorCode + "," + errorDesc);
                            }
                        }
                    });
                } else {
                    mLoginResultCode = LOGIN_RESULTS_FAILED_CODE;
                    mAcitity.closeDlg();
                    ToastUtils.showShort(mAcitity.getText(R.string.init_video_service_task_faided_msg).toString());
                }

            } catch (Exception e) {
                Log.e("Utils","e : " + e.getMessage());
                mLoginResultCode =  LOGIN_RESULTS_EXCEPTION_CODE;
                mAcitity.closeDlg();
                ToastUtils.showShort(mAcitity.getText(R.string.init_video_service_task_exception_msg).toString() +  " " + e.getMessage());
            }

            return mLoginResultCode;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer resultsCode) {
            super.onPostExecute(resultsCode);
            //Toast.makeText(context,"执行完毕",Toast.LENGTH_SHORT).show();
            //if (resultsCode == LOGIN_RESULTS_FAILED_CODE || resultsCode == LOGIN_RESULTS_EXCEPTION_CODE) {
            //    ((BaseActivity)getActivity()).closeDlg();
            //    ToastUtils.showShort("初始化视频加载服务相关内容失败，请稍后重试。");
            //}
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    //启动保活服务
    public static void startKeepaliveService(Context context){
        Intent toService = new Intent(context, NewKeepAliveService.class);
        context.startService(toService);
    }
}
