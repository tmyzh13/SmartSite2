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
import com.isoftstone.smartsite.model.tripartite.activity.ReadReportActivity;
import com.isoftstone.smartsite.model.tripartite.activity.ReplyReportActivity;
import com.isoftstone.smartsite.model.tripartite.activity.RevistReportActivity;
import com.isoftstone.smartsite.model.tripartite.activity.TripartiteActivity;
import com.isoftstone.smartsite.model.tripartite.data.ReportData;

import java.util.ArrayList;

/**
 * Created by yanyongjun 2017/10/17.
 */

public class InspectReportAdapter extends BaseAdapter {
    private Context mContext = null;
    private ArrayList<ReportData> mDatas;
    Resources mRes = null;
    Drawable mRevisitGray;
    Drawable mRevisitBitmap;

    public InspectReportAdapter(Context context, ArrayList<ReportData> datas) {
        mContext = context;
        mDatas = datas;
        mRes = mContext.getResources();

        mRevisitGray = mRes.getDrawable(R.drawable.revisite_gray);
        mRevisitGray.setBounds(0, 0, mRevisitGray.getIntrinsicWidth(), mRevisitGray.getIntrinsicHeight());

        mRevisitBitmap = mRes.getDrawable(R.drawable.revisite);
        mRevisitBitmap.setBounds(0, 0, mRevisitBitmap.getIntrinsicWidth(), mRevisitBitmap.getIntrinsicHeight());

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
            view = LayoutInflater.from(mContext).inflate(R.layout.listview_inspect_report_item, null);
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

                View btnView = view.findViewById(R.id.linear_read_report);
                if (btnView != null) {
                    btnView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, ReadReportActivity.class);
                            intent.putExtra("_id", mDatas.get(position).getId());
                            mContext.startActivity(intent);
                        }
                    });
                }
                View btnReply = view.findViewById(R.id.linear_reply_report);
                btnReply.setClickable(true);
                if (btnReply != null) {
                    btnReply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, ReplyReportActivity.class);
                            intent.putExtra("_id", mDatas.get(position).getId());
                            mContext.startActivity(intent);
                        }
                    });
                }

                View btnRevisit = view.findViewById(R.id.linear_revisit_report);
                TextView labRevisit = (TextView)view.findViewById(R.id.lab_revisit);

                btnRevisit.setClickable(true);
                btnRevisit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, RevistReportActivity.class);
                        intent.putExtra("_id", mDatas.get(position).getId());
                        mContext.startActivity(intent);
                    }
                });

                labRevisit.setTextColor(mRes.getColor(R.color.mainColor));
                labRevisit.setCompoundDrawables(mRevisitBitmap, null, null, null);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return view;
    }
}
