package com.isoftstone.smartsite.model.dirtcar.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.model.dirtcar.bean.ManualPhotographyBean;
import com.isoftstone.smartsite.model.dirtcar.imagecache.ImageLoader;
import com.isoftstone.smartsite.utils.ImageUtils;
import com.isoftstone.smartsite.utils.ToastUtils;
import com.isoftstone.smartsite.utils.Utils;

import java.util.ArrayList;


public class ManualPhotographyAdapter extends BaseAdapter{

	private static final String TAG = "PhotographyAdapter";
    ArrayList<ManualPhotographyBean> mListDate = new ArrayList<ManualPhotographyBean>();


	private ImageLoader mImageLoader;
	//private int mCount;
	private Context mContext;
	private boolean mBusy = false;
	private Activity mActivity;
	//private String[] urlArrays;


	/**public ManualPhotographyAdapter(int count, Context context, String []url) {
		this.mCount = count;
		this.mContext = context;
		urlArrays = url;
		mImageLoader = new ImageLoader(context);
	}*/

	public ManualPhotographyAdapter(Context context, ArrayList<ManualPhotographyBean> data, Activity activity) {
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

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.listview_manual_photography_item, null);
			viewHolder = new ViewHolder();
			//viewHolder.mTextView = (TextView) convertView.findViewById(R.id.tv_tips);
			//viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.iv_image);
			viewHolder.mTakePhotoUserHeadView = (ImageView) convertView.findViewById(R.id.user_head_img);
			viewHolder.mTakePhotoUserNameView = (TextView) convertView.findViewById(R.id.lab_user_name);
			viewHolder.mTakePhotoDateView = (TextView) convertView.findViewById(R.id.lab_task_date);
			viewHolder.mTakePhotoUserCompanyView = (TextView) convertView.findViewById(R.id.lab_company);
			viewHolder.mGradView = (GridView) convertView.findViewById(R.id.middle_gridview_layout);
			viewHolder.mTakePhotoAddrView = (TextView) convertView.findViewById(R.id.lab_addr);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		/**String url = "";
		url = urlArrays[position % urlArrays.length];
		
		viewHolder.mImageView.setImageResource(R.drawable.ic_launcher);
		

		if (!mBusy) {
			mImageLoader.DisplayImage(url, viewHolder.mImageView, false);
			viewHolder.mTextView.setText("--" + position
					+ "--IDLE ||TOUCH_SCROLL");
		} else {
			mImageLoader.DisplayImage(url, viewHolder.mImageView, false);
			viewHolder.mTextView.setText("--" + position + "--FLING");
		}*/

		final ManualPhotographyBean manualPhotographyBean = mListDate.get(position);

		ArrayList<String> photoList = new ArrayList<String>();
		//photoList.addAll(photoGrid.getList());
		String [] photoUrls = manualPhotographyBean.getPhotoSrc().split(",");
		for (int i=0; i < photoUrls.length; i++ ) {
			photoList.add(photoUrls[i]);
		}

		PhotoGridAdapter photoAdapter = new PhotoGridAdapter(mActivity, photoList, mImageLoader);
		viewHolder.mGradView.setAdapter(photoAdapter);


		viewHolder.mTakePhotoUserNameView.setText(manualPhotographyBean.getTakePhotoUserName());
		viewHolder.mTakePhotoDateView.setText(manualPhotographyBean.getTakePhotoTime());
		viewHolder.mTakePhotoUserCompanyView.setText(manualPhotographyBean.getTakePhotoUserCompany());
		viewHolder.mTakePhotoAddrView.setText(manualPhotographyBean.getAddr());


		viewHolder.mGradView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				//ToastUtils.showShort("position = " + position);
			}
		});

		boolean isHaveLoaded = false;
		try {
			isHaveLoaded = (null != viewHolder.mTakePhotoUserHeadView.getDrawable().getCurrent().getConstantState());
		} catch (Exception e) {
			Log.i(TAG,"throw a exception : " +  e.getMessage());
			isHaveLoaded = false;
		} finally {
			if (!isHaveLoaded) {
				if (Utils.isEmptyStr(manualPhotographyBean.getTakePhotoUserHeadPath())) {
					viewHolder.mTakePhotoUserHeadView.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.default_head, null));
				} else {
					ImageUtils.loadImageWithPlaceHolder(mContext, viewHolder.mTakePhotoUserHeadView, manualPhotographyBean.getTakePhotoUserHeadPath(), R.drawable.default_head);
				}
			}
		}

		return convertView;
	}

	static class ViewHolder {
		//TextView mTextView;
		//ImageView mImageView;
		ImageView mTakePhotoUserHeadView;
		TextView mTakePhotoUserNameView;
		TextView mTakePhotoDateView;
		TextView mTakePhotoUserCompanyView;
		GridView mGradView;
		TextView mTakePhotoAddrView;
	}

}
