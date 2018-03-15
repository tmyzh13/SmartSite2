package com.isoftstone.smartsite.model.map.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.http.patroltask.PatrolTaskBean;
import com.isoftstone.smartsite.http.patroluser.UserTrackBean;
import com.isoftstone.smartsite.model.map.ui.MapTaskDetailActivity;
import com.isoftstone.smartsite.model.tripartite.view.MyListView;
import com.isoftstone.smartsite.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw on 2017/11/19.
 */

public class ConstructionMonitorListAdapter extends BaseAdapter{

    private Context mContext;
    private List<UserTrackBean> userTrackBeans;
    private List<MapTaskAdapter> mapTaskAdapters;

    public ConstructionMonitorListAdapter(Context context,List<UserTrackBean> userTrackBeans){
        this.mContext = context;
        this.userTrackBeans = userTrackBeans;
        mapTaskAdapters = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return userTrackBeans == null ? 0 : userTrackBeans.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_construction_monitor_list_item,parent,false);
            MyListView myListView = (MyListView) convertView.findViewById(R.id.mlv);
            myListView.setDivider(new ColorDrawable(Color.parseColor("#eeeeee")));
            myListView.setDividerHeight(2);

            List<PatrolTaskBean> patrolTaskBeans = new ArrayList<>();
            patrolTaskBeans.add(userTrackBeans.get(position).getPatrolTask());
            MapTaskAdapter adapter = new MapTaskAdapter(mContext,patrolTaskBeans);
            mapTaskAdapters.add(adapter);
            myListView.setAdapter(adapter);
            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    UserTrackBean userTrackBean = userTrackBeans.get(position);
                    Intent intent = new Intent(mContext, MapTaskDetailActivity.class);
                    intent.putExtra("data",userTrackBean);
                    mContext.startActivity(intent);
                }
            });
        }
        TextView tv_person = (TextView) convertView.findViewById(R.id.tv_person);
        tv_person.setText(userTrackBeans.get(position).getUser().name);
        TextView tv_company = (TextView) convertView.findViewById(R.id.tv_company);
        tv_company.setText(userTrackBeans.get(position).getUser().address);

        return convertView;
    }

}
