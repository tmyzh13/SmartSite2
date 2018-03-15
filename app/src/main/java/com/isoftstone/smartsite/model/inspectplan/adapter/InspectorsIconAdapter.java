package com.isoftstone.smartsite.model.inspectplan.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.model.inspectplan.data.InspectorData;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-11-24.
 */

public class InspectorsIconAdapter extends BaseAdapter {
    private ArrayList<InspectorData> list = null;
    private Context mContext;

    public InspectorsIconAdapter(){super();}

    public InspectorsIconAdapter(Context mContext, ArrayList<InspectorData> list){
        this.mContext = mContext;
        this.list = list;
        Log.i("ContactAdapter","list length is:" + list.size());
    }

    @Override
    public InspectorData getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InspectorsIconAdapter.ViewHolder holder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.inspector_icon_item, parent, false);
            holder = new InspectorsIconAdapter.ViewHolder();
            holder.imageView_Icon = (ImageView) convertView.findViewById(R.id.imageView_icon);
            convertView.setTag(holder);
        }else {
            holder = (InspectorsIconAdapter.ViewHolder)convertView.getTag();
        }
        InspectorData contactDate = getItem(position);
        if (contactDate.getIsSelected()){
            holder.imageView_Icon.setVisibility(View.VISIBLE);
        } else {
            holder.imageView_Icon.setVisibility(View.GONE);
        }


        return convertView;
    }
//
//    @Nullable
//    @Override
//    public CharSequence[] getAutofillOptions() {
//        return new CharSequence[0];
//    }

    @Override
    public int getCount() {
        return list.size();
    }

    public class ViewHolder {
        public ImageView imageView_Icon;
    }
}
