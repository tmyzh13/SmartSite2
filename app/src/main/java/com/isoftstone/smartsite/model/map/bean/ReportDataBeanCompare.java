package com.isoftstone.smartsite.model.map.bean;

import com.isoftstone.smartsite.http.patrolinfo.ReportDataBean;

import java.util.Comparator;

/**
 * Created by zw on 2017/11/27.
 */

public class ReportDataBeanCompare implements Comparator{


    @Override
    public int compare(Object o1, Object o2) {
        ReportDataBean bean1 = (ReportDataBean) o1;
        ReportDataBean bean2 = (ReportDataBean) o2;
        int bean1Count = bean1.getUnCount() + bean1.getOff();
        int bean2Count = bean2.getUnCount() + bean2.getOff();
        return bean1Count > bean2Count ? -1 : 1;
    }
}
