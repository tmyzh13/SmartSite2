package com.isoftstone.smartsite.model.dirtcar.Data

/**
 * Created by yanyongjun on 2017/11/19.
 */
open class SelectImage(path: String, stats: Boolean, listener: OnStatusChangeListener) {
    var mPath = path
    var mListener = listener
    var mStatus = stats
        set(temp) {
            field = temp
            mListener?.onChange(field,mPath)
        }


    interface OnStatusChangeListener {
        fun onChange(status: Boolean,path:String)
    }
}