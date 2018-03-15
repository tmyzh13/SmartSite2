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


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.utils.ToastUtils;

/**
 * 意见反馈界面
 * created by zhangyinfu 2017-11-1
 */
public class OpinionFeedbackActivity extends BaseActivity implements View.OnClickListener{

    private TextView mSubmitView;
    private EditText mFeedbackView;
    private TextView mNumTextShow;
    private static final int MAX_LENGTH = 150;//最大输入字符数150 
    private int Rest_Length = MAX_LENGTH;
    private HttpPost mHttpPost;

    /* 查询请求识别码 查询成功*/
    private static final int RESULTS_SUCCESSFUL_CODE = 1;
    /* 查询请求识别码 查询失败*/
    private static final int RESULTS_FAILED_CODE = 2;
    /* 查询请求识别码 查询异常*/
    private static final int RESULTS_EXCEPTION_CODE = 3;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_opinion_feedback;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        initView();
        initToolbar();
    }

    private void initView() {
        mHttpPost = new HttpPost();
        mSubmitView = (TextView) findViewById(R.id.submit_view);
        mFeedbackView = (EditText) findViewById(R.id.feedback_text);
        mNumTextShow = (TextView) findViewById(R.id.feedback_text_show_num);
        mNumTextShow.setText(Html.fromHtml("您还可以输入:"+"<font color=\"red\">"+MAX_LENGTH+"/"+ MAX_LENGTH + "</font>"));

        mSubmitView.setOnClickListener(this);

        mFeedbackView.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mNumTextShow.setText(Html.fromHtml("您还可以输入:"+"<font color=\"red\">"+Rest_Length+"/"+ MAX_LENGTH + "</font>"));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(Rest_Length > 0){
                    Rest_Length = MAX_LENGTH - mFeedbackView.getText().length();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mNumTextShow.setText(Html.fromHtml("您还可以输入:"+"<font color=\"red\">"+Rest_Length+"/"+ MAX_LENGTH + "</font>"));
            }
        });
    }

    private void initToolbar(){
        TextView tv_title = (TextView) findViewById(R.id.toolbar_title);
        tv_title.setText(R.string.opinion_feedback);

        findViewById(R.id.btn_back).setOnClickListener(OpinionFeedbackActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                OpinionFeedbackActivity.this.finish();
                break;

            case R.id.submit_view:
                if (mFeedbackView.getText().length() <= 0) {
                    ToastUtils.showShort("请输入您的意见反馈信息，谢谢！");
                    return;
                }
                /**ToastUtils.showShort("您的意见反馈已收集，感谢您的反馈！");
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                FeedbackTask feedbackTask = new FeedbackTask(getApplicationContext());
                feedbackTask.execute();
                break;
            default:
                break;
        }
    }


    class FeedbackTask extends AsyncTask<Void,Void,Integer> {
        private Context context;
        FeedbackTask(Context context) {
            this.context = context;
        }

        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            showDlg("正在发送反馈意见");
        }
        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Integer doInBackground(Void... params) {

            try {
                //UserBean userBean = mHttpPost.getLoginUser();
                mHttpPost.feedback(HttpPost.mLoginBean.getmUserBean().getLoginUser().getId(),mFeedbackView.getText().toString());

            } catch (Exception e) {
                Log.e(TAG,"e : " + e.getMessage());
                return RESULTS_EXCEPTION_CODE;
            }

            return RESULTS_SUCCESSFUL_CODE;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer resultsCode) {
            super.onPostExecute(resultsCode);
            closeDlg();
            //Toast.makeText(context,"执行完毕",Toast.LENGTH_SHORT).show();
            Log.i("OpinionFeedbackActivity", "Feedback...  resultsCode = " + resultsCode);
            if (resultsCode == RESULTS_EXCEPTION_CODE) {
                ToastUtils.showShort("反馈意见发送失败，请稍后重试");
            } else {
                ToastUtils.showLong("您的意见反馈已收集，感谢您的反馈！");
                OpinionFeedbackActivity.this.finish();
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
}
