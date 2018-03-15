package com.isoftstone.smartsite.model.dirtcar.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.model.dirtcar.bean.ManualPhotographyBean;
import com.isoftstone.smartsite.model.dirtcar.imagecache.ImageLoader;
import com.isoftstone.smartsite.utils.ImageUtils;

import java.util.ArrayList;

public class CameraDetailsAdapter extends BaseAdapter {
	private static final String TAG = "CameraDetailsAdapter";

	ArrayList<String> mListDate = new ArrayList<String>();

	private ImageLoader mImageLoader;
	private Context mContext;
	private boolean mBusy = false;
	private Activity mActivity;

	public CameraDetailsAdapter(Context context, ArrayList<String> data, Activity activity) {
		this.mContext = context;
		mListDate = data;
		mImageLoader = new ImageLoader(context);
		mActivity = activity;
	}

	public ImageLoader getImageLoader( ) {
		return mImageLoader;
	}

	public void setFlagBusy(boolean busy) {
		mBusy = busy;
	}

	@Override
	public int getCount() {
		return mListDate.size();
	}

	@Override
	public Object getItem(int position) {
		return mListDate.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ManualPhotographyAdapter.ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.listview_camera_details_item, null);
			viewHolder = new ManualPhotographyAdapter.ViewHolder();
			viewHolder.mGradView = (GridView) convertView.findViewById(R.id.gridview_photo_layout);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ManualPhotographyAdapter.ViewHolder) convertView.getTag();
		}

		final String str  = mListDate.get(position);

		ArrayList<String> photoList = new ArrayList<String>();
		//photoList.addAll(photoGrid.getList());
		String [] photoUrls = str.split(",");
		for (int i=0; i < photoUrls.length; i++ ) {
			photoList.add(photoUrls[i]);
		}
		PhotoGridAdapter photoAdapter = new PhotoGridAdapter(mActivity, photoList, mImageLoader);
		viewHolder.mGradView.setAdapter(photoAdapter);

		return convertView;
	}

	static class ViewHolder {
		GridView mGradView;
	}

}
