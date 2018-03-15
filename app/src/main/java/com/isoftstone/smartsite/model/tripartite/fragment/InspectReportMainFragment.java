package com.isoftstone.smartsite.model.tripartite.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.BaseAdapter;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.base.BaseFragment;
import com.isoftstone.smartsite.common.widget.PullToRefreshListView;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.patrolreport.PatrolBean;
import com.isoftstone.smartsite.http.pageable.PageableBean;
import com.isoftstone.smartsite.model.message.data.MsgData;
import com.isoftstone.smartsite.model.tripartite.activity.TripartiteActivity;
import com.isoftstone.smartsite.model.tripartite.adapter.InspectReportAdapter;
import com.isoftstone.smartsite.model.tripartite.data.ReportData;
import com.isoftstone.smartsite.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by yanyongjun on 2017/10/16.
 * 巡查报告fragment
 */

public class InspectReportMainFragment extends BaseFragment {
    private TripartiteActivity mActivity = null;
    private PullToRefreshListView mListView = null;
    private ArrayList<ReportData> mDatas = new ArrayList<>();
    private BaseAdapter mAdapter = null;
    private HttpPost mHttpPost = null;
    private String mAccountName = "";
    //分页开始
    private int mCurPageNum = -1;
    public boolean isLoading = false;
    public String mSearchText = "";
/*    private Button mAdd = null;*/

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_inspect_report;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        mActivity = (TripartiteActivity) getActivity();
        mHttpPost = new HttpPost();
        init();
    }

    //分页代码
    private void init() {
        //初始化Listview
        mListView = (PullToRefreshListView) mActivity.findViewById(R.id.listview);
        PullToRefreshListView.OnRefreshListener listener = new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(TAG, "yanlog onRefresh:" + isLoading);
                if (isLoading) {
                    //mListView.onRefreshComplete();
                } else {
                    mCurPageNum = -1;
                    queryData(true, mSearchText);
                }
            }

            @Override
            public void onLoadMore() {
                Log.e(TAG, "yanlog onLoadMore:" + isLoading);
                if (isLoading) {
                    // mListView.onLoadMoreComplete();
                } else {
                    queryData(false, mSearchText);
                }
            }
        };
        mListView.setOnRefreshListener(listener);
        mAdapter = new InspectReportAdapter(mActivity, mDatas);
        mListView.setAdapter(mAdapter);
        queryData(true, mSearchText);
    }

    public void queryData(final boolean isreLoad, final String address) {
        if(isreLoad){
            mCurPageNum = -1;
        }
        new AsyncTask<String, Integer, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                isLoading = true;
            }

            @Override
            protected String doInBackground(String... strings) {
                PageableBean page = new PageableBean();
                page.setSize(BaseActivity.DEFAULT_PAGE_SIZE);
                page.setPage(mCurPageNum + 1 + "");
                ArrayList<PatrolBean> msgs = mHttpPost.getPatrolReportList("",address, page);
                if (msgs == null) {
                    return "";
                }
                Collections.sort(msgs, new Comparator<PatrolBean>() {
                    @Override
                    public int compare(PatrolBean o1, PatrolBean o2) {
                        try {
                            Date date1 = MsgData.format5.parse(o1.getDate());
                            Date date2 = MsgData.format5.parse(o2.getDate());
                            return date2.compareTo(date1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });
                if (isreLoad) {
                    mDatas.clear();
                }
                if (msgs != null && msgs.size() > 0) {
                    mCurPageNum++;
                }
                for (PatrolBean temp : msgs) {
                    ReportData reportData = new ReportData(temp);
                    Log.e(TAG, "reportData:" + reportData);
                    mDatas.add(reportData);
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                Log.e(TAG, "yanlog queryEnd:" + mCurPageNum);
                isLoading = false;
                if (mDatas == null || mDatas.size() == 0) {
//                    ToastUtils.showShort("未获取到数据");
                }
                TripartiteActivity activity = (TripartiteActivity)getActivity();
                if(TextUtils.isEmpty(address)){
                    activity.mIsDataInSearchMode = false;
                }else {
                    activity.mIsDataInSearchMode = true;
                }
                mListView.onLoadMoreComplete();
                mListView.onRefreshComplete();
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
//
//    private class QueryMsgTask extends AsyncTask<String, Integer, String> {
//        private boolean mIsReLoad = true;
//        public QueryMsgTask(boolean isreLoad){
//            mIsReLoad = isreLoad;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            Log.e(TAG,"yanlog query begin:"+mCurPageNum);
//            super.onPreExecute();
//            isLoading = true;
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            PageableBean page = new PageableBean();
//            page.setSize(BaseActivity.DEFAULT_PAGE_SIZE);
//            page.setPage(mCurPageNum + 1 + "");
//            ArrayList<PatrolBean> msgs = mHttpPost.getPatrolReportList("", page);
//            if (msgs == null) {
//                return "";
//            }
//            Collections.sort(msgs, new Comparator<PatrolBean>() {
//                @Override
//                public int compare(PatrolBean o1, PatrolBean o2) {
//                    try {
//                        Date date1 = MsgData.format5.parse(o1.getDate());
//                        Date date2 = MsgData.format5.parse(o2.getDate());
//                        return date2.compareTo(date1);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return 0;
//                }
//            });
//            if(mIsReLoad){
//                mDatas.clear();
//            }
//            if(msgs != null && msgs.size() > 0){
//                mCurPageNum++;
//            }
//            for (PatrolBean temp : msgs) {
//                ReportData reportData = new ReportData(temp);
//                Log.e(TAG, "reportData:" + reportData);
//                mDatas.add(reportData);
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            Log.e(TAG,"yanlog queryEnd:"+mCurPageNum);
//            isLoading = false;
//            if (mDatas == null || mDatas.size() == 0) {
//                ToastUtils.showShort("未获取到数据");
//            }
//            mListView.onLoadMoreComplete();
//            mListView.onRefreshComplete();
//            mAdapter.notifyDataSetChanged();
//        }
//    }
    //分页代码结束
}
