package com.isoftstone.smartsite.model.inspectplan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.http.patroltask.PatrolTaskBean;

import java.util.ArrayList;


public class PatrolPlanAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<PatrolTaskBean> mList;

	public PatrolPlanAdapter(Context context){
		this.mContext = context;
	}

	public void setList(ArrayList<PatrolTaskBean> list){
		mList = list;

	}
	@Override
	public int getCount() {
		if(mList == null){
             return  0;
		}
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		if(mList != null && mList.size() > position){
			return mList.get(position);
		}
        return  null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PatrolPlanAdapter.ViewHolder holder;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_patrolplan_adapter,parent,false);
			holder = new PatrolPlanAdapter.ViewHolder(convertView);
		} else {
			holder = (PatrolPlanAdapter.ViewHolder) convertView.getTag();
		}
        holder.title.setText(mList.get(position).getTaskName());
		holder.time.setText(mList.get(position).getTaskTimeStart());
		holder.address.setText(mList.get(position).getTaskTimeEnd());
		switch (mList.get(position).getPlanStatus()){
			case  3:
				holder.zhuangtai.setImageResource(R.drawable.green);
				break;
			case 4:
				holder.zhuangtai.setImageResource(R.drawable.red);
				break;
			case  2:
				holder.zhuangtai.setImageResource(R.drawable.blue);
				break;
		}
		return convertView;
	}

	private class ViewHolder{
		private TextView title;
		private TextView time;
		private TextView address;
		private ImageView zhuangtai;

		public ViewHolder(View convertView){
			this.title = (TextView) convertView.findViewById(R.id.title);
			this.time = (TextView) convertView.findViewById(R.id.time);
			this.address = (TextView) convertView.findViewById(R.id.address);
			this.zhuangtai = (ImageView) convertView.findViewById(R.id.zhuangtai);
			convertView.setTag(this);
		}
	}

}
