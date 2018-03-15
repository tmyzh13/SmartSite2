package com.isoftstone.smartsite.model.video.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.http.video.VideoMonitorBean;
import com.isoftstone.smartsite.model.main.listener.OnConvertViewClickListener;
import com.isoftstone.smartsite.model.video.VideoRePlayActivity;

import java.util.ArrayList;

/**
 * Created by zhangyinfu on 2017/10/22
 */

public class VideoRePlayAdapter extends BaseAdapter {


    private LayoutInflater mInflater;
    private ArrayList<VideoMonitorBean> mData = new ArrayList<VideoMonitorBean>();
    private final String IMAGE_TYPE = "image/*";
    private Context mContext = null;
    private AdapterViewOnClickListener listener;

    public VideoRePlayAdapter(Context context){
        this.mInflater = LayoutInflater.from(context);
        mContext = context;
        listener = (AdapterViewOnClickListener)context;
    }

    public interface AdapterViewOnClickListener {
        public void viewOnClickListener(ViewHolder viewHolder);
    }

    public void setData(ArrayList<VideoMonitorBean> list){
        mData = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        // 每列两项
        if (mData.size() % 2 == 0) {
            return mData.size() / 2;
        }

        return mData.size() / 2 + 1;
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.videoreplay_adapter, null);
            holder.mediaPreviewIv1 = (ImageView)convertView.findViewById(R.id.media_preview_iv_1);
            holder.mediaPreviewTv1 = (TextView)convertView.findViewById(R.id.media_preview_tv_1);
            holder.mediaPreviewIv2 = (ImageView) convertView.findViewById(R.id.media_preview_iv_2);
            holder.mediaPreviewTv2 = (TextView)convertView.findViewById(R.id.media_preview_tv_2);
            holder.mediaPreviewLayout1 = (LinearLayout)convertView.findViewById(R.id.media_preview_layout_1);
            holder.mediaPreviewLayout2 = (LinearLayout)convertView.findViewById(R.id.media_preview_layout_2);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        int position1 = position * 2;
        int position2 = position * 2 + 1;

        VideoMonitorBean videoMonitorBean =  mData.get(position1);
        holder.mediaPreviewTv1.setText(videoMonitorBean.getFlieName());

        if(position2 < mData.size()) {
            VideoMonitorBean sVideoMonitorBean =  mData.get(position2);
            holder.mediaPreviewTv2.setText(sVideoMonitorBean.getFlieName());
        } else {
            holder.mediaPreviewLayout2.setVisibility(View.INVISIBLE);
        }

        holder.beginTime = mData.get(position).getBeginData();
        holder.endTime = mData.get(position).getEndData();
        holder.resCode = mData.get(position).getResCode();
        holder.resSubType = mData.get(position).getResSubType();
        holder.isOnLine = mData.get(position).isOnline();
        holder.resName = mData.get(position).getResName();
        holder.mediaPreviewIv1.setOnClickListener(new OnConvertViewClickListener(convertView, position) {

            @Override
            public void onClickCallBack(View registedView, View rootView, int position) {
                ViewHolder viewHolder = (ViewHolder)rootView.getTag();
                if(null != viewHolder) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("resCode", viewHolder.resCode);
                    bundle.putString("beginTime", viewHolder.beginTime);
                    bundle.putString("endTime", viewHolder.endTime);
                    bundle.putString("fileName", viewHolder.mediaPreviewTv1.getText().toString());
                    bundle.putString("resSubType",viewHolder.resSubType + "");
                    bundle.putString("resName", viewHolder.resName);
                    bundle.putBoolean("isOnline", viewHolder.isOnLine);
                    bundle.putInt("position", position);
                    intent.putExtras(bundle);
                    intent.setClass(mContext, VideoRePlayActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, "errorException:  ViewHolder is null", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.mediaPreviewIv2.setOnClickListener(new OnConvertViewClickListener(convertView, position) {
            @Override
            public void onClickCallBack(View registedView, View rootView, int position) {
                ViewHolder viewHolder = (ViewHolder)rootView.getTag();

                if(null != viewHolder) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("resCode", viewHolder.resCode);
                    bundle.putString("beginTime", viewHolder.beginTime + " 00:00:00");
                    bundle.putString("endTime", viewHolder.endTime + " 23:59:59");
                    bundle.putString("fileName", viewHolder.mediaPreviewTv2.getText().toString());
                    bundle.putInt("position", position);
                    intent.putExtras(bundle);
                    intent.setClass(mContext, VideoRePlayActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, "errorException:  ViewHolder is null", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }


    public final class ViewHolder{
        public LinearLayout mediaPreviewLayout1;
        public LinearLayout mediaPreviewLayout2;
        public ImageView mediaPreviewIv1;
        public TextView mediaPreviewTv1;
        public ImageView mediaPreviewIv2;
        public TextView mediaPreviewTv2;
        public String resCode;
        public String beginTime;
        public String endTime;
        public String resName;
        public boolean isOnLine;
        public int resSubType;

        public String getResName() {
            return resName;
        }

        public void setResName(String resName) {
            this.resName = resName;
        }

        public boolean isOnLine() {
            return isOnLine;
        }

        public void setOnLine(boolean onLine) {
            isOnLine = onLine;
        }

        public int getResSubType() {
            return resSubType;
        }

        public void setResSubType(int resSubType) {
            this.resSubType = resSubType;
        }

        public String getResCode() {
            return resCode;
        }

        public void setResCode(String resCode) {
            this.resCode = resCode;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "resCode='" + resCode + '\'' +
                    ", beginTime='" + beginTime + '\'' +
                    ", endTime='" + endTime + '\'' +
                    '}';
        }
    }


}
