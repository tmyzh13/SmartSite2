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
import android.view.View;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.utils.Utils;

/**
 * 关于我们界面
 * created by zhangyinfu 2017-10-31
 */
public class AboutUsActivity extends Activity implements View.OnClickListener{

    private TextView mAppVersionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        initToolbar();

        mAppVersionView = (TextView) findViewById(R.id.app_version);
        mAppVersionView.setText("Version " + Utils.getAppVersionName(this.getPackageManager(), this.getPackageName()));
    }

    private void initToolbar(){
        TextView tv_title = (TextView) findViewById(R.id.toolbar_title);
        tv_title.setText(R.string.about_us);

        findViewById(R.id.btn_back).setOnClickListener(AboutUsActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                AboutUsActivity.this.finish();
                break;
            default:
                break;
        }
    }
}
