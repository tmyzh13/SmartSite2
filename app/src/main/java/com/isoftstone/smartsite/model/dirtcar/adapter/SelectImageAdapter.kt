package com.isoftstone.smartsite.model.dirtcar.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import com.isoftstone.smartsite.R
import com.isoftstone.smartsite.model.dirtcar.Data.SelectImage
import com.isoftstone.smartsite.model.dirtcar.activity.SelectImageActivity
import com.isoftstone.smartsite.utils.ImageUtils

/**
 * Created by yanyongjun on 2017/11/19.
 */

open class SelectImageAdapter(context: Context, path: ArrayList<SelectImage>) : BaseAdapter() {
    var mContext = context
    var mPathList = path
    val TAG = "SelectImageAdpater"

    var mActivity: SelectImageActivity = context as SelectImageActivity

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var v = if (convertView != null) convertView else {
            LayoutInflater.from(mContext).inflate(R.layout.grid_item_show_all_photo, null)
        }
        var imageView = v.findViewById(R.id.image) as ImageView
        var imgMask = v.findViewById(R.id.img_mask) as ImageView
        var checkBox = v.findViewById(R.id.check_box) as CheckBox
        var selectImg = mPathList.get(position)
        checkBox.setOnCheckedChangeListener(null)
        if (selectImg.mStatus) {
            imgMask.visibility = View.VISIBLE
            checkBox.isChecked = true
        } else {
            imgMask.visibility = View.INVISIBLE
            checkBox.isChecked = false
        }
        checkBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                Log.e(TAG, "yanlog onCheck:" + isChecked)
                if(isChecked) {
                    if (mActivity.getSelectNum() < 9) {
                        imgMask.visibility = if (isChecked) View.VISIBLE else View.INVISIBLE
                        selectImg.mStatus = isChecked
                    }else{
                        buttonView?.isChecked = false
                    }
                }else{
                    imgMask.visibility = if (isChecked) View.VISIBLE else View.INVISIBLE
                    selectImg.mStatus = isChecked
                }
            }
        })

        ImageUtils.loadImageViewDiskCacheResult(mContext, selectImg.mPath, imageView)
        return v
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return mPathList.size
    }
}