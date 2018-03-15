package com.isoftstone.smartsite.model.inspectplan.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.isoftstone.smartsite.R

/**
 * Created by yanyongjun on 2017/11/15.
 */
open class AddressAdapter(context: Context, list: ArrayList<String>, drawable: Drawable?, v: TextView?) : BaseAdapter() {
    var mContext = context
    var mList = list
    var drawableAdd = drawable
    var labLeft = v

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var v = if (convertView != null) convertView else {
            LayoutInflater.from(mContext).inflate(R.layout.listview_add_inspect_plan_address_item, null)
        }

        var lab_address = v.findViewById(R.id.lab_address) as TextView
        lab_address.setText(mList.get(position))

        var img_delete = v.findViewById(R.id.img_delete) as ImageView
        img_delete.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                mList.removeAt(position)
                if (mList.size == 0) {
                    labLeft?.setCompoundDrawables(drawableAdd, null, null, null)
                }
                this@AddressAdapter.notifyDataSetChanged()
            }
        })

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