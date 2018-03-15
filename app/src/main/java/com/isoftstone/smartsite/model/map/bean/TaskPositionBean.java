package com.isoftstone.smartsite.model.map.bean;

import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;

/**
 * Created by zw on 2017/11/23.
 */

public class TaskPositionBean {

    private ImageView imageView;
    private TextView textView;
    private LatLng latLng;

    public TaskPositionBean(ImageView imageView, TextView textView, LatLng latLng) {
        this.imageView = imageView;
        this.textView = textView;
        this.latLng = latLng;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTextView() {
        return textView;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
