package com.isoftstone.smartsite.model.map.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.utils.DensityUtils;

/**
 * Created by zw on 2017/10/17.
 */

public class ChooseCameraAdapter extends BaseAdapter {

    private String[] titles = new String[]{"实时视频","历史监控","视频抓拍"};
    private Context mContext;


    public ChooseCameraAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return titles.length;
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
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_choose_camera_item,parent,false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tv);
        tv.setText(titles[position]);
        return tv;
    }
}
