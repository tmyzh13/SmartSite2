package com.isoftstone.smartsite.model.dirtcar.View

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Created by yanyongjun on 2017/11/19.
 * 正方形的imageView
 */
open class SquareImageView : ImageView {
    constructor(mContext: Context) : super(mContext) {
        val context = mContext
    }

    constructor(mContext: Context, mAttributeSet: AttributeSet) : super(mContext, mAttributeSet) {
        val context = mContext
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}