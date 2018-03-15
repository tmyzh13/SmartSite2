package com.isoftstone.smartsite.model.map.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.isoftstone.smartsite.R;

/**
 * Created by zw on 2017/11/23.
 */

public class MapSearchPositionInfoWindowAdapter implements AMap.InfoWindowAdapter{

    private Context mContext;
    private View rootView;

    public MapSearchPositionInfoWindowAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        if(rootView == null){
            rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_map_search_position_info_winfow,null);
        }

        return rootView;
    }
}
