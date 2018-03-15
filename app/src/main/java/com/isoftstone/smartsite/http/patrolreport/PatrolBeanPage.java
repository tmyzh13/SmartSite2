package com.isoftstone.smartsite.http.patrolreport;

import com.isoftstone.smartsite.http.pageable.PageableBean;

import java.util.ArrayList;

/**
 * Created by gone on 2017/12/8.
 */

public class PatrolBeanPage extends PageableBean{
    ArrayList<PatrolBean> content;

    public ArrayList<PatrolBean> getContent() {
        return content;
    }

    public void setContent(ArrayList<PatrolBean> content) {
        this.content = content;
    }
}
