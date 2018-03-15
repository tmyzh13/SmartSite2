package com.isoftstone.smartsite.model.map.bean;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.isoftstone.smartsite.utils.LogUtils;

import java.util.List;

/**
 * Created by zw on 2017/11/25.
 */

public class MyXFormatter implements IAxisValueFormatter{

    private List<String> values;

    public MyXFormatter(List<String> values){
        this.values = values;
    }

    @Override
    public String getFormattedValue(float v, AxisBase axisBase) {
//        LogUtils.e("zw","v .. " + v + "  ... " + values.size());
        if(v < 0) v = 0;
        return values.get((int) (v % values.size()));
    }
}
