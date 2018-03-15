package com.isoftstone.smartsite.utils;

import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw on 2017/11/8.
 */

public class MapUtils {

    private MapUtils(){}

    public static List<LatLng> getAroundLatlons(){
        List<LatLng> latLngs = new ArrayList<LatLng>();
        latLngs.add(new LatLng(30.490247,114.479932));
        latLngs.add(new LatLng(30.491468,114.483494));
        latLngs.add(new LatLng(30.492355,114.486069));
        latLngs.add(new LatLng(30.492899,114.488186));
        latLngs.add(new LatLng(30.493095,114.492721));
        latLngs.add(new LatLng(30.492825,114.502935));
        latLngs.add(new LatLng(30.492799,114.507999));
        latLngs.add(new LatLng(30.493934,114.51332));
        latLngs.add(new LatLng(30.495757,114.518427));
        latLngs.add(new LatLng(30.500158,114.5298));
        latLngs.add(new LatLng(30.490173,114.533834));
        latLngs.add(new LatLng(30.456315,114.53407));
        latLngs.add(new LatLng(30.447751,114.504029));
        latLngs.add(new LatLng(30.447825,114.481241));
        latLngs.add(new LatLng(30.484408,114.481542));
        latLngs.add(new LatLng(30.490247,114.479932));

        return latLngs;
    }

}
