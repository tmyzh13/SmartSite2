package com.isoftstone.smartsite.model.tripartite.fragment;

import android.os.Bundle;
import android.widget.ListView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.base.BaseFragment;
import com.isoftstone.smartsite.model.tripartite.adapter.ReplyReportAdapter;
import com.isoftstone.smartsite.model.tripartite.data.ReplyReportData;

import java.util.ArrayList;

/**
 * Created by yanyongjun on 2017/10/29.
 */

public class ViewReplyReportFragment extends BaseFragment {
    private ListView mListView = null;

    private ArrayList<ReplyReportData> mDatas = new ArrayList<ReplyReportData>();
    private ReplyReportAdapter mAdapter = null;
    private ReplyReportData mData = new ReplyReportData();
    private BaseActivity mActivity = null;


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_view_reply_report;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        init();
    }

    private void init() {
        //初始化Listview
        mListView = (ListView) getView().findViewById(R.id.listview);
        mData.setPatrolBean(mActivity.getReportData());
        mAdapter = new ReplyReportAdapter(getActivity(), mData);
        mListView.setAdapter(mAdapter);
    }

    public void notifyDataSetChanged() {
        mData.setPatrolBean(mActivity.getReportData());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        mAdapter.unRegister();
        super.onDestroy();
    }
}
