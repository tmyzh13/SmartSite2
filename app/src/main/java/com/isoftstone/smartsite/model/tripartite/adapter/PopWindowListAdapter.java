package com.isoftstone.smartsite.model.tripartite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.isoftstone.smartsite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanyongjun on 2017/10/28.
 */

public class PopWindowListAdapter extends BaseAdapter {
    private List<String> mDatas = new ArrayList<>();
    private Context mContext = null;

    public PopWindowListAdapter(Context context, List<String> data) {
        mDatas = data;
        mContext = context;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.add_report_spinner_item, null);
        }
        ((TextView) convertView).setText(mDatas.get(position));
        return convertView;
    }
}
