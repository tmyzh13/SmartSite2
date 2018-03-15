package com.isoftstone.smartsite.model.main.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.http.aqi.EQIRankingBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by zw on 2017/11/4.
 */

public class AirMonitoringRankAdapter extends BaseAdapter {

    private Context mContext;
    private int backgroundColor;
    private ArrayList<EQIRankingBean.AQI> mList;
    int max;

    public AirMonitoringRankAdapter(Context context){
        this.mContext = context;
        backgroundColor = Color.parseColor("#FE5A5A");
    }

    public void setList(ArrayList<EQIRankingBean.AQI> list){
        mList = list;
        Collections.sort(list, new AirMonitoringRankAdapter.SortByValue());
        if(mList.size() > 0){
            max = doubleToInt(new Double(mList.get(0).getData()));
        }
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_rank_item,parent,false);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if(position <= 6){
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(backgroundColor);
            drawable.setCornerRadius(5);
            int alpha  = (int) (((float)(10 - position))/10 * 255);
            drawable.setAlpha(alpha);
            holder.tv_rank.setBackground(drawable);
            holder.tv_rank.setText(position+1 + "");

        }else {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(backgroundColor);
            drawable.setCornerRadius(5);
            int alpha  = (int) (0.3 * 255);
            drawable.setAlpha(alpha);
            holder.tv_rank.setBackground(drawable);
            holder.tv_rank.setText(position +1+ "");
        }
        holder.pb.setMax(max);
        holder.tv_address.setText(mList.get(position).getArchName());
        String data = mList.get(position).getData();
        int index = data.indexOf(".");
        if(data.length() > index+3){
            holder.tv_aqi.setText(data.substring(0,data.indexOf(".")+3));
        }else{
            holder.tv_aqi.setText(data);
        }

        holder.pb.setProgress(doubleToInt(new Double(mList.get(position).getData())));

        return convertView;
    }

    private class ViewHolder{
        private TextView tv_rank;
        private TextView tv_address;
        private TextView tv_aqi;
        private ProgressBar pb;

        public ViewHolder(View convertView){
            this.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            this.tv_aqi = (TextView) convertView.findViewById(R.id.tv_aqi);
            this.tv_rank = (TextView) convertView.findViewById(R.id.tv_rank);
            this.pb = (ProgressBar) convertView.findViewById(R.id.pb);
            convertView.setTag(this);
        }
    }

    public static class  SortByValue  implements Comparator {

        public int compare(Object o1, Object o2) {
            EQIRankingBean.AQI s1 = (EQIRankingBean.AQI) o1;
            EQIRankingBean.AQI s2 = (EQIRankingBean.AQI) o2;
            double double_1 = new Double(s1.getData())*100;
            double double_2 =  new Double(s2.getData())*100;
            return doubleToInt(double_2) - doubleToInt(double_1);
        }
    }

    public static  int doubleToInt(double d){
        String s1 = String.valueOf(d);
        String s2 = s1.substring(0, s1.indexOf("."));
        return  Integer.parseInt(s2);
    }
}
