package com.isoftstone.smartsite.model.inspectplan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.http.patrolplan.PatrolPlanBean;
import com.isoftstone.smartsite.http.user.BaseUserBean;
import com.isoftstone.smartsite.model.inspectplan.activity.PatrolPlanActivity;
import com.isoftstone.smartsite.model.inspectplan.bean.InspectPlanBean;
import com.isoftstone.smartsite.utils.DateUtils;

import java.util.ArrayList;


public class ApprovalPendingInspectPlansAdapter extends BaseAdapter{

	private static final String TAG = "LoaderAdapter";
	private Context mContext;
	private ArrayList<InspectPlanBean> mData = null;
	private ApprovalPendingInspectPlansAdapter.AdapterViewOnClickListener mListener;


	public ApprovalPendingInspectPlansAdapter(Context context, ArrayList<InspectPlanBean> data) {
		this.mContext = context;
		mData = data;
		mListener = (ApprovalPendingInspectPlansAdapter.AdapterViewOnClickListener)context;
	}

	public interface AdapterViewOnClickListener {
		public void viewOnClickListener(InspectPlanBean inspectPlanBean);
	}

	@Override
	public int getCount() {
		return mData.size();
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

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.listview_approval_pending_inspect_plans_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mLabTitleView = (TextView)convertView.findViewById(R.id.lab_title);
			viewHolder.mLabStatusView = (ImageView)convertView.findViewById(R.id.lab_status);
			viewHolder.mLabTimeView = (TextView)convertView.findViewById(R.id.lab_time);
			viewHolder.mLabNameView = (TextView)convertView.findViewById(R.id.lab_name);
			viewHolder.mLabcompanView = (TextView)convertView.findViewById(R.id.lab_company);
			viewHolder.mPlanApprovalView = (LinearLayout) convertView.findViewById(R.id.plan_approval_layout);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final  InspectPlanBean inspectPlanBean = mData.get(position);

		viewHolder.mLabTitleView.setText(inspectPlanBean.getTaskName());
		viewHolder.mLabStatusView.setImageDrawable(getStatusDictionariesToDrawable(inspectPlanBean.getTaskStatus()));
		viewHolder.mLabTimeView.setText(DateUtils.formatDate(inspectPlanBean.getTaskCreateTime()));
		viewHolder.mLabNameView.setText(inspectPlanBean.getUserName());
		viewHolder.mLabcompanView.setText(inspectPlanBean.getUserCompany());

		viewHolder.mPlanApprovalView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mListener.viewOnClickListener(inspectPlanBean);
			}
		});

		return convertView;
	}

	static class ViewHolder {
		TextView mLabTitleView;
		ImageView mLabStatusView;
		TextView mLabTimeView;
		TextView mLabNameView;
		TextView mLabcompanView;
		LinearLayout mPlanApprovalView;
	}

	private Drawable getStatusDictionariesToDrawable(int status) {
		Drawable statusDrawable = null;
		if (status == 1) {
			//statusDrawable  = mContext.getResources().getDrawable(R.drawable.inspect_plan_approval_pending,null);
		} else if (status == 2) {
			statusDrawable  = mContext.getResources().getDrawable(R.drawable.inspect_plan_approval_pending,null);
		} else if (status == 3) {
			statusDrawable  = mContext.getResources().getDrawable(R.drawable.inspect_plan_passed,null);
		} else if (status == 4) {
			statusDrawable  = mContext.getResources().getDrawable(R.drawable.inspect_plan_failed,null);
		}
		return statusDrawable;
	}
}
