package com.isoftstone.smartsite.model.muckcar.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.muckcar.ArchMonthFlowBean;
import com.isoftstone.smartsite.http.muckcar.CarInfoBean;
import com.isoftstone.smartsite.http.pageable.PageableBean;
import com.isoftstone.smartsite.http.patrolplan.PatrolPlanBean;
import com.isoftstone.smartsite.http.patrolplan.PatrolPlanBeanPage;
import com.isoftstone.smartsite.model.dirtcar.activity.DirtCarListActivity;
import com.isoftstone.smartsite.model.inspectplan.bean.InspectPlanBean;
import com.isoftstone.smartsite.utils.DateUtils;
import com.isoftstone.smartsite.utils.ToastUtils;
import com.isoftstone.smartsite.utils.Utils;

import java.util.ArrayList;

/**
 * Created by 2013020220 on 2017/11/22.
 */

public class SlagcarInfoActivity extends BaseActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageButton btn_icon;
    private ImageButton btn_back;
    private TextView title;
    private ArrayList<Fragment> fragmentLists;
    private MyPagerAdapter pagerAdapter;
    private String[] titles = new String[]{"月视图", "日视图"};
    private HttpPost mHttpPost = new HttpPost();
    private DaySlagcarInfoFragment mMonthSlagcarInfoFragment = null;
    private DaySlagcarInfoFragment mDaySlagcarInfoFragment = null;
    private ArrayList<CarInfoBean> mCarInfoList_day = null;
    private ArrayList<CarInfoBean> mCarInfoList_mouth = null;
    private ArchMonthFlowBean liuliangduibi;
    private ArchMonthFlowBean baojinglv;
    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        tabLayout = (TabLayout) findViewById(R.id.tab);
        viewPager = (ViewPager) findViewById(R.id.vp);
        btn_icon = (ImageButton) findViewById(R.id.btn_icon);
        btn_icon.setImageResource(R.drawable.environmentlist);
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("渣土车概览");
        btn_back.setOnClickListener(this);
        btn_icon.setOnClickListener(this);
        fragmentLists = new ArrayList<Fragment>();
        initTablayout();
        initViewPagerAndFragment();
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentLists);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(pagerAdapter);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_slagcar_info;
    }

    private void initViewPagerAndFragment() {
        mMonthSlagcarInfoFragment = new DaySlagcarInfoFragment();
        mMonthSlagcarInfoFragment.setDayOrMonthFlag(1);
        mDaySlagcarInfoFragment = new DaySlagcarInfoFragment();
        mDaySlagcarInfoFragment.setDayOrMonthFlag(0);
        fragmentLists.add(mMonthSlagcarInfoFragment);
        fragmentLists.add(mDaySlagcarInfoFragment);
    }

    private void initTablayout() {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText(titles[0]));
        tabLayout.addTab(tabLayout.newTab().setText(titles[1]));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_icon:
                enterDirctCar();
                break;
            default:
                break;
        }
    }

    public void getLiuliangpaimingData(int dayormonthflag) {
        String time = "";
        String date = "";
        int flag = 0;
        if (dayormonthflag == 0) {
            //日
            time = mDaySlagcarInfoFragment.getLiuliangpaimingTime();
            flag = mDaySlagcarInfoFragment.getDayOrMonthFlag();
        } else if (dayormonthflag == 1) {
            //月
            date = mMonthSlagcarInfoFragment.getLiuliangpaimingTime();
            flag = mMonthSlagcarInfoFragment.getDayOrMonthFlag();
        }
        new QueryDayFlowDataTask(this, time, date, flag).execute();

    }

    public void getLiuliangduibiData(int dayormonthflag) {
        long archid = 0;
        String time = "";
        String date = "";
        int flag = 0;
        if (dayormonthflag == 0) {
            //日
            time = mDaySlagcarInfoFragment.getLiuliangduibiTime();
            flag = mDaySlagcarInfoFragment.getDayOrMonthFlag();
            archid = mDaySlagcarInfoFragment.getLiuliangduibi_id();
        } else if (dayormonthflag == 1) {
            //月
            date = mMonthSlagcarInfoFragment.getLiuliangduibiTime();
            flag = mMonthSlagcarInfoFragment.getDayOrMonthFlag();
            archid = mMonthSlagcarInfoFragment.getLiuliangduibi_id();
        }
        new QueryArchMonthFlowDataTask(this,time,date,flag,archid).execute();
    }

    public void getBaojinglvData(int dayormonthflag){
        long[] archids = new long[2];
        String time = "";
        String date = "";
        int flag = 0;
        if (dayormonthflag == 0) {
            //日
            time = mDaySlagcarInfoFragment.getBaojinglvTime();
            flag = mDaySlagcarInfoFragment.getDayOrMonthFlag();
            archids = mDaySlagcarInfoFragment.getBaojinglvAddressId();
        } else if (dayormonthflag == 1) {
            //月
            date = mMonthSlagcarInfoFragment.getBaojinglvTime();
            flag = mMonthSlagcarInfoFragment.getDayOrMonthFlag();
            archids = mMonthSlagcarInfoFragment.getBaojinglvAddressId();
        }
        new QueryAlarmDataTask(this,time,date,flag,archids).execute();
    }

    public class QueryDayFlowDataTask extends AsyncTask<Void, Void, Integer> {
        private Context context;
        private String time;
        private String date;
        private int flag;

        public QueryDayFlowDataTask(Context context, String time, String date, int flag) {
            this.context = context;
            this.time = time;
            this.date = date;
            this.flag = flag;
        }

        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            showDlg("数据加载中");
        }

        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Integer doInBackground(Void... params) {
            if(flag == 0){
                mCarInfoList_day = mHttpPost.getDayFlow(time, "1", date, flag);
            }else  if(flag == 1){
                mCarInfoList_mouth = mHttpPost.getDayFlow(time, "1", date, flag);
            }
            return 1;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer resultsCode) {
            super.onPostExecute(resultsCode);
            //
            if (flag == 1) {
                mMonthSlagcarInfoFragment.setCarInfoList(mCarInfoList_mouth);
            } else if (flag == 0) {
                mDaySlagcarInfoFragment.setCarInfoList(mCarInfoList_day);
            }
            if (!mDaySlagcarInfoFragment.isShowDialog()){
                closeDlg();
            }


        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    public class QueryArchMonthFlowDataTask extends AsyncTask<Void, Void, Integer> {
        private Context context;
        private String time;
        private String date;
        private int flag;
        private long archId;

        public QueryArchMonthFlowDataTask(Context context, String time, String date, int flag,long archId) {
            this.context = context;
            this.time = time;
            this.date = date;
            this.archId = archId;
            this.flag = flag;
        }

        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            showDlg("数据加载中");
        }

        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Integer doInBackground(Void... params) {
            liuliangduibi = mHttpPost.getArchMonthFlow(time, date,archId, flag);
            return 1;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer resultsCode) {
            super.onPostExecute(resultsCode);
            //
            if (flag  == 1) {
                mMonthSlagcarInfoFragment.setLiuliangduibi(liuliangduibi);
            } else if (flag == 0) {
                mDaySlagcarInfoFragment.setLiuliangduibi(liuliangduibi);
            }

            if (!mDaySlagcarInfoFragment.isShowDialog()){
                closeDlg();
            }


        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    public class QueryAlarmDataTask extends AsyncTask<Void, Void, Integer> {
        private Context context;
        private String time;
        private String date;
        private int flag;
        private long[] archIds;

        public QueryAlarmDataTask(Context context, String time, String date, int flag,long[] archIds) {
            this.context = context;
            this.time = time;
            this.date = date;
            this.archIds = archIds;
            this.flag = flag;
        }

        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            showDlg("数据加载中");
        }

        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Integer doInBackground(Void... params) {
            baojinglv = mHttpPost.getAlarmData(time, date,archIds, flag);
            return 1;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer resultsCode) {
            super.onPostExecute(resultsCode);
            //
            if (flag  == 1) {
                mMonthSlagcarInfoFragment.setBaojinglv(baojinglv);
            } else if (flag == 0) {
                mDaySlagcarInfoFragment.setBaojinglv(baojinglv);
            }

                closeDlg();
            mDaySlagcarInfoFragment.setShowDialog(false);

        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    private void enterDirctCar() {
        Intent intent = new Intent(this, DirtCarListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> list;

        public MyPagerAdapter(FragmentManager fragmentManager, ArrayList<Fragment> list) {
            super(fragmentManager);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
