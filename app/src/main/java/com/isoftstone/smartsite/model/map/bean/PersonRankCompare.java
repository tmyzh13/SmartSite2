package com.isoftstone.smartsite.model.map.bean;


import com.isoftstone.smartsite.http.patrolinfo.UserTaskCountBean;

import java.util.Comparator;

/**
 * Created by zw on 2017/11/28.
 */

public class PersonRankCompare implements Comparator{



    @Override
    public int compare(Object o1, Object o2) {
        UserTaskCountBean bean1 = (UserTaskCountBean) o1;
        UserTaskCountBean bean2 = (UserTaskCountBean) o2;
        int bean1Count = bean1.getOffCount() + bean1.getUnCount();
        int bean2Count = bean2.getOffCount() + bean2.getUnCount();
        return bean1Count > bean2Count ? -1 : 1;
    }
}
