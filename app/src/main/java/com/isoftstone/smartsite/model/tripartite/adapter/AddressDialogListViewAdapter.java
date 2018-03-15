package com.isoftstone.smartsite.model.tripartite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.isoftstone.smartsite.R;

import java.util.ArrayList;

/**
 * Created by yanyongjun on 2017/11/29.
 */

public class AddressDialogListViewAdapter extends BaseAdapter {
    private Context mContext = null;
    private ArrayList<String> mList = new ArrayList<>();

    public AddressDialogListViewAdapter(Context context, ArrayList<String> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
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
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.listview_dialog_item, null);
        }
        if (view != null) {
            TextView item = (TextView) view.findViewById(R.id.lab_listview_add_report_dialog);
            item.setText(mList.get(position));
        }
        return view;
    }
}
