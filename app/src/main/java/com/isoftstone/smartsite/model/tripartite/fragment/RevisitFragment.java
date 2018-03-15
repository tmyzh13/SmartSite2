package com.isoftstone.smartsite.model.tripartite.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.NumberPicker;
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
import com.isoftstone.smartsite.model.tripartite.activity.AddReportActivity;
import com.isoftstone.smartsite.model.tripartite.adapter.AttachGridViewAdapter;
import com.isoftstone.smartsite.model.tripartite.data.ITime;
import com.isoftstone.smartsite.model.tripartite.data.ReportData;
import com.isoftstone.smartsite.utils.DateUtils;
import com.isoftstone.smartsite.utils.FilesUtils;
import com.isoftstone.smartsite.utils.ImageUtils;
import com.isoftstone.smartsite.utils.SPUtils;
import com.isoftstone.smartsite.utils.ToastUtils;
import com.isoftstone.smartsite.widgets.CustomDatePicker;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * 回访下面的白嫩及矿
 * Created by yanyongjun on 2017/10/30.
 */
public class RevisitFragment extends BaseFragment {
    private GridView mAttachView = null;
    private AttachGridViewAdapter mAttachAdapter = null;
    private ArrayList<Object> mData = null;
    private AddReportActivity mAddReportActivity = null;
    public final static int REQUEST_ACTIVITY_ATTACH = 0;//请求图片的request code

    private Resources mRes = null;
    private Drawable mWaittingAdd = null;
    private Drawable mWattingChanged = null;
    private HttpPost mHttpPost = null;
    public final static int SELECT_IMAGE_RESULT_CODE = 200;
    private TextView mName = null;
    private TextView mReportName = null;
    private TextView mReportMsg = null;
    private TextView mBeginTime = null;
    private TextView mEndTime = null;
    private TextView mRevisitTime = null;
    private TextView mLabTime = null;
    private TextView mReportPeopleName = null;

    private EditText mEditName = null;
    private EditText mEditReportName = null;
    private EditText mEditReportMsg = null;
    private TextView mEditRevisitTime = null;
    private Button mSubButton = null;
    private RadioButton mRadioYes = null;
    private RadioButton mRadioNo = null;
    private RevisitFragment mFragment;
    private boolean mBeginDate = false;
    private boolean mEndDate = false;
    private ITime mRevisitDate = null;

    private Dialog mLoginingDlg = null;

    private Calendar mCal = null;

    //private ArrayList<String> mFilesPath = new ArrayList<>();
    public static RevisitFragment newInstance() {
        RevisitFragment fragment = new RevisitFragment();
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_revisit_report;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        init();
    }

    private void init() {
        this.mFragment = this;
        mRes = getResources();
        mWaittingAdd = mRes.getDrawable(R.drawable.addcolumn);
        mWaittingAdd.setBounds(0, 0, mWaittingAdd.getIntrinsicWidth(), mWaittingAdd.getIntrinsicHeight());
        mWattingChanged = mRes.getDrawable(R.drawable.editcolumn);
        mWattingChanged.setBounds(0, 0, mWattingChanged.getIntrinsicWidth(), mWattingChanged.getIntrinsicHeight());

        mCal = Calendar.getInstance();
        mHttpPost = new HttpPost();
        if (getActivity() instanceof AddReportActivity) {
            mAddReportActivity = (AddReportActivity) getActivity();
        }
        initView();
        initListener();
        initGridView();
        initUserHead();
        restoreData();
    }

    /**
     * 加载用户的头像
     */
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

