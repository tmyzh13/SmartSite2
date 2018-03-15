package com.isoftstone.smartsite.model.video.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.model.video.bean.PhotoInfo;
import com.isoftstone.smartsite.model.video.imgaware.RotateImageViewAware;
import com.isoftstone.smartsite.model.video.utils.ThumbnailsUtil;
import com.isoftstone.smartsite.model.video.utils.UniversalImageLoadTool;


import java.util.List;

public class PhotoGridAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<PhotoInfo> list;
	private ViewHolder viewHolder;
	private int width;
	public PhotoGridAdapter(Context context, List<PhotoInfo> list){
		DisplayMetrics dm = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels/3  - context.getResources().getDimensionPixelOffset(R.dimen.grid_view_item_spacing);
		mInflater = LayoutInflater.from(context);
		this.list = list;
	}
	@Override
	public int getCount() {
		return list.size();
	}
	@Override
	public Object getItem(int paramInt) {
		return list.get(paramInt);
	}
	@Override
	public long getItemId(int paramInt) {
		return paramInt;
	}
	@Override
	public View getView(int paramInt, View convertView, ViewGroup paramViewGroup) {
		PhotoInfo photoInfo = list.get(paramInt);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.photogrid_item, null);
			ImageView imageView=(ImageView)convertView.findViewById(R.id.iv_thumbnail);
			viewHolder.image = imageView;
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		LayoutParams layoutParams = viewHolder.image.getLayoutParams();
		layoutParams.width = width;
		layoutParams.height = width;
		viewHolder.image.setLayoutParams(layoutParams);
		if(photoInfo != null){
			UniversalImageLoadTool.disPlay(ThumbnailsUtil.MapgetHashValue(photoInfo.getImage_id(),photoInfo.getPath_file()),
					new RotateImageViewAware(viewHolder.image,photoInfo.getPath_absolute()), R.drawable.ic_capture);
		}
		return convertView;
	}
	public class ViewHolder{
		public ImageView image;
	}
}
