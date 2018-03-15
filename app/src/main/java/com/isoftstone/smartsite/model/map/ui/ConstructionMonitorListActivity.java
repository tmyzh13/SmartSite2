package com.isoftstone.smartsite.model.map.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.http.patroluser.UserTrackBean;
import com.isoftstone.smartsite.model.map.adapter.ConstructionMonitorListAdapter;
import com.isoftstone.smartsite.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw on 2017/11/19.
 */

public class ConstructionMonitorListActivity extends BaseActivity implements View.OnClickListener {


    private ListView lv;
    private ArrayList<UserTrackBean> userTrackBeans;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_construciton_monitor_list;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        initToolBar();
        initData();
        initListView();
    }

    private void initToolBar(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        ImageButton btn = (ImageButton) findViewById(R.id.btn_icon);
        btn.setImageResource(R.drawable.search);
        btn.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("巡查人员列表");
    }

    private void initData(){
        userTrackBeans = (ArrayList<UserTrackBean>) getIntent().getSerializableExtra("data");
    }

    private void initListView(){
        lv = (ListView) findViewById(R.id.lv);
        ConstructionMonitorListAdapter listAdapter = new ConstructionMonitorListAdapter(this,userTrackBeans);
        lv.setAdapter(listAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                this.finish();
                break;
        }
    }
}
