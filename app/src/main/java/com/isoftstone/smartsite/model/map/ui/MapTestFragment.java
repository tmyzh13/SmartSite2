package com.isoftstone.smartsite.model.map.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;


//import com.amap.api.maps2d.AMap;
//import com.amap.api.maps2d.CameraUpdateFactory;
//import com.amap.api.maps2d.MapView;
//import com.amap.api.maps2d.SupportMapFragment;
//import com.amap.api.maps2d.model.MyLocationStyle;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseFragment;


/**
 * Created by zw on 2017/10/15.
 */

public class MapTestFragment extends BaseFragment {




    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_map_test;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        initView(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);





    }

    private void initView(Bundle savedInstanceState) {


    }


}
