package com.isoftstone.smartsite.model.tripartite.activity;

/**
 * Created by yanyongjun on 2017/10/21.
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.patrolreport.PatrolBean;
import com.isoftstone.smartsite.model.tripartite.fragment.CheckFragment;
import com.isoftstone.smartsite.model.tripartite.fragment.ReadReportFrag;
import com.isoftstone.smartsite.model.tripartite.fragment.ViewReplyReportFragment;

/**
 * 验收报告验收页
 */
public class CheckReportActivity extends BaseActivity {
    private int mId = 0;
    private HttpPost mHttpPost = null;
    private PatrolBean mData = null;
    private ReadReportFrag mReadReportFrag = null;
    private ViewReplyReportFragment mViewReplyReportFrag = null;
    private CheckFragment mCheckFrag = null;
    public final static boolean isDebug = false;
    public final static int debugMsg = 158;

    public PatrolBean getReportData() {
        return mData;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_recheck_check_report;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        Intent i = getIntent();
        mId = i.getIntExtra("_id", 0);
        if(isDebug){
            mId = debugMsg;
        }
        mReadReportFrag = (ReadReportFrag) getSupportFragmentManager().findFragmentById(R.id.frag_inspect_report_view);
        mViewReplyReportFrag = (ViewReplyReportFragment) getSupportFragmentManager().findFragmentById(R.id.frag_view_reply_inspect_report);
        mCheckFrag = (CheckFragment)getSupportFragmentManager().findFragmentById(R.id.frag_check_inspect_report);
        mHttpPost = new HttpPost();
        new QueryBaseData().execute();
    }

    private class QueryBaseData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            mData = mHttpPost.getPatrolReport(mId+"");
            Log.e(TAG,"yanlog mData:"+mData);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e(TAG,"yanlog notifyData changed");
            mReadReportFrag.notifyDataChanged();
            mViewReplyReportFrag.notifyDataSetChanged();
            mCheckFrag.notifyDataSetChanged();
        }
    }
}
