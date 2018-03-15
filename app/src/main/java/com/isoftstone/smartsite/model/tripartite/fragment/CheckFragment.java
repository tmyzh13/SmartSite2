package com.isoftstone.smartsite.model.tripartite.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.base.BaseFragment;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.patrolreport.PatrolBean;
import com.isoftstone.smartsite.http.patrolreport.ReportBean;
import com.isoftstone.smartsite.http.user.BaseUserBean;
import com.isoftstone.smartsite.model.tripartite.adapter.AttachGridViewAdapter;
import com.isoftstone.smartsite.utils.DateUtils;
import com.isoftstone.smartsite.utils.FilesUtils;
import com.isoftstone.smartsite.utils.ImageUtils;
import com.isoftstone.smartsite.widgets.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 点击验收后，下面验收的编辑框
 * Created by yanyongjun on 2017/10/30.
 */

public class CheckFragment extends BaseFragment {
    private GridView mAttachView = null;
    private ArrayList<Object> mData = null;
    private AttachGridViewAdapter mAttachAdapter = null;

    private Resources mRes = null;
    private Drawable mWaittingAdd = null;
    private Drawable mWattingChanged = null;

    private TextView mLabRevisitTime = null;
    private TextView mEditRevisitTime = null;
    private EditText mEditContent = null;
    private TextView mLabCreator = null;
    private Button mBtnYes = null;
    private Button mBtnNo = null;

    private Calendar mCal = null;
    private BaseActivity mActivity = null;

