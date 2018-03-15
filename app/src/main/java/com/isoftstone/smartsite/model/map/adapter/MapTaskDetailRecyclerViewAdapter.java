package com.isoftstone.smartsite.model.map.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.user.BaseUserBean;
import com.isoftstone.smartsite.utils.ImageUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zw on 2017/11/19.
 */

public class MapTaskDetailRecyclerViewAdapter extends RecyclerView.Adapter<MapTaskDetailRecyclerViewAdapter.MapTaskDetailViewHolder> implements View.OnClickListener {

    private Context mContext;
    private onMapTaskItemClickListener mItemClickListener;
    private int currentPosition = 0;
    private List<BaseUserBean> userBeans;

    public MapTaskDetailRecyclerViewAdapter(Context context,List<BaseUserBean> beans){
        this.mContext = context;
        this.userBeans = beans;
    }

    public void setDatas(List<BaseUserBean> datas,int position){
        this.currentPosition = position;
        this.userBeans = datas;
        notifyDataSetChanged();
    }

    public void setItemClickListener(onMapTaskItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public void updateViews(int position){
        currentPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public MapTaskDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_map_rv_task_detail_item,parent,false);
        convertView.setOnClickListener(this);
        MapTaskDetailViewHolder viewHolder = new MapTaskDetailViewHolder(convertView);
        viewHolder.civ = (CircleImageView) convertView.findViewById(R.id.civ);
        viewHolder.tv = (TextView) convertView.findViewById(R.id.tv);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MapTaskDetailViewHolder holder, int position) {

        if(position == currentPosition){
            holder.civ.setBorderColor(Color.parseColor("#4f6de6"));
        } else{
            holder.civ.setBorderColor(Color.TRANSPARENT);
        }
        if(userBeans.get(position).imageData != null){
            String url = HttpPost.URL + "/" + userBeans.get(position).imageData;
            ImageUtils.loadImageWithPlaceHolder(mContext,holder.civ,url);
        } else {
            holder.civ.setImageResource(R.drawable.default_head);
        }
        holder.tv.setText(userBeans.get(position).name);
    }

    @Override
    public int getItemCount() {
        return userBeans == null ? 0 : userBeans.size();
    }

    @Override
    public void onClick(View v) {
            mItemClickListener.onItemClick(v);
    }

    public class MapTaskDetailViewHolder extends RecyclerView.ViewHolder{

        CircleImageView civ;
        TextView tv;

        public MapTaskDetailViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface onMapTaskItemClickListener{

        public void onItemClick(View view);
    }
}
