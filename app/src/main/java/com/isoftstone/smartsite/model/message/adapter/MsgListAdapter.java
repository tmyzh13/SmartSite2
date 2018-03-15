package com.isoftstone.smartsite.model.message.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.model.message.data.MsgData;

import java.util.List;

/**
 * Created by yanyongjun on 2017/10/28.
 */


public class MsgListAdapter extends BaseAdapter {
    private final static String TAG = "MsglistAdapter";
    private Context mContext = null;
    private List<MsgData> mDatas = null;

    public MsgListAdapter(Context context, List<MsgData> list) {
        mContext = context;
        mDatas = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e(TAG, "getView:" + position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_msg_item, null);
        }
        if (convertView != null) {
            RelativeLayout relativeTime = (RelativeLayout) convertView.findViewById(R.id.relative_year);
            RelativeLayout relativeNormal = (RelativeLayout) convertView.findViewById(R.id.relative_normal);
            TextView labYear = (TextView) convertView.findViewById(R.id.lab_year);
            if (mDatas.get(position).getType() == MsgData.TYPE_YEAR) {
                relativeNormal.setVisibility(View.GONE);
                relativeTime.setVisibility(View.VISIBLE);
                labYear.setText(mDatas.get(position).getDateString());
            } else {
                relativeNormal.setVisibility(View.VISIBLE);
                relativeTime.setVisibility(View.GONE);

                TextView title = (TextView) convertView.findViewById(R.id.lab_title);
                TextView time = (TextView) convertView.findViewById(R.id.lab_time);
                TextView details = (TextView) convertView.findViewById(R.id.lab_details);
                ImageView unread = (ImageView) convertView.findViewById(R.id.img_unread);
                MsgData data = mDatas.get(position);
                title.setText(data.getTitle());
                time.setText(data.getDateString());
                details.setText(data.getDetails());
                if (data.getStatus() == MsgData.STATUS_READ) {
                    unread.setVisibility(View.GONE);
                } else {
                    unread.setVisibility(View.VISIBLE);
                }
            }
        }
        return convertView;
    }
}