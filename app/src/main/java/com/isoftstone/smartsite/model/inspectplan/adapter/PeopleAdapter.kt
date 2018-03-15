package com.isoftstone.smartsite.model.inspectplan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.isoftstone.smartsite.R
import com.isoftstone.smartsite.http.HttpPost
import com.isoftstone.smartsite.http.user.BaseUserBean
import com.isoftstone.smartsite.utils.ImageUtils

/**
 * Created by yanyongjun on 2017/11/15.
 */
open class PeopleAdapter(context: Context, list: ArrayList<BaseUserBean>) : BaseAdapter() {
    var mContext = context
    var mList = list
    var mHttpPost = HttpPost()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var v = if (convertView != null) convertView else {
            LayoutInflater.from(mContext).inflate(R.layout.listview_add_inspect_plan_people_item, null)
        }
        try {
            var bean = mList[position]
            var img_delete = v.findViewById(R.id.img_delete)
            img_delete.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    mList.removeAt(position)
                    this@PeopleAdapter.notifyDataSetChanged()
                }
            })

            var img_head = v.findViewById(R.id.img_head) as ImageView
            var headPath = bean.imageData
            var headuri = mHttpPost.getFileUrl(headPath)
            ImageUtils.loadImageWithPlaceHolder(mContext, img_head, headuri, R.drawable.default_head)
        } catch (e: Exception) {
            e.printStackTrace()
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
        return mList.size
    }
}