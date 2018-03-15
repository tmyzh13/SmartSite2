package com.isoftstone.smartsite.model.tripartite.activity;

import android.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.http.user.CompanyBean;
import com.isoftstone.smartsite.http.patrolreport.DictionaryBean;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.model.tripartite.adapter.AddressDialogListViewAdapter;
import com.isoftstone.smartsite.model.tripartite.adapter.DialogListViewAdapter;
import com.isoftstone.smartsite.model.tripartite.fragment.RevisitFragment;
import com.isoftstone.smartsite.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanyongjun on 2017/10/18.
 * 添加巡查报告界面
 */

public class AddReportActivity extends BaseActivity {
    public final static int REQUEST_ACTIVITY_ATTACH = 0;//请求图片的request code

    private List<Uri> attach = new ArrayList<>();
    private Resources mRes = null;
    private Drawable mWaittingAdd = null;
    private Drawable mWattingChanged = null;
    private HttpPost mHttpPost = null;
    private ArrayList<String> mQueryTypes = new ArrayList<>();

    //the view in this activity
    public EditText mAddressEdittext = null;
    public TextView mEditAddress = null;
    public TextView mTypesEditor = null;
    public TextView mAddress = null;
    public TextView mCompany = null;
    public TextView mLabTypes = null;
    public TextView mBuildCompany = null;
    public TextView mConsCompany = null;
    public TextView mSuperCompany = null;

    public EditText mEditCompany = null;
    public EditText mEditBuildCompany = null;
    public EditText mEditConsCompany = null;
    public EditText mEditSuperCompany = null;
    public boolean isSettedType = false;
    public RevisitFragment mRevisitFrag = null;
    public String mTypes = "1";

    public ArrayList<String> mAddressList = new ArrayList<>();
    public ArrayList<DictionaryBean> mTypesList = new ArrayList<>();
    public boolean isSettedAddress = false;

    String mCompanyName = "";

    public boolean isShowCompany = true;
    public final static boolean IS_COMPANY_SWITCH = false;


    @Override
    protected int getLayoutRes() {

        //TODO 这个界面还需要不少的润色
        return R.layout.activity_add_inspect_report;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        init();
    }

    public void init() {
        mHttpPost = new HttpPost();
        mRes = getResources();
        mWaittingAdd = mRes.getDrawable(R.drawable.addcolumn);
        mWaittingAdd.setBounds(0, 0, mWaittingAdd.getIntrinsicWidth(), mWaittingAdd.getIntrinsicHeight());
        mWattingChanged = mRes.getDrawable(R.drawable.editcolumn);
        mWattingChanged.setBounds(0, 0, mWattingChanged.getIntrinsicWidth(), mWattingChanged.getIntrinsicHeight());

        initView();
        initListener();
        //restoreData();
        new QueryReportTypeTask().execute();
    }

    public void initView() {
        mAddressEdittext = (EditText) findViewById(R.id.address_edittext);
        mEditAddress = (TextView) findViewById(R.id.edit_address);
        mTypesEditor = (TextView) findViewById(R.id.lab_report_types);
        mAddress = (TextView) findViewById(R.id.lab_address);
        mCompany = (TextView) findViewById(R.id.lab_company);
        mLabTypes = (TextView) findViewById(R.id.lab_types);
        mBuildCompany = (TextView) findViewById(R.id.lab_build_company);
        mConsCompany = (TextView) findViewById(R.id.lab_cons_company);
        mSuperCompany = (TextView) findViewById(R.id.lab_super_company);

        mEditBuildCompany = (EditText) findViewById(R.id.edit_build_company);
        mEditConsCompany = (EditText) findViewById(R.id.edit_cons_company);
        mEditSuperCompany = (EditText) findViewById(R.id.edit_super_company);
        mTypesEditor.setTextColor(getResources().getColor(R.color.des_text_color));
        mEditAddress.setTextColor(getResources().getColor(R.color.des_text_color));
        mRevisitFrag = (RevisitFragment) getSupportFragmentManager().findFragmentById(R.id.frag_reply_inspect_report);
        initCompany();
    }

