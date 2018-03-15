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
package com.isoftstone.smartsite.model.system.ui;


import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.utils.FileCacheUtils;
import com.isoftstone.smartsite.utils.SharedPreferencesUtils;
import com.isoftstone.smartsite.utils.ToastUtils;

import java.io.File;

/**
 * 意见反馈界面
 * created by zhangyinfu 2017-10-31
 */
public class SettingsActivity extends Activity implements View.OnClickListener{

    private Switch mPushMsgSwitch;
    private RelativeLayout mCleanCacheView;
    private TextView mCacheSizeView;
    private TextView mCacheSizeTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();

        initToolbar();

        initCacheSize();
    }

    private void initView() {
        boolean receive = SharedPreferencesUtils.getReceiveNotice(this);
        mPushMsgSwitch = (Switch) findViewById(R.id.settings_push_msg_switch);
        mPushMsgSwitch.setChecked(receive);

        mPushMsgSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesUtils.setReceiveNotice(SettingsActivity.this,true);
                    ToastUtils.showShort("打开");
                } else {
                    SharedPreferencesUtils.setReceiveNotice(SettingsActivity.this,false);
                    ToastUtils.showShort("关闭");
                }
            }
        });
    }

    private void initToolbar(){
        TextView tv_title = (TextView) findViewById(R.id.toolbar_title);
        tv_title.setText(R.string.settings);

        findViewById(R.id.btn_back).setOnClickListener(SettingsActivity.this);

        mCleanCacheView = (RelativeLayout) findViewById(R.id.clean_cache_view);
        mCleanCacheView.setOnClickListener(this);

        mCacheSizeView = (TextView) findViewById(R.id.settings_cleanup_cache);
        mCacheSizeTitleView = (TextView) findViewById(R.id.settings_cleanup_cache_title);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                SettingsActivity.this.finish();
                break;
            case R.id.clean_cache_view:
                FileCacheUtils.cleanExternalCache(getApplicationContext());
                //重新获取一次缓存大小，自处理M，byte
                initCacheSize();
                ToastUtils.showShort(getText(R.string.clean_cache).toString());
                break;
            default:
                break;
        }
    }


    private void initCacheSize() {
    /*
    * 获取SD卡根目录：Environment.getExternalStorageDirectory().getAbsolutePath();
        外部Cache路径：/mnt/sdcard/android/data/com.xxx.xxx/cache 一般存储缓存数据（注：通过getExternalCacheDir()获取）
        外部File路径：/mnt/sdcard/android/data/com.xxx.xxx/files 存储长时间存在的数据
        （注：通过getExternalFilesDir(String type)获取， type为特定类型，可以是以下任何一种
                    Environment.DIRECTORY_MUSIC,
                    Environment.DIRECTORY_PODCASTS,
                     Environment.DIRECTORY_RINGTONES,
                     Environment.DIRECTORY_ALARMS,
                     Environment.DIRECTORY_NOTIFICATIONS,
                     Environment.DIRECTORY_PICTURES,
                      Environment.DIRECTORY_MOVIES. ）
    * */
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File outCachePath = getApplicationContext().getExternalCacheDir();
        File outFilePath = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_ALARMS);

        try {
            String outCacheSize = FileCacheUtils.getCacheSize(outCachePath);
            String outFileSize = FileCacheUtils.getCacheSize(outFilePath);

            mCacheSizeView.setText(outCacheSize);

            if(outCacheSize.matches("0.00B")) {
                mCleanCacheView.setEnabled(false);
                mCacheSizeTitleView.setTextColor(getResources().getColor(R.color.hit_text_color));
                mCacheSizeView.setTextColor(getResources().getColor(R.color.hit_text_color));
            } else {
                mCleanCacheView.setEnabled(true);
                mCacheSizeTitleView.setTextColor(getResources().getColor(R.color.main_text_color));
                mCacheSizeView.setTextColor(getResources().getColor(R.color.single_text_color));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