    private void initView() {
        mReportPeopleName = (TextView) getView().findViewById(R.id.lab_report_people_name);
        try {
            mReportPeopleName.setText(HttpPost.mLoginBean.getmUserBean().getLoginUser().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mName = (TextView) getView().findViewById(R.id.lab_name);
        mReportName = (TextView) getView().findViewById(R.id.lab_report_name);
        mReportMsg = (TextView) getView().findViewById(R.id.lab_report_msg);
        mBeginTime = (TextView) getView().findViewById(R.id.lab_begin_time);
        mEndTime = (TextView) getView().findViewById(R.id.lab_end_time);
        mRevisitTime = (TextView) getView().findViewById(R.id.lab_revisit_time);
        mLabTime = (TextView) getView().findViewById(R.id.lab_inspect_report_time);

        mEditName = (EditText) getView().findViewById(R.id.edit_name);
        mEditReportName = (EditText) getView().findViewById(R.id.edit_report_name);
        mEditReportMsg = (EditText) getView().findViewById(R.id.edit_report_msg);
        mEditRevisitTime = (TextView) getView().findViewById(R.id.lab_edit_revisit_time);
        mSubButton = (Button) getView().findViewById(R.id.btn_add_report_submit);
        mRadioNo = (RadioButton) getView().findViewById(R.id.radio_no);
        mRadioYes = (RadioButton) getView().findViewById(R.id.radio_yes);
    }

    private String parseTime(String time) {
        try {
            Date date = DateUtils.format_yyyy_MM_dd_HH_mm_ss.parse(time);
            String result = DateUtils.format_yyyy_MM_dd_HH_mm_ss.format(date);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveData() {
        SPUtils.saveString("add_report_fragment_check_people_name", mEditName.getText().toString());
        String beginTime = mBeginTime.getText().toString();
        String endTime = mEndTime.getText().toString();
        SPUtils.saveString("add_report_fragment_report_name", mEditReportName.getText().toString());
        SPUtils.saveString("add_report_fragment_report_msg", mEditReportMsg.getText().toString());

        if (parseTime(beginTime) != null) {
            SPUtils.saveString("add_report_fragment_begin_time", beginTime);
        }
        if (parseTime(endTime) != null) {
            SPUtils.saveString("add_report_framgent_end_time", endTime);
        }
        boolean visit = mRadioYes.isChecked();
        String visitTime = mEditRevisitTime.getText().toString();
        SPUtils.saveBoolean("add_report_fragment_isVisit", visit);
        Log.e(TAG, "saveData:" + visitTime);
        if (parseTime(visitTime) != null) {
            Log.e(TAG, "saveData into sp");
            SPUtils.saveString("add_report_fragment_visit_time", visitTime);
        }
    }

    public void restoreData() {
        if (mAddReportActivity != null) {
            mEditName.setText(SPUtils.getString("add_report_fragment_check_people_name", ""));
            mEditReportName.setText(SPUtils.getString("add_report_fragment_report_name", ""));
            mEditReportMsg.setText(SPUtils.getString("add_report_fragment_report_msg", ""));

            String beginTime = SPUtils.getString("add_report_fragment_begin_time", null);
            String endTime = SPUtils.getString("add_report_framgent_end_time", null);
            String visitTime = SPUtils.getString("add_report_fragment_visit_time", null);
            Log.e(TAG, "visittime:" + visitTime);
            if (beginTime != null) {
                mBeginTime.setText(beginTime);
                mBeginTime.setTextColor(getActivity().getResources().getColor(R.color.main_text_color));
            }
            if (endTime != null) {
                mEndTime.setText(endTime);
                mEndTime.setTextColor(getActivity().getResources().getColor(R.color.main_text_color));
            }
            if (beginTime != null && endTime != null) {
                mLabTime.setCompoundDrawables(mWattingChanged, null, null, null);
            }
            boolean isVisit = SPUtils.getBoolean("add_report_fragment_isVisit", true);
            if (isVisit) {
                mRadioYes.setChecked(true);
                mRadioNo.setChecked(false);
                RelativeLayout relativeLayout = (RelativeLayout) getView().findViewById(R.id.relative_revisit_time);
                relativeLayout.setVisibility(View.VISIBLE);
            } else {
                mRadioNo.setChecked(true);
                mRadioYes.setChecked(false);
                RelativeLayout relativeLayout = (RelativeLayout) getView().findViewById(R.id.relative_revisit_time);
                relativeLayout.setVisibility(View.GONE);
            }
            if (visitTime != null) {
                mEditRevisitTime.setText(visitTime);
                mEditRevisitTime.setTextColor(getActivity().getResources().getColor(R.color.main_text_color));
                mRevisitTime.setCompoundDrawables(mWattingChanged, null, null, null);
            }
        }
    }

    private void initListener() {
        mSubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatrolBean reportData = new ReportData();
                if (mAddReportActivity != null) {
//                    private String company; //巡查单位
//                    private String developmentCompany;//	建设单位
//                    private String constructionCompany;//	施工单位
//                    private String supervisionCompany;//		监理单位
//                    String address = mAddReportActivity.mEditAddress.getText().toString();
                    String address = mAddReportActivity.mAddressEdittext.getText().toString();
                    String company = mAddReportActivity.mEditCompany.getText().toString();
                    String type = mAddReportActivity.mTypesEditor.getText().toString();
                    String developmentCompany = mAddReportActivity.mEditBuildCompany.getText().toString();
                    String constructionCompany = mAddReportActivity.mEditConsCompany.getText().toString();
                    String supervisionCompany = mAddReportActivity.mEditSuperCompany.getText().toString();
                    if (TextUtils.isEmpty(address) || !mAddReportActivity.isSettedType) {
                        Toast.makeText(getActivity(), "还有未填写的数据", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (AddReportActivity.IS_COMPANY_SWITCH) {
                        if (mAddReportActivity.isShowCompany && (TextUtils.isEmpty(constructionCompany) ||
                                TextUtils.isEmpty(supervisionCompany) || TextUtils.isEmpty(developmentCompany))) {
                            Toast.makeText(getActivity(), "还有未填写的数据", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    reportData.setAddress(address);
                    reportData.setCompany(company);
                    reportData.setDevelopmentCompany(developmentCompany);
                    reportData.setConstructionCompany(constructionCompany);
                    reportData.setSupervisionCompany(supervisionCompany);
                    reportData.setDate(DateUtils.format_yyyy_MM_dd_HH_mm_ss.format(new Date()));
                    reportData.setCategory(mAddReportActivity.mTypes);
                    //
                    BaseUserBean user = new BaseUserBean();
                    reportData.setCreator(user);
                    //TODO type?
                    boolean visit = mRadioYes.isChecked();
                    String visitTime = mEditRevisitTime.getText().toString();
                    if (visit) {
                        reportData.setVisitDate(parseTime(visitTime));
                    }
                } else {
                    reportData = ((BaseActivity) getActivity()).getReportData();
                }
                String checkPeopleName = mEditName.getText().toString();
                String beginTime = mBeginTime.getText().toString();
                String endTime = mEndTime.getText().toString();
                String reportName = mEditReportName.getText().toString();
                String reportMsg = mEditReportMsg.getText().toString();
                boolean visit = mRadioYes.isChecked();
                String visitTime = mEditRevisitTime.getText().toString();
                Log.e(TAG, "visit:" + visit + ":" + visitTime);

                if (TextUtils.isEmpty(reportName) || TextUtils.isEmpty(checkPeopleName) || TextUtils.isEmpty(reportMsg) || TextUtils.isEmpty(reportName) ||
                        parseTime(beginTime) == null || parseTime(endTime) == null || (visit && parseTime(visitTime) == null)) {
                    Toast.makeText(getActivity(), "还有未填写的数据", Toast.LENGTH_SHORT).show();
                    return;
                }

                //ensure the begin time is earlier than the end time
                try {
                    Date beginDate = DateUtils.format_yyyy_MM_dd_HH_mm_ss.parse(beginTime);
                    Date endDate = DateUtils.format_yyyy_MM_dd_HH_mm_ss.parse(endTime);
                    if (beginDate.after(endDate)) {
                        ToastUtils.showShort("巡查开始时间必须位于巡查结束时间之前");
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ReportBean reportBean = new ReportBean();
                reportBean.setPatrolUser(checkPeopleName);
                reportBean.setPatrolDateStart(parseTime(beginTime));
                reportBean.setPatrolDateEnd(parseTime(endTime));
                reportBean.setContent(reportMsg);
                reportBean.setCategory(1);
                reportBean.setName(reportName);
                reportBean.setDate(DateUtils.format_yyyy_MM_dd_HH_mm_ss.format(new Date()));
                reportBean.setVisit(visit);
                BaseUserBean userBean = new BaseUserBean();
                userBean.setId(mHttpPost.mLoginBean.getmUserBean().getLoginUser().getId());
                reportData.setCreator(userBean);
                reportBean.setCreator(userBean);

                reportData.setVisit(visit);
                if (visit) {
                    reportBean.setVisitDate(parseTime(visitTime));
                }
                new SubReport(mAddReportActivity != null, reportData, reportBean).execute();
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
        mBeginTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(mBeginTime, mLabTime);
            }

        });

        mEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(mEndTime, mLabTime);
            }

        });

        mEditRevisitTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(mEditRevisitTime, mRevisitTime);
            }

        });
        mEditName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() != 0) {
                    mName.setCompoundDrawables(mWattingChanged, null, null, null);
                } else {
                    mName.setCompoundDrawables(mWaittingAdd, null, null, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEditReportName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() != 0) {
                    mReportName.setCompoundDrawables(mWattingChanged, null, null, null);
                } else {
                    mReportName.setCompoundDrawables(mWaittingAdd, null, null, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEditReportMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() != 0) {
                    mReportMsg.setCompoundDrawables(mWattingChanged, null, null, null);
                } else {
                    mReportMsg.setCompoundDrawables(mWaittingAdd, null, null, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    private void initTimePicker(final Dialog dialog, final View view, final TextView editRight, final TextView labLeft) {
        final NumberPicker year = (NumberPicker) view.findViewById(R.id.picker_year);
        final NumberPicker month = (NumberPicker) view.findViewById(R.id.picker_month);
        final NumberPicker day = (NumberPicker) view.findViewById(R.id.picker_day);
        final NumberPicker hour = (NumberPicker) view.findViewById(R.id.picker_hour);
        final NumberPicker min = (NumberPicker) view.findViewById(R.id.picker_min);
        Date curData = new Date();
        year.setMinValue(2016);
        year.setMaxValue(2099);
        year.setValue(Integer.parseInt(DateUtils.format_yyyy.format(curData)));
        month.setMaxValue(12);
        month.setMinValue(1);
        month.setValue(Integer.parseInt(DateUtils.format_MM.format(curData)));
        day.setMaxValue(31);
        day.setMinValue(1);
        day.setValue(Integer.parseInt(DateUtils.format_dd.format(curData)));
        month.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal == 1 || newVal == 3 || newVal == 5 || newVal == 7 || newVal == 8 || newVal == 10 || newVal == 12) {
                    day.setMaxValue(31);
                } else {
                    if (newVal == 2) {
                        day.setMaxValue(28);
                    } else {
                        day.setMaxValue(30);
                    }
                }

                int value = year.getValue();
                if ((value % 4 == 0 && value % 100 != 0) || (value % 400 == 0)) {
                    if (newVal == 2) {
                        day.setMaxValue(29);
                    }
                }
            }
        });
        year.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                int value = year.getValue();
                if (month.getValue() == 2) {
                    if ((value % 4 == 0 && value % 100 != 0) || (value % 400 == 0)) {
                        day.setMaxValue(29);
                    } else {
                        day.setMaxValue(28);
                    }
                }
            }
        });
        hour.setMaxValue(23);
        hour.setMinValue(0);
        hour.setValue(Integer.parseInt(DateUtils.format_hour.format(curData)));
        min.setMaxValue(59);
        min.setMinValue(0);
        min.setValue(Integer.parseInt(DateUtils.format_min.format(curData)));
        DateUtils.setNumberPickerDividerColor(year);
        DateUtils.setNumberPickerDividerColor(month);
        DateUtils.setNumberPickerDividerColor(day);
        DateUtils.setNumberPickerDividerColor(hour);
        DateUtils.setNumberPickerDividerColor(min);
        view.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editRight == mBeginTime) {
                    mBeginDate = true;
                } else if (editRight == mEndTime) {
                    mEndDate = true;

                }
                //editRight.setText("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                String result = year.getValue() + "-" + month.getValue() + "-" + day.getValue() + " " + hour.getValue() + ":" + min.getValue() + ":00";
                editRight.setText(result);
                editRight.setTextColor(mRes.getColor(R.color.main_text_color));
                if (mBeginDate && mEndDate) {
                    labLeft.setCompoundDrawables(mWattingChanged, null, null, null);
                }
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private class SubReport extends AsyncTask<String, Integer, Boolean> {
        private PatrolBean mReportData = null;
        private ReportBean mRevisitData = null;
        private boolean mIsAddReport = true;

        public SubReport(boolean isAddReport, PatrolBean data, ReportBean revisit) {
            mIsAddReport = isAddReport;
            mReportData = data;
            mRevisitData = revisit;
        }

        @Override
        protected void onPreExecute() {
            initDlg();
            showDlg();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Log.e(TAG, "yanlog addReport");
            try {
                int id = -1;
                if (mIsAddReport) {
                    PatrolBean reponse = mHttpPost.addPatrolReport(mReportData);
                    PatrolBean temp = new PatrolBean();
                    temp.setId(reponse.getId());
                    id = reponse.getId();
                    mRevisitData.setPatrol(temp);
                } else {
                    PatrolBean temp = new PatrolBean();
                    temp.setId(mReportData.getId());
                    mRevisitData.setPatrol(temp);
                    id = mReportData.getId();

                }
                mRevisitData.setDate(DateUtils.format_yyyy_MM_dd_HH_mm_ss.format(new Date()));
                mRevisitData.setStatus(2);
                mHttpPost.addPatrolVisit(mRevisitData);

                PatrolBean report = mHttpPost.getPatrolReport(id + "");
                if (report.getStatus() == 2) {
                    ArrayList<ReportBean> reports = report.getReports();

                    int reportId = reports.get(reports.size() - 1).getId();
                    for (Object path : mData) {
                        if (path instanceof String) {
                            mHttpPost.reportFileUpload((String) path, reportId);
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            closeDlg();
            if (s == true) {
                Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), "提交失败", Toast.LENGTH_SHORT).show();
            }

        }
    }


    public void initGridView() {
        mAttachView = (GridView) getView().findViewById(R.id.grid_view);

        mData = new ArrayList<Object>();
        mData.add(R.drawable.attachment);
        //mAttachAdapter = new SimpleAdapter(getActivity(), mData, R.layout.add_attach_grid_item, new String[]{"image"}, new int[]{R.id.image});
        mAttachAdapter = new AttachGridViewAdapter(getActivity(), mData);
        mAttachAdapter.setmIsShowDelete(true);
        mAttachView.setAdapter(mAttachAdapter);

        mAttachView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mData.size() - 1) {
                    //点击添加附件
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.setType("*/*");
                    startActivityForResult(i, REQUEST_ACTIVITY_ATTACH);
//                    new LFilePicker().withSupportFragment(mFragment)
//                            .withRequestCode(REQUEST_ACTIVITY_ATTACH)
//                            .withTitle(getString(R.string.select_title))
//                            .withMutilyMode(true)
//                            .withBackgroundColor("#4d7ef9")
//                            .withAddText(getString(R.string.selected))
//                            .start();
                }
            }
        });
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
                        addAttach(uri.getPath());
                        return;
                    }
                    String path = FilesUtils.getPath(getActivity(), uri);
                    addAttach(path);
                }
                break;

            }
        }