    private RadioButton mRadioYes = null;
    private RadioButton mRadioNo = null;
    public final static int REQUEST_ACTIVITY_ATTACH = 0;//请求图片的request code
    //private ArrayList<String> mFilesPath = new ArrayList<>();
    private PatrolBean mReportData = null;
    Dialog mLoginingDlg;


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_recheck_check_report;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        init();
    }

    public void init() {
        mRes = getResources();
        mWaittingAdd = mRes.getDrawable(R.drawable.addcolumn);
        mWaittingAdd.setBounds(0, 0, mWaittingAdd.getIntrinsicWidth(), mWaittingAdd.getIntrinsicHeight());
        mWattingChanged = mRes.getDrawable(R.drawable.editcolumn);
        mWattingChanged.setBounds(0, 0, mWattingChanged.getIntrinsicWidth(), mWattingChanged.getIntrinsicHeight());

        mCal = Calendar.getInstance();
        mActivity = (BaseActivity) getActivity();
        initView();
        initListener();
        initGridView();
        initUserHead();
    }

    public void initView() {
        mLabRevisitTime = (TextView) rootView.findViewById(R.id.lab_revisit_time);
        mEditRevisitTime = (TextView) rootView.findViewById(R.id.edit_lab_revisit_time);
        mLabCreator = (TextView) rootView.findViewById(R.id.lab_report_people_name);
        try {
            mLabCreator.setText(HttpPost.mLoginBean.getmUserBean().getLoginUser().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBtnNo = (Button) rootView.findViewById(R.id.btn_no);
        mBtnYes = (Button) rootView.findViewById(R.id.btn_yes);
        mEditContent = (EditText) rootView.findViewById(R.id.edit_report_msg);

        mRadioNo = (RadioButton) getView().findViewById(R.id.radio_no);
        mRadioYes = (RadioButton) getView().findViewById(R.id.radio_yes);
    }

    private void initUserHead() {
        try {
            ImageView v = (ImageView) getView().findViewById(R.id.img_head);
            String headPath = HttpPost.mLoginBean.getmUserBean().getLoginUser().imageData;
            String uri = mHttpPost.getFileUrl(headPath);
            ImageUtils.loadImageWithPlaceHolder(mContext, v, uri, R.drawable.default_head);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initListener() {
        mBtnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllMsgSetted()) {
                    ReportBean bean = getReportBean();
                    if (mRadioYes.isChecked()) {
                        bean.setStatus(3);
                    } else {
                        bean.setStatus(5);
                    }
                    new DealTask(bean).execute();
                } else {
                    Toast.makeText(getActivity(), "您还有未填写的信息", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBtnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllMsgSetted()) {
                    ReportBean bean = getReportBean();
                    bean.setStatus(4);
                    new DealTask(bean).execute();
                } else {
                    Toast.makeText(getActivity(), "您还有未填写的信息", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mRadioYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RelativeLayout relativeLayout = (RelativeLayout) getView().findViewById(R.id.relative_revisit_time);
                if (isChecked) {
                    relativeLayout.setVisibility(View.VISIBLE);
                } else {
                    relativeLayout.setVisibility(View.GONE);
                }
            }
        });

        //选时间
        mEditRevisitTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        mEditRevisitTime.setText("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
//                        //mRevisitDate = new ITime(year, monthOfYear + 1, dayOfMonth);
//                        mEditRevisitTime.setTextColor(mRes.getColor(R.color.main_text_color));
//                        mLabRevisitTime.setCompoundDrawables(mWattingChanged, null, null, null);
//                    }
//                }, mCal.get(Calendar.YEAR), mCal.get(Calendar.MONTH), mCal.get(Calendar.DAY_OF_MONTH));
//                dialog.show();
                showDatePickerDialog();
            }
        });
    }

    public void initGridView() {
        mAttachView = (GridView) getView().findViewById(R.id.grid_view);

        mData = new ArrayList<Object>();
        mData.add(R.drawable.attachment);
        mAttachAdapter = new AttachGridViewAdapter(getActivity(), mData);
        mAttachView.setAdapter(mAttachAdapter);
        mAttachAdapter.setmIsShowDelete(true);
        mAttachView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mData.size() - 1) {
                    //点击添加附件
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.setType("*/*");
                    startActivityForResult(i, REQUEST_ACTIVITY_ATTACH);
                } else {
//                    mFilesPath.remove(position);
//                    mData.remove(position);
//                    mAttachAdapter.notifyDataSetChanged();
//                    ToastUtils.showShort("附件删除成功");
                }
            }
        });
    }

    public void notifyDataSetChanged() {
        mReportData = mActivity.getReportData();
        if (mReportData != null) {
            if (mReportData.isVisit()) {
                LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.linear_is_report);
                linearLayout.setVisibility(View.GONE);
            }
        }
    }

    private boolean isAllMsgSetted() {
        if (TextUtils.isEmpty(mEditContent.getText())) {
            return false;
        }
        if (mReportData.isVisit()) {
            return true;
        }
        if (mRadioYes.isChecked()) {
            try {
                Log.e(TAG, "yanlog checkfrag" + mEditRevisitTime.getText().toString());
                DateUtils.format_yyyy_MM_dd_HH_mm_ss.parse(mEditRevisitTime.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private ReportBean getReportBean() {
        PatrolBean tempBeam = new PatrolBean();
        tempBeam.setId(mActivity.getReportData().getId());

        ReportBean reportBean = new ReportBean();
        reportBean.setPatrol(tempBeam);
        //reportBean.setCreator(mHttpPost.mLoginBean.getmName());
        reportBean.setContent(mEditContent.getText().toString()); //TODO

        reportBean.setDate(DateUtils.format_yyyy_MM_dd_HH_mm_ss.format(new Date()));
        if (!mReportData.isVisit()) {
            if (mRadioYes.isChecked()) {
                String visitTime = "";
                try {
                    visitTime = mEditRevisitTime.getText().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                reportBean.setVisitDate(visitTime);
            }
            reportBean.setVisit(mRadioYes.isChecked());
        }
        reportBean.setCategory(3);
        BaseUserBean userBean = new BaseUserBean();
        userBean.setId(mHttpPost.mLoginBean.getmUserBean().getLoginUser().getId());
        Log.e(TAG, "yanlog checkid:" + mHttpPost.mLoginBean.getmUserBean().getLoginUser().getId() + " checkName:" + mHttpPost.mLoginBean.getmUserBean().getLoginUser().getAccount());
        reportBean.setCreator(userBean);
        return reportBean;
    }

    /* 初始化正在登录对话框 */
    private void initDlg() {

        mLoginingDlg = new Dialog(getActivity(), R.style.loginingDlg);
        mLoginingDlg.setContentView(R.layout.dialog_submit);

        mLoginingDlg.setCanceledOnTouchOutside(false); // 设置点击Dialog外部任意区域关闭Dialog
    }

    /* 显示正在登录对话框 */
    private void showDlg() {
        if (mLoginingDlg != null)
            mLoginingDlg.show();
    }

    /* 关闭正在登录对话框 */
    private void closeDlg() {
        if (mLoginingDlg != null && mLoginingDlg.isShowing())
            mLoginingDlg.dismiss();
    }

    private class DealTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            initDlg();
            showDlg();
            super.onPreExecute();
        }

        private ReportBean mBean = null;

        public DealTask(ReportBean reportBean) {
            mBean = reportBean;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.e(TAG, "deal task begin");
            mHttpPost.addPatrolCheck(mBean);
            try {
                if (mData != null && mData.size() >= 1) {
                    PatrolBean report = mHttpPost.getPatrolReport(mBean.getPatrol().getId() + "");
                    ArrayList<ReportBean> reports = report.getReports();
                    int id = report.getReports().get(reports.size() - 1).getId();
                    for (Object path : mData) {
                        if (path instanceof String) {
                            mHttpPost.reportFileUpload((String) path, id);
                        }
                    }
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean temp) {
            super.onPostExecute(temp);
            closeDlg();
            if (temp == true) {
                Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), "提交失败", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ACTIVITY_ATTACH: {
                Log.e(TAG, "onActivityResult:" + data);
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    Log.e(TAG, "yanlog uri:" + uri);
                    if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                        //Toast.makeText(getActivity(), uri.getPath() + "11111", Toast.LENGTH_SHORT).show();
                        addAttach(uri.getPath());
                        return;
                    }
                    String path = FilesUtils.getPath(getActivity(), uri);
                    //Toast.makeText(getActivity(), path, Toast.LENGTH_SHORT).show();
                    addAttach(path);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //add files
    public void addAttach(String path) {
        Log.e(TAG, "yanlog remove begin size:" + mData.size());
        String formatPath = FilesUtils.getFormatString(path);
        Log.e(TAG, "yanlog remove begin size at0:" + mData.get(0));
        mData.remove(mData.size() - 1);
        //mFilesPath.add(path);
 //       if (TripartiteActivity.mImageList.contains(formatPath)) {
            //mData.add(uri);
            mData.add(path);
//        } else if (TripartiteActivity.mXlsList.contains(formatPath)) {
//            mData.add(TripartiteActivity.mAttach.get(".xls"));
//        } else if (TripartiteActivity.mDocList.contains(formatPath)) {
//            mData.add(TripartiteActivity.mAttach.get(".doc"));
//        } else if (TripartiteActivity.mPdfList.contains(formatPath)) {
//            mData.add(TripartiteActivity.mAttach.get(".pdf"));
//        } else if (TripartiteActivity.mPptList.contains(formatPath)) {
//            mData.add(TripartiteActivity.mAttach.get(".ppt"));
//        } else if (TripartiteActivity.mVideoList.contains(formatPath)) {
//            mData.add(TripartiteActivity.mAttach.get(".video"));
//        } else {
//            mData.add(TripartiteActivity.mAttach.get(".doc"));
//        }

        mData.add(R.drawable.attachment);
        Log.e(TAG, "yanlog remove end size:" + mData.size());
        Log.e(TAG, "yanlog mData at 0:" + mData.get(0));
        mAttachAdapter.notifyDataSetChanged();
        mAttachView.requestLayout();
        mAttachView.setMinimumHeight(600);
    }

    public void showDatePickerDialog() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());

        CustomDatePicker customDatePicker = new CustomDatePicker(getActivity(), new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                try {
                    mEditRevisitTime.setText(DateUtils.format_yyyy_MM_dd_HH_mm_ss.format(DateUtils.format_yyyy_MM_dd_HH_mm.parse(time)));
                    mLabRevisitTime.setCompoundDrawables(mWattingChanged, null, null, null);
                    mEditRevisitTime.setTextColor(mRes.getColor(R.color.main_text_color));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, "1970-01-01 00:00", "2099-12-12 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(true); // 不显示时和分
        //customDatePicker.showYearMonth();
        customDatePicker.setIsLoop(false); // 不允许循环滚动
        //customDatePicker.show(dateText.getText().toString() + " " + timeText.getText().toString());
        customDatePicker.show(DateUtils.format_yyyy_MM_dd_HH_mm.format(new Date()));
    }
}
