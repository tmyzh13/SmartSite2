package com.isoftstone.smartsite.model.main.listener;



import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

/**
 * Created by zyf on 2017/10/20.
 */

public abstract class OnConvertViewClickListener implements View.OnClickListener{

    private View convertView;
    private int positionId;

    public OnConvertViewClickListener(View convertView, int position) {
        this.convertView = convertView;
        this.positionId = position;

    }

    @TargetApi(Build.VERSION_CODES.DONUT)
    @Override
    public void onClick(View v) {
        onClickCallBack(v, convertView, positionId);

    }


    public abstract void onClickCallBack(View registedView, View rootView, int position);

}