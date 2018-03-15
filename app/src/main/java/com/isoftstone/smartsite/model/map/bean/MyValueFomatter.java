package com.isoftstone.smartsite.model.map.bean;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by zw on 2017/11/26.
 */

public class MyValueFomatter implements IValueFormatter{


    @Override
    public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
        int value = (int) v;
        return String.valueOf(value);
    }
}
