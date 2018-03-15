package com.isoftstone.smartsite.model.map.ui;

import android.os.Bundle;


import com.amap.api.maps.model.MyLocationStyle;
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseFragment;
import com.isoftstone.smartsite.utils.LogUtils;
import com.isoftstone.smartsite.utils.ToastUtils;
import com.orhanobut.logger.Logger;

/**
 * Created by zw on 2017/10/11.
 *
 *
 * 开发版SHA1 ： 17:02:19:67:57:D4:F4:AF:3E:AE:22:1F:95:65:9A:27:FD:F7:8D:D0
 *
 *             17:02:19:67:57:D4:F4:AF:3E:AE:22:1F:95:65:9A:27:FD:F7:8D:D0;com.isoftstone.smartsite
 *
 *
 * 发布版SHA1 ： C3:83:83:56:68:FD:2B:BC:EE:BB:16:AF:BA:52:EC:6A:C9:24:19:D5
 *
 *          isoftstone:90:E0:C0:33:34:8B:6A:7D:6B:3C:68:3D:38:68:CB:0C:DE:06:80:A1
 *
 *              C3:83:83:56:68:FD:2B:BC:EE:BB:16:AF:BA:52:EC:6A:C9:24:19:D5;com.isoftstone.smartsite
 */

public class MapFragment extends BaseFragment{

    private static final String TAG_MAP_MAIN_FRAGMENT = "MAP_MAIN_FRAGMENT";
    private MapMainFragment mapMainFragment;
    private MapTestFragment mapTestFragment;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_map;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {

        mapMainFragment = new MapMainFragment();
        mapTestFragment = new MapTestFragment();


        getChildFragmentManager().beginTransaction().add(R.id.fl_content, mapMainFragment,TAG_MAP_MAIN_FRAGMENT)
                .commit();

    }


}
