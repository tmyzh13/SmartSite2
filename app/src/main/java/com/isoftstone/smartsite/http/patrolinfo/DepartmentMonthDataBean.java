package com.isoftstone.smartsite.http.patrolinfo;

import com.isoftstone.smartsite.User;

import java.util.ArrayList;

/**
 * Created by gone on 2017/11/22.
 */

public class DepartmentMonthDataBean {
    private ArrayList<ReportDataBean>  off;//当前单位本月每天已完成的任务数量
    private ArrayList<ReportDataBean>  all;//当前单位本月每天总的任务数量

    public ArrayList<ReportDataBean> getOff() {
        return off;
    }

    public void setOff(ArrayList<ReportDataBean> off) {
        this.off = off;
    }

    public ArrayList<ReportDataBean> getAll() {
        return all;
    }

    public void setAll(ArrayList<ReportDataBean> all) {
        this.all = all;
    }
}