    private void initCompany() {
        try {
            mEditCompany = (EditText) findViewById(R.id.edit_company);
            mEditCompany.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s != null && s.length() != 0) {
                        mCompany.setCompoundDrawables(mWattingChanged, null, null, null);
                    } else {
                        mCompany.setCompoundDrawables(mWaittingAdd, null, null, null);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            String departId = mHttpPost.mLoginBean.getmUserBean().getLoginUser().getDepartmentId();
            Log.e(TAG, "yanlog departId:" + departId);
            if (departId != null) {
                //mEditCompany.setText(departId);
            }
            mEditCompany.setOnClickListener(null);
            mEditCompany.setClickable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveData() {
        mRevisitFrag.saveData();
        SPUtils.saveString("add_report_company", mEditCompany.getText().toString());
        SPUtils.saveString("add_report_build_company", mEditBuildCompany.getText().toString());
        SPUtils.saveString("add_report_cons_company", mEditConsCompany.getText().toString());
        SPUtils.saveString("add_report_super_company", mEditSuperCompany.getText().toString());

        if (isSettedType) {
            SPUtils.saveString("add_report_type", mTypesEditor.getText().toString());
        }
        if (isSettedAddress) {
            SPUtils.saveString("add_report_address", mEditAddress.getText().toString());
        }

        SPUtils.saveString("add_report_address", mAddressEdittext.getText().toString());
    }

    public void restoreData() {
        mEditCompany.setText(SPUtils.getString("add_report_company", ""));
        mEditBuildCompany.setText(SPUtils.getString("add_report_build_company", ""));
        mEditConsCompany.setText(SPUtils.getString("add_report_cons_company", ""));
        mEditSuperCompany.setText(SPUtils.getString("add_report_super_company", ""));

        String type = SPUtils.getString("add_report_type", "");
        if (!TextUtils.isEmpty(type)) {
            mTypesEditor.setText(type);
            isSettedType = true;
            mLabTypes.setCompoundDrawables(mWattingChanged, null, null, null);
            mTypesEditor.setTextColor(getResources().getColor(R.color.main_text_color));
        }

        String address = SPUtils.getString("add_report_address", "");
        if (!TextUtils.isEmpty(address)) {
            mEditAddress.setText(address);
            isSettedAddress = true;
            mAddress.setCompoundDrawables(mWattingChanged, null, null, null);
            mEditAddress.setTextColor(getResources().getColor(R.color.main_text_color));
        }

    }

    public void initListener() {
        mTypesEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddReportActivity.this);
                View dialogLayout = LayoutInflater.from(AddReportActivity.this).inflate(R.layout.dialog_add_report, null);
                ListView listView = (ListView) dialogLayout.findViewById(R.id.listview_dialog_add_report);

                DialogListViewAdapter adapter = new DialogListViewAdapter(AddReportActivity.this, mTypesList);
                listView.setAdapter(adapter);

                builder.setView(dialogLayout);
                final AlertDialog dialog = builder.create();
                dialog.show();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            mTypesEditor.setText(mTypesList.get(position).getContent());
                            isSettedType = true;
                            //mTypes = position + 1 + "";
                            mLabTypes.setCompoundDrawables(mWattingChanged, null, null, null);
                            mTypesEditor.setTextColor(getResources().getColor(R.color.main_text_color));
                            //mTypes = mTypesList.get(position)+"";
                            DictionaryBean bean = mTypesList.get(position);
                            int type = Integer.parseInt(bean.getValue());
                            mTypes = type + "";
                            if (IS_COMPANY_SWITCH) {
                                View v = AddReportActivity.this.findViewById(R.id.linear_company);
                                if (v != null && type / 100 == 2) {
                                    v.setVisibility(View.GONE);
                                    isShowCompany = false;
                                } else {
                                    v.setVisibility(View.VISIBLE);
                                    isShowCompany = true;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });

            }
        });

        mEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddReportActivity.this);
                View dialogLayout = LayoutInflater.from(AddReportActivity.this).inflate(R.layout.dialog_add_report, null);
                ListView listView = (ListView) dialogLayout.findViewById(R.id.listview_dialog_add_report);

                AddressDialogListViewAdapter adapter = new AddressDialogListViewAdapter(AddReportActivity.this, mAddressList);
                listView.setAdapter(adapter);

                builder.setView(dialogLayout);
                final AlertDialog dialog = builder.create();
                dialog.show();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mEditAddress.setText(mAddressList.get(position));
                        isSettedAddress = true;
                        mAddress.setCompoundDrawables(mWattingChanged, null, null, null);
                        mEditAddress.setTextColor(getResources().getColor(R.color.main_text_color));
                        dialog.dismiss();
                    }
                });

            }
        });


        mEditBuildCompany.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() != 0) {
                    mBuildCompany.setCompoundDrawables(mWattingChanged, null, null, null);
                } else {
                    mBuildCompany.setCompoundDrawables(mWaittingAdd, null, null, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEditConsCompany.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() != 0) {
                    mConsCompany.setCompoundDrawables(mWattingChanged, null, null, null);
                } else {
                    mConsCompany.setCompoundDrawables(mWaittingAdd, null, null, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEditSuperCompany.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() != 0) {
                    mSuperCompany.setCompoundDrawables(mWattingChanged, null, null, null);
                } else {
                    mSuperCompany.setCompoundDrawables(mWaittingAdd, null, null, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    /**
     * 点击保存按钮
     *
     * @param v
     */
    public void onBtnSaveClick(View v) {
        saveData();
        finish();
        //TODO
    }

    /**
     * 点击返回按钮
     *
     * @param v
     */
    public void onBtnBackClick(View v) {
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ACTIVITY_ATTACH: {
                Log.e(TAG, "onactivityresult:" + data.getData());
                //mRevisitFrag.addAttachUri(data.getDataString());
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private class QueryReportTypeTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
//            ArrayList<MessageBean> msgs = mHttpPost.getPatrolReportList("", "", "", "1");
            try {
                mAddressList = mHttpPost.getPatrolAddress();
                ArrayList<DictionaryBean> tempLists = mHttpPost.getDictionaryList("zh");
                if (tempLists != null) {
                    for (DictionaryBean temp : tempLists) {
                        Log.e(TAG, "yanlog dictionarybean temp:" + temp);
                    }
                }
                if (tempLists != null && tempLists.size() > 0) {
                    mTypesList.clear();
                    mTypesList.addAll(tempLists);
                }


                ArrayList<CompanyBean> companyList = mHttpPost.getCompanyList("zh");
                String myid = mHttpPost.mLoginBean.getmUserBean().getLoginUser().getDepartmentId();
                for (int i = 0; i < companyList.size(); i++) {
                    String value = companyList.get(i).getValue();
                    if (value != null && value.equals(myid + "")) {
                        mCompanyName = companyList.get(i).getContent();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (mCompanyName != null) {
                mEditCompany.setText(mCompanyName);
            }
            super.onPostExecute(s);
        }
    }

}
