package com.isoftstone.smartsite.http.patroltask;

import com.isoftstone.smartsite.http.pageable.PageBean;

import java.util.ArrayList;

/**
 * Created by gone on 2017/11/18.
 */

public class PatrolTaskBeanPage extends PageBean{
    private ArrayList<PatrolTaskBean> content;

    public ArrayList<PatrolTaskBean> getContent() {
        return content;
    }

    public void setContent(ArrayList<PatrolTaskBean> content) {
        this.content = content;
    }
}
