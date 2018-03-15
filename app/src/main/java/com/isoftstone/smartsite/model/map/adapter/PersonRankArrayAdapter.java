package com.isoftstone.smartsite.model.map.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by zw on 2017/11/28.
 */

public class PersonRankArrayAdapter<String> extends ArrayAdapter<String> {

    private List<String> datas;

    public PersonRankArrayAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.datas = objects;
    }

    public void setDatas(List<String> data){
        this.datas =data;
    }


}
