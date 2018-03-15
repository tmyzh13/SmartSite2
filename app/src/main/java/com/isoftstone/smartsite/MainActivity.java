package com.isoftstone.smartsite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.model.dirtcar.Service.RecognizeDirtCarService;
import com.isoftstone.smartsite.model.main.ui.MainFragment;
import com.isoftstone.smartsite.model.map.ui.MapFragment;
import com.isoftstone.smartsite.model.message.ui.MsgFragment;
import com.isoftstone.smartsite.model.system.ui.IndividualCenterFragment;
import com.isoftstone.smartsite.model.system.ui.SystemMainFragment;

public class MainActivity extends BaseActivity implements IndividualCenterFragment.BackHandlerInterface {

    private String[] tabTitles;
    private FragmentTabHost tabHost;
    private IndividualCenterFragment mFrame;

    private int[] tabIcons = new int[]{R.drawable.selector_tab_home,R.drawable.selector_tab_round,
            R.drawable.selector_tab_me,R.drawable.selector_tab_more};

    private Class[] fragments = new Class[]{MainFragment.class,MapFragment.class,
            MsgFragment.class, SystemMainFragment.class};
    public static final String ACTION_CHANGE_TAB = "com.isoftstone.smartsite.change_tab";

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try{
                String action = intent.getAction();
                Log.d(TAG,"receive action:"+action);
                if (ACTION_CHANGE_TAB.equals(action)) {
                    int tab = intent.getIntExtra("tab",0);
                    setCurrentTab(tab);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    };

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        tabTitles = this.getResources().getStringArray(R.array.tab_title);

        initView();
    }

    @Override
    protected void onStart() {
        Log.i(TAG,"yanlog mainActivity OnStart");
        Intent i = new Intent(this, RecognizeDirtCarService.class);
        i.putExtra("sync",true);
        startService(i);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CHANGE_TAB);
        registerReceiver(mReceiver,filter);
        super.onStart();
    }

    private void initView(){
        tabHost = (FragmentTabHost) findViewById(R.id.tab_host);

        initTabWidget();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(mReceiver);
        super.onStop();
    }

    private void initTabWidget(){
        //设置Fragment的容器
        tabHost.setup(this,getSupportFragmentManager(),android.R.id.tabcontent);
        //设置有多少Item
        for (int i = 0; i < fragments.length; i++) {
            View view = getLayoutInflater().inflate(R.layout.layout_tab,null);
            ImageView iv = (ImageView) view.findViewById(R.id.iv_tab);
            iv.setImageResource(tabIcons[i]);
            TextView tv = (TextView) view.findViewById(R.id.tv_tab);
            tv.setText(tabTitles[i]);
            tabHost.addTab(tabHost.newTabSpec("" + i).setIndicator(view),fragments[i],null);
        }
        tabHost.setCurrentTab(0);
    }

    @Override
    public void setSelectedFragment(IndividualCenterFragment backHandledFragment) {
        mFrame = backHandledFragment;
    }

    @Override
    public void onBackPressed() {
        if ((null != mFrame) && !mFrame.onFragmentBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setCurrentTab(int index){
        tabHost.setCurrentTab(index);
    }
}
