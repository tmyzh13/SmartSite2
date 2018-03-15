package com.isoftstone.smartsite.model.dirtcar.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.isoftstone.smartsite.R
import com.isoftstone.smartsite.utils.ImageUtils

/**
 * Created by yanyongjun on 2017/11/19.
 * 拍照上传界面的GridView的Adapter
 */

open class UpdatePhotoAdapter(context: Context, pathList: ArrayList<String>) : BaseAdapter() {
    val TAG = "UpdatePhotoAdapter"
    var mContext = context
    var mPathList = pathList

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var v = if (convertView != null) convertView else {
            LayoutInflater.from(mContext).inflate(R.layout.grid_view_update_photo_item, null)
        }
        var imageView = v.findViewById(R.id.image) as ImageView

        Log.e(TAG, "getView position:" + position + " size:" + mPathList?.size)
        if (position == mPathList?.size) {
            ImageUtils.loadFromRes(mContext as?Activity, R.drawable.tianjiazhaopian, imageView)
        } else {
            ImageUtils.loadImageViewCache(mContext, mPathList?.get(position), imageView)
        }
        return v
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return if (mPathList.size >= 9) mPathList.size else mPathList.size + 1
    }
}