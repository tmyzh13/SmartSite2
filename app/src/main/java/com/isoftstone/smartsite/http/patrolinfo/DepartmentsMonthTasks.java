package com.isoftstone.smartsite.http.patrolinfo;

import java.util.ArrayList;

/**
 * Created by gone on 2017/11/22.
 */

public class DepartmentsMonthTasks {
    private ArrayList<ArrayList<ReportDataBean>>  list;
    private ArrayList<String>  date;
    private ArrayList<ArrayList<ReportDataBean>>  data;

    public ArrayList<ArrayList<ReportDataBean>> getList() {
        return list;
    }

    public void setList(ArrayList<ArrayList<ReportDataBean>> list) {
        this.list = list;
    }

    public ArrayList<ArrayList<ReportDataBean>> getDate() {
        return data;
    }

    public void setDate(ArrayList<ArrayList<ReportDataBean>> date) {
        this.data = date;
    }

    public ArrayList<String> getData() {
        return date;
    }

    public void setData(ArrayList<String> data) {
        this.date = data;
    }

    @Override
    public String toString() {
        return "DepartmentsMonthTasks{" +
                "list=" + list +
                ", date=" + date +
                ", data=" + data +
                '}';
    }
}