//        if (resultCode == RESULT_OK) {
//            if (requestCode == REQUEST_ACTIVITY_ATTACH) {
//                List<String> list = data.getStringArrayListExtra("paths");
//                Log.i("LeonFilePicker", list.size()+"...."+list);
//                for (String path : list) {
//                    addAttach(path);
//                }
//            }
//        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    //add files
    public void addAttach(String path) {
        Log.e(TAG, "yanlog remove begin size:" + mData.size());
        String formatPath = FilesUtils.getFormatString(path);
        Log.e(TAG, "yanlog remove begin size at0:" + mData.get(0));
        mData.remove(mData.size() - 1);
        mData.add(path);

        mData.add(R.drawable.attachment);
        Log.e(TAG, "yanlog remove end size:" + mData.size());
        Log.e(TAG, "yanlog mData at 0:" + mData.get(0));
        mAttachAdapter.notifyDataSetChanged();
    }


    public void showDatePickerDialog(final TextView editRight, final TextView labLeft) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());

        CustomDatePicker customDatePicker = new CustomDatePicker(getActivity(), new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                try {
                    editRight.setText(DateUtils.format_yyyy_MM_dd_HH_mm_ss.format(DateUtils.format_yyyy_MM_dd_HH_mm.parse(time)));
                    labLeft.setCompoundDrawables(mWattingChanged, null, null, null);
                    editRight.setTextColor(mRes.getColor(R.color.main_text_color));
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
