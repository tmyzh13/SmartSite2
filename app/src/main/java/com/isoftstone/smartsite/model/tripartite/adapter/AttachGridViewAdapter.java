package com.isoftstone.smartsite.model.tripartite.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.model.tripartite.activity.TripartiteActivity;
import com.isoftstone.smartsite.utils.FilesUtils;
import com.isoftstone.smartsite.utils.ImageUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by yanyongjun on 2017/11/6.
 * 附件界面的Adapter
 */

public class AttachGridViewAdapter extends BaseAdapter {
    private ArrayList<Object> mDatas = null;
    private Context mContext = null;
    private Resources mRes = null;
    private final static String TAG = "AttachGridViewAdapter";
    //private boolean mIsPath = false;
    private ArrayList<String> mAllPath = new ArrayList<>();
    private boolean mIsShowDelete = false;
    //private ArrayList<String> mPathList = null;

    public AttachGridViewAdapter(Context context, ArrayList<Object> datas) {
        mDatas = datas;
        mContext = context;
        mRes = mContext.getResources();
    }

//    public AttachGridViewAdapter(Context context, ArrayList<Object> datas, ArrayList<String> pathList) {
//        mDatas = datas;
//        mContext = context;
//        mRes = mContext.getResources();
//        mPathList = pathList;
//    }

    public void setmIsShowDelete(boolean showDelete) {
        mIsShowDelete = showDelete;
    }

    @Override
    public int getCount() {
        return mDatas.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.add_attach_grid_item, null);
        }
        Object data = mDatas.get(position);
        ImageView imgView = (ImageView) convertView.findViewById(R.id.image);
        Log.e(TAG, "yanlog GridView:data:" + data);
        if (data instanceof Integer) {
            ImageUtils.loadImageWithPlaceHolder(mContext, imgView, null, (Integer) data);
            //imgView.setImageDrawable(mRes.getDrawable((Integer) data, null));
        } else if (data instanceof Uri) {
            Uri uri = (Uri) data;
            ImageUtils.loadImageWithPlaceHolder(mContext, imgView, uri.toString());
        } else {
            String str = (String) data;
            if (isPic(str) && new File(str).exists()) {
                ImageUtils.loadImageViewDiskCache(mContext, str, imgView);
            } else {
                ImageUtils.loadImageWithPlaceHolder(mContext, imgView, null, getShowPicId(str));
            }
        }

        View imgDelete = convertView.findViewById(R.id.img_delete);
        if (!mIsShowDelete || position == mDatas.size() - 1) {
            imgDelete.setVisibility(View.GONE);
        } else {
            imgDelete.setVisibility(View.VISIBLE);
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatas.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
        return convertView;
    }

    public void setAllPath(ArrayList<String> path) {
        mAllPath = path;
    }

    public ArrayList<String> getAllPath() {
        return mAllPath;
    }

    public ArrayList<Object> getAllData() {
        return mDatas;
    }

    private boolean isPic(String path) {
        String formatPath = FilesUtils.getFormatString(path);
        return TripartiteActivity.mImageList.contains(formatPath);
    }

    private int getShowPicId(String path) {
        String formatPath = FilesUtils.getFormatString(path);
        if (TripartiteActivity.mImageList.contains(formatPath)) {
            return TripartiteActivity.mAttach.get(".image");
        } else if (TripartiteActivity.mXlsList.contains(formatPath)) {
            return TripartiteActivity.mAttach.get(".xls");
        } else if (TripartiteActivity.mDocList.contains(formatPath)) {
            return TripartiteActivity.mAttach.get(".image");
        } else if (TripartiteActivity.mPdfList.contains(formatPath)) {
            return TripartiteActivity.mAttach.get(".pdf");
        } else if (TripartiteActivity.mPptList.contains(formatPath)) {
            return TripartiteActivity.mAttach.get(".ppt");
        } else if (TripartiteActivity.mVideoList.contains(formatPath)) {
            return TripartiteActivity.mAttach.get(".video");
        } else {
            return TripartiteActivity.mAttach.get(".doc");
        }
    }

}
