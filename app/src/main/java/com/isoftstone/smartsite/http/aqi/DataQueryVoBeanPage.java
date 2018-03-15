package com.isoftstone.smartsite.http.aqi;

import com.isoftstone.smartsite.http.pageable.PageBean;
import com.isoftstone.smartsite.http.pageable.PageableBean;

import java.util.ArrayList;

/**
 * Created by gone on 2017/12/7.
 */

public class DataQueryVoBeanPage extends PageBean {
    private ArrayList<DataQueryVoBean> content;

    public ArrayList<DataQueryVoBean> getContent() {
        return content;
    }

    public void setContent(ArrayList<DataQueryVoBean> content) {
        this.content = content;
    }
}
