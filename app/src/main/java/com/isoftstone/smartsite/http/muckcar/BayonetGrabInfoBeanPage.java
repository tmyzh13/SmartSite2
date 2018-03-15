package com.isoftstone.smartsite.http.muckcar;

import com.isoftstone.smartsite.http.pageable.PageBean;

import java.util.ArrayList;

/**
 * Created by gone on 2017/11/18.
 */

/*
BayonetGrabInfoBean 分页是用
 */
public class BayonetGrabInfoBeanPage extends PageBean {
    private ArrayList<BayonetGrabInfoBean>  rawRecords;  //未识别渣土列表
    private ArrayList<BayonetGrabInfoBean>  content;  //未识别渣土列表

    public ArrayList<BayonetGrabInfoBean> getRawRecords() {
        return rawRecords;
    }

    public void setRawRecords(ArrayList<BayonetGrabInfoBean> rawRecords) {
        this.rawRecords = rawRecords;
    }

    public ArrayList<BayonetGrabInfoBean> getContent() {
        return content;
    }

    public void setContent(ArrayList<BayonetGrabInfoBean> content) {
        this.content = content;
    }

}

