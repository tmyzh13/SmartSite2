package com.isoftstone.smartsite.model.tripartite.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.model.message.data.MsgData;
import com.isoftstone.smartsite.model.tripartite.activity.CheckReportActivity;
import com.isoftstone.smartsite.model.tripartite.activity.ReadReportActivity;
import com.isoftstone.smartsite.model.tripartite.activity.TripartiteActivity;
import com.isoftstone.smartsite.model.tripartite.data.ReportData;

import java.util.ArrayList;

/**
 * Created by yanyongjun 2017/10/17.
 */

public class CheckReportAdapter extends BaseAdapter {
    private Context mContext = null;
    private ArrayList<ReportData> mDatas;
    Resources mRes = null;

    Drawable mCheckGray;
    Drawable mCheckBitmap;

    public CheckReportAdapter(Context context, ArrayList<ReportData> datas) {
        mContext = context;
        mDatas = datas;
        mRes = mContext.getResources();

        mCheckGray = mRes.getDrawable(R.drawable.revisite_gray);
        mCheckGray.setBounds(0, 0, mCheckGray.getIntrinsicWidth(), mCheckGray.getIntrinsicHeight());

        mCheckBitmap = mRes.getDrawable(R.drawable.revisite);
        mCheckBitmap.setBounds(0, 0, mCheckBitmap.getIntrinsicWidth(), mCheckBitmap.getIntrinsicHeight());

    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.listview_check_report_item, null);
        }
        if (view != null) {
            try {
                TextView time = (TextView) view.findViewById(R.id.lab_time);
                TextView title = (TextView) view.findViewById(R.id.lab_title);
                ImageView imageStatus = (ImageView) view.findViewById(R.id.img_status);
                TextView name = (TextView) view.findViewById(R.id.lab_name);
                TextView company = (TextView) view.findViewById(R.id.lab_company);
                ReportData reportData = mDatas.get(position);
                time.setText(MsgData.format3.format(reportData.getFormatDate()));
                title.setText(reportData.getAddress());
                name.setText(reportData.getCreator().getName());
                company.setText(reportData.getCompany());
                int status = reportData.getStatus();
                if (status >= 1) {
                    status--;
                }
                imageStatus.setImageDrawable(mRes.getDrawable(TripartiteActivity.STATUS_IMG[status]));
                View v = view.findViewById(R.id.linear_read);
                TextView labCheck = (TextView)view.findViewById(R.id.lab_check);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ReadReportActivity.class);
                        intent.putExtra("_id", mDatas.get(position).getId());
                        mContext.startActivity(intent);
                    }
                });
                View v1 = view.findViewById(R.id.linear_check);
                if (reportData.getStatus() == ReportData.STATUS_WAITTING_CHECK || reportData.getStatus() == ReportData.STATUS_REJECT) {
                    v1.setClickable(true);
                    v1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, CheckReportActivity.class);
                            intent.putExtra("_id", mDatas.get(position).getId());
                            mContext.startActivity(intent);
                        }
                    });

                    labCheck.setTextColor(mRes.getColor(R.color.mainColor));
                    labCheck.setCompoundDrawables(mCheckBitmap,null,null,null);
                } else {
                    v1.setClickable(false);

                    labCheck.setTextColor(mRes.getColor(R.color.des_text_color));
                    labCheck.setCompoundDrawables(mCheckGray,null,null,null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return view;
    }
}
