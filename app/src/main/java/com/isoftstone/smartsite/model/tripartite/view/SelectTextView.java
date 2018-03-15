package com.isoftstone.smartsite.model.tripartite.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by yanyongjun on 2017/10/31.
 */

public class SelectTextView extends TextView {
    public SelectTextView(Context context) {
        super(context);
    }

    public SelectTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private OnSelectedListener mListener = null;

    public void setOnSelectedLisener(OnSelectedListener lisener) {
        mListener = lisener;
    }

    public void select() {
        if (mListener != null) {
            mListener.onSelectedLisnter();
        }
    }


    public interface OnSelectedListener {
        void onSelectedLisnter();
    }
}
