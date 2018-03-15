package com.isoftstone.smartsite.http.patrolinfo;

/**
 * Created by gone on 2017/11/21.
 */

public class ReportDataBean {
    private  String departmentId;   //部门编号
    private  int off; 	         //已完成的任务数量
    private  int  unCount;		//未完成的任务数量
    private  int count;         //任务数量
    private  String time;       //任务时间

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public int getOff() {
        return off;
    }

    public void setOff(int off) {
        this.off = off;
    }

    public int getUnCount() {
        return unCount;
    }

    public void setUnCount(int unCount) {
        this.unCount = unCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ReportDataBean{" +
                "departmentId='" + departmentId + '\'' +
                ", off=" + off +
                ", unCount=" + unCount +
                ", count=" + count +
                ", time='" + time + '\'' +
                '}';
    }
}
